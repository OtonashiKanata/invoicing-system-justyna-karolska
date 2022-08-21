package pl.futurecollars.invoicing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.futurecollars.invoicing.model.Invoice;

public class JsonService {

  private final ObjectMapper objectMapper;

  public JsonService() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public String objectToString(Invoice invoice) {
    try {
      return objectMapper.writeValueAsString(invoice) + "\n";
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Serialization from object to json string failed", e);
    }
  }

  public Invoice stringToObject(String objectAsString) {
    try {
      return objectMapper.readValue(objectAsString, Invoice.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Serialization from string to object failed", e);
    }
  }
}
