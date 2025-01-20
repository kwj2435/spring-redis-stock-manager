package com.uijin.stockmanager.inventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uijin.stockmanager.common.enums.ApiExceptionCode;
import com.uijin.stockmanager.common.model.ApiException;
import com.uijin.stockmanager.external.kafka.service.InventoryProducer;
import com.uijin.stockmanager.inventory.model.InventoryModel;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonCache;
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
  private final RedissonClient redissonClient;

  private final ObjectMapper mapper = new ObjectMapper();

  public InventoryModel.InventoryResponse getInventory(long inventoryId) {
    InventoryModel.Inventory inventory = getInventoryFromRedis(inventoryId);

    return InventoryModel.InventoryResponse.of(inventoryId, inventory);
  }

  public InventoryModel.InventoryResponse decreaseInventoryStock(long inventoryId) {
    String lockKey = INVENTORY_DETAILS_KEY + inventoryId;
    RLock lock = redissonClient.getLock(lockKey);

    try {
      if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
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
      } else {
        // 1. 재고 감소 RetryQueue
        // 2. ReturyQueue 실패시 Dead Letter Queue 전송을 통한 수기처리
        throw ApiException.from(ApiExceptionCode.ERR_409_10002);
      }
    } catch (Exception e) {
      // 1. 재고 감소 여부를 확인할 수 없으므로 Dead Letter Queue로 전달하여 직저 확인
      throw ApiException.from(ApiExceptionCode.ERR_409_10002);
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
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
