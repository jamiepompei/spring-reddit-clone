package com.springredditclone.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.springredditclone.dto.PostRequest;
import com.springredditclone.dto.PostResponse;
import com.springredditclone.model.Post;
import com.springredditclone.model.Subreddit;
import com.springredditclone.model.User;
import com.springredditclone.repository.CommentRepository;
import com.springredditclone.repository.VoteRepository;
import com.springredditclone.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant ="0")
    @Mapping(target = "description", source = "postRequest.description")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post){
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post){
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

}
