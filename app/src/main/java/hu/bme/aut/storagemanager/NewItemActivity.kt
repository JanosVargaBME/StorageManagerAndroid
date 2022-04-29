package hu.bme.aut.storagemanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.data.StorageItemDatabase
import hu.bme.aut.storagemanager.databinding.NewStorageItemBinding
import kotlin.concurrent.thread
import kotlin.random.Random

class NewItemActivity : AppCompatActivity(){

    private lateinit var binding : NewStorageItemBinding
    private lateinit var database: StorageItemDatabase
    private lateinit var data: List<StorageItem>
    private lateinit var idActual: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = StorageItemDatabase.getDatabase(applicationContext)
        binding = NewStorageItemBinding.inflate(layoutInflater)

        idActual = intent.getStringExtra("id").toString()

        if(idActual != null && idActual != "-")
            binding.btnSubmit.text = getString(R.string.btnTextUpdate)
        else
            binding.btnSubmit.text = getString(R.string.btnTextCreate)

        binding.btnReset.setOnClickListener{
            binding.itemPrice.setText("")
            binding.itemPlace.setText("")
            binding.itemDesc.setText("")
            binding.itemCode.setText("")
            binding.itemYear.setText("")
            binding.itemName.setText("")
        }
        binding.btnBack.setOnClickListener { finish() }

        binding.btnSubmit.setOnClickListener{
            if(binding.itemName.text.toString() == "" || binding.itemPlace.text.toString() == ""){
                Toast.makeText(this@NewItemActivity, getString(R.string.warningToFill), Toast.LENGTH_LONG).show()
            }
            else{
                checkNulls()
                if(idActual != null && idActual == "-") {
                    Toast.makeText(this@NewItemActivity, getString(R.string.successCreated), Toast.LENGTH_LONG).show()
                    itemCreated(getStorageItem())
                }
                else{
                    Toast.makeText(this@NewItemActivity, getString(R.string.successUpdate), Toast.LENGTH_LONG).show()
                    itemUpdated(getStorageItem())
                }
                finish()
            }
        }

        setContentView(binding.root)
    }

    override fun onResume(){
        super.onResume()
        if(idActual != null && idActual != "-")
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
                if (x.id.toString() == idActual) {
                    binding.itemPrice.setText(x.price.toString())
                    binding.itemPlace.setText(x.place)
                    binding.itemDesc.setText(x.description)
                    binding.itemCode.setText(x.code)
                    binding.itemYear.setText(x.year.toString())
                    binding.itemName.setText(x.name)
                }
            }
        }
    }

    private fun itemUpdated(upItem: StorageItem){
        thread{
            database.storageItemDao().update(upItem)
        }
    }

    private fun itemCreated(newItem: StorageItem){
        thread {
            database.storageItemDao().insert(newItem)
        }
    }

    private fun getStorageItem() = StorageItem(
        id = getID(),
        name = binding.itemName.text.toString(),
        place = binding.itemPlace.text.toString(),
        year = binding.itemYear.text.toString().toInt(),
        code = createCode(),
        description = binding.itemDesc.text.toString(),
        price = binding.itemPrice.text.toString().toInt()
    )

    private fun getID(): Long{
        if(idActual == null)
            return 0
        if(idActual == "-")
            return Random.nextLong(0,10000)
        return idActual.toLong()
    }

    private fun createCode():String{
        if(binding.itemCode.text.toString() != "")
            return binding.itemCode.text.toString()
        else
            return Random.nextInt(0, 10000000).toString()
    }

    private fun checkNulls(){
        if(binding.itemYear.text.toString() == "")
            binding.itemYear.setText("0")
        else if(binding.itemPrice.text.toString() == "")
            binding.itemPrice.setText("0")
        else if(binding.itemDesc.text.toString() == "")
            binding.itemDesc.setText("-")
    }
}