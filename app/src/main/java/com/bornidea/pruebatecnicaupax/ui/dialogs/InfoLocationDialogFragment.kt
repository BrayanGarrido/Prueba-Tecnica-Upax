package com.bornidea.pruebatecnicaupax.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.DialogFragmentInfoLocationBinding
import com.bornidea.pruebatecnicaupax.model.data.Locations
import com.google.gson.Gson

class InfoLocationDialogFragment : DialogFragment() {

    private lateinit var binding: DialogFragmentInfoLocationBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_fragment_info_location,
            container,
            false
        )

        val arguments = arguments
        val data = arguments?.getString("info") ?: ""
        val gson = Gson()

        val location = gson.fromJson(data, Locations::class.java)

        binding.tvFecha.text = "${getString(R.string.fecha)} ${location.Fecha}"
        binding.tvHora.text = "${getString(R.string.Hora)} ${location.Hora}"
        binding.tvLatitude.text = "${getString(R.string.Latitude)} ${location.Latitud}"
        binding.tvLongitude.text = "${getString(R.string.Longitude)} ${location.Longitud}"

        binding.btCerrar.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}