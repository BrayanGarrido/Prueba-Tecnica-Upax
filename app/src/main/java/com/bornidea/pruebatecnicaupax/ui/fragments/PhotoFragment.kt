package com.bornidea.pruebatecnicaupax.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.FragmentPhotoBinding
import com.bornidea.pruebatecnicaupax.ui.adapters.PhotosAdapter
import com.bornidea.pruebatecnicaupax.ui.dialogs.*
import com.bornidea.pruebatecnicaupax.util.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject

class PhotoFragment : Fragment() {

    companion object {
        const val TAG_GALLERY = "TagGallery"
        const val TAG_CAMERADIALOG = "CameraDialog"
        const val TAG_CAMERAFRAGMENT_SETTINGS = "DialogSettings"
        const val TAG_CAMERA = "TagCamera"
    }

    private var photoList: ArrayList<String>? = null
    val db = Firebase.firestore
    private var adapter: PhotosAdapter? = null
    private lateinit var binding: FragmentPhotoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container, false)

        binding.recyclerPhotos.setHasFixedSize(true)
        binding.recyclerPhotos.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = PhotosAdapter()
        binding.recyclerPhotos.adapter = adapter
        photoList = ArrayList()
        updatePhotoList()
        binding.btUpdate.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            binding.noPictureContainer.visibility = View.GONE
            binding.PictureContainer.visibility = View.GONE
            updatePhotoList()
        }

        binding.fabGallery.setOnClickListener {
            val dialog = GalleryDialogFragment()
            dialog.isCancelable = true
            dialog.show(requireActivity().supportFragmentManager, TAG_GALLERY)
        }

        binding.fabCamera.setOnClickListener {
            if (isCameraPermission()) {
                //Se tiene el permiso
                val dialogCamera = PhotoDialogFragment()
                dialogCamera.isCancelable = true
                dialogCamera.show(
                    requireActivity().supportFragmentManager,
                    TAG_CAMERA
                )
            } else {
                requestCameraPermission()
            }
        }
        return binding.root
    }

    private fun updatePhotoList() {
        db.collection(Constants.COLLECTION_IMAGES)
            .get()
            .addOnSuccessListener { result ->
                photoList?.clear()
                for (document in result) {
                    val map = document.data
                    photoList?.add(map["url"].toString())
                }
                adapter?.setList(photoList!!)
                adapter?.notifyDataSetChanged()
                verificarLista()
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents.", exception)
            }

    }

    private fun verificarLista() {
        if (photoList?.size!! > 0) {
            binding.noPictureContainer.visibility = View.GONE
            binding.PictureContainer.visibility = View.VISIBLE
        } else {
            binding.noPictureContainer.visibility = View.VISIBLE
            binding.PictureContainer.visibility = View.GONE
        }
        binding.progress.visibility = View.GONE
        binding.btUpdate.visibility = View.VISIBLE
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            )
        ) {
            //Ve a las configuraciones y habilita el permiso
            val dialogSetings = SettingsCameraDialogFragment()
            dialogSetings.isCancelable = false
            dialogSetings.show(
                requireActivity().supportFragmentManager,
                TAG_CAMERAFRAGMENT_SETTINGS
            )
        } else {
            /**Primera vez que se piden los permisos*/
            val dialog = CameraDialogFragment()
            dialog.isCancelable = false
            dialog.show(
                requireActivity().supportFragmentManager,
                TAG_CAMERADIALOG
            )
        }
    }

    private fun isCameraPermission() = ContextCompat.checkSelfPermission(
        requireActivity().applicationContext,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}