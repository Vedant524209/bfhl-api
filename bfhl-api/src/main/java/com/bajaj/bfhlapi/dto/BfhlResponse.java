package com.bajaj.bfhlapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BfhlResponse {

    @JsonProperty("is_success")
    private boolean success;

    @JsonProperty("user_id")
    private String userId;

    private String email;

    @JsonProperty("roll_number")
    private String rollNumber;

    private List<String> numbers;

    private List<String> alphabets;

    @JsonProperty("highest_lowercase_alphabet")
    private List<String> highestLowercaseAlphabet;

    @JsonProperty("is_prime_found")
    private boolean primeFound;

    @JsonProperty("file_valid")
    private boolean fileValid;

    @JsonProperty("file_mime_type")
    private String fileMimeType;

    @JsonProperty("file_size_kb")
    private String fileSizeKb;
}
