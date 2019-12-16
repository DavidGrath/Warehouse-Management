package com.example.warehousemanagement.database

import androidx.lifecycle.LiveData

class InventoryItemRepository(var inventoryItemDao: InventoryItemDao) {
    suspend fun insertInventoryItem(inventoryItem: InventoryItem) = inventoryItemDao.insertInventoryItem(inventoryItem)
    suspend fun updateInventoryItem(inventoryItem: InventoryItem) = inventoryItemDao.updateInventoryItem(inventoryItem)
    fun getAllInventoryItems() : LiveData<List<InventoryItem>> = inventoryItemDao.getAllInventoryItems()
    suspend fun deleteInventoryItem(inventoryItem: InventoryItem) = inventoryItemDao.deleteInventoryItem(inventoryItem)
}