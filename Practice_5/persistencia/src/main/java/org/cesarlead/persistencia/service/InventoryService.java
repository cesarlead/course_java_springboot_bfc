package org.cesarlead.persistencia.service;

public interface InventoryService {
    void updateStock(Long productId, int quantity);
}
