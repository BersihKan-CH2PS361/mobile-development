package com.example.bersihkan.utils

import androidx.compose.ui.res.stringResource
import com.example.bersihkan.R

enum class OrderStatus
    (
    val status: String,
    val text: Int,
    val button: String,
    val notifTitle: String,
    val notifText: String
) {
    INITIAL
        (
        "initial",
        R.string.order_created,
        "Pick up",
        "Order Pick-up!",
        "Collector is on the way to pick up your order"
    ),
    PICK_UP
        (
        "pick_up",
        R.string.pick_up_desc,
        "Arrived",
        "Pick up your order!",
        "Collector is on the way to pick up your order"
    ),
    ARRIVED(
        "arrived",
        R.string.arrived_desc,
        "Delivering",
        "Collector arrived!",
        "Collector has arrived at the pick-up location"
    ),
    DELIVERING(
        "delivering",
        R.string.delivering_desc,
        "Delivered",
        "Delivering waste!",
        "Collector is delivering your waste order to the recycle house"
    ),
    DELIVERED(
        "delivered",
        R.string.delivered_desc,
        "Delivered",
        "Waste delivered",
        "Collector has delivered your waste order to the recycle house"
    ),
}