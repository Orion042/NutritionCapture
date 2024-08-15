package com.example.nutritioncapture.view

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutritioncapture.R
import com.example.nutritioncapture.data.entity.DishesEntity
import com.example.nutritioncapture.data.model.CardData
import com.example.nutritioncapture.data.model.UserInfo
import com.example.nutritioncapture.data.repository.NutritionCaptureRepository
import com.example.nutritioncapture.data.service.getDummyCardData
import com.example.nutritioncapture.data.service.getDummyUserData
import com.example.nutritioncapture.utils.byteArrayToBitmap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val TAG = "HomeView"

@Composable
fun HomeView(navController: NavController) {
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val databaseRepository = NutritionCaptureRepository(context)

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (selectedDish, setSelectedDish) = remember { mutableStateOf<DishesEntity?>(null) }

    val (showMenuIndex, setShowMenuIndex) = remember { mutableStateOf(-1) }

    val dishesList = rememberSaveable { mutableStateOf<List<DishesEntity>?>(null) }
    val otherUserList = rememberSaveable { mutableStateOf<List<UserInfo>?>(null) }

    LaunchedEffect(Unit) {
        val data = getDishesList(databaseRepository)
        dishesList.value = data
    }

    LaunchedEffect(Unit) {
        val userData = getDummyUserData(context)
        otherUserList.value = userData
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                if (dishesList.value != null && dishesList.value!!.isNotEmpty()) {
                    item {
                        Column {
                            Text(
                                text = stringResource(id = R.string.homeview_card_title_string),
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 16.dp, bottom = 8.dp)
                            )
                            LazyRow(
                                modifier = Modifier
                                    .padding(horizontal = 15.dp)
                                    .height(170.dp)
                            ) {
                                itemsIndexed(dishesList.value!!) { index, cardData ->
                                    DishHistoryList(
                                        index = cardData.id,
                                        date = cardData.createdAt,
                                        imageData = cardData.dishesImageByteArrayString,
                                        onCardClick = {
                                            setSelectedDish(cardData)
                                            setShowDialog(true)
                                        },
                                        showMenuIndex = showMenuIndex,
                                        setShowMenuIndex = setShowMenuIndex,
                                        menuItems = listOf(
                                            "修正" to {
                                                // TODO: 未定
                                                showLog("修正")
                                            },
                                            "削除" to {
                                                showLog("削除")
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    try {
                                                        showLog("DishesのDB削除成功")
                                                        databaseRepository.deleteDishes(cardData)

                                                        val data = getDishesList(databaseRepository)
                                                        dishesList.value = data
                                                    } catch (ex: Exception) {
                                                        showLog("DishesのDB削除失敗 ERROR: ${ex.message}")
                                                    }
                                                }
                                            }
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                if (otherUserList.value != null && otherUserList.value!!.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(id = R.string.homeview_recommend_user_string),
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 10.dp, bottom = 8.dp)
                        )

                        LazyRow(
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .height(170.dp)
                        ) {
                            itemsIndexed(otherUserList.value!!) { index, otherUserData ->
                                OtherUserIcons(
                                    index = otherUserData.id,
                                    imageData = otherUserData.imageData,
                                    onCardClick = {
                                        showLog("他のユーザーアイコンタップ")
                                    }
                                )
                            }
                        }
                    }
                }

                // ダミーアイテム TODO: 未定
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

        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate("camera") {
                    popUpTo(MainScreenTab.Home.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "カメラを起動する",
                    modifier = Modifier.size(25.dp),
                    tint = Color.White
                )
            },
            text = {
                Text(text = stringResource(id = R.string.homeview_take_picture_string), color = Color.White, fontSize = 18.sp)
            },
            backgroundColor = colorResource(id = R.color.cornflower_blue),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 25.dp, bottom = 25.dp)
                .width(110.dp)
                .height(60.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 7.dp
            )
        )
    }

    if (showDialog && selectedDish != null) {
        displayDialog(
            showDialog = showDialog,
            onDismissRequest = { setShowDialog(false) },
            title = "詳細",
            dishesName = selectedDish!!.dishesName.joinToString(", "),
            dishesIngredients = selectedDish!!.dishesIngredients.joinToString(", "),
            dishesCalorie = selectedDish!!.dishesCalorie,
            text = "料理の詳細を表示します",
            onConfirm = { setShowDialog(false) }
        )
    }
}

@Composable
fun OtherUserIcons(
    index: String,
    backgroundColor: Color = Color.White,
    imageData: ByteArray?,
    onCardClick: () -> Unit
) {
    imageData?.let {
        val bitmap = byteArrayToBitmap(it)

        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "Other User Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(7.dp)
                .size(80.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(4.dp, colorResource(id = R.color.dark_green), CircleShape)
                .clickable(onClick = {
                    showLog("おすすめユーザータップ")
                })
        )
    }
}


@Composable
fun DishHistoryList(
    index: Int,
    backgroundColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(12.dp),
    elevation: Dp = 4.dp,
    cardWidth: Dp = 200.dp,
    cardHeight: Dp = 180.dp,
    date: Date,
    imageData: ByteArray?,
    onCardClick: () -> Unit,
    showMenuIndex: Int,
    setShowMenuIndex: (Int) -> Unit,
    menuItems: List<Pair<String, () -> Unit>>
) {
    Card(
        backgroundColor = backgroundColor,
        shape = shape,
        border = BorderStroke(4.dp, color = colorResource(id = R.color.dark_green)),
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
            imageData?.let {
                val bitmap = byteArrayToBitmap(it)
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Card Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = convertDateToString(date),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(Color.LightGray),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = {
                        showLog("index: $index, showMenuIndex: $showMenuIndex")
                        setShowMenuIndex(if (showMenuIndex == index) -1 else index)
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
        }
    }
}

@Composable
fun displayDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    dishesName: String,
    dishesIngredients: String,
    dishesCalorie: Float,
    text: String,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = {
                Text(
                    text = "料理名: $dishesName\n使用材料: $dishesIngredients\n総カロリー: $dishesCalorie"
                )
            },
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

fun convertDateToString(date: Date): String {
    val dateFormat = SimpleDateFormat("yyyy/M/d/ HH:mm", Locale.getDefault())
    return dateFormat.format(date)
}

suspend fun getDishesList(repository: NutritionCaptureRepository) : List<DishesEntity> {
    val data: List<DishesEntity> = try {
        showLog("DishesのDB結果取得成功")
        val fetchedData = repository.getAllDishes()
        fetchedData
    } catch (ex: Exception) {
        showLog("DishesのDB結果取得失敗: ${ex.message}")
        emptyList()
    }
    return data
}

private fun showLog(action: String) {
    Log.d(TAG, "showLog() : $action")
}
