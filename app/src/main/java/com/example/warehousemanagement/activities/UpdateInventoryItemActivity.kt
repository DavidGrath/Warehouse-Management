package com.example.warehousemanagement.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.warehousemanagement.Constants.Companion.ITEM_CURRENCY
import com.example.warehousemanagement.Constants.Companion.ITEM_ID
import com.example.warehousemanagement.Constants.Companion.ITEM_IMAGE
import com.example.warehousemanagement.Constants.Companion.ITEM_NAME
import com.example.warehousemanagement.Constants.Companion.ITEM_PRICE
import com.example.warehousemanagement.Constants.Companion.ITEM_QUANTITY
import com.example.warehousemanagement.R
import com.example.warehousemanagement.database.InventoryItem
import com.example.warehousemanagement.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_update_inventory_item.*

class UpdateInventoryItemActivity : AppCompatActivity() , View.OnClickListener {

    lateinit var mainViewModel : MainViewModel
    var imageUri : Uri? = null
    val STORAGE_REQUEST_CODE = 100
    val IMAGE_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_inventory_item)
        supportActionBar?.title = "Update Item"

        //OnClickListeners
        finish_input_update.setOnClickListener(this)
        button_choose_image_update.setOnClickListener(this)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[MainViewModel::class.java]

        //TODO Refactor to get item from viewModel
        input_item_name_update.setText(intent.getStringExtra(ITEM_NAME))
        input_item_quantity_update.setText(intent.getStringExtra(ITEM_QUANTITY))
        input_item_price_update.setText(intent.getStringExtra(ITEM_PRICE))
        imageUri = Uri.parse(intent.getStringExtra(ITEM_IMAGE))
        item_image_update.setImageURI(imageUri)
        val currencyValues = resources.getStringArray(R.array.currency_characters)
        val currencyPosition = currencyValues.indexOf(intent.getStringExtra(ITEM_CURRENCY))
        item_currency_update.setSelection(currencyPosition)
    }

    override fun onClick(v: View?) {
        v?.let {
            when(it) {
                finish_input_update -> {
                    if(validateInput()) {
                        val name = input_item_name_update.editableText.toString()
                        val quantity = input_item_quantity_update.editableText.toString().toInt()
                        val currency = item_currency_update.selectedItem.toString()
                        val price = input_item_price_update.editableText.toString().toFloat()
                        mainViewModel.updateInventoryItem(
                            InventoryItem(
                                intent.getIntExtra(ITEM_ID, 0), quantity, imageUri.toString(),
                                name, currency, price
                            )
                        )
                        Toast.makeText(this, "Item Updated", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                button_choose_image_update -> {
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
                    .into(item_image_update)

            }
        }
    }

    fun validateInput() : Boolean {
        var validated = true
        val ERROR = "Field must not be empty!"
        if(input_item_name_update.editableText.isEmpty()) {
            input_item_name_update.error = ERROR
            validated = false
        }
        if(input_item_price_update.editableText.isEmpty()) {
            input_item_price_update.error = ERROR
            validated = false
        }
        if(input_item_quantity_update.editableText.isEmpty()) {
            input_item_quantity_update.error = ERROR
            validated = false
        }
        return validated
    }
}
