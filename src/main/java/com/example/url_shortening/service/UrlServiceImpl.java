package com.example.url_shortening.service;

import com.example.url_shortening.model.Url;
import com.example.url_shortening.model.dto.UrlDto;
import com.example.url_shortening.repository.UrlRepository;
import io.micrometer.common.util.StringUtils;

public class UrlServiceImpl implements UrlService {

    UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto) {

        if(StringUtils.isNotEmpty(urlDto.getUrl())){
            String encoded = encodeUrl(urlDto.getUrl());
        }
        return null;
    }

    private String encodeUrl(String url) {
        String encodedUrl = null;
        urlRepository.count();
        return encodedUrl;
    }

    @Override
    public Url persistShortLink(Url url) {
        return null;
    }

    @Override
    public Url getEncodedUrl(String url) {
        return null;
    }

    @Override
    public void deleteShortLink(Url url) {

    }
}
