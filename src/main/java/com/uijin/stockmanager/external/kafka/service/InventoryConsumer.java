package com.uijin.stockmanager.external.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uijin.stockmanager.inventory.model.Inventory;
import com.uijin.stockmanager.inventory.repository.InventoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryConsumer {

  private final InventoryEntityRepository inventoryEntityRepository;
  private final ObjectMapper mapper = new ObjectMapper();

  @KafkaListener(topics = "stock-updates")
  public void consumeStockUpdateEvent(String message) throws JsonProcessingException {
    Inventory inventory = parseMessageToInventory(message);
    inventoryEntityRepository.updateInventoryStock(
            inventory.getInventoryId(),
            inventory.getStockQuantity()
    );

    // JsonProcessingException 발생시 DLQ로 전달 로직 추가 필요
  }

  private Inventory parseMessageToInventory(String message) throws JsonProcessingException {
    return mapper.readValue(message, Inventory.class);
  }
}
