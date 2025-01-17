package com.uijin.stockmanager.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "INVENTORY")
public class InventoryEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "INVENTORY_ID")
  private long inventoryId;

  @Column(name = "PRODUCT_NAME")
  private String productName;

  @Column(name = "STOCK_QUANTITY")
  private int stockQuantity;

  @Column(name = "LAST_UPDATED")
  private LocalDateTime lastUpdated;

  public InventoryEntity updateStockQuantity(int stockQuantity) {
    this.stockQuantity = stockQuantity;
    this.lastUpdated = LocalDateTime.now();
    return this;
  }
}
