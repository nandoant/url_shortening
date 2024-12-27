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
import com.example.url_shortening.repository.UrlRepository;


@Component
public class UrlServiceImpl implements UrlService {

    private static final long DEFAULT_EXPIRATION_TIME_SECONDS = 60;
    private static final String URL_ALPHABET = "x9uKkSHvNrq6OYRsFfi7jDPdG58EAb0ILhQ34lcnXzWZToUV2twJeagmpy1CMB";
    private static final int SHORT_URL_MIN_LENGTH = 6;
    
    private final UrlRepository urlRepository;
    private final Sqids sqids;

    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
        this.sqids = Sqids.builder()
                .alphabet(URL_ALPHABET)
                .minLength(SHORT_URL_MIN_LENGTH)
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
            throw new IllegalArgumentException("UrlDto cannot be null");
        }
        if (!StringUtils.hasText(urlDto.getUrl())) {
            throw new IllegalArgumentException("URL cannot be empty or blank");
        }
        if (!isValidUrl(urlDto.getUrl())) {
            throw new IllegalArgumentException("Invalid URL format");
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
        long effectiveExpiration = expirationSeconds > 0 ? expirationSeconds : DEFAULT_EXPIRATION_TIME_SECONDS;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'persistShortLink'");
    }
}