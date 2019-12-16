package com.example.warehousemanagement.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.warehousemanagement.R
import com.example.warehousemanagement.database.InventoryItem
import java.text.DecimalFormat
import java.util.*

class InventoryRecyclerAdapter(var inventoryItems: List<InventoryItem>) : RecyclerView.Adapter<InventoryRecyclerAdapter.InventoryViewHolder>() {

    interface ItemPopupListener{
        fun onItemPopupSelected(position: Int, inventoryItem: InventoryItem, itemId : Int)
    }

    lateinit var menuItemClickListener: ItemPopupListener

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        var item = inventoryItems[position]
        holder.itemName.text = item.name
        holder.itemQuantity.text = "Quantity: ${item.quantity.toString()}"
        Glide.with(holder.itemView.context)
            .load(Uri.parse(item.image))
            .placeholder(R.drawable.icon_no_image)
            .error(R.drawable.icon_no_image)
            .into(holder.itemImage)
        holder.popupButton.setOnClickListener{
            val popupMenu = PopupMenu(it.context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_inventory_popup, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener{
                menuItemClickListener.onItemPopupSelected(position, item, it.itemId)
                true
            }
                popupMenu.show()
        }
        holder.itemPrice.text = item.currency + " " + DecimalFormat("0.00").format(item.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_inventory_item, parent, false)
        return InventoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return inventoryItems.size
    }

    class InventoryViewHolder(item : View) : RecyclerView.ViewHolder(item) {
        var itemName = item.findViewById<TextView>(R.id.inventory_item_name)
        var itemQuantity = item.findViewById<TextView>(R.id.inventory_item_quantity)
        var itemImage = item.findViewById<ImageView>(R.id.inventory_item_image)
        var popupButton = item.findViewById<ImageView>(R.id.inventory_popup_button)
        var itemPrice = item.findViewById<TextView>(R.id.inventory_item_price)
    }
}