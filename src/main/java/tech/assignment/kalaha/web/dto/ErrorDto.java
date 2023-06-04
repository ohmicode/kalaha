package tech.assignment.kalaha.web.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

public class ErrorDto {
    @ApiModelProperty(notes = "Tracer to find the problem in the logs", example = "77aff062-72e7-426b-8748-0a3ed1124fb8", required = true)
    private UUID traceId;
    @ApiModelProperty(notes = "Type of the error", example = "game", allowableValues = "global, game", required = true)
    private String type;
    @ApiModelProperty(notes = "Error message", example = "Player 13 does not exist", required = true)
    private String message;

    public ErrorDto(UUID traceId, String type, String message) {
        this.traceId = traceId;
        this.type = type;
        this.message = message;
    }

    public UUID getTraceId() {
        return traceId;
    }

    public void setTraceId(UUID traceId) {
        this.traceId = traceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
