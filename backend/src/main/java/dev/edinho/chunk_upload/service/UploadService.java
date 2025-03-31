package dev.edinho.chunk_upload.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

import org.springframework.stereotype.Service;

@Service
public class UploadService {
    String chunks = "chunks";
    String output = "output";

    public UploadService() {
        String[] paths = {chunks, output};
 
        for (String p : paths) {
            File f = new File(p);
            if (!f.exists()) f.mkdirs();
        }
    }

    public void writeChunk(String chunk_id, String item_id, byte[] chunk_data) throws IOException {
        File chunk_dir = Path.of(chunks, item_id).toFile();
        chunk_dir.mkdir();
    
        File chunk_file = Path.of(chunks, item_id, chunk_id).toFile();

        if (!chunk_file.exists()) chunk_file.createNewFile();

        FileOutputStream fos = new FileOutputStream(chunk_file);
        
        fos.write(chunk_data);

        fos.close();
    }

    public void mountChunk(String item_id) throws IOException {
        File chunk_dir = Path.of(chunks, item_id).toFile();
        File[] chunks_files = chunk_dir.listFiles();
        Arrays.sort(chunks_files, Comparator.comparing(File::getName));

        File output_file = Path.of(output, item_id).toFile();
        output_file.createNewFile();

        FileOutputStream fos = new FileOutputStream(output_file);
        
        for (File chunk: chunks_files) {
            FileInputStream fis = new FileInputStream(chunk);
            fos.write(fis.readAllBytes());
            fis.close();
        }
        fos.close();
    }
}
