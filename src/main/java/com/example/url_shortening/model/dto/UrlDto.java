package com.example.url_shortening.model.dto;


public class UrlDto {
    private String url;
    private String suggestedShortLink;
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
