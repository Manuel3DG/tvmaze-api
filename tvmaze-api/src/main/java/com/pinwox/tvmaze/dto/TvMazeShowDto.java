package com.pinwox.tvmaze.dto;

import java.util.List;

public class TvMazeShowDto {
    private Long id;
    private String name;
    private String summary;
    private List<String> genres;
    private NetworkDto network;
    private NetworkDto webChannel;

    public TvMazeShowDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public NetworkDto getNetwork() {
        return network;
    }

    public void setNetwork(NetworkDto network) {
        this.network = network;
    }

    public NetworkDto getWebChannel() {
        return webChannel;
    }

    public void setWebChannel(NetworkDto webChannel) {
        this.webChannel = webChannel;
    }

    public static class NetworkDto {
        private String name;

        public NetworkDto() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
