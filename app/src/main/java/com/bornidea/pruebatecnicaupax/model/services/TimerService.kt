package com.bornidea.pruebatecnicaupax.model.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bornidea.pruebatecnicaupax.ui.activities.MainActivity
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.util.Constants.COLLECTION_LOCATION
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TimerService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "channelID"
        const val NOTIFICATION_ID = 0
        private val CHANNEL_NAME = "channelName"
        const val TAG = "SERVICETimer"
    }

    private var locationManager: LocationManager? = null
    val db = Firebase.firestore
    private var context: Context? = null

    init {
        Log.d(TAG, "Service is runninvg...")
    }

    override fun onCreate() {
        super.onCreate()
        locationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        context = this
        /**Usado para Android mayor a Android 8 (Oreo)*/
        createNotificationChannel()
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /**Evitar que se congele el hilo principal*/
        Thread {
            while (true) {
                /**UBICACION*/
                val location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val longitud = location!!.longitude
                val latitude = location.latitude

                /**FECHA*/
                val c = Calendar.getInstance()
                val day = c.get(Calendar.DAY_OF_MONTH)
                val month = c.get(Calendar.MONTH)
                val year = c.get(Calendar.YEAR)
                val hour = c.get(Calendar.HOUR)
                val minutes = c.get(Calendar.MINUTE)
                val second = c.get(Calendar.SECOND)

                val fecha = "$day/${month + 1}/$year"
                val hora = "$hour:$minutes:$second"

                val ubicacion = hashMapOf(
                    "Longitud" to longitud,
                    "Latitud" to latitude,
                    "Fecha" to fecha,
                    "Hora" to hora
                )
                CoroutineScope(Dispatchers.IO).launch {
                    db.collection(COLLECTION_LOCATION)
                        .add(ubicacion)
                        .addOnSuccessListener { documentReference ->
                            val result = "Ubicacion Guardada"
                            sendNotification(result)
                        }
                        .addOnFailureListener { e ->
                            val result = "No se pudo guardar su ubicación"
                            sendNotification(result)
                        }
                }

                Thread.sleep(300000)
            }
        }.start()

        return START_STICKY
    }

    private fun sendNotification(result: String) {
        /**Interactuar con la notificacion*/
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        /**Configuracion de la notificacion*/
        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(result)
            .setSmallIcon(R.drawable.icono)
            .setPriority(NotificationCompat.PRIORITY_HIGH) //Prioridad de mostrar la notificacion
            .setContentIntent(pendingIntent) // Permite la interaccion con la aplicación
            .setAutoCancel(true) // Se quita la notificacion al tocarla
        /**Ejecuta la notificacion*/
        with(NotificationManagerCompat.from(context!!)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableVibration(true)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service is being killed...")
    }
}