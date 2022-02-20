package com.bornidea.pruebatecnicaupax.ui.dialogs

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.GalleryFragmentDialogBinding
import com.bornidea.pruebatecnicaupax.model.data.ImageFirestore
import com.bornidea.pruebatecnicaupax.ui.adapters.ImageSelectedAdapter
import com.bornidea.pruebatecnicaupax.util.Constants
import com.bornidea.pruebatecnicaupax.util.Constants.COLLECTION_IMAGES
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class GalleryDialogFragment : DialogFragment() {

    private var imageUriList: ArrayList<Uri>? = null
    private var adapter: ImageSelectedAdapter? = null

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUriList?.add(it.data?.data!!)
                adapter?.setList(imageUriList!!)
                adapter?.notifyDataSetChanged()
                verificarLista()
            }
        }

    val db = Firebase.firestore
    private lateinit var binding: GalleryFragmentDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.gallery_fragment_dialog, container, false)

        imageUriList = ArrayList()
        verificarLista()

        binding.recyclerImages.setHasFixedSize(true)
        binding.recyclerImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter =
            ImageSelectedAdapter({ position: Int -> deleteElement(position) }, { openGallery() })
        binding.recyclerImages.adapter = adapter

        binding.btSearchGallery.setOnClickListener {
            openGallery()
        }

        binding.btSend.setOnClickListener {
            isCancelable = false
            uploadImage()
            Toast.makeText(requireContext(), "Imágenes Subidas", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        return binding.root
    }

    private fun verificarLista() {
        binding.apply {
            if (imageUriList!!.size > 0) {
                //Contiene imagenes
                binding.firstContainer.visibility = View.VISIBLE
                binding.secondContainer.visibility = View.GONE
            } else {
                binding.firstContainer.visibility = View.GONE
                binding.secondContainer.visibility = View.VISIBLE
            }
        }

    }

    private fun deleteElement(position: Int) {
        imageUriList?.removeAt(position)
        verificarLista()
        if (imageUriList!!.size > 0) {
            adapter?.setList(imageUriList!!)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun uploadImage() {
        binding.progressIndicator.visibility = View.VISIBLE
        binding.tvProgressIndicator.visibility = View.VISIBLE
        for (imagenUri in imageUriList!!) {
            val imageFirestore = ImageFirestore()
            imageFirestore.documentId =
                FirebaseFirestore.getInstance().collection(Constants.GALLERY_COLLECTION)
                    .document().id

            val storageRef = FirebaseStorage.getInstance().reference.child(Constants.GALLERY_FOLDER)

            imagenUri.let { uri ->
                val photoRef = storageRef.child(imageFirestore.documentId!!)
                photoRef.putFile(uri)
                    .addOnProgressListener {
                        val progress = (100 * it.bytesTransferred / it.totalByteCount).toInt()
                        it.run {
                            binding.progressIndicator.progress = progress
                            binding.tvProgressIndicator.text = String.format("%s%%", progress)
                        }
                    }
                    .addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                            val url = hashMapOf("url" to downloadUrl.toString())
                            db.collection(COLLECTION_IMAGES)
                                .add(url)
                        }
                    }
            }
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }
}