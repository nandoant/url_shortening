package com.example.url_shortening.model.dto;


import java.net.URL;
import com.example.url_shortening.config.UrlConfig;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UrlDto {
    @NotBlank(message = "URL cannot be empty or blank")
    private String url;

    private String suggestedShortLink;

    @Min(value = 0, message = "Expiration time cannot be negative")
    @Max(value = UrlConfig.MAX_EXPIRATION_TIME_SECONDS,
    message = "Expiration time cannot be greater than "+ UrlConfig.MAX_EXPIRATION_TIME_SECONDS +" seconds")

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
