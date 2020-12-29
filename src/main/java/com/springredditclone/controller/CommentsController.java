package com.springredditclone.controller;

import com.springredditclone.dto.CommentsDto;
import com.springredditclone.exceptions.PostNotFoundException;
import com.springredditclone.exceptions.SpringRedditException;
import com.springredditclone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentsDto commentsDto) throws SpringRedditException, PostNotFoundException {
        commentService.createComment(commentsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<CommentsDto>> getAllCommentsByUser(@RequestParam("userName") String userName){
        return status(HttpStatus.OK).body(commentService.getCommentsByUser(userName));
    }
}
