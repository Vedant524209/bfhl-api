package com.bajaj.bfhlapi.service.impl;

import com.bajaj.bfhlapi.dto.RequestDto;
import com.bajaj.bfhlapi.dto.ResponseDto;
import com.bajaj.bfhlapi.dto.SummaryDto;
import com.bajaj.bfhlapi.exception.InvalidInputException;
import com.bajaj.bfhlapi.service.BfhlService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class BfhlServiceImpl implements BfhlService {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");
    private static final Pattern ALPHA_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern SPECIAL_PATTERN = Pattern.compile("^[^a-zA-Z0-9]+$");
    private static final Set<Character> VOWELS = Set.of('A', 'E', 'I', 'O', 'U');

    @Override
    public ResponseDto processRequest(RequestDto request, String requestId) {
        long startTime = System.nanoTime();

        if (request.getData() == null || request.getData().isEmpty()) {
            throw new InvalidInputException("data must not be null or empty");
        }

        List<String> rawData = request.getData();
        int totalReceived = rawData.size();

        Set<String> seen = new HashSet<>();
        boolean containsDuplicates = false;
        List<String> uniqueData = new ArrayList<>();

        for (String item : rawData) {
            if (item == null || item.isBlank()) {
                continue;
            }
            String trimmed = item.trim();
            if (!seen.add(trimmed)) {
                containsDuplicates = true;
            } else {
                uniqueData.add(trimmed);
            }
        }

        if (uniqueData.isEmpty()) {
            throw new InvalidInputException("data must contain at least one valid element");
        }

        List<BigDecimal> numbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        Map<String, Integer> alphabetFrequency = new LinkedHashMap<>();
        int vowelCount = 0;
        int invalidCount = 0;

        for (String value : uniqueData) {
            if (NUMBER_PATTERN.matcher(value).matches()) {
                numbers.add(new BigDecimal(value));
            } else if (ALPHA_PATTERN.matcher(value).matches()) {
                alphabets.add(value);
                vowelCount += countVowels(value);
                trackFrequency(value, alphabetFrequency);
            } else if (SPECIAL_PATTERN.matcher(value).matches()) {
                specialCharacters.add(value);
            } else if (isAlphanumeric(value)) {
                StringBuilder letters = new StringBuilder();
                StringBuilder digits = new StringBuilder();
                boolean hasNegative = false;

                for (int i = 0; i < value.length(); i++) {
                    char c = value.charAt(i);
                    if (Character.isLetter(c)) {
                        letters.append(c);
                    } else if (Character.isDigit(c)) {
                        digits.append(c);
                    } else if (c == '-' && digits.isEmpty()) {
                        hasNegative = true;
                    }
                }

                if (!letters.isEmpty()) {
                    String letterStr = letters.toString();
                    alphabets.add(letterStr);
                    vowelCount += countVowels(letterStr);
                    trackFrequency(letterStr, alphabetFrequency);
                }
                if (!digits.isEmpty()) {
                    String numStr = hasNegative ? "-" + digits : digits.toString();
                    numbers.add(new BigDecimal(numStr));
                }
            } else {
                invalidCount++;
            }
        }

        List<BigDecimal> oddNumbers = new ArrayList<>();
        List<BigDecimal> evenNumbers = new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal largest = null;
        BigDecimal smallest = null;

        for (BigDecimal num : numbers) {
            sum = sum.add(num);

            if (largest == null || num.compareTo(largest) > 0) {
                largest = num;
            }
            if (smallest == null || num.compareTo(smallest) < 0) {
                smallest = num;
            }

            if (isWholeNumber(num)) {
                if (num.toBigInteger().mod(java.math.BigInteger.TWO).equals(java.math.BigInteger.ZERO)) {
                    evenNumbers.add(num);
                } else {
                    oddNumbers.add(num);
                }
            }
        }

        List<BigDecimal> sortedNumbers = new ArrayList<>(numbers);
        sortedNumbers.sort(Comparator.naturalOrder());

        String longest = null;
        String shortest = null;
        for (String alpha : alphabets) {
            if (longest == null || alpha.length() > longest.length()) {
                longest = alpha;
            }
            if (shortest == null || alpha.length() < shortest.length()) {
                shortest = alpha;
            }
        }

        int validProcessed = uniqueData.size() - invalidCount;

        SummaryDto summary = SummaryDto.builder()
                .totalElementsReceived(totalReceived)
                .validElementsProcessed(validProcessed)
                .invalidElementsIgnored(totalReceived - validProcessed)
                .build();

        long processingTimeMs = (System.nanoTime() - startTime) / 1_000_000;

        return ResponseDto.builder()
                .isSuccess(true)
                .requestId(requestId)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialCharacters)
                .sum(sum)
                .largestNumber(largest)
                .smallestNumber(smallest)
                .alphabetCount(alphabets.size())
                .numberCount(numbers.size())
                .specialCharacterCount(specialCharacters.size())
                .containsDuplicates(containsDuplicates)
                .uniqueElementCount(uniqueData.size())
                .sortedNumbers(sortedNumbers)
                .vowelCount(vowelCount)
                .alphabetFrequency(alphabetFrequency)
                .longestAlphabeticValue(longest)
                .shortestAlphabeticValue(shortest)
                .processingTimeMs(processingTimeMs)
                .summary(summary)
                .build();
    }

    private int countVowels(String value) {
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (VOWELS.contains(Character.toUpperCase(value.charAt(i)))) {
                count++;
            }
        }
        return count;
    }

    private void trackFrequency(String value, Map<String, Integer> frequency) {
        for (int i = 0; i < value.length(); i++) {
            String ch = String.valueOf(Character.toUpperCase(value.charAt(i)));
            frequency.merge(ch, 1, Integer::sum);
        }
    }

    private boolean isAlphanumeric(String value) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (hasLetter && hasDigit) return true;
        }
        return false;
    }

    private boolean isWholeNumber(BigDecimal num) {
        return num.stripTrailingZeros().scale() <= 0;
    }
}
