package com.example.url_shortening.url.controller;

import com.example.url_shortening.url.model.Url;
import com.example.url_shortening.url.dto.UrlDto;
import com.example.url_shortening.url.dto.UrlErrorResponseDto;
import com.example.url_shortening.url.dto.UrlResponseDto;
import com.example.url_shortening.url.service.UrlService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "URL Shortening API", 
     description = "API for managing URL shortening operations including creation and redirection")
public class UrlShorteningController {

    private static final String URL_NOT_FOUND = "URL does not exist or it might have expired.";
    private static final String INVALID_URL = "Invalid URL. Please try again.";
    private static final String URL_EXPIRED = "URL has expired.";

    private final UrlService urlService;

    @PostMapping("/shorten")
    @Operation(
        summary = "Generate short URL",
        description = "Creates a shortened version of a provided URL with optional expiration time and custom short link"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "URL successfully shortened",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UrlResponseDto.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "originalUrl": "https://www.example.com",
                      "shortLink": "abc123",
                      "expirationDate": "2024-02-01T12:00:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid URL or request parameters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UrlErrorResponseDto.class)
            )
        )
    })
    public ResponseEntity<Object> generateShortLink(
        @Valid @RequestBody 
        @Parameter(description = "URL details for shortening", required = true)
        UrlDto urlDto) throws Exception {
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