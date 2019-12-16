package com.example.warehousemanagement.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.warehousemanagement.database.InventoryItem
import com.example.warehousemanagement.database.InventoryItemDatabase
import com.example.warehousemanagement.database.InventoryItemRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){

    var inventoryItems : LiveData<List<InventoryItem>>
    val inventoryItemRepository : InventoryItemRepository

    init {
        val inventoryItemDao = InventoryItemDatabase.getDatabase(application).inventoryItemDao()
        inventoryItemRepository = InventoryItemRepository(inventoryItemDao)
        inventoryItems  = inventoryItemRepository.getAllInventoryItems()
    }

    fun insertInventoryItem(inventoryItem: InventoryItem) = viewModelScope.launch{
        inventoryItemRepository.insertInventoryItem(inventoryItem)
    }
    fun updateInventoryItem(inventoryItem: InventoryItem) = viewModelScope.launch {
        inventoryItemRepository.updateInventoryItem(inventoryItem)
    }
    fun deleteInventoryItem(inventoryItem: InventoryItem) = viewModelScope.launch {
        inventoryItemRepository.deleteInventoryItem(inventoryItem)
    }


}