package com.bajaj.bfhlapi.service;

import com.bajaj.bfhlapi.dto.RequestDto;
import com.bajaj.bfhlapi.dto.ResponseDto;

public interface BfhlService {

    ResponseDto processRequest(RequestDto request, String requestId);
}
