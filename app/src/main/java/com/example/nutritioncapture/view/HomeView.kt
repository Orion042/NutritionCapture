package com.example.nutritioncapture.view

import android.app.AlertDialog
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutritioncapture.R
import com.example.nutritioncapture.data.model.CardData
import com.example.nutritioncapture.data.service.getDummyCardDataFromRoom
import kotlinx.coroutines.launch

private val TAG = "HomeView.kt"

@Composable
fun HomeView() {
    val listState = rememberLazyListState()

    // ダイアログの表示状態
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (dialogContent, setDialogContent) = remember { mutableStateOf("") }

    // ポップアップメニューの表示状態
    val (showMenuIndex, setShowMenuIndex) = remember { mutableStateOf(-1) }

    val cardDataList = remember { mutableStateOf<List<CardData>?>(null) }

    LaunchedEffect(Unit) {
        val data = getDummyCardDataFromRoom()
        cardDataList.value = data
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Column {
                        Text(
                            text = "HomeView Cards",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 8.dp)
                        )
                        if(cardDataList.value != null) {
                            LazyRow(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .height(170.dp)
                            ) {
                                itemsIndexed(cardDataList.value!!) { index, cardData ->
                                    CardViewItem(
                                        index = index,
                                        date = cardData.date,
                                        cardContent = {
                                            Text(text = cardData.title)
                                        },
                                        onCardClick = {
                                            setDialogContent("Details for ${cardData.title}")
                                            setShowDialog(true)
                                        },
                                        showMenuIndex = showMenuIndex,
                                        setShowMenuIndex = setShowMenuIndex,
                                        menuItems = listOf(
                                            "修正" to {
                                                // TODO: 未定
                                            },
                                            "削除" to {
                                                // TODO: 削除処理
                                            }
                                        )
                                    )
                                }
                            }
                        }
                        else {
                            showLog("DBの結果がnull")
                        }
                    }
                }

                items(100) { index ->
                    Text(
                        text = "Item No.$index",
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

        // 撮影ボタン
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .background(
                    color = colorResource(id = R.color.cornflower_blue),
                    RoundedCornerShape(8.dp)
                )
                .clickable {
                    showLog("撮影ボタンタップ")
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera Icon",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "撮影",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showDialog) {
        displayDialog(
            showDialog = showDialog,
            onDismissRequest = { setShowDialog(false) },
            title = "詳細",
            text = dialogContent,
            onConfirm = { setShowDialog(false) }
        )
    }
}

@Composable
fun CardViewItem(
    index: Int,
    backgroundColor: Color = Color.LightGray,
    shape: Shape = RoundedCornerShape(12.dp),
    elevation: Dp = 4.dp,
    cardWidth: Dp = 190.dp,
    cardHeight: Dp = 170.dp,
    date: String,
    cardContent: @Composable () -> Unit,
    onCardClick: () -> Unit,
    showMenuIndex: Int,
    setShowMenuIndex: (Int) -> Unit,
    menuItems: List<Pair<String, () -> Unit>>
) {
    Card(
        backgroundColor = backgroundColor,
        shape = shape,
        elevation = elevation,
        modifier = Modifier
            .padding(8.dp)
            .width(cardWidth)
            .height(cardHeight)
            .clickable(onClick = onCardClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = date,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                style = MaterialTheme.typography.caption,
                color = Color.Black
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = {
                        setShowMenuIndex(index)
                    },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options Menu"
                    )
                }

                if (showMenuIndex == index) {
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { setShowMenuIndex(-1) },
                        modifier = Modifier
                            .background(Color.White)
                            .offset {
                                IntOffset(x = 0, y = 0)
                            }
                            .align(Alignment.TopEnd)
                    ) {
                        menuItems.forEach { (text, action) ->
                            DropdownMenuItem(onClick = {
                                action()
                                setShowMenuIndex(-1)
                            }) {
                                Text(text)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                cardContent()
            }
        }
    }
}

@Composable
fun displayDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = {
                Button(
                    onClick = onConfirm
                ) {
                    Text("OK")
                }
            }
        )
    }
}

private fun showLog(action: String) {
    Log.d(TAG, "showLog() action: $action")
}

@Preview
@Composable
fun PreviewHomeView() {
    Surface{
        HomeView()
    }
}
