package com.bornidea.pruebatecnicaupax.ui.activities

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.ActivityMainBinding
import com.bornidea.pruebatecnicaupax.ui.dialogs.PhotoDialogFragment
import com.bornidea.pruebatecnicaupax.ui.dialogs.SettingsCameraDialogFragment
import com.bornidea.pruebatecnicaupax.ui.fragments.LocationFragment
import com.bornidea.pruebatecnicaupax.ui.dialogs.SettingsLocationDialogFragment
import com.bornidea.pruebatecnicaupax.ui.fragments.PhotoFragment.Companion.TAG_CAMERA
import com.bornidea.pruebatecnicaupax.ui.fragments.PhotoFragment.Companion.TAG_CAMERAFRAGMENT_SETTINGS
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_LOCATION = 0
        const val REQUEST_CODE_CAMERA = 1
    }

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_PruebaTecnicaUpax)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()

        /**MENU CONFIGURATION*/
        val navigationView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigationFragment) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.navigationPeliculas,
                    R.id.navigationLocation,
                    R.id.navigationPhoto
                )
            )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navController.navigate(R.id.navigationLocation)
            } else {
                val dialogSetings = SettingsLocationDialogFragment()
                dialogSetings.isCancelable = false
                dialogSetings.show(
                    supportFragmentManager,
                    LocationFragment.TAG_LOCATIONFRAGMENT_SETTINGS
                )
            }
            REQUEST_CODE_CAMERA ->if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val dialogCamera = PhotoDialogFragment()
                dialogCamera.isCancelable = true
                dialogCamera.show(
                    supportFragmentManager,
                    TAG_CAMERA
                )
            }else{
                val dialogSetings = SettingsCameraDialogFragment()
                dialogSetings.isCancelable = false
                dialogSetings.show(
                    supportFragmentManager,
                    TAG_CAMERAFRAGMENT_SETTINGS
                )
            }
            else -> {}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}