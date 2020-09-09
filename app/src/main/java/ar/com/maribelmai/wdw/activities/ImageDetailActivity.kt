package ar.com.maribelmai.wdw.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import ar.com.maribelmai.wdw.R
import ar.com.maribelmai.wdw.model.Picture
import ar.com.maribelmai.wdw.viewmodels.ImageDetailViewModel
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.activity_image_detail.*
import kotlinx.android.synthetic.main.fragment_images_list.progressBar

/**
 * Example of ViewModel usage on Activity
 */
class ImageDetailActivity : AppCompatActivity(R.layout.activity_image_detail) {

    private val viewModelFactory = ImageDetailViewModel.Factory(0.9F)
    private val viewModel: ImageDetailViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerObservers()
        (intent.getParcelableExtra<Picture>(EXTRA_PICTURE))?.also {
            val imageUri = it.uri.toUri()
            image.setImageURI(imageUri)
            viewModel.retrieveInfo(InputImage.fromFilePath(this, imageUri))
        }
    }

    private fun registerObservers() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is ImageDetailViewModel.State.Loading -> progressBar.isVisible = true
                is ImageDetailViewModel.State.Success -> {
                    progressBar.isVisible = false
                    labels.text = state.imageLabels.joinToString(", ") { it.text }
                }
                is ImageDetailViewModel.State.Failure -> {
                    progressBar.isVisible = false
                    showError(state.message)
                }
            }
        })
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_PICTURE = "EXTRA_PICTURE"
    }
}