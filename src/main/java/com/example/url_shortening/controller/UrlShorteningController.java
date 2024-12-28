package com.example.url_shortening.controller;

import com.example.url_shortening.model.Url;
import com.example.url_shortening.model.dto.UrlDto;
import com.example.url_shortening.model.dto.UrlErrorResponseDto;
import com.example.url_shortening.model.dto.UrlResponseDto;
import com.example.url_shortening.service.UrlService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
@Tag(name = "URL Shortening API", description = "API for shortening and redirecting URLs")
public class UrlShorteningController {

    private static final String URL_NOT_FOUND = "URL does not exist or it might have expired.";
    private static final String INVALID_URL = "Invalid URL. Please try again.";
    private static final String URL_EXPIRED = "URL has expired.";

    private final UrlService urlService;

    @PostMapping("/shorten")
    @Operation(summary = "Generate short URL")
    @ApiResponse(responseCode = "200", description = "URL successfully shortened")
    @ApiResponse(responseCode = "400", description = "Invalid URL provided")
    public ResponseEntity<Object> generateShortLink(@Valid @RequestBody UrlDto urlDto) throws Exception {
        Url newShortLink = urlService.generateShortLink(urlDto);
        
        if (newShortLink != null) {
            UrlResponseDto response = UrlResponseDto.builder()
                    .originalUrl(newShortLink.getLongUrl())
                    .shortLink(newShortLink.getShortUrl())
                    .expirationDate(newShortLink.getExpirationDate())
                    .build();
            
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.badRequest()
                .body(createErrorResponse(HttpStatus.BAD_REQUEST.value(), INVALID_URL));
    }

    @GetMapping("/{shortLink}")
    @Operation(summary = "Redirect to original URL")
    @ApiResponse(responseCode = "302", description = "Successful redirect")
    @ApiResponse(responseCode = "404", description = "URL not found or expired")
    public ResponseEntity<Object> redirectToOriginalUrl(
            @PathVariable String shortLink,
            HttpServletResponse response) throws IOException {

        if (StringUtils.isEmpty(shortLink)) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(HttpStatus.BAD_REQUEST.value(), INVALID_URL));
        }

        Url urlFound = urlService.getEncodedUrl(shortLink);

        if (urlFound == null) {
            return ResponseEntity.notFound()
                    .build();
        }

        if (urlFound.getExpirationDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(createErrorResponse(HttpStatus.GONE.value(), URL_EXPIRED));
        }

        response.sendRedirect(urlFound.getLongUrl());
        return null;
    }

    private UrlErrorResponseDto createErrorResponse(int status, String message) {
        return UrlErrorResponseDto.builder()
                .status(String.valueOf(status))
                .error(message)
                .build();
    }
}