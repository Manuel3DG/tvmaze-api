package com.pinwox.tvmaze.service;

import com.pinwox.tvmaze.dto.ShowResponseDto;
import com.pinwox.tvmaze.dto.TvMazeSearchDto;
import com.pinwox.tvmaze.model.ShowDocument;
import com.pinwox.tvmaze.repository.ShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TvMazeService {

    private final RestTemplate restTemplate;
    private final ShowRepository showRepository;

    public TvMazeService(RestTemplate restTemplate, ShowRepository showRepository) {
        this.restTemplate = restTemplate;
        this.showRepository = showRepository;
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

    public Object getShowById(Long showId) {
        Optional<ShowDocument> cachedShow = showRepository.findById(showId);
        if (cachedShow.isPresent()) {
            return cachedShow.get().getData();
        }

        String url = "https://tvmaze.com" + showId;
        try {
            Object externalShow = restTemplate.getForObject(url, Object.class);
            if (externalShow != null) {
                showRepository.save(new ShowDocument(showId, externalShow));
            }
            return externalShow;
        } catch (Exception e) {
            return null;
        }
    }
}
