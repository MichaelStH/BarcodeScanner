package com.riders.barcodescannerstl.data.remote.dto

import com.riders.barcodescannerstl.data.local.model.OrderModel

data class Order(
    val date: String,
    val number: Int,
    val buyerName: String,
    val state: String,
    val price: String,
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
    val salesCommission: Double,
    val totalPrice: Double
)

fun Order.toModel(): OrderModel = OrderModel(
    this.date,
    this.number,
    this.buyerName,
    this.state,
    this.price,
    this.description,
    this.quantity,
    this.unitPrice,
    this.salesCommission,
    this.totalPrice
)
