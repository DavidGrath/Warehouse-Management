package com.example.warehousemanagement.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehousemanagement.Constants.Companion.ITEM_CURRENCY
import com.example.warehousemanagement.Constants.Companion.ITEM_ID
import com.example.warehousemanagement.Constants.Companion.ITEM_IMAGE
import com.example.warehousemanagement.Constants.Companion.ITEM_NAME
import com.example.warehousemanagement.Constants.Companion.ITEM_PRICE
import com.example.warehousemanagement.Constants.Companion.ITEM_QUANTITY
import com.example.warehousemanagement.R
import com.example.warehousemanagement.adapters.InventoryRecyclerAdapter
import com.example.warehousemanagement.database.InventoryItem
import com.example.warehousemanagement.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , InventoryRecyclerAdapter.ItemPopupListener {

    lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[MainViewModel::class.java]
        inventory_recyclerview.layoutManager = LinearLayoutManager(this)
        mainViewModel.inventoryItems.observe(this, Observer{inventoryItems ->
            inventoryItems.let{
                var adapter = InventoryRecyclerAdapter(it)
                adapter.menuItemClickListener = this
                inventory_recyclerview.adapter = adapter
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_item_menuitem -> {
                val anIntent = Intent(this, AddInventoryItemActivity::class.java)
                startActivity(anIntent)
                return true
            }
            else -> {
                return false
            }
        }
    }

    override fun onItemPopupSelected(position: Int, inventoryItem: InventoryItem, itemId : Int) {
        when(itemId) {
            R.id.update_popup_item -> {
                val anIntent = Intent(this, UpdateInventoryItemActivity::class.java)
                anIntent.putExtra(ITEM_ID, inventoryItem.id)
                anIntent.putExtra(ITEM_NAME, inventoryItem.name)
                anIntent.putExtra(ITEM_PRICE, inventoryItem.price.toString())
                anIntent.putExtra(ITEM_QUANTITY, inventoryItem.quantity.toString())
                anIntent.putExtra(ITEM_CURRENCY, inventoryItem.currency)
                anIntent.putExtra(ITEM_IMAGE, inventoryItem.image)
                startActivity(anIntent)
            }
            R.id.delete_popup_item -> {
                val builder = AlertDialog.Builder(this)
                    .setTitle("Delete Item?")
                    .setMessage("Are you sure you want to delete ${inventoryItem.name}?")
                    .setPositiveButton("Yes", object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            mainViewModel.deleteInventoryItem(inventoryItem)
                        }
                    })
                    .setNegativeButton("No", null)
                builder.create().show()
            }
        }
    }
}
