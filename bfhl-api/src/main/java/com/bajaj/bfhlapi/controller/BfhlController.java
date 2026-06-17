package com.bajaj.bfhlapi.controller;

import com.bajaj.bfhlapi.dto.RequestDto;
import com.bajaj.bfhlapi.dto.ResponseDto;
import com.bajaj.bfhlapi.service.BfhlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/bfhl")
@RequiredArgsConstructor
public class BfhlController {

    private final BfhlService bfhlService;

    @PostMapping
    public ResponseEntity<ResponseDto> processRequest(
            @RequestHeader(value = "X-Request-Id", required = false) String requestId,
            @Valid @RequestBody RequestDto request) {

        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        ResponseDto response = bfhlService.processRequest(request, requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);

        return ResponseEntity.ok().headers(headers).body(response);
    }
}
