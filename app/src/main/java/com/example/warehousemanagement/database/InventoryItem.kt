package com.example.warehousemanagement.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InventoryItem(@PrimaryKey(autoGenerate = true)var id : Int?, var quantity : Int, var image : String, var name : String,
                         var currency : String, var price : Float) {
}