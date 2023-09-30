package com.riders.barcodescannerstl.data

import android.content.Context
import com.riders.barcodescannerstl.data.local.IDb
import com.riders.barcodescannerstl.data.remote.dto.Order

interface IRepository : IDb {

    suspend fun getOrderFromAssetsToJsonWithCSV(context: Context): List<Order>?
    suspend fun getOrderFromAssetsToJson(context: Context): List<Order>?
}