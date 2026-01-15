package com.example.iotapplication.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.traceprocessor.Row
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.iotapplication.data.model.garden.DeviceStatus
import com.example.iotapplication.data.model.garden.GardenDetail
import com.example.iotapplication.data.model.garden.SensorLog
import com.example.iotapplication.data.remote.api.ApiService
import com.example.iotapplication.ui.viewmodel.GardenSensorViewModel
import com.example.iotapplication.ui.viewmodel.GardenSensorViewModelFactory
import com.example.iotapplication.ui.viewmodel.GardenViewModel
import java.lang.System.console
import java.time.LocalDate
import java.time.OffsetDateTime

@Composable
fun GardenHeaderSection(
    garden: GardenDetail,
    deviceStatus: DeviceStatus?,
    onSave: (String, String?) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(garden.gardenName) }
    var desc by remember { mutableStateOf(garden.description ?: "") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            if (isEditing) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên vườn") },
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Mô tả") },
                    shape = RoundedCornerShape(12.dp)
                )
            } else {
                Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(desc)
            }
                // ===== DEVICE LINE =====
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                    Text(
                        text = "Device: ${garden.device?.deviceCode ?: "—"}",
                        color = Color.Gray
                    )

                    Spacer(Modifier.width(8.dp))

                    deviceStatus?.let {
                        Text(
                            text = if (it.isOnline) "Online" else "Offline",
                            color = if (it.isOnline) Color(0xFF2E7D32) else Color.Red,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
            }

        }

        IconButton(
            onClick = {
                if (isEditing) onSave(name, desc)
                isEditing = !isEditing
            }
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                contentDescription = null
            )
        }
    }
}


@Composable
fun PlantSection(
    garden: GardenDetail,
    api: ApiService,
    onSave: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var isChanging by remember { mutableStateOf(false) }

    val plantsVm: GardenViewModel = remember {
        GardenViewModel(api)
    }

    LaunchedEffect(isChanging) {
        if (isChanging) plantsVm.loadPlants()
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Loại cây đang trồng: ${garden.plant?.name ?: "—"}")
            Icon(
                imageVector = if (expanded)
                    Icons.Default.KeyboardArrowUp
                else
                    Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        if (expanded) {
            Spacer(Modifier.height(8.dp))

            if (!isChanging) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    PlantCard(
                        plant = garden.plant!!,
                        selected = true,
                        onClick = {}
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = { isChanging = true }) {
                        Text("Thay đổi")
                    }
                }

            } else {
                LazyRow {
                    items(plantsVm.plants) { plant ->
                        PlantCard(
                            plant = plant,
                            selected = plantsVm.selectedPlant == plant,
                            onClick = { plantsVm.selectedPlant = plant }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            plantsVm.selectedPlant?.let {
                                onSave(it.id)
                            }
                            isChanging = false
                        }
                    ) {
                        Text("Xác nhận")
                    }
                }
            }
        }
    }
}

@Composable
fun NumberAdjuster(
    label: String,
    value: Int,
    onChange: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f))
        IconButton(onClick = { if (value > 0) onChange(value - 1) }) {
            Icon(Icons.Default.Remove, null)
        }
        Text(value.toString())
        IconButton(onClick = { onChange(value + 1) }) {
            Icon(Icons.Default.Add, null)
        }
    }
}

@Composable
fun IrrigationSection(
    garden: GardenDetail,
    pumpError: String?,
    onSaveAuto: (String, Int?, Int?, Int?) -> Unit,
    onPumpOn: (Int) -> Unit,
    onPumpOff: () -> Unit,
    navController: NavHostController
) {
    var mode by remember { mutableStateOf(garden.irrigationMode) }
    var threshold by remember { mutableIntStateOf(garden.autoIrrigationThreshold ?: 0) }
    var duration by remember { mutableIntStateOf(garden.autoIrrigationDuration ?: 0) }
    var interval by remember { mutableIntStateOf(garden.periodicIntervalHours ?: 0) }

    var manualDuration by remember { mutableIntStateOf(60) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Chế độ tưới", fontWeight = FontWeight.Bold)

            IconButton(onClick = {
                navController.navigate("irrigation-history/${garden.id}")
            }) {
                Icon(Icons.Default.History, contentDescription = "Lịch sử tưới")
            }
        }


        DropdownMenuBox(
            options = listOf("manual", "auto", "scheduled"),
            selected = mode,
            onSelect = {
                mode = it
                if (it != "auto") {
                    onSaveAuto(it, null, null, null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // ===== AUTO MODE =====
        if (mode == "auto") {
            NumberAdjuster("Threshold", threshold) { threshold = it }
            NumberAdjuster("Duration", duration) { duration = it }
            NumberAdjuster("Interval (h)", interval) { interval = it }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    onSaveAuto(mode, threshold, duration, interval)
                }) {
                    Text("Lưu chế độ auto")
                }
            }
        }

        // ===== MANUAL MODE =====
        if (mode == "manual") {
            Spacer(Modifier.height(12.dp))

            NumberAdjuster(
                label = "Duration (seconds)",
                value = manualDuration,
                onChange = { manualDuration = it.coerceAtLeast(1) }
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { onPumpOn(manualDuration) }) {
                    Text("Bật")
                }

                Spacer(Modifier.width(12.dp))

                OutlinedButton(onClick = onPumpOff) {
                    Text("Tắt")
                }
            }

            pumpError?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }
        }
    }
}


@Composable
fun LedToggleSection(
    gardenId: Int,
    autoMode: Boolean,
    isLedOn: Boolean,
    error: String?,
    onToggleAuto: (Boolean) -> Unit,
    onLedOn: () -> Unit,
    onLedOff: () -> Unit
) {
    var manualLedOn by remember { mutableStateOf(isLedOn) }

    Column {
        Text("Chế độ đèn", fontWeight = FontWeight.Bold)
        // ===== AUTO MODE =====
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Đèn tự động")
            Switch(
                checked = autoMode,
                onCheckedChange = onToggleAuto
            )
        }

        // ===== MANUAL MODE =====
        if (!autoMode) {
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Bật đèn")

                Switch(
                    checked = manualLedOn,
                    onCheckedChange = { checked ->
                        if (checked) {
                            manualLedOn = true
                            onLedOn()
                        } else {
                            manualLedOn = false
                            onLedOff()
                        }
                    }
                )
            }

            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )

                // rollback UI
                LaunchedEffect(error) {
                    manualLedOn = false
                }
            }
        }
    }
}


@Composable
fun AlertThresholdSection(
    garden: GardenDetail,
    onSave: (Int, Int, Int) -> Unit
) {
    var minT by remember { mutableIntStateOf(garden.alertMinTemperature) }
    var maxT by remember { mutableIntStateOf(garden.alertMaxTemperature) }
    var minSoil by remember { mutableIntStateOf(garden.alertMinSoilMoisture) }

    var changed by remember { mutableStateOf(false) }

    Column {
        Text("Cài đặt thông báo giới hạn", fontWeight = FontWeight.Bold)
        NumberAdjuster("Min Temp", minT) { minT = it; changed = true }
        NumberAdjuster("Max Temp", maxT) { maxT = it; changed = true }
        NumberAdjuster("Min Soil", minSoil) { minSoil = it; changed = true }

        if (changed) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    onSave(minT, maxT, minSoil)
                    changed = false
                }) {
                    Text("Xác nhận")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SensorCard(
    title: String,
    value: String,
    expanded: Boolean,
    selector: (SensorLog) -> Double?,
    logs: List<SensorLog>,
    min: Double?,
    max: Double?,
    onClick: () -> Unit
) {
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable(onClick = onClick)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(title)
                Text(value, style = MaterialTheme.typography.headlineSmall)
            }
        }

        if (expanded) {
            SensorLineChart(
                logs = logs,
                selector = selector
            )

            Spacer(Modifier.height(8.dp))

            Text("Min: ${min ?: "--"}")
            Text("Max: ${max ?: "--"}")
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SensorStatusSection(
    gardenId: Int,
    api: ApiService
) {
    val viewModel: GardenSensorViewModel = viewModel(
        factory = GardenSensorViewModelFactory(api)
    )

    // Start realtime
    LaunchedEffect(gardenId) {
        viewModel.startRealtime(gardenId)
    }

    // Stop khi rời màn
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopRealtime()
        }
    }

    val status = viewModel.status ?: return


    val device = status.device
    if (device == null) {
        Text(
            text = "Chưa có thiết bị được gắn cho vườn này",
            color = MaterialTheme.colorScheme.error
        )
        return
    }

    val sensors = device.sensors

    Column {
        Text("Cảm biến trực tiếp", fontWeight = FontWeight.Bold)

        SensorCard(
            title = "Nhiệt độ",
            value = "${sensors.temperature ?: "--"} °C",
            expanded = viewModel.expandedSensor == "temp",
            selector = { it.temperature },
            logs = viewModel.logs,
            min = viewModel.statistics?.minTemperature,
            max = viewModel.statistics?.maxTemperature
        ) {
            viewModel.toggleSensor(gardenId, "temp")
        }

        SensorCard(
            title = "Độ ẩm không khí",
            value = "${sensors.airHumidity ?: "--"} %",
            expanded = viewModel.expandedSensor == "air",
            selector = { it.airHumidity },
            logs = viewModel.logs,
            min = null,
            max = null
        ) {
            viewModel.toggleSensor(gardenId, "air")
        }

        SensorCard(
            title = "Độ ẩm đất",
            value = "${sensors.soilMoisture ?: "--"} %",
            expanded = viewModel.expandedSensor == "soil",
            selector = { it.soilMoisture },
            logs = viewModel.logs,
            min = viewModel.statistics?.minSoilMoisture,
            max = viewModel.statistics?.maxSoilMoisture
        ) {
            viewModel.toggleSensor(gardenId, "soil")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun averagePerDay(
    logs: List<SensorLog>,
    selector: (SensorLog) -> Double?
): List<Pair<LocalDate, Double>> {

    return logs
        .mapNotNull { log ->
            val value = selector(log)
            if (value != null) {
                val date = OffsetDateTime.parse(log.recordedAt).toLocalDate()
                date to value
            } else null
        }
        .groupBy { it.first }
        .mapValues { entry ->
            entry.value.map { it.second }.average()
        }
        .toList()
        .sortedBy { it.first }
        .takeLast(7)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineChart7Days(
    data: List<Pair<LocalDate, Double>>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(220.dp)
) {
    if (data.isEmpty()) {
        Text("Không có dữ liệu")
        return
    }

    val values = data.map { it.second }

    val max = values.maxOrNull() ?: 1.0
    val min = values.minOrNull() ?: 0.0

    // mở rộng biên để line nằm giữa
    val paddingValue = (max - min) * 0.1
    val chartMax = max + paddingValue
    val chartMin = min - paddingValue
    val range = (chartMax - chartMin).takeIf { it > 0 } ?: 1.0

    val textPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.DKGRAY
        textSize = 28f
        isAntiAlias = true
    }

    Canvas(modifier = modifier.padding(16.dp)) {

        val leftPadding = 80f
        val bottomPadding = 60f
        val topPadding = 20f

        val chartWidth = size.width - leftPadding
        val chartHeight = size.height - bottomPadding - topPadding

        val stepX = chartWidth / (data.size - 1).coerceAtLeast(1)

        fun valueToY(value: Double): Float {
            return topPadding + chartHeight -
                    ((value - chartMin) / range * chartHeight).toFloat()
        }

        val points = data.mapIndexed { index, pair ->
            Offset(
                x = leftPadding + stepX * index,
                y = valueToY(pair.second)
            )
        }

        /* ===== Trục Y ===== */
        drawLine(
            color = Color.Gray,
            start = Offset(leftPadding, topPadding),
            end = Offset(leftPadding, topPadding + chartHeight),
            strokeWidth = 2f
        )

        // chia trục Y (4 mốc)
        val ySteps = 4
        for (i in 0..ySteps) {
            val value = chartMin + range / ySteps * i
            val y = valueToY(value)

            drawLine(
                color = Color.LightGray,
                start = Offset(leftPadding, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )

            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.1f", value),
                0f,
                y + 8f,
                textPaint
            )
        }

        /* ===== Trục X ===== */
        drawLine(
            color = Color.Gray,
            start = Offset(leftPadding, topPadding + chartHeight),
            end = Offset(size.width, topPadding + chartHeight),
            strokeWidth = 2f
        )

        data.forEachIndexed { index, pair ->
            val x = leftPadding + stepX * index
            val label = pair.first.dayOfMonth.toString()

            drawContext.canvas.nativeCanvas.drawText(
                label,
                x - 14f,
                size.height,
                textPaint
            )
        }

        /* ===== Line ===== */
        for (i in 0 until points.lastIndex) {
            drawLine(
                color = Color(0xFF4CAF50),
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f
            )
        }

        /* ===== Point + value ===== */
        points.forEachIndexed { index, point ->
            drawCircle(
                color = Color(0xFF2E7D32),
                radius = 6f,
                center = point
            )

            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.1f", data[index].second),
                point.x - 20,
                point.y - 12,
                textPaint
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SensorLineChart(
    logs: List<SensorLog>,
    selector: (SensorLog) -> Double?
) {


    val dailyAverage = remember(logs, selector) {
        averagePerDay(logs, selector)
    }



    Column {
        Text(
            "Biểu đồ trung bình 7 ngày",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        LineChart7Days(dailyAverage)
    }
}






