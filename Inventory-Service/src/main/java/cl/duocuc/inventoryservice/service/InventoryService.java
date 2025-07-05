package cl.duocuc.inventoryservice.service;

import cl.duocuc.inventoryservice.model.Inventory;
import cl.duocuc.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    
    private final InventoryRepository InventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.InventoryRepository = inventoryRepository;
    }

    public List<Inventory> findAll() {
        return InventoryRepository.findAll();
    }

    public Inventory findById(String id) {
        return InventoryRepository.findById(id).orElse(null);
    }

    public boolean addInventory(Inventory inventory) {
        if (InventoryRepository.existsById(inventory.getId())) {
            return false;
        }
        InventoryRepository.save(inventory);
        return true;
    }

    public boolean updateInventory(String id, Inventory inventory) {
        if (InventoryRepository.existsById(id)) {
            InventoryRepository.save(inventory);
            return true;
        }
        return false;
    }

    public boolean removeInventory(String id) {
        if (InventoryRepository.existsById(id)) {
            InventoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
