package com.bornidea.pruebatecnicaupax.ui.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.DialogFragmentCameraSettingsBinding
import com.bornidea.pruebatecnicaupax.databinding.DialogFragmentLocationSettingsBinding

class SettingsCameraDialogFragment : DialogFragment() {

    private lateinit var binding: DialogFragmentCameraSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_fragment_camera_settings,
            container,
            false
        )

        binding.btAceptar.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivity(intent)
            dismiss()
            requireActivity().finish()
        }

        return binding.root
    }
}