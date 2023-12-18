package com.example.bersihkan.data.local

import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailCollectorByIdResponse
import com.example.bersihkan.data.remote.response.DetailCollectorResponse
import com.example.bersihkan.data.remote.response.DetailOrderAll
import com.example.bersihkan.data.remote.response.DetailOrderCollectorAll
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.remote.response.DetailUserResponse
import com.example.bersihkan.data.remote.response.User

object DataDummy {

    val detailOrderResponse = listOf(
        DetailOrderResponse(
            pickupFee = 50,
            pickupLongitude = 123.456f,
            userNotes = "Sample user notes",
            subtotalFee = 100,
            wasteType = "Plastic",
            orderDatetime = "2023-12-14T10:00:00.000Z",
            pickupDatetime = "2023-12-15T12:00:00.000Z",
            wasteQty = 5,
            orderStatus = "delivered",
            userId = "7494534b-ccc5-4d21-a6c4-38813da2d4f7",
            pickupLatitude = 78.910f,
            recycleFee = 20,
            orderId = 789
        ),
        DetailOrderResponse(
            pickupFee = 60,
            pickupLongitude = 78.910f,
            userNotes = "Another user notes",
            subtotalFee = 120,
            wasteType = "Paper",
            orderDatetime = "2023-12-16T09:00:00.000Z",
            pickupDatetime = "2023-12-17T11:00:00.000Z",
            wasteQty = 8,
            orderStatus = "delivered",
            userId = "7494534b-ccc5-4d21-a6c4-38813da2d4f7",
            pickupLatitude = 12.345f,
            recycleFee = 30,
            orderId = 890
        ),
        // Additional DetailOrderResponseItem instances
        DetailOrderResponse(
            pickupFee = 70,
            pickupLongitude = 45.678f,
            userNotes = "Third user notes",
            subtotalFee = 150,
            wasteType = "Rubber",
            orderDatetime = "2023-12-18T08:00:00.000Z",
            pickupDatetime = "2023-12-19T10:00:00.000Z",
            wasteQty = 10,
            orderStatus = "delivering",
            userId = "7494534b-ccc5-4d21-a6c4-38813da2d4f7",
            pickupLatitude = 90.123f,
            recycleFee = 40,
            orderId = 901
        ),
        DetailOrderResponse(
            pickupFee = 80,
            pickupLongitude = 90.123f,
            userNotes = "Fourth user notes",
            subtotalFee = 200,
            wasteType = "Plastic",
            orderDatetime = "2023-12-20T07:00:00.000Z",
            pickupDatetime = "2023-12-21T09:00:00.000Z",
            wasteQty = 12,
            orderStatus = "delivered",
            userId = "7494534b-ccc5-4d21-a6c4-38813da2d4f7",
            pickupLatitude = 34.567f,
            recycleFee = 50,
            orderId = 912
        ),
    )

    val contentResponseList = listOf(
        ContentsResponse(
            contentTitle = "Content Title 1",
            contentText = "Content text for item 1",
            id = 1
        ),
        ContentsResponse(
            contentTitle = "Content Title 2",
            contentText = "Content text for item 2",
            id = 2
        ),
        ContentsResponse(
            contentTitle = "Content Title 3",
            contentText = "Content text for item 3",
            id = 3
        ),
        ContentsResponse(
            contentTitle = "Content Title 4",
            contentText = "Content text for item 4",
            id = 4
        ),
        ContentsResponse(
            contentTitle = "Content Title 5",
            contentText = "Content text for item 5",
            id = 5
        )
    )

    val user = DetailUserResponse(
        role = "user",
        phone = "+1234567890",
        name = "John Doe",
        id = "1",
        email = "john@example.com",
        username = "johndoe"
    )

    // DetailCollectorResponse dummy data
    val detailCollectorResponse1 = DetailCollectorByIdResponse(
        userId = "98765",
        phone = "9876543210",
        collectorName = "Alice Smith",
        facilityId = 2,
        facilityName = "Facility XYZ",
        email = "alice@example.com",
        username = "alicesmith"
    )

    val detailCollectorResponse2 = DetailCollectorByIdResponse(
        userId = "54321",
        phone = "5555555555",
        collectorName = "Bob Johnson",
        facilityId = 3,
        facilityName = "Facility PQR",
        email = "bob@example.com",
        username = "bobjohnson"
    )

    // DetailOrderResponse dummy data
    val detailOrderResponse1 = DetailOrderResponse(
        pickupFee = 75,
        pickupLongitude = 15.6789f,
        userNotes = "More user notes",
        subtotalFee = 120,
        wasteType = "Glass",
        orderDatetime = "2023-12-02 10:30:00.000Z",
        pickupDatetime = "2023-12-02 11:30:00.000Z",
        wasteQty = 8,
        orderStatus = "Processing",
        userId = "24680",
        pickupLatitude = 25.4321f,
        collectorId = "13579",
        recycleFee = 30,
        orderId = 456
    )

    val detailOrderResponse2 = DetailOrderResponse(
        pickupFee = 90,
        pickupLongitude = 20.9876f,
        userNotes = "Additional notes",
        subtotalFee = 150,
        wasteType = "Metal",
        orderDatetime = "2023-12-03 11:00:00.000Z",
        pickupDatetime = "2023-12-03 12:00:00.000Z",
        wasteQty = 10,
        orderStatus = "Completed",
        userId = "13579",
        pickupLatitude = 30.1234f,
        collectorId = "98765",
        recycleFee = 40,
        orderId = 789
    )

    // DetailOrderAll dummy data
    val detailOrderAll1 = DetailOrderAll(
        detailOrderResponse = detailOrderResponse[1],
        detailCollectorResponse = detailCollectorResponse1,
        addressFromLatLng = "123 Main St, City A"
    )

    val detailOrderAll2 = DetailOrderAll(
        detailOrderResponse = detailOrderResponse[2],
        detailCollectorResponse = detailCollectorResponse2,
        addressFromLatLng = "456 Elm St, City B"
    )

    val detailUserResponse = DetailUserResponse(
        role = "Role",
        phone = "1234567890",
        name = "John Doe",
        id = "user_id_123",
        email = "johndoe@example.com",
        username = "johndoe123"
    )

    val detailOrderCollectorAll = DetailOrderCollectorAll(
        detailOrderResponse = detailOrderResponse[1],
        addressFromLatLng = "123 Main Street, City, Country"
    )

}