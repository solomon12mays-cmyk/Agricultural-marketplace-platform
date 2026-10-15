package com.agricultural.marketplace.organization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UploadDocumentRequest(
        @NotBlank(message = "Document type is required")
        @Size(max = 64, message = "Document type must not exceed 64 characters")
        String documentType,

        @NotBlank(message = "Storage key / URI is required")
        @Size(max = 255, message = "Storage key must not exceed 255 characters")
        String storageKey
) {
}

