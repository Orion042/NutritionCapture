package com.example.nutritioncapture.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost

@Composable
fun GraphView(navController: NavController) {
    Text(text = "Graph")

    // データの準備
    val entries = listOf(
        Entry(1f, 1f),
        Entry(2f, 2f),
        Entry(3f, 0f),
        Entry(4f, 4f),
        Entry(5f, 3f)
    )
    val dataSet = LineDataSet(entries, "Sample Data")
    val lineData = LineData(dataSet)

    // LineChartの表示
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            LineChart(context).apply {
                this.data = lineData
                invalidate() // データを反映して再描画
            }
        }
    )
}
