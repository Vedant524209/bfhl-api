package com.bajaj.bfhlapi.service.impl;

import com.bajaj.bfhlapi.dto.RequestDto;
import com.bajaj.bfhlapi.dto.ResponseDto;
import com.bajaj.bfhlapi.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BfhlServiceImplTest {

    private BfhlServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BfhlServiceImpl();
    }

    @Test
    void processRequest_withNullData_throwsException() {
        RequestDto request = RequestDto.builder().data(null).build();
        assertThrows(InvalidInputException.class, () -> service.processRequest(request, "id-1"));
    }

    @Test
    void processRequest_withEmptyData_throwsException() {
        RequestDto request = RequestDto.builder().data(Collections.emptyList()).build();
        assertThrows(InvalidInputException.class, () -> service.processRequest(request, "id-2"));
    }

    @Test
    void processRequest_withAllNullValues_throwsException() {
        RequestDto request = RequestDto.builder().data(Arrays.asList(null, null)).build();
        assertThrows(InvalidInputException.class, () -> service.processRequest(request, "id-3"));
    }

    @Test
    void processRequest_withAllBlankValues_throwsException() {
        RequestDto request = RequestDto.builder().data(Arrays.asList("", "  ", "   ")).build();
        assertThrows(InvalidInputException.class, () -> service.processRequest(request, "id-4"));
    }

    @Test
    void processRequest_ignoresNullAndBlankEntries() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList(null, "", "  ", "A", "1"))
                .build();

        ResponseDto response = service.processRequest(request, "id-5");

        assertTrue(response.isSuccess());
        assertEquals(1, response.getAlphabetCount());
        assertEquals(1, response.getNumberCount());
        assertEquals(2, response.getUniqueElementCount());
    }

    @Test
    void processRequest_negativeNumbers() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("-10", "-5", "3"))
                .build();

        ResponseDto response = service.processRequest(request, "id-6");

        assertEquals(3, response.getNumberCount());
        assertEquals(new BigDecimal("-10"), response.getSmallestNumber());
        assertEquals(new BigDecimal("3"), response.getLargestNumber());
        assertEquals(new BigDecimal("-12"), response.getSum());
        assertTrue(response.getOddNumbers().contains(new BigDecimal("-5")));
        assertTrue(response.getOddNumbers().contains(new BigDecimal("3")));
        assertTrue(response.getEvenNumbers().contains(new BigDecimal("-10")));
    }

    @Test
    void processRequest_decimalNumbers() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("25.5", "-100.75", "0.5"))
                .build();

        ResponseDto response = service.processRequest(request, "id-7");

        assertEquals(3, response.getNumberCount());
        assertEquals(new BigDecimal("25.5"), response.getLargestNumber());
        assertEquals(new BigDecimal("-100.75"), response.getSmallestNumber());
        assertEquals(0, new BigDecimal("-74.75").compareTo(response.getSum()));
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
    }

    @Test
    void processRequest_mixedNegativeAndDecimal() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("-10", "25.5", "-100.75", "4"))
                .build();

        ResponseDto response = service.processRequest(request, "id-8");

        assertEquals(4, response.getNumberCount());
        assertEquals(new BigDecimal("-100.75"), response.getSmallestNumber());
        assertEquals(new BigDecimal("25.5"), response.getLargestNumber());
        assertTrue(response.getEvenNumbers().contains(new BigDecimal("-10")));
        assertTrue(response.getEvenNumbers().contains(new BigDecimal("4")));
    }

    @Test
    void processRequest_detectsDuplicates() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("A", "A", "B", "B", "1"))
                .build();

        ResponseDto response = service.processRequest(request, "id-9");

        assertTrue(response.isContainsDuplicates());
        assertEquals(3, response.getUniqueElementCount());
        assertEquals(2, response.getAlphabetCount());
        assertEquals(1, response.getNumberCount());
    }

    @Test
    void processRequest_noDuplicates() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("A", "B", "1"))
                .build();

        ResponseDto response = service.processRequest(request, "id-10");

        assertFalse(response.isContainsDuplicates());
        assertEquals(3, response.getUniqueElementCount());
    }

    @Test
    void processRequest_alphabetFrequency() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("Apple", "Ant"))
                .build();

        ResponseDto response = service.processRequest(request, "id-11");

        Map<String, Integer> freq = response.getAlphabetFrequency();
        assertEquals(2, freq.get("A"));
        assertEquals(2, freq.get("P"));
        assertEquals(1, freq.get("L"));
        assertEquals(1, freq.get("E"));
        assertEquals(1, freq.get("N"));
        assertEquals(1, freq.get("T"));
    }

    @Test
    void processRequest_alphabetFrequencyMixedCase() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("aA", "Bb"))
                .build();

        ResponseDto response = service.processRequest(request, "id-12");

        Map<String, Integer> freq = response.getAlphabetFrequency();
        assertEquals(2, freq.get("A"));
        assertEquals(2, freq.get("B"));
    }

    @Test
    void processRequest_vowelCount_singleWord() {
        RequestDto request = RequestDto.builder()
                .data(List.of("Education"))
                .build();

        ResponseDto response = service.processRequest(request, "id-13");

        assertEquals(5, response.getVowelCount());
    }

    @Test
    void processRequest_vowelCount_multipleWords() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("Apple", "Orange", "B"))
                .build();

        ResponseDto response = service.processRequest(request, "id-14");

        assertEquals(5, response.getVowelCount());
    }

    @Test
    void processRequest_vowelCount_noVowels() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("B", "C", "D"))
                .build();

        ResponseDto response = service.processRequest(request, "id-15");

        assertEquals(0, response.getVowelCount());
    }

    @Test
    void processRequest_longestAlphabeticValue() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("Hi", "Hello", "Hey"))
                .build();

        ResponseDto response = service.processRequest(request, "id-16");

        assertEquals("Hello", response.getLongestAlphabeticValue());
    }

    @Test
    void processRequest_shortestAlphabeticValue() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("Hi", "Hello", "Hey"))
                .build();

        ResponseDto response = service.processRequest(request, "id-17");

        assertEquals("Hi", response.getShortestAlphabeticValue());
    }

    @Test
    void processRequest_singleAlphabet_longestAndShortestSame() {
        RequestDto request = RequestDto.builder()
                .data(List.of("Z"))
                .build();

        ResponseDto response = service.processRequest(request, "id-18");

        assertEquals("Z", response.getLongestAlphabeticValue());
        assertEquals("Z", response.getShortestAlphabeticValue());
    }

    @Test
    void processRequest_noAlphabets_longestAndShortestNull() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("1", "2", "$"))
                .build();

        ResponseDto response = service.processRequest(request, "id-19");

        assertNull(response.getLongestAlphabeticValue());
        assertNull(response.getShortestAlphabeticValue());
    }

    @Test
    void processRequest_specialCharacters() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("$", "@", "#", "A", "1"))
                .build();

        ResponseDto response = service.processRequest(request, "id-20");

        assertEquals(3, response.getSpecialCharacterCount());
        assertTrue(response.getSpecialCharacters().containsAll(List.of("$", "@", "#")));
    }

    @Test
    void processRequest_sortedNumbers() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("5", "1", "3", "2", "4"))
                .build();

        ResponseDto response = service.processRequest(request, "id-21");

        List<BigDecimal> expected = List.of(
                new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"),
                new BigDecimal("4"), new BigDecimal("5"));
        assertEquals(expected, response.getSortedNumbers());
    }

    @Test
    void processRequest_alphanumericExtraction() {
        RequestDto request = RequestDto.builder()
                .data(List.of("A1B2"))
                .build();

        ResponseDto response = service.processRequest(request, "id-22");

        assertTrue(response.getAlphabets().contains("AB"));
        assertEquals(1, response.getNumberCount());
        assertTrue(response.getSortedNumbers().contains(new BigDecimal("12")));
    }

    @Test
    void processRequest_alphanumericMultipleEntries() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("Test123", "X9"))
                .build();

        ResponseDto response = service.processRequest(request, "id-23");

        assertTrue(response.getAlphabets().containsAll(List.of("Test", "X")));
        assertEquals(2, response.getNumberCount());
    }

    @Test
    void processRequest_requestIdPassedThrough() {
        RequestDto request = RequestDto.builder()
                .data(List.of("A"))
                .build();

        ResponseDto response = service.processRequest(request, "my-custom-id");

        assertEquals("my-custom-id", response.getRequestId());
    }

    @Test
    void processRequest_summaryPopulated() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("A", "1", null, "", "$"))
                .build();

        ResponseDto response = service.processRequest(request, "id-24");

        assertNotNull(response.getSummary());
        assertEquals(5, response.getSummary().getTotalElementsReceived());
        assertEquals(3, response.getSummary().getValidElementsProcessed());
    }

    @Test
    void processRequest_processingTimeTracked() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("A", "1"))
                .build();

        ResponseDto response = service.processRequest(request, "id-25");

        assertTrue(response.getProcessingTimeMs() >= 0);
    }

    @Test
    void processRequest_oddEvenSplit() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("1", "2", "3", "4", "5"))
                .build();

        ResponseDto response = service.processRequest(request, "id-26");

        assertEquals(3, response.getOddNumbers().size());
        assertEquals(2, response.getEvenNumbers().size());
        assertTrue(response.getOddNumbers().contains(new BigDecimal("1")));
        assertTrue(response.getEvenNumbers().contains(new BigDecimal("4")));
    }

    @Test
    void processRequest_sumCalculation() {
        RequestDto request = RequestDto.builder()
                .data(Arrays.asList("10", "20", "30"))
                .build();

        ResponseDto response = service.processRequest(request, "id-27");

        assertEquals(new BigDecimal("60"), response.getSum());
    }
}
