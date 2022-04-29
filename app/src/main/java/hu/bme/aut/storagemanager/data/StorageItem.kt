package hu.bme.aut.storagemanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "storageitem")
data class StorageItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "place") var place: String,
    @ColumnInfo(name = "year") var year: Int,
    @ColumnInfo(name = "code") var code: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "price") var price: Int
)
