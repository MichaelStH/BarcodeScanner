package com.riders.barcodescannerstl.data.local

import com.riders.barcodescannerstl.data.local.model.OrderModel

interface IDb {
    fun insert(order: OrderModel): Long
    fun insertAll(orders: List<OrderModel>): List<Long>
    fun getOrder(orderNumber: String): OrderModel?
    fun getOrders(): List<OrderModel>
    fun deleteAll()
}