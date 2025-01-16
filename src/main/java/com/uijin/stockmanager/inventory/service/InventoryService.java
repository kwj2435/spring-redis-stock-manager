package com.uijin.stockmanager.inventory.service;

import com.uijin.stockmanager.common.enums.ApiExceptionCode;
import com.uijin.stockmanager.common.model.ApiException;
import com.uijin.stockmanager.inventory.model.InventoryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static com.uijin.stockmanager.external.redis.constant.RedisKeyConstant.INVENTORY_DETAILS_KEY;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final RedisTemplate<String, Object> redisTemplate;

  public InventoryModel.InventoryResponse getInventory(long inventoryId) {
    InventoryModel.Inventory inventory = getInventoryFromRedis(inventoryId);

    return InventoryModel.InventoryResponse.of(inventoryId, inventory);
  }

  public InventoryModel.InventoryResponse decreaseInventoryStock(long inventoryId) {
    InventoryModel.Inventory inventory = getInventoryFromRedis(inventoryId);

    if(inventory.getStockQuantity() == 0) {
      throw ApiException.from(ApiExceptionCode.ERR_409_10001);
    }

    redisTemplate.opsForHash().put(
            INVENTORY_DETAILS_KEY,
            String.valueOf(inventoryId),
            inventory.getStockQuantity() - 1
    );
    
    // kafka 재고 DB 동기화 이벤트 발행
    
    return InventoryModel.InventoryResponse.of(inventoryId, inventory);
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
