package com.riders.barcodescannerstl.data

import android.content.Context
import com.riders.barcodescannerstl.data.local.DbImpl
import com.riders.barcodescannerstl.data.local.model.OrderModel
import com.riders.barcodescannerstl.data.remote.dto.Order
import io.github.kotools.csv.reader.csvReader
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
import javax.inject.Inject


class RepositoryImpl @Inject constructor(dbImpl: DbImpl) : IRepository {

    private val mDbImpl: DbImpl = dbImpl
    override suspend fun getOrderFromAssetsToJsonWithCSV(context: Context): List<Order>? =
        runCatching {
            Timber.d("getOrderFromAssetsToJson()")
            val list = csvReader<Order> { file = "bizouk_orders_2023_09_30.csv" }
            Timber.d("runCatching | list: $list")
            list
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("runCatching | onFailure | Exception caught with message: ${it.message}")
            }
            .onSuccess {
                Timber.d("runCatching | onSuccess")
            }
            .getOrNull()

    override suspend fun getOrderFromAssetsToJson(context: Context): List<Order>? = runCatching {
        Timber.d("getOrderFromAssetsToJson()")

        val orderList = mutableListOf<Order>()
        val inputStreamAsset =
            InputStreamReader(context.assets.open("bizouk_orders_2023_09_30.csv"))
        val reader = BufferedReader(inputStreamAsset)
        reader.readLine()
        var line: String?
        var st: StringTokenizer? = null

        while (reader.readLine().also { line = it } != null) {
            Timber.d("line: $line")
            st = StringTokenizer(line, ";")
            val order = Order(
                date = st.nextToken(),
                number = st.nextToken(),
                buyerName = st.nextToken(),
                state = st.nextToken(),
                price = st.nextToken(),
                description = st.nextToken(),
                quantity = st.nextToken().toInt(),
                unitPrice = st.nextToken(),
                salesCommission = st.nextToken(),
                totalPrice = st.nextToken()
            )

            orderList.add(order)
        }

        inputStreamAsset.close()
        Timber.d("runCatching | order list: $orderList")
        orderList
    }
        .onFailure {
            it.printStackTrace()
            Timber.e("runCatching | onFailure | Exception caught with message: ${it.message}")
        }
        .onSuccess {
            Timber.d("runCatching | onSuccess")
        }
        .getOrNull()

    override fun insert(order: OrderModel): Long = mDbImpl.insert(order)

    override fun insertAll(orders: List<OrderModel>): List<Long> = mDbImpl.insertAll(orders)

    override fun getOrder(orderNumber: String): OrderModel? = mDbImpl.getOrder(orderNumber)

    override fun getOrders(): List<OrderModel> = mDbImpl.getOrders()

    override fun deleteAll() = mDbImpl.deleteAll()
    override fun tagAsChecked(orderId: Long) = mDbImpl.tagAsChecked(orderId)
}