package com.example.bersihkan.data.local

import com.example.bersihkan.data.remote.response.ContentsResponse
import com.example.bersihkan.data.remote.response.DetailOrderResponse
import com.example.bersihkan.data.remote.response.DetailOrderResponseItem

object DataDummy {

    val detailOrderResponse = listOf(
        DetailOrderResponseItem(
            pickupFee = 50,
            pickupLongitude = 123.456,
            userNotes = "Sample user notes",
            subtotalFee = 100,
            wasteType = "Plastic",
            orderDatetime = "2023-12-14T10:00:00.000Z",
            pickupDatetime = "2023-12-15T12:00:00.000Z",
            wasteQty = 5,
            orderStatus = "delivered",
            userId = "7494534b-ccc5-4d21-a6c4-38813da2d4f7",
            pickupLatitude = 78.910,
            recycleFee = 20,
            orderId = 789
        ),
        DetailOrderResponseItem(
            pickupFee = 60,
            pickupLongitude = 78.910,
            userNotes = "Another user notes",
            subtotalFee = 120,
            wasteType = "Paper",
            orderDatetime = "2023-12-16T09:00:00.000Z",
            pickupDatetime = "2023-12-17T11:00:00.000Z",
            wasteQty = 8,
            orderStatus = "delivered",
            userId = "7494534b-ccc5-4d21-a6c4-38813da2d4f7",
            pickupLatitude = 12.345,
            recycleFee = 30,
            orderId = 890
        ),
        // Additional DetailOrderResponseItem instances
        DetailOrderResponseItem(
            pickupFee = 70,
            pickupLongitude = 45.678,
            userNotes = "Third user notes",
            subtotalFee = 150,
            wasteType = "Rubber",
            orderDatetime = "2023-12-18T08:00:00.000Z",
            pickupDatetime = "2023-12-19T10:00:00.000Z",
            wasteQty = 10,
            orderStatus = "delivering",
            userId = "7494534b-ccc5-4d21-a6c4-38813da2d4f7",
            pickupLatitude = 90.123,
            recycleFee = 40,
            orderId = 901
        ),
        DetailOrderResponseItem(
            pickupFee = 80,
            pickupLongitude = 90.123,
            userNotes = "Fourth user notes",
            subtotalFee = 200,
            wasteType = "Plastic",
            orderDatetime = "2023-12-20T07:00:00.000Z",
            pickupDatetime = "2023-12-21T09:00:00.000Z",
            wasteQty = 12,
            orderStatus = "delivered",
            userId = "7494534b-ccc5-4d21-a6c4-38813da2d4f7",
            pickupLatitude = 34.567,
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


}