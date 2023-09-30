package com.riders.barcodescannerstl.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@kotlinx.serialization.Serializable
@Entity(tableName = "order")
data class OrderModel(
    val date: String,
    val number: String,
    val buyerName: String,
    val state: String,
    val price: String,
    val description: String,
    val quantity: Int,
    val unitPrice: String,
    val salesCommission: String,
    val totalPrice: String
) : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var _id: Long = 0L
}
