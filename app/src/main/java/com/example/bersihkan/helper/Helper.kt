package com.example.bersihkan.helper

import android.content.Context
import com.example.bersihkan.R
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.remote.retrofit.maps.MapsApiConfig
import com.example.bersihkan.utils.OrderStatus
import com.example.bersihkan.utils.Statistics
import com.example.bersihkan.utils.WasteType
import java.text.SimpleDateFormat
import java.util.Locale

fun convertToDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault())

    val date = inputFormat.parse(dateString)
    return outputFormat.format(date)
}

fun isEmailValid(email: String): Boolean{
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return emailRegex.matches(email)
}

fun isPasswordValid(password: String): Boolean {
    return password.length >= 8
}

fun calculateStatisticsTotals(histories: List<DetailOrderResponse>, context: Context): List<Statistics> {
    val totalQuantity = histories.sumOf { it.wasteQty ?: 0 }
    val totalTypes = histories.map { it.wasteType }.distinct().count()
    val totalOrders = histories.map { it.orderId }.distinct().count()

    val statOrders = Statistics(
        qty = totalOrders,
        item = context.getString(R.string.orders),
        desc = context.getString(R.string.have_been_made)
    )
    val statQty = Statistics(
        qty = totalQuantity,
        item = context.getString(R.string.kg_of_waste),
        desc = context.getString(R.string.have_been_collected)
    )
    val statType = Statistics(
        qty = totalTypes,
        item = context.getString(R.string.type_of_waste),
        desc = context.getString(R.string.have_been_recycled)
    )

    return listOf(statOrders, statQty, statType)
}

fun calculateStatisticsCount(histories: List<DetailOrderResponse>, context: Context): List<Statistics> {
    val totalLocations = histories.map { it.pickupLatitude to it.pickupLongitude }.distinct().count()
    val totalFacilities = histories.map { it.facilityName }.distinct().count()
    val totalOrders = histories.map { it.orderId }.distinct().count()

    val statOrders = Statistics(
        qty = totalOrders,
        item = context.getString(R.string.orders_2),
        desc = ""
    )
    val statLoc = Statistics(
        qty = totalLocations,
        item = context.getString(R.string.location),
        desc = ""
    )
    val statFacility = Statistics(
        qty = totalFacilities,
        item = context.getString(R.string.recycle_house),
        desc = ""
    )

    return listOf(statOrders, statLoc, statFacility)
}

fun findWasteType(type: String): WasteType {
    return WasteType.values().find { it.type.equals(type, ignoreCase = true) } ?: WasteType.INITIAL
}


fun findOrderStatus(status: String): OrderStatus {
    return OrderStatus.values().find { it.status.equals(status, ignoreCase = true) } ?: OrderStatus.INITIAL
}

data class WasteTypeTotal(val wasteType: WasteType, val totalQuantity: Int)

fun calculateTotalWasteByType(detailOrderResponse: List<DetailOrderResponse>): List<WasteTypeTotal> {
    return detailOrderResponse
        .groupBy { it.wasteType }
        .map { (wasteType, orders) ->
            WasteTypeTotal(findWasteType(wasteType.toString()), orders.sumOf { it.wasteQty ?: 0 })
        }
}
