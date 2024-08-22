package org.sebastiandev.azureescapehotel.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@Component
public class ImageDecompressor {

    public byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        byte[] decompressedData = new byte[1024];
        int decompressedDataLength = inflater.inflate(decompressedData);
        inflater.end();
        return java.util.Arrays.copyOf(decompressedData, decompressedDataLength);
    }
}
