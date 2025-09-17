package com.example.autobanking.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<?>, String> {

    private final Gson gson = new Gson();

    @Override
    public String convertToDatabaseColumn(List<?> attribute) {
        if (attribute == null) return "[]";
        return gson.toJson(attribute);
    }

    @Override
    public List<?> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return new ArrayList<>();
        Type type = new TypeToken<List<Object>>() {}.getType();
        return gson.fromJson(dbData, type);
    }
}
