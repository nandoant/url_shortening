package com.example.url_shortening.url.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}