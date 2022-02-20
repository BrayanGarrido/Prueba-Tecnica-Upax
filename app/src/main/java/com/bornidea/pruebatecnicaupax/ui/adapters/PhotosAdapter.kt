package com.bornidea.pruebatecnicaupax.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.TemplatePhotosItemBinding
import com.squareup.picasso.Picasso

class PhotosAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val photoList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: TemplatePhotosItemBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.template_photos_item,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(photoList[position])
        }
    }

    override fun getItemCount(): Int = photoList.size

    fun setList(image: List<String>) {
        photoList.clear()
        photoList.addAll(image)
    }

    inner class ViewHolder(private val binding: TemplatePhotosItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {
            Picasso.get().load(image).into(binding.imPhoto)
            binding.imPhoto.setOnClickListener {}
        }
    }
}