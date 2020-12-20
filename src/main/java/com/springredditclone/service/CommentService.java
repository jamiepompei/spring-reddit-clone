package com.springredditclone.service;

import com.springredditclone.dto.CommentsDto;
import com.springredditclone.exceptions.PostNotFoundException;
import com.springredditclone.exceptions.SpringRedditException;
import com.springredditclone.mapper.CommentMapper;
import com.springredditclone.model.Comment;
import com.springredditclone.model.NotificationEmail;
import com.springredditclone.model.Post;
import com.springredditclone.model.User;
import com.springredditclone.repository.CommentRepository;
import com.springredditclone.repository.PostRepository;
import com.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    //TODO: construct POST URL
    private static final String POST_URL = "";

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void createComment(CommentsDto commentsDto) throws PostNotFoundException, SpringRedditException {
       Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
       Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
       commentRepository.save(comment);

       String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    public List<CommentsDto> getCommentByPost(Long postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getCommentsByUser(String userName){
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    private void sendCommentNotification(String message, User user) throws SpringRedditException {
      mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }


}
