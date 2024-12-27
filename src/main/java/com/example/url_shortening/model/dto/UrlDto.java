package com.example.url_shortening.model.dto;


public class UrlDto {
    private String url;
    private long expirationTimeInSeconds;

    public UrlDto(String url, long expirationTimeInSeconds) {
        this.url = url;
        this.expirationTimeInSeconds = expirationTimeInSeconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
                ", expirationDate='" + expirationTimeInSeconds + '\'' +
                '}';
    }
}
