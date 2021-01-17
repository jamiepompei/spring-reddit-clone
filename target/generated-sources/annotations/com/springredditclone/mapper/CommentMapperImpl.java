package com.springredditclone.mapper;

import com.springredditclone.dto.CommentsDto;
import com.springredditclone.model.Comment;
import com.springredditclone.model.Comment.CommentBuilder;
import com.springredditclone.model.Post;
import com.springredditclone.model.User;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-01-16T22:08:22-0500",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_221 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment map(CommentsDto commentsDto, Post post, User user) {
        if ( commentsDto == null && post == null && user == null ) {
            return null;
        }

        CommentBuilder comment = Comment.builder();

        if ( commentsDto != null ) {
            comment.text( commentsDto.getText() );
        }
        if ( post != null ) {
            comment.post( post );
            comment.user( post.getUser() );
        }
        comment.createdDate( java.time.Instant.now() );

        return comment.build();
    }

    @Override
    public CommentsDto mapToDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentsDto commentsDto = new CommentsDto();

        commentsDto.setId( comment.getId() );
        commentsDto.setCreatedDate( comment.getCreatedDate() );
        commentsDto.setText( comment.getText() );

        commentsDto.setPostId( comment.getPost().getPostId() );
        commentsDto.setUsername( comment.getUser().getUsername() );

        return commentsDto;
    }
}
