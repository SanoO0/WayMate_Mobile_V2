package com.example.waymate_mobile.dtos.map

data class HereGeocodingResponse(
    val items: List<Item>
) {
    data class Item(
        val position: Position
    ) {
        data class Position(
            val lat: Double,
            val lng: Double
        )
    }
}

