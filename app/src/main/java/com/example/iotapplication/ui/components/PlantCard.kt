package com.example.iotapplication.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.iotapplication.data.model.garden.Plant

@Composable
fun PlantCard(
    plant: Plant,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        border = if (selected) BorderStroke(2.dp, Color.Blue) else null,
        modifier = Modifier
            .padding(8.dp)
            .width(220.dp)
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(plant.name, fontWeight = FontWeight.Bold)
            Text(plant.description)
            Text("Temp: ${plant.minTemperature}-${plant.maxTemperature}")
            Text("Air: ${plant.minAirHumidity}-${plant.maxAirHumidity}")
            Text("Soil: ${plant.minSoilMoisture}-${plant.maxSoilMoisture}")
        }
    }
}
