package hu.bme.aut.storagemanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.data.StorageItemDatabase
import hu.bme.aut.storagemanager.databinding.ActivityShowItemBinding
import kotlin.concurrent.thread

class ShowItemActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShowItemBinding
    private lateinit var database: StorageItemDatabase
    private lateinit var id : String
    private lateinit var data: List<StorageItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = StorageItemDatabase.getDatabase(applicationContext)
        binding = ActivityShowItemBinding.inflate(layoutInflater)

        id = intent.getStringExtra("id").toString()
        binding.btnBack.setOnClickListener{ finish() }

        binding.btnUpdate.setOnClickListener{
            val i = Intent(this@ShowItemActivity, NewItemActivity::class.java)
            i.putExtra("id", id)
            startActivity(i)
        }

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        if(id != "-")
            loadFromDb()
    }

    private fun loadFromDb(){
        thread {
            data = database.storageItemDao().getAll()
            loadData()
        }
    }

    private fun loadData(){
        runOnUiThread {
            for (x in data) {
                if (x.id.toString() == id) {
                    var plus = ""

                    plus = getString(R.string.stringItemPrice) + " " + x.price
                    binding.priceTextView.text = plus

                    plus = getString(R.string.stringItemPlace) + " " + x.place
                    binding.placeTextView.text = plus

                    plus = getString(R.string.stringItemDesc) + " " + x.description
                    binding.descTextView.text = plus

                    plus = getString(R.string.stringItemCode) + " " + x.code
                    binding.codeTextView.text = plus

                    plus = getString(R.string.stringItemYear) + " " + x.year
                    binding.yearTextView.text = plus

                    plus = getString(R.string.stringItemName) + " " + x.name
                    binding.nameTextView.text = plus
                    break
                }
            }
        }

    }
}