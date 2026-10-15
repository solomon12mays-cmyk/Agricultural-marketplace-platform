package com.agricultural.marketplace.document.dto;

public record DocumentUploadResponse(
        String storageKey,
        String fileName,
        String contentType,
        long sizeBytes
) {
}

