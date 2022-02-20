package com.bornidea.pruebatecnicaupax.ui.dialogs

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.DialogFragmentCameraBinding
import com.bornidea.pruebatecnicaupax.databinding.DialogFragmentLocationBinding
import com.bornidea.pruebatecnicaupax.ui.activities.MainActivity

class CameraDialogFragment : DialogFragment() {

    private lateinit var binding: DialogFragmentCameraBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_camera, container, false)

        binding.btAceptar.setOnClickListener {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.CAMERA),
                MainActivity.REQUEST_CODE_CAMERA
            )
            dismiss()
        }

        return binding.root
    }
}