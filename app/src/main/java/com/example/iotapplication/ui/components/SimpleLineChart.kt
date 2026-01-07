package com.example.iotapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SimpleLineChart(values: List<Double>) {
    if (values.isEmpty()) {
        Text("Không có dữ liệu")
        return
    }

    Column {
        Text("Biểu đồ 7 ngày (trung bình)")
        values.forEach {
            Box(
                Modifier
                    .height(4.dp)
                    .fillMaxWidth(fraction = (it / (values.maxOrNull() ?: 1.0)).toFloat())
                    .background(Color.Green)
            )
            Spacer(Modifier.height(4.dp))
        }
    }
}
