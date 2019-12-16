package com.example.warehousemanagement.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.warehousemanagement.R
import com.example.warehousemanagement.database.InventoryItem
import com.example.warehousemanagement.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_add_inventory_item.*

class AddInventoryItemActivity : AppCompatActivity() , View.OnClickListener{

    lateinit var mainViewModel : MainViewModel
    var imageUri : Uri? = null
    val STORAGE_REQUEST_CODE = 100
    val IMAGE_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inventory_item)
        supportActionBar?.title = "Add Item"

        //OnClickListeners
        finish_input_add.setOnClickListener(this)
        button_choose_image_add.setOnClickListener(this)

        //TODO Remember to credit <a target="_blank" href="/icons/set/image">Image icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
        //item_image_add.setImageDrawable()
        mainViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[MainViewModel::class.java]
    }

    override fun onClick(v: View?) {
        v?.let {
            when(it) {
                finish_input_add -> {
                    if(validateInput()) {
                        val name = input_item_name_add.editableText.toString()
                        val quantity = input_item_quantity_add.editableText.toString().toInt()
                        val currency = item_currency_add.selectedItem.toString()
                        val price = input_item_price_add.editableText.toString().toFloat()
                        mainViewModel.insertInventoryItem(
                            InventoryItem(
                                null, quantity, imageUri.toString(),
                                name, currency, price
                            )
                        )
                        Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                button_choose_image_add -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission_group.STORAGE), STORAGE_REQUEST_CODE)
                    }
                    var anIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    anIntent.addCategory(Intent.CATEGORY_OPENABLE)
                    anIntent.type = "image/*"
                    startActivityForResult(anIntent, IMAGE_REQUEST_CODE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == IMAGE_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                imageUri = data?.data
                Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.icon_no_image)
                    .error(R.drawable.icon_no_image)
                    .into(item_image_add)
            }
        }
    }

    fun validateInput() : Boolean {
        var validated = true
        val ERROR = "Field must not be empty!"

        if(input_item_name_add.editableText.isEmpty()) {
            validated = false
            input_item_name_add.error = ERROR
        }
        if(input_item_price_add.editableText.isEmpty()) {
            validated = false
            input_item_price_add.error = ERROR
        }
        if(input_item_quantity_add.editableText.isEmpty()) {
            validated = false
            input_item_quantity_add.error = ERROR
        }
        return validated
    }
}
