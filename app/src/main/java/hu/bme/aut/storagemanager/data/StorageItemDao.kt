package hu.bme.aut.storagemanager.data

import androidx.room.*

@Dao
interface StorageItemDao {
    @Query("SELECT * FROM storageitem")
    fun getAll(): List<StorageItem>

    @Insert
    fun insert(storageItem: StorageItem): Long

    @Update
    fun update(storageItem: StorageItem)

    @Delete
    fun deleteItem(storageItem: StorageItem)
}