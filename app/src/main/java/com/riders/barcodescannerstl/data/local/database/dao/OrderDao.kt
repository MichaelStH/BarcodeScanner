package com.riders.barcodescannerstl.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riders.barcodescannerstl.data.local.model.OrderModel

@Dao
interface OrderDao {
    /* Method to insert contacts fetched from api to room */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(order: OrderModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(orders: List<OrderModel>): List<Long>

    /* Method to fetch orders stored locally */
    @Query("SELECT * FROM `order` WHERE number LIKE :orderNumber")
    fun getOrder(orderNumber: String): OrderModel

    @Query("SELECT * FROM `order`")
    fun getOrders(): List<OrderModel>

    @Query("DELETE FROM `order`")
    fun deleteAll()
}