package com.example.bersihkan.utils

enum class WasteType(val type: String, val price: Int, val idx: Int) {
    INITIAL("Select Type", 0, -1),
    PLASTIC("Plastic", 500, 3),
    PAPER("Paper", 200, 3),
    RUBBER("Rubber", 700, 2),
}