package com.example.bersihkan.data.remote.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("password_repeat")
    val passwordRepeat: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String
)

data class LoginRequest(

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("email")
    val email: String? = null

)

data class EditProfileRequest(

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,
)

data class OrderRequest(

    @field:SerializedName("facilityId")
    val facilityId: String,

    @field:SerializedName("pickup_fee")
    val pickupFee: Int,

    @field:SerializedName("pickup_longitude")
    val pickupLongitude: Long,

    @field:SerializedName("pickup_latitude")
    val pickupLatitude: Long,

    @field:SerializedName("user_notes")
    val userNotes: String,

    @field:SerializedName("subtotal_fee")
    val subtotalFee: Int,

    @field:SerializedName("waste_type")
    val wasteType: String,

    @field:SerializedName("recycle_fee")
    val recycleFee: Int,

    @field:SerializedName("waste_qty")
    val wasteQty: Int
)

data class UpdateStatusRequest(

    @field:SerializedName("order_status")
    val orderStatus: String
)