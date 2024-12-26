package com.example.url_shortening.controller;

import com.example.url_shortening.model.Url;
import com.example.url_shortening.model.dto.UrlDto;
import com.example.url_shortening.model.dto.UrlErrorResponseDto;
import com.example.url_shortening.model.dto.UrlResponseDto;
import com.example.url_shortening.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlShorteningController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        Url newShortLink = urlService.generateShortLink(urlDto);

        if (newShortLink != null) {
            UrlResponseDto urlResonse = new UrlResponseDto();
            urlResonse.setOriginalUrl(newShortLink.getLongUrl());
            urlResonse.setShortLink(newShortLink.getShortUrl());
            urlResonse.setExpirationDate(newShortLink.getExpirationDate());
            return new ResponseEntity<UrlResponseDto>(urlResonse, HttpStatus.OK);
        }

        UrlErrorResponseDto urlErrorResponse = new UrlErrorResponseDto();
        urlErrorResponse.setStatus("404");
        urlErrorResponse.setError("There was an error generating the link. Please try again.");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponse, HttpStatus.OK);
    }
}
