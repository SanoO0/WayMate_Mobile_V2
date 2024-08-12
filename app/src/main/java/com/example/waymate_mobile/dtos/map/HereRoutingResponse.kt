package com.example.waymate_mobile.dtos.map

data class HereRoutingResponse(
    val routes: List<Route>
) {
    data class Route(
        val sections: List<Section>
    ) {
        data class Section(
            val summary: Summary
        ) {
            data class Summary(
                val duration: Int // Durée en secondes
            )
        }
    }
}

