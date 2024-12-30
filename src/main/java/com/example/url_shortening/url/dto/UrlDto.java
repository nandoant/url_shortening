package com.example.url_shortening.url.dto;


import java.net.URL;
import com.example.url_shortening.config.UrlConfig;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data Transfer Object for URL operations")
public class UrlDto {
    @Schema(description = "Original URL to be shortened", 
            example = "https://www.example.com")
    @NotBlank(message = "URL cannot be empty or blank")
    private String url;

    @Schema(description = "Custom short link suggestion", 
            example = "custom_link")
    private String suggestedShortLink;

    @Schema(description = "Expiration time in seconds", 
            example = "3600")
    @Min(value = 0, message = "Expiration time cannot be negative")
    @Max(value = UrlConfig.MAX_EXPIRATION_TIME_SECONDS,
        message = "Expiration time cannot be greater than "+ UrlConfig.MAX_EXPIRATION_TIME_SECONDS +" seconds"
        )
    private long expirationTimeInSeconds;

    public UrlDto(String url, long expirationTimeInSeconds, String shortLink) {
        this.url = url;
        this.expirationTimeInSeconds = expirationTimeInSeconds;
        this.suggestedShortLink = shortLink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSuggestedShortLink() {
        return suggestedShortLink;
    }

    public void setSuggestedShortLink(String shortLink) {
        this.suggestedShortLink = shortLink;
    }

    public long getExpirationTimeInSeconds() {
        return expirationTimeInSeconds;
    }

    @Schema(hidden = true)
    public boolean isValidUrl() {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setExpirationTimeInSeconds(long expirationTimeInSeconds) {
        this.expirationTimeInSeconds = expirationTimeInSeconds;
    }

    @Override
    public String toString() {
        return "UrlDto{" +
                "url='" + url + '\'' +
                ", shortLink='" + suggestedShortLink + '\'' +
                ", expirationTimeInSeconds=" + expirationTimeInSeconds +
                '}';
    }
}
