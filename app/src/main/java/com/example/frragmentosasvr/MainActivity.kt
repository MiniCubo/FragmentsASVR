package com.example.frragmentosasvr

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : AppCompatActivity() {
    private lateinit var qrResult : String
    private var onQrResult: ((String) -> Unit)? = null
    private val barcodeLauncher = registerForActivityResult<ScanOptions?, ScanIntentResult?>(
        ScanContract(), { result: ScanIntentResult? ->
            if(result!!.contents == null) Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            else {
                Toast.makeText(this, "Scanned ${result.contents}", Toast.LENGTH_SHORT).show()
                qrResult = result.contents
                onQrResult?.invoke(qrResult)  // ✅ Call callback HERE, after result arrives
            }
        }
    )

    private lateinit var fusedLocationClient : FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
        permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ->{}
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {}
                else -> {}
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val calculadora = findViewById<Button>(R.id.Calculadora)
        val inicio = findViewById<Button>(R.id.Inicio)
        val imc = findViewById<Button>(R.id.imc)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        calculadora.setOnClickListener { taskId ->
            replaceFragment(Calculadora())
        }
        inicio.setOnClickListener { taskId ->
            replaceFragment(Inicio())
        }
        imc.setOnClickListener { taskId ->
            replaceFragment(Imc())
        }
    }

    fun replaceFragment(fragmento : Fragment){
        val manager = supportFragmentManager
        val transaccion = manager.beginTransaction()
        transaccion.replace(R.id.fragmentContainerView, fragmento).addToBackStack(null)
        transaccion.commit()
    }

    fun mensaje(){
        Toast.makeText(this, "mensaje desde la actividad main", Toast.LENGTH_SHORT).show()
    }

    fun leerQR(onResult: (String) -> Unit){
        val options = ScanOptions()
        onQrResult = onResult
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a QR CODE")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        barcodeLauncher!!.launch(options)
    }

    fun requestPermissions(){
        locationPermissionRequest.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    fun obtenerUbicacion(callback : (String) -> Unit){
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
            return
        }
        else{
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                val ubicacion = location?.toString() ?: "Ubicacion no disponible"
                callback(ubicacion)
            }
        }
    }
}