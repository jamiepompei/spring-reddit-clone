package com.springredditclone.controller;

import com.springredditclone.dto.PostRequest;
import com.springredditclone.dto.PostResponse;
import com.springredditclone.exceptions.PostNotFoundException;
import com.springredditclone.exceptions.SubredditNotFoundException;
import com.springredditclone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor

public class PostController {

    private final PostService postService;

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) throws SubredditNotFoundException {
        postService.save(postRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) throws SubredditNotFoundException, PostNotFoundException {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return status(HttpStatus.OK).body(postService.getAllPosts());
       // return postService.getAllPosts();
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(Long id) throws SubredditNotFoundException {
        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
        //return postService.getPostsBySubreddit(id);
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(String username){
       return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
        // return postService.getPostsByUsername(username);
    }


}
