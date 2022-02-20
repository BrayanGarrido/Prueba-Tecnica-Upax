package com.bornidea.pruebatecnicaupax.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.TemplateHeaderImageAddBinding
import com.bornidea.pruebatecnicaupax.databinding.TemplateImagesItemBinding

private const val HEADER_VIEWTYPE = 1

class ImageSelectedAdapter(
    private val clickListener: (Int) -> Unit,
    private val clickListenerHeader: () -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val imageList = ArrayList<Uri>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == HEADER_VIEWTYPE) {
            val binding: TemplateHeaderImageAddBinding =
                DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.template_header_image_add,
                    parent,
                    false
                )
            return HeaderHolderImage(binding)
        } else {
            val binding: TemplateImagesItemBinding =
                DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.template_images_item,
                    parent,
                    false
                )
            return ViewHolderImage(binding)
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return HEADER_VIEWTYPE
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderHolderImage) {
            holder.bind(clickListenerHeader)
        } else if (holder is ViewHolderImage) {
            holder.bind(imageList[position - 1], clickListener, position)
        }
    }

    override fun getItemCount(): Int = imageList.size + 1

    fun setList(images: List<Uri>) {
        imageList.clear()
        imageList.addAll(images)
    }

    inner class ViewHolderImage(val binding: TemplateImagesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: Uri, clickListener: (Int) -> Unit, position: Int) {
            binding.apply {
                imSelected.setImageURI(imageUri)
                imDelete.setOnClickListener {
                    clickListener(position - 1)
                }
            }
        }
    }

    inner class HeaderHolderImage(val binding: TemplateHeaderImageAddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListenerHeader: () -> Unit) {
            binding.addImage.setOnClickListener {
                clickListenerHeader()
            }
        }
    }
}


