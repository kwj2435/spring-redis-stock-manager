package com.uijin.stockmanager.inventory.controller;

import com.uijin.stockmanager.inventory.model.InventoryModel;
import com.uijin.stockmanager.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  @GetMapping("/{inventoryId}")
  public InventoryModel.InventoryResponse getInventory(@PathVariable("inventoryId") long inventoryId) {
    return inventoryService.getInventory(inventoryId);
  }
}
