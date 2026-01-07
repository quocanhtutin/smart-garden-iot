package com.example.iotapplication.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.iotapplication.data.model.garden.toUpdateRequest
import com.example.iotapplication.data.remote.api.ApiService
import com.example.iotapplication.ui.components.AlertThresholdSection
import com.example.iotapplication.ui.components.GardenHeaderSection
import com.example.iotapplication.ui.components.IrrigationSection
import com.example.iotapplication.ui.components.LedToggleSection
import com.example.iotapplication.ui.components.PlantSection
import com.example.iotapplication.ui.components.SensorStatusSection
import com.example.iotapplication.ui.viewmodel.GardenDetailViewModel
import com.example.iotapplication.ui.viewmodel.GardenDetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GardenDetailScreen(
    gardenId: Int,
    api: ApiService,
    navController: NavHostController
) {
    val factory = remember { GardenDetailViewModelFactory(api) }
    val viewModel: GardenDetailViewModel = viewModel(factory = factory)

    LaunchedEffect(gardenId) {
        viewModel.loadGarden(gardenId)
    }

    val garden = viewModel.gardenDetail ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết vườn") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                GardenHeaderSection(
                    garden = garden,
                    deviceStatus = viewModel.deviceStatus,
                    onSave = { name, desc ->
                        viewModel.updateGarden(
                            garden.toUpdateRequest(
                                gardenName = name,
                                description = desc
                            )
                        )
                    }
                )
            }

            item {
                PlantSection(
                    garden = garden,
                    api = api,
                    onSave = { newPlantId ->
                        viewModel.updateGarden(
                            garden.toUpdateRequest(plantId = newPlantId)
                        )
                    }
                )
            }

            item {
                IrrigationSection(
                    garden = garden,
                    pumpError = viewModel.pumpError,
                    onSaveAuto = { mode, threshold, duration, interval ->
                        viewModel.updateGarden(
                            garden.toUpdateRequest(
                                irrigationMode = mode,
                                autoIrrigationThreshold = threshold,
                                autoIrrigationDuration = duration,
                                periodicIntervalHours = interval
                            )
                        )
                    },
                    onPumpOn = { viewModel.pumpOn(it) },
                    onPumpOff = { viewModel.pumpOff() }
                )
            }

            item {
                LedToggleSection(
                    gardenId = garden.id,
                    autoMode = garden.ledAutoMode,
                    isLedOn = garden.device?.isLedOn == true,
                    error = viewModel.ledError,
                    onToggleAuto = {
                        viewModel.updateGarden(
                            garden.toUpdateRequest(ledAutoMode = it)
                        )
                    },
                    onLedOn = { viewModel.ledOn(garden.id) },
                    onLedOff = { viewModel.ledOff(garden.id) }
                )
            }

            item {
                AlertThresholdSection(
                    garden = garden,
                    onSave = { minT, maxT, minSoil ->
                        viewModel.updateGarden(
                            garden.toUpdateRequest(
                                alertMinTemperature = minT,
                                alertMaxTemperature = maxT,
                                alertMinSoilMoisture = minSoil
                            )
                        )
                    }
                )
            }

            item {
                SensorStatusSection(
                    gardenId = garden.id,
                    api = api
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            viewModel.deleteGarden(
                                gardenId = garden.id,
                                onSuccess = { navController.popBackStack() },
                                onError = {}
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Delete, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Xóa vườn")
                    }
                }
            }

            item {
                Spacer(Modifier.height(24.dp)) // tránh bị che
            }
        }
    }

}
