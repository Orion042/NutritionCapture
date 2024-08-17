package com.example.nutritioncapture.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.nutritioncapture.R
import com.example.nutritioncapture.data.repository.NutritionCaptureRepository
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun GraphView(navController: NavController) {

    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .fillMaxSize()
    ) {
        DisplayGraph()
        DisplayAiAdvice()
    }
}

@Composable
fun DisplayAiAdvice() {
    Text(
        text = "AIアドバイス",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 40.dp, start = 5.dp)
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DisplayGraph() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var lineData by remember { mutableStateOf<LineData?>(null) }
    var isAcquiredFromDatabase by remember { mutableStateOf(false) }

    // TODO: UIや表示内容は未定
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            val databaseRepository = NutritionCaptureRepository(context)

            val dishes = databaseRepository.getAllDishes()

            val entries = dishes.mapIndexed { index, dish ->
                showLog("graph index: $index, dish calorie: ${dish.dishesCalorie}")
                Entry(index.toFloat(), dish.dishesCalorie)
            }

            showLog("entries: ${entries.joinToString(", ")}")

            val dataSet = LineDataSet(entries, "Calorie Graph")

            lineData = LineData(dataSet)

            isAcquiredFromDatabase = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "カロリーの推移",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.cornflower_blue),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        if (isAcquiredFromDatabase) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .height(400.dp),
                factory = { context ->
                    LineChart(context).apply {
                        this.data = lineData
                        configureChart()
                    }
                }
            )
        }
    }
}

private fun LineChart.configureChart() {
    xAxis.apply {
        isEnabled = true
        valueFormatter = null
        setDrawLabels(false)
        setDrawAxisLine(true)
        setDrawGridLines(false)
    }

    axisLeft.apply {
        isEnabled = true
        valueFormatter = null
        setDrawLabels(false)
        setDrawAxisLine(true)
        setDrawGridLines(false)
    }

    axisRight.apply {
        isEnabled = false
        valueFormatter = null
        setDrawLabels(false)
        setDrawAxisLine(true)
        setDrawGridLines(false)
    }

    legend.isEnabled = false
    description.isEnabled = false
    setTouchEnabled(false)
    setDragEnabled(false)
    setScaleEnabled(false)
    isScaleXEnabled = false
    isScaleYEnabled = false
    isHighlightPerTapEnabled = false

    invalidate()
}

fun convertDateToFloat(date: Date): Float {
    val elapsedTimeMillis = date.time
    return (elapsedTimeMillis / (1000 * 60 * 60 * 24)).toFloat()
}

private fun showLog(logMessage: String) {
    Log.d("GraphView", logMessage)
}