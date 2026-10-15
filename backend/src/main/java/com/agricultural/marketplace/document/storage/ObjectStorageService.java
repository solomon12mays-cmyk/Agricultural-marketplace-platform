package com.agricultural.marketplace.document.storage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Stores uploaded documents and images in object storage.
 */
@Service
public class ObjectStorageService {

    private static final Logger log = LoggerFactory.getLogger(ObjectStorageService.class);
    private final Map<String, byte[]> storageMap = new ConcurrentHashMap<>();

    public String put(String key, byte[] content, String contentType) {
        String effectiveKey = (key != null && !key.isBlank()) ? key : "doc-" + UUID.randomUUID();
        storageMap.put(effectiveKey, content != null ? content : new byte[0]);
        log.info("Stored object with key '{}', content-type '{}', size {} bytes",
                effectiveKey, contentType, content != null ? content.length : 0);
        return effectiveKey;
    }

    public byte[] get(String key) {
        log.info("Retrieving object with key '{}'", key);
        return storageMap.getOrDefault(key, new byte[0]);
    }

    public void delete(String key) {
        log.info("Deleting object with key '{}'", key);
        storageMap.remove(key);
    }
}
