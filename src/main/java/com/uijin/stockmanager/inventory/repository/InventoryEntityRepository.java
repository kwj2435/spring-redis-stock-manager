package com.uijin.stockmanager.inventory.repository;

import com.uijin.stockmanager.inventory.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryEntityRepository extends JpaRepository<InventoryEntity, Long> {

  int updateInventoryStock(long inventoryId, int stock);
}
