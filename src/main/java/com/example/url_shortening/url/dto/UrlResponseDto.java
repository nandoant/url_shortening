package com.example.url_shortening.url.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "UrlResponseDto", description = "Response DTO for URL response")
public class UrlResponseDto {
    @Schema(description = "Original URL",
            example = "https://www.example.com")
    private String originalUrl;

    @Schema(description = "Shortened URL",
            example = "abc123")
    private String shortLink;

    @Schema(description = "Expiration date of the shortened URL",
            example = "2025-12-12T23:59:59")
    private LocalDateTime expirationDate;

    public UrlResponseDto(String originalUrl, String shortLink, LocalDateTime expirationDate) {
        this.originalUrl = originalUrl;
        this.shortLink = shortLink;
        this.expirationDate = expirationDate;
    }

    public UrlResponseDto() {}

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "UrlResponseDto{" +
                "originalUrl='" + originalUrl + '\'' +
                ", shortLink='" + shortLink + '\'' +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
