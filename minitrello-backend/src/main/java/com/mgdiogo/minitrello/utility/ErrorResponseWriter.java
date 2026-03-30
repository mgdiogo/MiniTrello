package com.mgdiogo.minitrello.utility;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgdiogo.minitrello.dtos.responses.ErrorResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ErrorResponseWriter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final ObjectMapper objectMapper;

    public void write(
            HttpServletResponse response,
            int status,
            String error,
            String message,
            String field) throws IOException {

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now().format(FORMATTER),
                status,
                error,
                message,
                field);

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
