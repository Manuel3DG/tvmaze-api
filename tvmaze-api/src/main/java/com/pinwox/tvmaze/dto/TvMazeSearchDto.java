package com.pinwox.tvmaze.dto;

public class TvMazeSearchDto {
    private Double score;
    private TvMazeShowDto show;

    public TvMazeSearchDto() {
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public TvMazeShowDto getShow() {
        return show;
    }

    public void setShow(TvMazeShowDto show) {
        this.show = show;
    }
}
