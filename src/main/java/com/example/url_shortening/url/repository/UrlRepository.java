package com.example.url_shortening.url.repository;

import com.example.url_shortening.url.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    public Url findByShortUrl(String url);
}
