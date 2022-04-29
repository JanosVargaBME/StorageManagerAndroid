package hu.bme.aut.storagemanager.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.storagemanager.ShowItemActivity
import hu.bme.aut.storagemanager.data.StorageItem
import hu.bme.aut.storagemanager.databinding.ItemStorageListBinding

class StorageAdapter(private val listener: StorageItemClickListener) :
    RecyclerView.Adapter<StorageAdapter.StorageViewHolder>()  {
    private val items = mutableListOf<StorageItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StorageViewHolder(
        ItemStorageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StorageViewHolder, position: Int) {
        val storageItem = items[position]

        holder.binding.tvName.text = storageItem.name
        holder.binding.tvPlace.text = storageItem.place
        holder.binding.tvDescription.text = storageItem.description
        holder.binding.tvID.text = "id: ${storageItem.id.toString()}"

        holder.binding.ibWatch.setOnClickListener{ v ->
            val intent = Intent(v.context, ShowItemActivity::class.java)
            intent.putExtra("id", storageItem.id.toString())
            v.context.startActivity(intent)
        }

        holder.binding.ibRemove.setOnClickListener{
            listener.onItemDeleted(storageItem, holder.adapterPosition)
        }
    }

    fun deleteItem(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(storageItems: List<StorageItem>) {
        items.clear()
        items.addAll(storageItems)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = items.size

    interface StorageItemClickListener {
        fun onItemChanged(item: StorageItem)
        fun onItemDeleted(item: StorageItem, position: Int)
    }

    inner class StorageViewHolder(val binding: ItemStorageListBinding) : RecyclerView.ViewHolder(binding.root)
}