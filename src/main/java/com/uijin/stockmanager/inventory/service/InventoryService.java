package com.uijin.stockmanager.inventory.service;

import com.uijin.stockmanager.inventory.model.InventoryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final RedisTemplate<String, Object> redisTemplate;

  public InventoryModel.InventoryResponse getInventory(long inventoryId) {
    InventoryModel.Inventory inventory =
            (InventoryModel.Inventory) redisTemplate.opsForHash().get("inventory:details", inventoryId);

    return InventoryModel.InventoryResponse.of(inventoryId, inventory);
  }
}
