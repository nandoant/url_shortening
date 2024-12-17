package com.example.url_shortening.service;

import com.example.url_shortening.model.Url;
import org.springframework.stereotype.Service;

@Service
public class UrlService {
    public Url generateShortLink(UrlDto urlDto);
    public Url persistShortLink(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);

}
