package com.example.url_shortening.url;

import com.example.url_shortening.url.entity.Url;
import com.example.url_shortening.url.repository.UrlRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

@DataJpaTest
public class UrlRepositoryTest {
   
    @Autowired 
    private UrlRepository urlRepository;

    private static final String LONG_URL = "https://example.com/very/long/url/path";
    private static final String SHORT_URL = "abc123";
    private Url testUrl;
    private LocalDateTime now = LocalDateTime.now();

        @BeforeEach
    void setUp() {
        urlRepository.deleteAll();
        testUrl = Url.builder()
                .longUrl(LONG_URL)
                .shortUrl(SHORT_URL)
                .creationDate(now)
                .expirationDate(now.plusDays(7))
                .build();
    }

    @Test
    @DisplayName("Should save URL with all fields correctly")
    public void UrlRepository_SaveUrl_ReturnSavedUrl() {
        Url savedUrl = urlRepository.save(testUrl);

        assertThat(savedUrl).isNotNull();
        assertThat(savedUrl.getId()).isNotNull();
        assertThat(savedUrl.getLongUrl()).isEqualTo(LONG_URL);
        assertThat(savedUrl.getShortUrl()).isEqualTo(SHORT_URL);
        assertThat(savedUrl.getCreationDate()).isEqualTo(now);
        assertThat(savedUrl.getExpirationDate()).isEqualTo(now.plusDays(7));
    }

    @Test
    @DisplayName("Should handle URL with maximum length")
    public void UrlRepository_SaveUrl_ReturnUrlMaxLength() {
        String veryLongUrl = "https://example.com/" + "a".repeat(2048);
        testUrl.setLongUrl(veryLongUrl);

        Url savedUrl = urlRepository.save(testUrl);

        assertThat(savedUrl.getLongUrl()).isEqualTo(veryLongUrl);
    }

    @Test
    @DisplayName("Should find URL by short URL")
    public void UrlRepository_FindByShortUrl_ReturnFoundUrl() {
        urlRepository.save(testUrl);

        Url foundUrl = urlRepository.findByShortUrl(SHORT_URL);
        
        assertThat(foundUrl).isNotNull();
        assertThat(foundUrl.getLongUrl()).isEqualTo(LONG_URL);
    }

    @Test
    @DisplayName("Should return null for non-existent short URL")
    public void UrlRepository_FindByShortUrl_ReturnNull() {
        Url foundUrl = urlRepository.findByShortUrl("nonexistent");
        assertThat(foundUrl).isNull();
    }

    @Test
    @DisplayName("Should delete URL by ID")
    public void UrlRepository_DeleteUrl_ReturnEmpty() {
        Url savedUrl = urlRepository.save(testUrl);

        urlRepository.deleteById(savedUrl.getId());

        assertThat(urlRepository.findById(savedUrl.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should update existing URL")
    public void UrlRepository_UpdateUrl_ReturnUpdatedUrl() {
        Url savedUrl = urlRepository.save(testUrl);

        LocalDateTime newExpirationDate = now.plusDays(14);
        savedUrl.setLongUrl("https://updated-example.com");
        savedUrl.setExpirationDate(newExpirationDate);
        Url updatedUrl = urlRepository.save(savedUrl);

        assertThat(updatedUrl.getLongUrl()).isEqualTo("https://updated-example.com");
        assertThat(updatedUrl.getExpirationDate()).isEqualToIgnoringNanos(newExpirationDate);
        assertThat(updatedUrl.getId()).isEqualTo(savedUrl.getId());
    }
}
