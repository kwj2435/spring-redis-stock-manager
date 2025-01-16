package com.uijin.stockmanager.inventory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uijin.stockmanager.inventory.model.InventoryModel;
import com.uijin.stockmanager.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  /**
   * 재고 조회 API
   */
  @GetMapping("/{inventoryId}")
  public InventoryModel.InventoryResponse getInventory(@PathVariable("inventoryId") long inventoryId) {
    return inventoryService.getInventory(inventoryId);
  }

  /**
   * 재고 감소 API
   */
  @PostMapping("/{inventoryId}/decrease")
  public InventoryModel.InventoryResponse decreaseInventoryStock(
          @PathVariable("inventoryId") long inventoryId) {
    return inventoryService.decreaseInventoryStock(inventoryId);
  }
}
