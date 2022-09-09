package pl.futurecollars.invoicing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.model.Invoice;

@Service
public class JsonService {

  private final ObjectMapper objectMapper;

  {
    objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public String objectToString(Invoice invoice) {
    try {
      return objectMapper.writeValueAsString(invoice) + "\n";
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Serialization from object to json string failed", e);
    }
  }

  public <T> T stringToObject(String json, Class<T> clas) {
    try {
      return objectMapper.readValue(json, clas);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Serialization from string to object failed", e);
    }
  }
}
