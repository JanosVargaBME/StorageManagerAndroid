package hu.bme.aut.storagemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StorageItem::class], version = 2)
abstract class StorageItemDatabase : RoomDatabase() {
    abstract fun storageItemDao(): StorageItemDao

    companion object {
        fun getDatabase(applicationContext: Context): StorageItemDatabase {
            return Room.databaseBuilder(
                applicationContext,
                StorageItemDatabase::class.java,
                "storage-list"
            ).build();
        }
    }
}