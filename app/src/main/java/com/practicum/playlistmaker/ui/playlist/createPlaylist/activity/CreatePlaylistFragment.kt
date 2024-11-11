package com.practicum.playlistmaker.ui.playlist.createPlaylist.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.ui.playlist.createPlaylist.activity.view_model.CreatePlaylistViewModel
import com.practicum.playlistmaker.ui.root.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

open class CreatePlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }

    open val viewModel by viewModel<CreatePlaylistViewModel>()

    var _binding: FragmentCreatePlaylistBinding? = null
    val binding: FragmentCreatePlaylistBinding
        get() = _binding!!

    lateinit var confirmDialog: MaterialAlertDialogBuilder
    lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBackToMenu.setOnClickListener {
            checkExitDialog()
        }

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

        binding.creationButton.setOnClickListener {
            val playlist = Playlist(
                0,
                binding.textInTitle.text.toString(),
                binding.textInDescription.text.toString(),
                getFilePath(),
                mutableListOf(),
                0
            )
            viewModel.createPlaylist(playlist)
            requireActivity().supportFragmentManager.popBackStack()
            Toast.makeText(requireContext(), "Плейлист ${binding.textInTitle.text} создан", Toast.LENGTH_SHORT).show()
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                checkExitDialog()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is RootActivity) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                View.GONE
        } else {
            requireActivity().findViewById<ConstraintLayout>(R.id.main_layout).visibility = View.GONE
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (activity is RootActivity) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                View.VISIBLE
        } else {
            requireActivity().findViewById<ConstraintLayout>(R.id.main_layout).visibility = View.VISIBLE
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

    private fun checkExitDialog() {
        if (binding.image.drawable != null || binding.textInTitle.text!!.isNotEmpty() || binding.textInDescription.text!!.isNotEmpty()) {
            confirmDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNeutralButton("Отмена") { dialog, which ->

                }.setPositiveButton("Закрыть") { dialog, which ->
                    requireActivity().supportFragmentManager.popBackStack()
                }
            confirmDialog.show()
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}