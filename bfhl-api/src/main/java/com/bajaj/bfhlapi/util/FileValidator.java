package com.bajaj.bfhlapi.util;

import java.util.Base64;

public final class FileValidator {

    private FileValidator() {
    }

    public static boolean isValidBase64(String base64String) {
        try {
            Base64.getDecoder().decode(base64String);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String getMimeType(String base64String) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64String);
            if (bytes.length >= 4) {
                if (bytes[0] == (byte) 0x89 && bytes[1] == 0x50) return "image/png";
                if (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8) return "image/jpeg";
                if (bytes[0] == 0x25 && bytes[1] == 0x50) return "application/pdf";
            }
            return "application/octet-stream";
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFileSizeKb(String base64String) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64String);
            double sizeKb = bytes.length / 1024.0;
            return String.format("%.2f", sizeKb);
        } catch (Exception e) {
            return "0";
        }
    }
}
