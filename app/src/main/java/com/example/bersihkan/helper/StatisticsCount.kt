package com.example.bersihkan.helper

import android.content.Context
import com.example.bersihkan.R
import com.example.bersihkan.data.remote.response.DetailOrderResponseItem
import com.example.bersihkan.utils.Statistics

//data class StatisticsTotals(
//    val totalQuantity: Int,
//    val totalTypes: Int,
//    val totalOrders: Int
//)

fun calculateStatisticsTotals(histories: List<DetailOrderResponseItem>, context: Context): List<Statistics> {
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
