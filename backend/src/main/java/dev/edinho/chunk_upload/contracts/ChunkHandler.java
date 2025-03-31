package dev.edinho.chunk_upload.contracts;

public interface ChunkHandler {
    void writeChunk();
    void mountChunk();
}