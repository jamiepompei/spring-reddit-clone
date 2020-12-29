package com.springredditclone.repository;

import com.springredditclone.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    void deleteByToken(String token);

    <T> Optional<T> findByToken(String token);

    RefreshToken save(RefreshToken refreshToken);
}
