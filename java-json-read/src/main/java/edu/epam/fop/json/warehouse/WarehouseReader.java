package edu.epam.fop.json.warehouse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.epam.fop.json.warehouse.item.Item;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface WarehouseReader {

  Collection<Item> readItems(InputStream data);

  static WarehouseReader getInstance() {
    return new WarehouseReader() {
      @Override
      public Collection<Item> readItems(InputStream data) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Item> items = new ArrayList<>();
        try {
          items = objectMapper.readValue(data,
                  objectMapper.getTypeFactory().constructCollectionType(List.class, Item.class));
        } catch (JsonParseException e) {
          e.printStackTrace();
        } catch (JsonMappingException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return items;
      }
    };
  }
}