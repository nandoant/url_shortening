package com.example.url_shortening.url.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "UrlErrorResponseDto", description = "Response DTO for error response")
public class UrlErrorResponseDto {
    @Schema(description = "Status of the response",
            example = "400")
    private String statusCode;

    @Schema(description = "Error message",
            example = "Invalid URL")
    private String error;

    public UrlErrorResponseDto(String statusCode, String error) {
        this.statusCode = statusCode;
        this.error = error;
    }

    public UrlErrorResponseDto() {
    }

    public UrlErrorResponseDto(int statusCode, String error) {
        this.statusCode = String.valueOf(statusCode);
        this.error = error;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "UrlErrorResponseDto{" +
                "status='" + statusCode + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
