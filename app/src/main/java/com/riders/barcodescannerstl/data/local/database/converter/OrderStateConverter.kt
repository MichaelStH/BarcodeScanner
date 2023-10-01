package com.riders.barcodescannerstl.data.local.database.converter

import androidx.room.TypeConverter
import com.riders.barcodescannerstl.data.local.model.OrderState

class OrderStateConverter {
    @TypeConverter
    fun toOrderState(value: String) = enumValueOf<OrderState>(value)

    @TypeConverter
    fun fromOrderState(value: OrderState) = value.name
}