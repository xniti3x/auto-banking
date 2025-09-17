package com.example.autobanking.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openapitools.client.model.AccountTransactions;
import org.openapitools.client.model.TransactionSchema;
import com.example.autobanking.entity.TransactionEntity;
import com.example.autobanking.mapper.TransactionMapper;
import com.example.autobanking.mapper.TransactionMapperImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.io.InputStreamReader;
import java.time.OffsetDateTime;

import com.google.gson.JsonSerializer;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

class BankServiceTest {

    @Test
void testDtoToEntityMapping() throws Exception {
    // Create Gson with custom OffsetDateTime adapter
    Gson gson = new GsonBuilder()
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
        .create();
    
    // Read JSON from file
    InputStream is = getClass().getClassLoader().getResourceAsStream("transactions.json");
    
    // Use InputStreamReader with Gson instead of ObjectMapper
    AccountTransactions accountTransactions = gson.fromJson(new InputStreamReader(is), AccountTransactions.class);
    
    List<TransactionSchema> booked = accountTransactions.getTransactions().getBooked();
    List<TransactionEntity> entities = new ArrayList<>();
        TransactionMapper tMapper = new TransactionMapperImpl(); 
    for (TransactionSchema tx : booked) {
        TransactionEntity entity = tMapper.toEntity(tx);
        entities.add(entity);
    }
    assertTrue(!entities.isEmpty());
}

}
