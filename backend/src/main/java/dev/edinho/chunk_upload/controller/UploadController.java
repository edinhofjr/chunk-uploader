package dev.edinho.chunk_upload.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.edinho.chunk_upload.service.UploadService;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UploadController {
    @Autowired
    UploadService uploadService;

    @PostMapping("upload")
    public ResponseEntity<?> uploadChunk(
        @RequestHeader("Chunk-Index") String chunkIndex,
        @RequestHeader("Chunk-Total") int chunkTotal,
        @RequestHeader("Upload-ID") UUID uploadId,
        @RequestBody byte[] chunk
    ) throws IOException {
        uploadService.writeChunk(chunkIndex, uploadId.toString(), chunk);
        return ResponseEntity.ok().build();
    }

    @GetMapping("upload-id")
    public ResponseEntity<?> getUploadId() {
        return ResponseEntity.ok(UUID.randomUUID().toString());
    }

    @PostMapping("upload-finalize")
    public ResponseEntity<?> mountFile(
        @RequestHeader("Upload-ID") UUID uploadId
    ) throws IOException {
        uploadService.mountChunk(uploadId.toString());
        return ResponseEntity.ok(uploadId);
    }
}
