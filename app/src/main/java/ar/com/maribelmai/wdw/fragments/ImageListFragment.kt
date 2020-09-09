package ar.com.maribelmai.wdw.fragments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import ar.com.maribelmai.wdw.R
import ar.com.maribelmai.wdw.model.Picture
import ar.com.maribelmai.wdw.adapters.PicturesAdapter
import ar.com.maribelmai.wdw.activities.ImageDetailActivity
import ar.com.maribelmai.wdw.viewmodels.ImageListViewModel
import kotlinx.android.synthetic.main.fragment_images_list.*

/**
 * Example of ViewModel usage on Fragments
 */
class ImageListFragment : Fragment(R.layout.fragment_images_list) {

    private val viewModel: ImageListViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerObservers()
        checkPermissions()
    }

    private fun registerObservers() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ImageListViewModel.State.Loading -> progressBar.isVisible = true
                is ImageListViewModel.State.Success -> {
                    progressBar.isVisible = false
                    recyclerView.adapter =
                        PicturesAdapter(state.pictures)
                }
            }
        })
    }

    // Permission handling
    private fun checkPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_RC
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_RC && grantResults.first() == PermissionChecker.PERMISSION_GRANTED) {
            viewModel.loadFiles(requireActivity().contentResolver)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.error_filesystem),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val PERMISSION_RC = 22
        fun newInstance() =
            ImageListFragment()
    }
}