package org.cesarlead.persistencia.service;

import org.springframework.stereotype.Service;

@Service
class DummyInventoryServiceImpl implements InventoryService {
    public void updateStock(Long productId, int quantity) {
        System.out.println("Actualizando stock para producto " + productId + ", cantidad: " + quantity);
    }
}
