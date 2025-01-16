package com.uijin.stockmanager.inventory.service;

import com.uijin.stockmanager.inventory.model.InventoryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

import static com.uijin.stockmanager.external.redis.constant.RedisKeyConstant.INVENTORY_DETAILS_KEY;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final RedisTemplate<String, Object> redisTemplate;

  public InventoryModel.InventoryResponse getInventory(long inventoryId) {
    InventoryModel.Inventory inventory =
            (InventoryModel.Inventory) redisTemplate.opsForHash()
                    .get(INVENTORY_DETAILS_KEY, String.valueOf(inventoryId));

    if(inventory == null) {
      throw new InvalidParameterException();
    }

    return InventoryModel.InventoryResponse.of(inventoryId, inventory);
  }
}
