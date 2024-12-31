package com.example.url_shortening.url.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.url_shortening.url.dto.UrlDto;
import com.example.url_shortening.url.dto.UrlErrorResponseDto;
import com.example.url_shortening.url.dto.UrlResponseDto;
import com.example.url_shortening.exception.BaseException;
import com.example.url_shortening.url.exception.UrlErrorCode;
import com.example.url_shortening.url.model.Url;
import com.example.url_shortening.url.service.UrlService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
            Url shortenedUrl = urlService.generateShortLink(urlDto);
            UrlResponseDto response = UrlResponseDto.builder()
                    .originalUrl(shortenedUrl.getLongUrl())
                    .shortLink(shortenedUrl.getShortUrl())
                    .expirationDate(shortenedUrl.getExpirationDate())
                    .build();
            return ResponseEntity.ok(response);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(UrlErrorCode.SYSTEM_ERROR);
        }
    }

    @GetMapping(value = "/{shortUrl}", produces = "application/json")
    @Operation(summary = "Get original URL", 
                description = "Get the original URL from the shortened URL")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Original URL found",
            content = @Content(schema = @Schema(implementation = UrlResponseDto.class))
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

                response.sendRedirect(url.getLongUrl());

            return null;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(UrlErrorCode.SYSTEM_ERROR);
        }
    }
}