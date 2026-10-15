package com.pinwox.tvmaze.controller;

import com.pinwox.tvmaze.dto.CommentRequestDto;
import com.pinwox.tvmaze.dto.ShowResponseDto;
import com.pinwox.tvmaze.service.TvMazeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShowController {

    private final TvMazeService tvMazeService;

    public ShowController(TvMazeService tvMazeService) {
        this.tvMazeService = tvMazeService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShowResponseDto>> search(@RequestParam("search_query") String searchQuery) {
        List<ShowResponseDto> results = tvMazeService.searchShows(searchQuery);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/shows/{id}")
    public ResponseEntity<Object> getShowById(@PathVariable("id") Long id) {
        Object show = tvMazeService.getShowById(id);
        if (show == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(show);
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> addComment(@Valid @RequestBody CommentRequestDto commentRequestDto) {
        tvMazeService.saveComment(commentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
