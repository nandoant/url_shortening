package com.example.url_shortening.url.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "UrlErrorResponseDto", description = "Response DTO for error response")
public class UrlErrorResponseDto {
    @Schema(description = "Status of the response",
            example = "400")
    private String status;

    @Schema(description = "Error message",
            example = "Invalid URL")
    private String error;

    public UrlErrorResponseDto(String status, String error) {
        this.status = status;
        this.error = error;
    }

    public UrlErrorResponseDto() {
    }

    public UrlErrorResponseDto(int status, String error) {
        this.status = String.valueOf(status);
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                "status='" + status + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
