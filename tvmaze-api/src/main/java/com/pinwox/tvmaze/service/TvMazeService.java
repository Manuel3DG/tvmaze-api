package com.pinwox.tvmaze.service;

import com.pinwox.tvmaze.dto.CommentDto;
import com.pinwox.tvmaze.dto.CommentRequestDto;
import com.pinwox.tvmaze.dto.ShowResponseDto;
import com.pinwox.tvmaze.dto.TvMazeSearchDto;
import com.pinwox.tvmaze.dto.TvMazeShowDto;
import com.pinwox.tvmaze.model.CommentDocument;
import com.pinwox.tvmaze.model.ShowDocument;
import com.pinwox.tvmaze.repository.CommentRepository;
import com.pinwox.tvmaze.repository.ShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TvMazeService {

    private static final String SEARCH_URL = "https://api.tvmaze.com/search/shows?q={query}";
    private static final String SHOW_URL = "https://api.tvmaze.com/shows/{id}";

    private final RestTemplate restTemplate;
    private final ShowRepository showRepository;
    private final CommentRepository commentRepository;

    public TvMazeService(RestTemplate restTemplate, ShowRepository showRepository, CommentRepository commentRepository) {
        this.restTemplate = restTemplate;
        this.showRepository = showRepository;
        this.commentRepository = commentRepository;
    }

    public List<ShowResponseDto> searchShows(String query) {
        TvMazeSearchDto[] response = restTemplate.getForObject(SEARCH_URL, TvMazeSearchDto[].class, query);
        if (response == null) {
            return List.of();
        }

        List<TvMazeShowDto> shows = Arrays.stream(response)
                .map(TvMazeSearchDto::getShow)
                .filter(Objects::nonNull)
                .toList();

        List<Long> showIds = shows.stream().map(TvMazeShowDto::getId).toList();
        Map<Long, List<CommentDto>> commentsByShow = findCommentsByShowIds(showIds);

        return shows.stream()
                .map(show -> toResponseDto(show, commentsByShow.getOrDefault(show.getId(), List.of())))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public Object getShowById(Long showId) {
        Object showData = showRepository.findById(showId)
                .map(ShowDocument::getData)
                .orElseGet(() -> fetchAndCacheShow(showId));

        if (showData == null) {
            return null;
        }

        Map<String, Object> enrichedShow = new LinkedHashMap<>((Map<String, Object>) showData);
        enrichedShow.put("comments", findCommentsByShowId(showId));
        return enrichedShow;
    }

    public void saveComment(CommentRequestDto request) {
        CommentDocument document = new CommentDocument(
                request.getShowId(),
                request.getComment(),
                request.getRating()
        );
        commentRepository.save(document);
    }

    private Object fetchAndCacheShow(Long showId) {
        try {
            Object showData = restTemplate.getForObject(SHOW_URL, Object.class, showId);
            if (showData != null) {
                showRepository.save(new ShowDocument(showId, showData));
            }
            return showData;
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    private List<CommentDto> findCommentsByShowId(Long showId) {
        return commentRepository.findByShowId(showId).stream()
                .map(this::toCommentDto)
                .toList();
    }

    private Map<Long, List<CommentDto>> findCommentsByShowIds(List<Long> showIds) {
        return commentRepository.findByShowIdIn(showIds).stream()
                .collect(Collectors.groupingBy(
                        CommentDocument::getShowId,
                        Collectors.mapping(this::toCommentDto, Collectors.toList())));
    }

    private ShowResponseDto toResponseDto(TvMazeShowDto show, List<CommentDto> comments) {
        ShowResponseDto dto = new ShowResponseDto();
        dto.setId(show.getId());
        dto.setName(show.getName());
        dto.setChannel(resolveChannel(show));
        dto.setSummary(show.getSummary());
        dto.setGenres(show.getGenres());
        dto.setComments(comments);
        return dto;
    }

    private String resolveChannel(TvMazeShowDto show) {
        if (show.getNetwork() != null) {
            return show.getNetwork().getName();
        }
        return show.getWebChannel() != null ? show.getWebChannel().getName() : null;
    }

    private CommentDto toCommentDto(CommentDocument document) {
        return new CommentDto(document.getComment(), document.getRating());
    }
}