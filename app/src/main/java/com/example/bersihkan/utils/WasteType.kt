package com.example.bersihkan.utils

enum class WasteType(val type: String, val price: Int) {
    INITIAL("Select Type", 0),
    PLASTIC("Plastic", 500),
    PAPER("Paper", 200),
    RUBBER("Rubber", 700),
}