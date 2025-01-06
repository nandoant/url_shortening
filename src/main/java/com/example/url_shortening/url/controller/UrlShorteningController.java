package com.example.url_shortening.url.controller;

import com.example.url_shortening.exception.exceptions.SystemErrorException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.url_shortening.url.dto.UrlDto;
import com.example.url_shortening.url.dto.UrlErrorResponseDto;
import com.example.url_shortening.url.dto.UrlResponseDto;
import com.example.url_shortening.exception.exceptions.ServiceLayerException;
import com.example.url_shortening.url.entity.Url;
import com.example.url_shortening.url.service.UrlService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/url")
@Tag(name = "URL Shortening", 
    description = "API endpoints for managing URL shortening")
public class UrlShorteningController {

    private final UrlService urlService;

    public UrlShorteningController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping(value = "/shorten", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Create shortened URL", 
                description = "Create a shortened URL from the provided URL")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "URL successfully shortened",
            content = @Content(schema = @Schema(implementation = UrlResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid URL format or parameters",
            content = @Content(schema = @Schema(implementation = UrlErrorResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Custom alias already in use",
            content = @Content(schema = @Schema(implementation = UrlErrorResponseDto.class))
        )
    })
    public ResponseEntity<UrlResponseDto> shortenUrl(@Valid @RequestBody UrlDto urlDto) {
        try {

            Url generatedUrl = urlService.generateShortLink(urlDto);
            URI shortenedUrl = URI.create(generatedUrl.getShortUrl());

            UrlResponseDto response = UrlResponseDto.builder()
                    .originalUrl(generatedUrl.getLongUrl())
                    .shortLink(generatedUrl.getShortUrl())
                    .expirationDate(generatedUrl.getExpirationDate())
                    .build();

            return ResponseEntity
                    .created(shortenedUrl)
                    .body(response);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException();
        }
    }

    @GetMapping(value = "/{shortUrl}", produces = "application/json")
    @Operation(summary = "Get original URL", 
                description = "Get redirected to the original URL from the shortened URL")
    @ApiResponses({
        @ApiResponse(
            responseCode = "302",
            description = "Redirect to original URL",
            content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Short URL not found",
            content = @Content(schema = @Schema(implementation = UrlErrorResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "410",
            description = "URL has expired",
            content = @Content(schema = @Schema(implementation = UrlErrorResponseDto.class))
        )
    })
    public ResponseEntity<UrlResponseDto> redirectToOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) {
        try {
            Url url = urlService.getEncodedUrl(shortUrl);

            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, url.getLongUrl())
                    .build();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException();
        }
    }
}