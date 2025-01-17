package com.uijin.stockmanager.external.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uijin.stockmanager.common.enums.ApiExceptionCode;
import com.uijin.stockmanager.common.model.ApiException;
import com.uijin.stockmanager.inventory.entity.InventoryEntity;
import com.uijin.stockmanager.inventory.model.Inventory;
import com.uijin.stockmanager.inventory.repository.InventoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryConsumer {

  private final InventoryEntityRepository inventoryEntityRepository;
  private final ObjectMapper mapper = new ObjectMapper();

  @Transactional
  @KafkaListener(topics = "stock-updates")
  public void consumeStockUpdateEvent(String message) throws JsonProcessingException {
    Inventory inventory = parseMessageToInventory(message);

    InventoryEntity inventoryEntity =
            inventoryEntityRepository.findById(inventory.getInventoryId())
                    .orElseThrow(() -> ApiException.from(ApiExceptionCode.ERR_500_10001));
    inventoryEntity.updateStockQuantity(inventory.getStockQuantity());

    // todo JsonProcessingException 발생시 DLQ로 전달 로직 추가 필요
  }

  private Inventory parseMessageToInventory(String message) throws JsonProcessingException {
    return mapper.readValue(message, Inventory.class);
  }
}
