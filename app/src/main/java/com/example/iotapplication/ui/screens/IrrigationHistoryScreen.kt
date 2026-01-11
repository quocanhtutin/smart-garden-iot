package com.example.iotapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.iotapplication.data.model.garden.IrrigationLog
import com.example.iotapplication.data.model.garden.IrrigationStatistics
import com.example.iotapplication.data.remote.api.ApiService
import com.example.iotapplication.ui.viewmodel.IrrigationHistoryViewModel
import com.example.iotapplication.ui.viewmodel.IrrigationHistoryViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IrrigationHistoryScreen(
    gardenId: Int,
    api: ApiService,
    navController: NavHostController
) {
    val factory = remember { IrrigationHistoryViewModelFactory(api) }
    val viewModel: IrrigationHistoryViewModel = viewModel(factory = factory)

    LaunchedEffect(Unit) {
        viewModel.load(
            gardenId = gardenId,
            from = "2024-01-01T00:00:00Z",
            to = "2026-12-31T23:59:59Z"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lịch sử tưới tiêu") },
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
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text("Thống kê tưới", fontWeight = FontWeight.Bold)
            }

            item {
                viewModel.statistics?.let {
                    IrrigationStatisticsCard(it)
                }
            }

            item {
                Text("Lịch sử tưới", fontWeight = FontWeight.Bold)
            }

            items(viewModel.logs) { log ->
                IrrigationLogRow(log)
            }
        }
    }
}

@Composable
fun IrrigationStatisticsCard(stat: IrrigationStatistics) {

        Column(Modifier.padding(12.dp)) {
            Text("Số lần tưới: ${stat.totalIrrigations}")
            Text("Tổng thời gian: ${stat.totalDuration} giây")
            Text("Thời gian trung bình: ${stat.avgDuration} giây")

            Spacer(Modifier.height(8.dp))
            stat.byMode.forEach {
                Text("• ${it.mode}: ${it.count} lần")
            }
        }

}

@Composable
fun IrrigationLogRow(log: IrrigationLog) {
    Card {
        Column(Modifier.padding(12.dp)) {
            Text("Chế độ: ${log.mode}", fontWeight = FontWeight.Medium)
            Text("Bắt đầu: ${log.startTime}")
            Text("Kết thúc: ${log.endTime ?: "-"}")
            Text("Thời gian: ${log.duration ?: 0} giây")
            Text("Trạng thái: ${log.status}")
            log.note?.let {
                Text("Ghi chú: $it", fontSize = 12.sp)
            }
        }
    }
}

