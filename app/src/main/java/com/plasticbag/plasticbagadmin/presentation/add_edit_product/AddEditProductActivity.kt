package com.plasticbag.plasticbagadmin.presentation.add_edit_product

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.plasticbag.plasticbagadmin.R
import com.plasticbag.plasticbagadmin.databinding.ActivityAddEditProductBinding
import com.plasticbag.plasticbagadmin.util.Constants.Companion.IMAGE
import com.plasticbag.plasticbagadmin.util.Constants.Companion.PRODUCT_ID
import com.plasticbag.plasticbagadmin.util.Constants.Companion.QUANTITY
import com.plasticbag.plasticbagadmin.util.Constants.Companion.TITLE

class AddEditProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditProductBinding
    private val viewModel: AddEditProductViewModel by viewModels()

    private var productId: String? = null
    private lateinit var image: String
    private lateinit var title: String
    private lateinit var quantity: String

    private var selectedImageUri: Uri? = null

    private var croppedImageUri: Uri? = null

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            croppedImageUri = result.uriContent!!

            Glide.with(this)
                .load(croppedImageUri)
                .into(binding.image)

            removeAddImage()
        } else {
            val exception = result.error
            if (exception != null) {
                Toast.makeText(this, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val incomingTitle = intent.getStringExtra(TITLE)
        if (incomingTitle != null) {
            setIncomingData()
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {

            binding.btnSave.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            title = binding.title.editText?.text.toString().trim()
            quantity = binding.quantity.editText?.text.toString().trim()

            if (productId != null) {
                if (croppedImageUri != null) {
                    productId?.let { it1 ->
                        viewModel.editProductInFirebase(croppedImageUri!!,
                            it1, title, quantity)
                    }
                }else {
                    productId?.let { it1 -> viewModel.editTitleQuantityToFirebase(it1, title, quantity) }
                }
            }else {
                if (title == "" || quantity == "" || croppedImageUri == null) {
                    Toast.makeText(this, "Add all fields", Toast.LENGTH_SHORT).show()
                    binding.btnSave.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }else {
                    viewModel.saveProductToFirebase(croppedImageUri!!, title, quantity)
                }
            }

        }

        binding.imageCl.setOnClickListener {
            startCrop()
        }

        viewModel.successCallback = {
            clearDataFields()
            Toast.makeText(this, "Product Saved Successfully!!", Toast.LENGTH_SHORT).show()
            binding.btnSave.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE

        }

        viewModel.errorCallback = {
            Toast.makeText(this, "Error occurred, Try Again!!", Toast.LENGTH_SHORT).show()
            binding.btnSave.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun startCrop() {
        cropImage.launch(
            CropImageContractOptions(
                selectedImageUri, CropImageOptions(
                    guidelines = CropImageView.Guidelines.ON_TOUCH,
                    cropShape = CropImageView.CropShape.RECTANGLE,
                    fixAspectRatio = true,
                    aspectRatioY = 9,
                    aspectRatioX = 7,
                    initialCropWindowPaddingRatio = 0f,
                    cropMenuCropButtonTitle = getString(R.string.send),
                    outputRequestSizeOptions = CropImageView.RequestSizeOptions.RESIZE_INSIDE,
                )
            )
        )
    }

    private fun clearDataFields() {
        binding.title.editText?.setText("")
        binding.quantity.editText?.setText("")

        Glide.with(this)
            .load("")
            .into(binding.image)

        binding.addBgImg.visibility = View.VISIBLE
        binding.imgAdd.visibility = View.VISIBLE

    }

    private fun removeAddImage() {
        binding.addBgImg.visibility = View.GONE
        binding.imgAdd.visibility = View.GONE
    }

    private fun setIncomingData() {

        removeAddImage()

        productId = intent.getStringExtra(PRODUCT_ID).toString()
        title = intent.getStringExtra(TITLE).toString()
        quantity = intent.getStringExtra(QUANTITY).toString()
        image = intent.getStringExtra(IMAGE).toString()

        Glide.with(this)
            .load(image)
            .into(binding.image)

        binding.title.editText?.setText(title)
        binding.quantity.editText?.setText(quantity)
    }
}