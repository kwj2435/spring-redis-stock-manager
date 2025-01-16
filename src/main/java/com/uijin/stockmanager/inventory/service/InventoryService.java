package com.uijin.stockmanager.inventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uijin.stockmanager.common.enums.ApiExceptionCode;
import com.uijin.stockmanager.common.model.ApiException;
import com.uijin.stockmanager.external.kafka.service.InventoryProducer;
import com.uijin.stockmanager.inventory.model.InventoryModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static com.uijin.stockmanager.external.kafka.constant.InventoryConstant.INVENTORY_UPDATE_TOPIC;
import static com.uijin.stockmanager.external.redis.constant.RedisKeyConstant.INVENTORY_DETAILS_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

  private final RedisTemplate<String, Object> redisTemplate;
  private final InventoryProducer inventoryProducer;

  private final ObjectMapper mapper = new ObjectMapper();

  public InventoryModel.InventoryResponse getInventory(long inventoryId) {
    InventoryModel.Inventory inventory = getInventoryFromRedis(inventoryId);

    return InventoryModel.InventoryResponse.of(inventoryId, inventory);
  }

  public InventoryModel.InventoryResponse decreaseInventoryStock(long inventoryId) {
    InventoryModel.Inventory inventory = getInventoryFromRedis(inventoryId);
    inventory.decreaseStock(1);

    redisTemplate.opsForHash().put(
            INVENTORY_DETAILS_KEY,
            String.valueOf(inventoryId),
            inventory
    );

    InventoryModel.InventoryResponse response =
            InventoryModel.InventoryResponse.of(inventoryId, inventory);

    // kafka 재고 DB 동기화 이벤트 발행
    publishInventoryUpdateEvent(response);

    return response;
  }

  private void publishInventoryUpdateEvent(InventoryModel.InventoryResponse response) {
    try {
      String responseToJson = mapper.writeValueAsString(response);
      inventoryProducer.sendMessage(INVENTORY_UPDATE_TOPIC, responseToJson);
    } catch (Exception e) {
      log.error("Inventory Stock Update Fail");
      // DLQ 전달 로직 추가 필요
    }
  }

  private InventoryModel.Inventory getInventoryFromRedis(long inventoryId) {
    InventoryModel.Inventory inventory =
            (InventoryModel.Inventory) redisTemplate.opsForHash()
                    .get(INVENTORY_DETAILS_KEY, String.valueOf(inventoryId));

    if(inventory == null) {
      throw ApiException.from(ApiExceptionCode.ERR_400_10001);
    }
    return inventory;
  }
}
