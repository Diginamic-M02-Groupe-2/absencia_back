package com.absencia.diginamic.serializer;

import com.absencia.diginamic.dto.ErrorResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ErrorResponseSerializer extends JsonSerializer<ErrorResponse> {

    @Override
    public void serialize(ErrorResponse errorResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("message", errorResponse.getMessage());
        jsonGenerator.writeEndObject();
    }
}

