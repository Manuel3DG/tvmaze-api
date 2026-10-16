package com.pinwox.tvmaze.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentRequestDto {

    @NotNull
    @JsonProperty("show_id")
    private Long showId;

    @NotBlank
    private String comment;

    @NotNull
    @Min(0)
    @Max(5)
    private Integer rating;

    public CommentRequestDto() {
    }

    public Long getShowId() {
        return showId;
    }

    @JsonProperty("show_id")
    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
