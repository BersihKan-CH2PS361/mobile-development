package com.example.bersihkan.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String? = null
)

data class LoginResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class User(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("last_login")
	val lastLogin: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("registered")
	val registered: String? = null,

	@field:SerializedName("facility_ID")
	val facilityID: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("facility_name")
	val facilityName: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("last_logout")
	val lastLogout: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

// Used for Logout, Update Profile, Create Order, Update Order Status,
data class GeneralResponse(

	@field:SerializedName("status")
	val status: String? = null
)

data class DetailUserResponse(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

// used for Detail Order, Detail History, Ongoing Order Collector
data class DetailOrderResponse(

	@field:SerializedName("pickup_fee")
	val pickupFee: Int? = null,

	@field:SerializedName("pickup_location")
	val pickupLocation: String? = null,

	@field:SerializedName("pickup_longitude")
	val pickupLongitude: Float? = null,

	@field:SerializedName("user_notes")
	val userNotes: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("user_phone")
	val userPhone: String? = null,

	@field:SerializedName("subtotal_fee")
	val subtotalFee: Int? = null,

	@field:SerializedName("waste_type")
	val wasteType: String? = null,

	@field:SerializedName("order_datetime")
	val orderDatetime: String? = null,

	@field:SerializedName("pickup_datetime")
	val pickupDatetime: String? = null,

	@field:SerializedName("waste_qty")
	val wasteQty: Int? = null,

	@field:SerializedName("order_status")
	val orderStatus: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("pickup_latitude")
	val pickupLatitude: Float? = null,

	@field:SerializedName("collector_id")
	val collectorId: String? = null,

	@field:SerializedName("recycle_fee")
	val recycleFee: Int? = null,

	@field:SerializedName("order_id")
	val orderId: Int? = null,

	@field:SerializedName("facility_name")
	val facilityName: String? = null,

)

data class ContentsResponse(

	@field:SerializedName("content_title")
	val contentTitle: String? = null,

	@field:SerializedName("content_text")
	val contentText: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class DetailCollectorResponse(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("facility_id")
	val facilityId: Int? = null,

	@field:SerializedName("facility_name")
	val facilityName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class DetailCollectorByIdResponse(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("collector_name")
	val collectorName: String? = null,

	@field:SerializedName("facility_id")
	val facilityId: Int? = null,

	@field:SerializedName("facility_name")
	val facilityName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class DetailOrderAll(

	var detailOrderResponse: DetailOrderResponse? = null,

	var detailCollectorResponse: DetailCollectorByIdResponse? = null,

	var addressFromLatLng: String? = null
)

data class DetailOrderCollectorAll(

	var detailOrderResponse: DetailOrderResponse? = null,

	var addressFromLatLng: String? = null
)

data class GeocodingResponse(
	val results: List<Result>,
	val status: String
)

data class Result(
	val formatted_address: String
	// Add other address components as needed
)
