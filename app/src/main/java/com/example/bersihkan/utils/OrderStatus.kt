package com.example.bersihkan.utils

import androidx.compose.ui.res.stringResource
import com.example.bersihkan.R

enum class OrderStatus(val status: String, val text: Int) {
    INITIAL("initial", R.string.order_created),
    PICK_UP("pick_up", R.string.pick_up_desc),
    ARRIVED("arrived", R.string.arrived_desc),
    DELIVERING("delivering", R.string.delivering_desc),
    DELIVERED("Delivered", R.string.delivered_desc),
}