package com.practicum.playlistmaker.ui.playlist.changePlaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.state.PlaylistState
import com.practicum.playlistmaker.ui.playlist.changePlaylist.view_model.ChangePlaylistViewModel
import com.practicum.playlistmaker.ui.playlist.createPlaylist.activity.CreatePlaylistFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream

class ChangePlaylistFragment : CreatePlaylistFragment() {

    companion object {
        private const val DELAY_TO_EDIT = 100L
        const val PLAYLIST_ID = "id"
        fun newInstance(id: Int) =
            ChangePlaylistFragment().apply {
                arguments = bundleOf(PLAYLIST_ID to id)
            }
    }

    lateinit private var startFilePath: String
    var flag_for_image = false

    override val viewModel by viewModel<ChangePlaylistViewModel> {
        parametersOf(requireArguments().getInt(PLAYLIST_ID))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.titleText.text = requireContext().resources.getString(R.string.edit)
        binding.creationButton.text = requireContext().resources.getString(R.string.save)
        viewModel.loadData()
        viewModel.observeState.observe(viewLifecycleOwner) {
            renderPlaylist(it)
        }

        binding.buttonBackToMenu.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.textInTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    binding.creationButton.isEnabled = true
                } else {
                    binding.creationButton.isEnabled = false
                }

            }
        })

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(requireContext())
                        .load(uri)
                        .apply(
                            RequestOptions().transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.icon_padding))
                                )
                            )
                        )
                        .into(binding.image)
                    saveImageToPrivateStorage(uri)
                }
            }

        binding.image.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            flag_for_image = true
        }

        binding.creationButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.editPlaylist(binding.textInTitle.text.toString(), binding.textInDescription.text.toString(), checkImage())
                delay(DELAY_TO_EDIT)
                requireActivity().supportFragmentManager.popBackStack()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun renderPlaylist(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> {
                val playlist = state.playlist
                binding.textInTitle.setText(playlist.name)
                binding.textInDescription.setText(playlist.description)
                Glide.with(requireContext())
                    .load(playlist.path)
                    .apply(
                        RequestOptions().transform(
                            MultiTransformation(
                                CenterCrop(),
                                RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.icon_padding))
                            )
                        )
                    )
                    .into(binding.image)
                startFilePath = playlist.path
            }
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "$binding")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun getFilePath(): String {
        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val file = File(filePath, "$binding")
        return file.toString()
    }

    private fun checkImage(): String {
        if (flag_for_image == false) {
            return startFilePath
        } else {
            return getFilePath()
        }
    }
}