package com.example.url_shortening.controller;

import com.example.url_shortening.model.Url;
import com.example.url_shortening.model.dto.UrlDto;
import com.example.url_shortening.model.dto.UrlErrorResponseDto;
import com.example.url_shortening.model.dto.UrlResponseDto;
import com.example.url_shortening.service.UrlService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class UrlShorteningController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        try{
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
        } catch (Exception UrlAlreadyExistsException) {
            UrlErrorResponseDto urlErrorResponse = new UrlErrorResponseDto();
            urlErrorResponse.setStatus("400");
            urlErrorResponse.setError("Url already exists. Please try again.");
            return new ResponseEntity<>(urlErrorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse httpServletResponse) throws IOException {
        if (StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDto urlErrorResponse = new UrlErrorResponseDto();
            urlErrorResponse.setStatus("400");
            urlErrorResponse.setError("Invalid url. Please try again.");
            return new ResponseEntity<>(urlErrorResponse, HttpStatus.BAD_REQUEST);
        }

        Url urlFound = urlService.getEncodedUrl(shortLink);

        if (urlFound == null) {
            UrlErrorResponseDto urlErrorResponse = new UrlErrorResponseDto();
            urlErrorResponse.setStatus("404");
            urlErrorResponse.setError("Url does not exist or it might have expired.");
            return new ResponseEntity<>(urlErrorResponse, HttpStatus.NOT_FOUND);
        }

        if (urlFound.getExpirationDate().isBefore(LocalDateTime.now())) {
            UrlErrorResponseDto urlErrorResponse = new UrlErrorResponseDto();
            urlErrorResponse.setStatus("404");
            urlErrorResponse.setError("Url Expired.");
            return new ResponseEntity<>(urlErrorResponse, HttpStatus.NOT_FOUND);
        }


        httpServletResponse.sendRedirect(urlFound.getLongUrl());
        return null;
    }
}
