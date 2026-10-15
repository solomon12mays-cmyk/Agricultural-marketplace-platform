package com.agricultural.marketplace.document.controller;

import java.io.IOException;
import java.util.UUID;

import com.agricultural.marketplace.document.dto.DocumentUploadResponse;
import com.agricultural.marketplace.document.storage.ObjectStorageService;
import com.agricultural.marketplace.shared.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final ObjectStorageService objectStorageService;

    public DocumentController(ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DocumentUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file) throws IOException {
        String key = "docs/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        String storageKey = objectStorageService.put(key, file.getBytes(), file.getContentType());

        DocumentUploadResponse response = new DocumentUploadResponse(
                storageKey,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{key}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String key) {
        byte[] data = objectStorageService.get(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}

