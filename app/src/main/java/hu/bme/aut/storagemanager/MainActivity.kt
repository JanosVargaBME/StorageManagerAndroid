package hu.bme.aut.storagemanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.storagemanager.adapter.StorageAdapter
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.data.StorageItemDatabase
import hu.bme.aut.storagemanager.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), StorageAdapter.StorageItemClickListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: StorageItemDatabase
    private lateinit var adapter: StorageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = StorageItemDatabase.getDatabase(applicationContext)

        binding.reader.setOnClickListener {
            val intent = Intent(this@MainActivity, ReaderActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddItem.setOnClickListener {
            val intent = Intent(this@MainActivity, NewItemActivity::class.java)
            intent.putExtra("id", "-")
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
    }

    override fun onItemChanged(item: StorageItem) {
        thread {
            database.storageItemDao().update(item)
        }
    }

    override fun onItemDeleted(item: StorageItem, position: Int) {
        thread {
            database.storageItemDao().deleteItem(item)
            runOnUiThread {
                adapter.deleteItem(position)
            }
        }
    }

    private fun initRecyclerView() {
        adapter = StorageAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.storageItemDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }
}