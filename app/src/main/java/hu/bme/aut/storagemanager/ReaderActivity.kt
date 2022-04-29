package hu.bme.aut.storagemanager

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.data.StorageItemDatabase
import hu.bme.aut.storagemanager.databinding.ActivityReaderBinding
import kotlin.concurrent.thread

class ReaderActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var database: StorageItemDatabase
    private lateinit var id : String
    private lateinit var data: List<StorageItem>
    private lateinit var binding: ActivityReaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityReaderBinding.inflate(layoutInflater)
        val scannerView = binding.scannerView
        database = StorageItemDatabase.getDatabase(applicationContext)
        codeScanner = CodeScanner(this, scannerView)

        requestNeededPermission()

        codeScanner.decodeCallback = DecodeCallback {
            id = it.text
            checkIfExists()
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        setContentView(binding.root)
    }

    private fun init(){
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false
    }

    private fun requestNeededPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 50)
        }
        else
            init()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            50 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    init()
                else
                    Toast.makeText(this, getString(R.string.permNotGranted), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfExists(){
        var isIn = false
        thread {
            data = database.storageItemDao().getAll()
            for(x in data){
                if(x.id.toString() == id) {
                    isIn = true
                    break
                }
            }
            createIntent(isIn)
        }
        runOnUiThread {
            if(!isIn) {
                Toast.makeText(this, "ID not found: $id", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createIntent(isIn: Boolean){
        if(isIn){
            val i = Intent(this@ReaderActivity, ShowItemActivity::class.java)
            i.putExtra("id", id)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}