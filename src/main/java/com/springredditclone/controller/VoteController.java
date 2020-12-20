package com.springredditclone.controller;

import com.springredditclone.dto.VoteDto;
import com.springredditclone.exceptions.PostNotFoundException;
import com.springredditclone.exceptions.SpringRedditException;
import com.springredditclone.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<?> vpte(@RequestBody VoteDto voteDto) throws SpringRedditException, PostNotFoundException {
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
