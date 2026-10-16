package com.pinwox.tvmaze.dto;

public class CommentDto {
    private String comment;
    private Integer rating;

    public CommentDto() {
    }

    public CommentDto(String comment, Integer rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public Integer getRating() {
        return rating;
    }
}