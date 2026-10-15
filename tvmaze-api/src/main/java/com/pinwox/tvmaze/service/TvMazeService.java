package com.pinwox.tvmaze.service;

import com.pinwox.tvmaze.dto.CommentRequestDto;
import com.pinwox.tvmaze.dto.ShowResponseDto;
import com.pinwox.tvmaze.dto.TvMazeSearchDto;
import com.pinwox.tvmaze.model.CommentDocument;
import com.pinwox.tvmaze.model.ShowDocument;
import com.pinwox.tvmaze.repository.CommentRepository;
import com.pinwox.tvmaze.repository.ShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TvMazeService {

    private final RestTemplate restTemplate;
    private final ShowRepository showRepository;
    private final CommentRepository commentRepository;

    public TvMazeService(RestTemplate restTemplate, ShowRepository showRepository, CommentRepository commentRepository) {
        this.restTemplate = restTemplate;
        this.showRepository = showRepository;
        this.commentRepository = commentRepository;
    }

    public List<ShowResponseDto> searchShows(String query) {
        String url = "http://tvmaze.com" + query;
        TvMazeSearchDto[] response = restTemplate.getForObject(url, TvMazeSearchDto[].class);
        List<ShowResponseDto> mappedShows = new ArrayList<>();

        if (response != null) {
            for (TvMazeSearchDto searchDto : response) {
                if (searchDto.getShow() != null) {
                    var show = searchDto.getShow();
                    ShowResponseDto dto = new ShowResponseDto();
                    dto.setId(show.getId());
                    dto.setName(show.getName());
                    dto.setSummary(show.getSummary());
                    dto.setGenres(show.getGenres());

                    String channelName = null;
                    if (show.getNetwork() != null) {
                        channelName = show.getNetwork().getName();
                    } else if (show.getWebChannel() != null) {
                        channelName = show.getWebChannel().getName();
                    }
                    dto.setChannel(channelName);

                    mappedShows.add(dto);
                }
            }
        }
        return mappedShows;
    }

    @SuppressWarnings("unchecked")
    public Object getShowById(Long showId) {
        Object showData;
        Optional<ShowDocument> cachedShow = showRepository.findById(showId);
        
        if (cachedShow.isPresent()) {
            showData = cachedShow.get().getData();
        } else {
            String url = "https://tvmaze.com" + showId;
            try {
                showData = restTemplate.getForObject(url, Object.class);
                if (showData != null) {
                    showRepository.save(new ShowDocument(showId, showData));
                }
            } catch (Exception e) {
                showData = null;
            }
        }

        if (showData instanceof Map) {
            Map<String, Object> enrichedShow = new LinkedHashMap<>((Map<String, Object>) showData);
            List<CommentDocument> comments = commentRepository.findByShowId(showId);
            enrichedShow.put("comments", comments);
            return enrichedShow;
        }

        return showData;
    }

    public void saveComment(CommentRequestDto request) {
        CommentDocument document = new CommentDocument(
                request.getShowId(),
                request.getComment(),
                request.getRating()
        );
        commentRepository.save(document);
    }
}
