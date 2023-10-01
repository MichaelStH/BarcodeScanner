package com.riders.barcodescannerstl.data.local

import com.riders.barcodescannerstl.data.local.database.dao.OrderDao
import com.riders.barcodescannerstl.data.local.model.OrderModel
import javax.inject.Inject

class DbImpl @Inject constructor(
    orderDao: OrderDao
) : IDb {

    private val mOrderDao: OrderDao = orderDao
    override fun insert(order: OrderModel): Long = mOrderDao.insert(order)

    override fun insertAll(orders: List<OrderModel>): List<Long> = mOrderDao.insertAll(orders)

    override fun getOrder(orderNumber: String): OrderModel? = mOrderDao.getOrder(orderNumber)

    override fun getOrders(): List<OrderModel> = mOrderDao.getOrders()

    override fun deleteAll() = mOrderDao.deleteAll()
    override fun tagAsChecked(orderId: Long) = mOrderDao.tagAsChecked(orderId)
}