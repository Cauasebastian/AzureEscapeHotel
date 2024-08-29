package org.sebastiandev.azureescapehotel.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Component
public class BlobSerializer extends JsonSerializer<Blob> {

    @Override
    public void serialize(Blob value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            byte[] bytes = value.getBytes(1, (int) value.length());
            String base64String = Base64.encodeBase64String(bytes);
            gen.writeString(base64String);
        } catch (SQLException e) {
            throw new IOException("Error serializing Blob", e);
        }
    }
}
