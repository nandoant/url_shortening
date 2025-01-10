package com.example.url_shortening.url.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "url_seq")
    @SequenceGenerator(name = "url_seq", sequenceName = "url_seq", allocationSize = 1)
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