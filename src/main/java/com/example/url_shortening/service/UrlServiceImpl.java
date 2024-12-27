package com.example.url_shortening.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sqids.Sqids;

import com.example.url_shortening.model.Url;
import com.example.url_shortening.model.dto.UrlDto;
import com.example.url_shortening.repository.UrlRepository;
import io.micrometer.common.util.StringUtils;


@Component
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto) {
        if(StringUtils.isNotEmpty(urlDto.getUrl().trim())){
            String encodedUrl = encodeUrl(urlDto.getUrl());

            Url urlToPersist = getUrlToPersist(urlDto, encodedUrl);

            return persistShortLink(urlToPersist);
        }

        return null;
    }

    private static Url getUrlToPersist(UrlDto urlDto, String encodedUrl) {
        Url urlToPersist = new Url();
        urlToPersist.setLongUrl(urlDto.getUrl());
        urlToPersist.setShortUrl(encodedUrl);
        LocalDateTime dateTimeNow = LocalDateTime.now();
        urlToPersist.setCreationDate(dateTimeNow);
        setExpiredTime(urlToPersist, dateTimeNow, urlDto);
        return urlToPersist;
    }

    private static void setExpiredTime(Url urlToPersist, LocalDateTime dateTimeNow, UrlDto urlDto) {
        if (urlDto.getExpirationTimeInSeconds() != 0) {
            urlToPersist.setExpirationDate(dateTimeNow.plusSeconds(urlDto.getExpirationTimeInSeconds()));
        } else {
            //transformar isso depois em uma variavel global ou um DEFINE
            long defaultExpirationTime = 60;
            urlToPersist.setExpirationDate(dateTimeNow.plusSeconds(defaultExpirationTime));
        }
    }

    private String encodeUrl(String url) {
        Sqids sqids = Sqids.builder()
                .alphabet("x9uKkSHvNrq6OYRsFfi7jDPdG58EAb0ILhQ34lcnXzWZToUV2twJeagmpy1CMB")
                .minLength(6)
                .build();
        
        long nextId = urlRepository.count() + 1;

        return sqids.encode(List.of(nextId));
    }

    @Override
    public Url persistShortLink(Url url) {
        return urlRepository.save(url);
    }

    @Override
    public Url getEncodedUrl(String url) {
        return urlRepository.findByShortUrl(url);
    }

    @Override
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }
}
