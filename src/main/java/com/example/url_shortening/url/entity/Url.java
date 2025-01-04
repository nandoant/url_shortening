package com.example.url_shortening.url.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;

@Entity
public class Url {
    @Id
    @GeneratedValue
    private long id;
    @Lob
    private String longUrl;
    private String shortUrl;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;

    public Url(long id, String originalUrl, String shortUrl, LocalDateTime creationDate, LocalDateTime expirationDate) {
        this.id = id;
        this.longUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
    }

    public Url() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String originalUrl) {
        this.longUrl = originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "Url{" +
                "id=" + id +
                ", originalUrl='" + longUrl + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", creationDate=" + creationDate +
                ", expirationDate=" + expirationDate +
                '}';
    }

    public static class UrlBuilder {
        private long id;
        private String longUrl;
        private String shortUrl;
        private LocalDateTime creationDate;
        private LocalDateTime expirationDate;

        public UrlBuilder id(long id) {
            this.id = id;
            return this;
        }

        public UrlBuilder longUrl(String longUrl) {
            this.longUrl = longUrl;
            return this;
        }

        public UrlBuilder shortUrl(String shortUrl) {
            this.shortUrl = shortUrl;
            return this;
        }

        public UrlBuilder creationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public UrlBuilder expirationDate(LocalDateTime expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Url build() {
            return new Url(id, longUrl, shortUrl, creationDate, expirationDate);
        }
    }

    public static UrlBuilder builder() {
        return new UrlBuilder();
    }

}
