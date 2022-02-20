package com.bornidea.pruebatecnicaupax.ui.dialogs

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bornidea.pruebatecnicaupax.ui.activities.MainActivity
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.DialogFragmentLocationBinding

class LocationDialogFragment : DialogFragment() {

    private lateinit var binding: DialogFragmentLocationBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_location, container, false)

            binding.btAceptar.setOnClickListener{
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MainActivity.REQUEST_CODE_LOCATION
                )
                dismiss()
            }

        return binding.root
    }
}