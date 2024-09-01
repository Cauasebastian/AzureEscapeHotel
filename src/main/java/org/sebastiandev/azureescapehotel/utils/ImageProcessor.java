package org.sebastiandev.azureescapehotel.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.DataFormatException;

@Component
public class ImageProcessor {

    // Redimensiona a imagem para um máximo de 1080p
    public byte[] resizeImage(byte[] data) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Thumbnails.of(inputStream)
                    .size(1920, 1080)  // Limite de 1080p
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        }
    }

    // Comprime a imagem usando Deflater
    public byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        } finally {
            deflater.end();
        }
    }

    // Descomprime a imagem usando Inflater
    public byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        } finally {
            inflater.end();
        }
    }
}
