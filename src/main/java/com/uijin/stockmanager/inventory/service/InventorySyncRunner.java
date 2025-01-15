package com.uijin.stockmanager.inventory.service;

import com.uijin.stockmanager.inventory.entity.InventoryEntity;
import com.uijin.stockmanager.inventory.model.Inventory;
import com.uijin.stockmanager.inventory.repository.InventoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventorySyncRunner implements CommandLineRunner {

  private final InventoryEntityRepository inventoryEntityRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void run(String... args) throws Exception {
    List<InventoryEntity> inventories = inventoryEntityRepository.findAll();
    for (InventoryEntity inventory : inventories) {
      Inventory inventoryDTO = Inventory.from(inventory);
      redisTemplate.opsForHash().put("inventory:details", inventory.getInventoryId(), inventoryDTO);
    }
  }
}
