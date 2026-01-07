package com.example.iotapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import com.example.iotapplication.ui.components.DropdownMenuBox
import com.example.iotapplication.ui.components.PlantCard
import com.example.iotapplication.ui.viewmodel.GardenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGardenBottomSheet(
    viewModel: GardenViewModel,
    onClose: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var deviceCode by remember { mutableStateOf("") }
    var irrigation by remember { mutableStateOf("manual") }

    LaunchedEffect(Unit) {
        viewModel.loadPlants()
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        modifier = Modifier.fillMaxHeight(1f)
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Thêm vườn mới", fontWeight = FontWeight.Bold)
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, null)
                }
            }

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên vườn*") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Mô tả") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = deviceCode, onValueChange = { deviceCode = it }, label = { Text("Device code*") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(8.dp))
            Text(
                "Loại cây: ${viewModel.selectedPlant?.name ?: "hãy chọn 1 loại cây"}",
                color = if (viewModel.selectedPlant == null) Color.Gray else Color.Unspecified
            )

            LazyRow {
                items(viewModel.plants) { plant ->
                    PlantCard(
                        plant = plant,
                        selected = viewModel.selectedPlant == plant,
                        onClick = { viewModel.selectedPlant = plant }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            DropdownMenuBox(
                options = listOf("manual", "auto", "scheduled"),
                selected = irrigation,
                onSelect = { irrigation = it }
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.createGarden(
                        name, desc, deviceCode, irrigation
                    )
                    onClose()
                },
                enabled = name.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tạo vườn mới")
            }
        }
    }
}
