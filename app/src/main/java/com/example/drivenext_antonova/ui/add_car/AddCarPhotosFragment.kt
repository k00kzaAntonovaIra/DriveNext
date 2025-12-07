package com.example.drivenext_antonova.ui.add_car

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenext_antonova.R
import com.google.android.material.button.MaterialButton

class AddCarPhotosFragment : Fragment(R.layout.fragment_car_photos) {

    private lateinit var ivBack: ImageView
    private lateinit var btnNext: MaterialButton

    private lateinit var photoContainer1: FrameLayout
    private lateinit var photo2: FrameLayout
    private lateinit var photo3: FrameLayout
    private lateinit var photo4: FrameLayout
    private lateinit var photo5: FrameLayout

    private val photoViews = mutableListOf<FrameLayout>()
    private val addedPhotos = mutableMapOf<Int, Uri?>()

    private var currentPhotoIndex: Int = -1

    private val pickPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null && currentPhotoIndex != -1) {
                addedPhotos[currentPhotoIndex] = uri
                setPhotoToFrame(photoViews[currentPhotoIndex], uri)
                updateNextButtonState()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupClickListeners()
        updateNextButtonState()
    }

    private fun initViews(view: View) {
        ivBack = view.findViewById(R.id.ivBack)
        btnNext = view.findViewById(R.id.btnNext)

        photoContainer1 = view.findViewById(R.id.photoContainer1)
        photo2 = view.findViewById(R.id.photo2)
        photo3 = view.findViewById(R.id.photo3)
        photo4 = view.findViewById(R.id.photo4)
        photo5 = view.findViewById(R.id.photo5)

        photoViews.addAll(listOf(photoContainer1, photo2, photo3, photo4, photo5))
    }

    private fun setupClickListeners() {
        ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        photoViews.forEachIndexed { index, frame ->
            frame.setOnClickListener {
                currentPhotoIndex = index
                openImagePicker()
            }
        }

        btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_AddCarPhotosFragment_to_addSuccessFragment)

            println("Фото успешно добавлены: $addedPhotos")
        }
    }

    private fun openImagePicker() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(MediaStore.ACTION_PICK_IMAGES)
        } else {
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
        }
        pickPhoto.launch(intent)
    }

    private fun setPhotoToFrame(frame: FrameLayout, uri: Uri) {
        frame.removeAllViews()
        val imageView = ImageView(requireContext())
        imageView.setImageURI(uri)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        frame.addView(imageView)
    }

    private fun updateNextButtonState() {
        val hasPhotos = addedPhotos.values.any { it != null }

        btnNext.isEnabled = hasPhotos
        btnNext.backgroundTintList = ContextCompat.getColorStateList(
            requireContext(),
            if (hasPhotos) R.color.primary_color else R.color.disabled_color
        )
    }
}
