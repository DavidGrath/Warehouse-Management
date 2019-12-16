package com.example.warehousemanagement.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class InventoryItemDao {
    @Insert
    abstract suspend fun insertInventoryItem(inventoryItem: InventoryItem)

    @Update
    abstract suspend fun updateInventoryItem(inventoryItem: InventoryItem)

    @Delete
    abstract suspend fun deleteInventoryItem(inventoryItem: InventoryItem)

    @Query("SELECT * FROM InventoryItem")
    abstract fun getAllInventoryItems() : LiveData<List<InventoryItem>>
}