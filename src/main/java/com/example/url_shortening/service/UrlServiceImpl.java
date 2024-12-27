package com.example.url_shortening.service;

import java.time.LocalDateTime;
import java.util.List;
import java.net.URL;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.sqids.Sqids;

import com.example.url_shortening.model.Url;
import com.example.url_shortening.model.dto.UrlDto;
import com.example.url_shortening.model.exception.UrlAlreadyExistsException;
import com.example.url_shortening.model.exception.UrlException;
import com.example.url_shortening.repository.UrlRepository;
import com.example.url_shortening.config.*;;

@Component
public class UrlServiceImpl implements UrlService {
    
    private final UrlRepository urlRepository;
    private final Sqids sqids;

    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
        this.sqids = Sqids.builder()
                .alphabet(UrlConfig.URL_ALPHABET)
                .minLength(UrlConfig.SHORT_URL_MIN_LENGTH)
                .build();
    }

    @Override
    public Url generateShortLink(UrlDto urlDto) throws Exception {
        validateUrlDto(urlDto);
        String encodedUrl = createShortUrl(urlDto);
        Url urlToPersist = createUrlEntity(urlDto, encodedUrl);
        return persistShortLink(urlToPersist);
    }

    private void validateUrlDto(UrlDto urlDto) {
        if (urlDto == null) {
            throw new UrlException("UrlDto cannot be null");
        }
        if (!StringUtils.hasText(urlDto.getUrl())) {
            throw new UrlException("URL cannot be empty or blank");
        }
        if (!isValidUrl(urlDto.getUrl())) {
            throw new UrlException("Invalid URL format");
        }
        if (urlDto.getExpirationTimeInSeconds() < 0) {
            throw new UrlException("Expiration time cannot be negative");
        }
        if (urlDto.getExpirationTimeInSeconds() > 0 && urlDto.getExpirationTimeInSeconds() > UrlConfig.MAX_EXPIRATION_TIME_SECONDS){
            throw new UrlException("Expiration time cannot be greater than "+ UrlConfig.MAX_EXPIRATION_TIME_SECONDS + " seconds");
        }
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Url createUrlEntity(UrlDto urlDto, String encodedUrl) {
        LocalDateTime now = LocalDateTime.now();
        return Url.builder()
                .longUrl(urlDto.getUrl())
                .shortUrl(encodedUrl)
                .creationDate(now)
                .expirationDate(calculateExpirationDate(now, urlDto.getExpirationTimeInSeconds()))
                .build();
    }

    private LocalDateTime calculateExpirationDate(LocalDateTime creationDate, long expirationSeconds) {
        long effectiveExpiration = expirationSeconds > 0 ? expirationSeconds : UrlConfig.DEFAULT_EXPIRATION_TIME_SECONDS;
        return creationDate.plusSeconds(effectiveExpiration);
    }

    private String createShortUrl(UrlDto urlDto) throws UrlAlreadyExistsException {
        if (StringUtils.hasText(urlDto.getSuggestedShortLink())) {
            validateSuggestedShortLink(urlDto.getSuggestedShortLink());
            return urlDto.getSuggestedShortLink();
        }
        return generateUniqueShortUrl();
    }

    private void validateSuggestedShortLink(String suggestedShortLink) throws UrlAlreadyExistsException {
        if (urlRepository.findByShortUrl(suggestedShortLink) != null) {
            throw new UrlAlreadyExistsException("Suggested short link already exists");
        }
    }

    private String generateUniqueShortUrl() {
        long nextId = urlRepository.count() + 1;
        return sqids.encode(List.of(nextId));
    }

    @Override
    public Url getEncodedUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }

    @Override
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }

    @Override
    public Url persistShortLink(Url url) {
        return urlRepository.save(url);
    }
}