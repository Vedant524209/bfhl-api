package com.bajaj.bfhlapi.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class DataProcessor {

    private DataProcessor() {
    }

    public static List<String> extractNumbers(List<String> data) {
        return data.stream()
                .filter(s -> s.matches("-?\\d+"))
                .collect(Collectors.toList());
    }

    public static List<String> extractAlphabets(List<String> data) {
        return data.stream()
                .filter(s -> s.matches("[a-zA-Z]"))
                .collect(Collectors.toList());
    }

    public static List<String> findHighestLowercaseAlphabet(List<String> alphabets) {
        return alphabets.stream()
                .filter(s -> s.matches("[a-z]"))
                .max(String::compareTo)
                .map(List::of)
                .orElse(Collections.emptyList());
    }

    public static boolean containsPrime(List<String> numbers) {
        return numbers.stream()
                .map(Integer::parseInt)
                .anyMatch(DataProcessor::isPrime);
    }

    private static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
