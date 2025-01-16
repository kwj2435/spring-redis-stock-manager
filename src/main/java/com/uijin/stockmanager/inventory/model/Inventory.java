package com.uijin.stockmanager.inventory.model;

import com.uijin.stockmanager.inventory.entity.InventoryEntity;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class Inventory{
  /** 상품 ID */
  private final long inventoryId;
  /** 상품 명 */
  private final String productName;
  /** 상품 수량 */
  private final int stockQuantity;

  private Inventory(long inventoryId, String productName, int stockQuantity) {
    this.inventoryId = inventoryId;
    this.productName = productName;
    this.stockQuantity = stockQuantity;
  }

  public static Inventory from(InventoryEntity inventory) {
    return new Inventory(
            inventory.getInventoryId(),
            inventory.getProductName(),
            inventory.getStockQuantity()
    );
  }
}
