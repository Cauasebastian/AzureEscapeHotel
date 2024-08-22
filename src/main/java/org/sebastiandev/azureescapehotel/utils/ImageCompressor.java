package org.sebastiandev.azureescapehotel.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.zip.Deflater;

@Component
public class ImageCompressor {

    public byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        byte[] compressedData = new byte[1024];
        int compressedDataLength = deflater.deflate(compressedData);
        deflater.end();
        return java.util.Arrays.copyOf(compressedData, compressedDataLength);
    }
}
