package com.springredditclone.service;

import com.springredditclone.dto.SubredditDto;
import com.springredditclone.exceptions.SpringRedditException;
import com.springredditclone.mapper.SubredditMapper;
import com.springredditclone.model.Subreddit;
import com.springredditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId((save.getId()));
        return subredditDto;

    }


    @Transactional(readOnly = true)
    public List<SubredditDto> getAll(){
     return  subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) throws SpringRedditException {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with id-" + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }

}
