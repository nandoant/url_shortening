package com.example.url_shortening.service;

import com.example.url_shortening.model.Url;
import com.example.url_shortening.model.dto.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {
    Url generateShortLink(UrlDto urlDto) throws Exception;
    Url persistShortLink(Url url);
    Url getEncodedUrl(String url);
    void deleteShortLink(Url url);
}
