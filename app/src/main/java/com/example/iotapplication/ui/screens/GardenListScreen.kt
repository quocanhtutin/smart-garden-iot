package com.example.iotapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iotapplication.data.model.garden.Garden
import com.example.iotapplication.ui.viewmodel.GardenViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun formatDate(iso: String): String {
    return OffsetDateTime.parse(iso)
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}
@Composable
fun GardenListScreen(
    viewModel: GardenViewModel,
    onGardenClick: (Garden) -> Unit
) {
    var showAdd by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadGardens()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { padding ->

        Column {

            Text(
                "Danh sách vườn của bạn",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp, 0.dp),
                fontSize = 20.sp
            )

            LazyColumn(Modifier.padding(0.dp ,16.dp)) {
                items(viewModel.gardens) { garden ->

                    val isConnected = garden.device?.isConnected == true

                    val cardColor =
                        if (isConnected) Color(0xFF2E7D32)
                        else Color(0xFFC62828)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable { onGardenClick(garden) },
                        colors = CardDefaults.cardColors(
                            containerColor = cardColor
                        )
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            // ===== TÊN + NGÀY TẠO =====
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    garden.gardenName,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Text(
                                    formatDate(garden.createdAt),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(Modifier.height(6.dp))

                            // ===== MÔ TẢ ========== TRẠNG THÁI KẾT NỐI =====
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                garden.description?.let {
                                    Text(it, color = Color.White)
                                }

                                Text(
                                    text = if (isConnected) "Connected" else "Disconnected",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {
        AddGardenBottomSheet(
            viewModel = viewModel,
            onClose = { showAdd = false }
        )
    }
}

