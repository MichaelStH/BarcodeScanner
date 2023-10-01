package com.riders.barcodescannerstl.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.riders.barcodescannerstl.data.local.database.converter.OrderStateConverter
import com.riders.barcodescannerstl.data.local.database.dao.OrderDao
import com.riders.barcodescannerstl.data.local.model.OrderModel

@Database(
    entities = [OrderModel::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(OrderStateConverter::class)
abstract class OrderDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "barcode_database"
    }

    abstract fun getOrderDao(): OrderDao
}