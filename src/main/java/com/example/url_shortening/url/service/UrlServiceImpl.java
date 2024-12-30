package com.example.url_shortening.url.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.sqids.Sqids;

import com.example.url_shortening.url.model.Url;
import com.example.url_shortening.url.dto.UrlDto;
import com.example.url_shortening.url.exception.BaseUrlException;
import com.example.url_shortening.url.exception.UrlErrorCode;
import com.example.url_shortening.url.repository.UrlRepository;

import org.springframework.transaction.annotation.Transactional;

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
            throw new BaseUrlException(UrlErrorCode.VALIDATION_ERROR);
        }
        if (!urlDto.isValidUrl()) {
            throw new BaseUrlException(UrlErrorCode.INVALID_URL);
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

    private String createShortUrl(UrlDto urlDto) {
        if (StringUtils.hasText(urlDto.getSuggestedShortLink())) {
            validateSuggestedShortLink(urlDto.getSuggestedShortLink());
            return urlDto.getSuggestedShortLink();
        }
        return generateUniqueShortUrl();
    }

    private void validateSuggestedShortLink(String suggestedShortLink) {
        if (urlRepository.findByShortUrl(suggestedShortLink) != null) {
            throw new BaseUrlException(UrlErrorCode.DUPLICATE_SHORT_LINK);
        }
    }

    private String generateUniqueShortUrl() {
        long nextId = urlRepository.count() + 1;
        return sqids.encode(List.of(nextId));
    }

    @Override
    @Transactional(readOnly = true)
    public Url getEncodedUrl(String shortUrl) {
        Url shortLink = urlRepository.findByShortUrl(shortUrl);
        if (shortLink == null) {
            throw new BaseUrlException(UrlErrorCode.URL_NOT_FOUND);
        }

        if (shortLink.getExpirationDate().isBefore(LocalDateTime.now())) {
            deleteShortLink(shortLink);
            throw new BaseUrlException(UrlErrorCode.URL_EXPIRED);
        }

        return shortLink;
    }

    @Override
    @Transactional
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }

    @Override
    public Url persistShortLink(Url url) {
        return urlRepository.save(url);
    }
}