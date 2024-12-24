package com.example.url_shortening.service;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.sqids.Sqids;

import com.example.url_shortening.model.Url;
import com.example.url_shortening.model.dto.UrlDto;
import com.example.url_shortening.repository.UrlRepository;
import io.micrometer.common.util.StringUtils;

public class UrlServiceImpl implements UrlService {

    UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto) {
        if(StringUtils.isNotEmpty(urlDto.getUrl())){
            String encodedUrl = encodeUrl(urlDto.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setLongUrl(urlDto.getUrl());
            urlToPersist.setShortUrl(encodedUrl);
            LocalDateTime DateTimeNow = LocalDateTime.now();
            urlToPersist.setCreationDate(DateTimeNow);
            urlToPersist.setExpirationDate(DateTimeNow.plusSeconds(60));
            return persistShortLink(urlToPersist);
        }

        return null;
    }

    private String encodeUrl(String url) {
        Sqids sqids = Sqids.builder()
                .alphabet("x9uKkSHvNrq6OYRsFfi7jDPdG58EAb0ILhQ34lcnXzWZToUV2twJeagmpy1CMB")
                .minLength(6)
                .build();
        
        long nextId = urlRepository.count() + 1;

        return sqids.encode(Arrays.asList(nextId));  
    }

    @Override
    public Url persistShortLink(Url url) {
        return urlRepository.save(url);
    }

    @Override
    public Url getEncodedUrl(String url) {
        return null;
    }

    @Override
    public void deleteShortLink(Url url) {

    }
}
