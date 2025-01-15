package com.uijin.stockmanager.external.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uijin.stockmanager.inventory.repository.InventoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryConsumer {

  private final InventoryEntityRepository inventoryEntityRepository;

  @KafkaListener(topics = "stock-updates", groupId = "stock-sync-group")
  public void consume(String message) {
    System.out.println("Received message: " + message);

    ObjectMapper mapper = new ObjectMapper();
    try {
      StockUpdate stockUpdate = mapper.readValue(message, StockUpdate.class);
      inventoryEntityRepository.updateStock(stockUpdate.getProductId(), stockUpdate.getChange());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
