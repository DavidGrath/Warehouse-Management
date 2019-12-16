package com.example.warehousemanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.warehousemanagement.Constants.Companion.INVENTORY_ITEM_DATABASE_NAME

@Database(entities = arrayOf(InventoryItem::class), version = 1)
abstract class InventoryItemDatabase : RoomDatabase(){
    abstract fun inventoryItemDao() : InventoryItemDao

    companion object {
        @Volatile
        private var INSTANCE : InventoryItemDatabase? = null
        fun getDatabase(context: Context) : InventoryItemDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, InventoryItemDatabase::class.java,
                    INVENTORY_ITEM_DATABASE_NAME).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}