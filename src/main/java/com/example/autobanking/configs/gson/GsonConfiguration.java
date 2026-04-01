package com.example.autobanking.configs.gson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfiguration {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime.class, new JsonDeserializer<OffsetDateTime>() {
            @Override
            public OffsetDateTime deserialize(JsonElement json, Type typeOfT, 
                    JsonDeserializationContext context) throws JsonParseException {
                return OffsetDateTime.parse(json.getAsString());
            }
        })
        .registerTypeAdapter(OffsetDateTime.class, new JsonSerializer<OffsetDateTime>() {
            @Override
            public JsonElement serialize(OffsetDateTime src, Type typeOfSrc, 
                    JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());
            }
        })

        .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc,
                                         JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());
            }
        })
        .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT,
                                             JsonDeserializationContext context) {
                return LocalDateTime.parse(json.getAsString());
            }
        })
        .create();
    }
}

