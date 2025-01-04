package com.example.url_shortening.url;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.url_shortening.exception.exceptions.ServiceLayerException;
import com.example.url_shortening.url.dto.UrlDto;
import com.example.url_shortening.url.exception.UrlErrorCode;
import com.example.url_shortening.url.entity.Url;
import com.example.url_shortening.url.repository.UrlRepository;
import com.example.url_shortening.url.service.UrlServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    private UrlServiceImpl urlService;

    @BeforeEach
    void setUp() {
        urlService = new UrlServiceImpl(urlRepository);
    }

    @Test
    void generateShortLink_WithValidUrl_ShouldSucceed() throws Exception {
        // Arrange
        UrlDto urlDto = new UrlDto("https://www.example.com", 3600, null);
        Url expectedUrl = new Url();
        expectedUrl.setLongUrl(urlDto.getUrl());
        expectedUrl.setShortUrl("abc123");
        
        when(urlRepository.count()).thenReturn(1L);
        when(urlRepository.save(any(Url.class))).thenReturn(expectedUrl);

        // Act
        Url result = urlService.generateShortLink(urlDto);

        // Assert
        assertNotNull(result);
        assertEquals(urlDto.getUrl(), result.getLongUrl());
        assertNotNull(result.getShortUrl());
        verify(urlRepository).save(any(Url.class));
    }

    @Test
    void generateShortLink_WithInvalidUrl_ShouldThrowException() {
        // Arrange
        UrlDto urlDto = new UrlDto("invalid-url", 3600, null);

        // Act & Assert
        ServiceLayerException exception = assertThrows(ServiceLayerException.class,
            () -> urlService.generateShortLink(urlDto));
        assertEquals(UrlErrorCode.INVALID_URL, exception.getErrorCode());
    }

    @Test
    void getEncodedUrl_WithValidShortUrl_ShouldReturnUrl() {
        // Arrange
        String shortUrl = "abc123";
        Url expectedUrl = new Url();
        expectedUrl.setShortUrl(shortUrl);
        expectedUrl.setLongUrl("https://www.example.com");
        expectedUrl.setExpirationDate(LocalDateTime.now().plusHours(1));

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(expectedUrl);

        // Act
        Url result = urlService.getEncodedUrl(shortUrl);

        // Assert
        assertNotNull(result);
        assertEquals(shortUrl, result.getShortUrl());
        assertEquals(expectedUrl.getLongUrl(), result.getLongUrl());
    }

    @Test
    void getEncodedUrl_WithExpiredUrl_ShouldThrowException() {
        // Arrange
        String shortUrl = "abc123";
        Url expiredUrl = new Url();
        expiredUrl.setShortUrl(shortUrl);
        expiredUrl.setExpirationDate(LocalDateTime.now().minusHours(1));

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(expiredUrl);

        // Act & Assert
        ServiceLayerException exception = assertThrows(ServiceLayerException.class,
            () -> urlService.getEncodedUrl(shortUrl));
        assertEquals(UrlErrorCode.URL_EXPIRED, exception.getErrorCode());
    }

    @Test
    void getEncodedUrl_WithNonExistentUrl_ShouldThrowException() {
        // Arrange
        String shortUrl = "nonexistent";
        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(null);

        // Act & Assert
        ServiceLayerException exception = assertThrows(ServiceLayerException.class,
            () -> urlService.getEncodedUrl(shortUrl));
        assertEquals(UrlErrorCode.URL_NOT_FOUND, exception.getErrorCode());
    }

}
