package com.uijin.stockmanager.inventory.model;

import com.uijin.stockmanager.inventory.entity.InventoryEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InventoryModel{

  @Getter
  public static class Inventory {
    /** 상품 명 */
    private String productName;

    /** 상품 명 */
    private int stockQuantity;

    private Inventory(String productName, int stockQuantity) {
      this.productName = productName;
      this.stockQuantity = stockQuantity;
    }

    public static Inventory from(InventoryEntity inventory) {
      return new Inventory(inventory.getProductName(), inventory.getStockQuantity());
    }
  }

  @Getter
  public static class InventoryResponse {
    /** 상품 ID */
    private long inventoryId;

    /** 상품 명 */
    private String productName;

    /** 상품 수량 */
    private int stockQuantity;

    @Builder
    private InventoryResponse(long inventoryId, String productName, int stockQuantity) {
      this.inventoryId = inventoryId;
      this.productName = productName;
      this.stockQuantity = stockQuantity;
    }

    public static InventoryResponse of(long inventoryId, Inventory inventory) {
      return InventoryResponse.builder()
              .inventoryId(inventoryId)
              .productName(inventory.getProductName())
              .stockQuantity(inventory.getStockQuantity())
              .build();
    }
  }
}
