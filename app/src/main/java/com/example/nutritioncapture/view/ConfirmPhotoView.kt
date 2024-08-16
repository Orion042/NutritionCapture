package com.example.nutritioncapture.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.nutritioncapture.R
import com.example.nutritioncapture.data.dao.DishesDao
import com.example.nutritioncapture.data.database.NutritionCaptureDatabase
import com.example.nutritioncapture.data.repository.NutritionCaptureRepository
import com.example.nutritioncapture.utils.ChatGptUtils
import com.example.nutritioncapture.utils.GeminiUtils
import com.example.nutritioncapture.utils.JsonParserUtils
import com.example.nutritioncapture.utils.bitmapToBase64
import com.example.nutritioncapture.utils.byteArrayToBitmap
import com.example.nutritioncapture.utils.compressAndEncodeBitmap
import com.example.nutritioncapture.utils.resizeBitmap
import com.example.nutritioncapture.viewmodel.DishesViewModel
import com.example.nutritioncapture.viewmodel.ViewModelOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private val TAG = "ConfirmPhotoView"


@Composable
fun ConfirmPhotoView(navController: NavController) {

    val imageBitmap = byteArrayToBitmap(ViewModelOwner().getPhotoImageViewModel().imageByteArrayMutableState!!)

    val focusManager = LocalFocusManager.current

    val databaseRepository = NutritionCaptureRepository(LocalContext.current)

    var isTappedNextButton by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val dishesName by ViewModelOwner().getDishesViewModel().dishesName
    val dishesIngredients by ViewModelOwner().getDishesViewModel().dishesIngredients
    val dishesCalorie by ViewModelOwner().getDishesViewModel().dishesCalorie

    val aggregatedState by remember {
        derivedStateOf {
            Triple(dishesName, dishesIngredients, dishesCalorie)
        }
    }

    LaunchedEffect(aggregatedState) {
        showLog("dishesName: ${aggregatedState.first}")
        showLog("dishesIngredients: ${aggregatedState.second}")
        showLog("dishesCalorie: ${aggregatedState.third}")
    }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            showLog("Loading started")
        } else {
            showLog("Loading ended")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { focusManager.clearFocus() }
    ) {
        Column {
            Row {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "前の画面に戻る",
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                        .size(30.dp)
                        .clickable {
                            navController.navigateUp()
                        }
                )
                
                Text(
                    text = stringResource(id = R.string.confirmphotoview_take_photo_string),
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(10.dp))

            if(imageBitmap != null) {
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = "撮影した画像",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(height = 300.dp)
                        .width(380.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            else {
                showLog("imageBitmap == null")
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        try {
                            isLoading = true

                            CoroutineScope(Dispatchers.IO).launch {
                                val geminiResponse =
                                    GeminiUtils().getGeminiProResponseFromBitmap(imageBitmap!!)
                                showLog("gemini result: $geminiResponse")

                                val geminiDishesImageResponse =
                                    JsonParserUtils().parseGeminiImageResponse(geminiResponse!!)

                                geminiDishesImageResponse.dishes.let {
                                    ViewModelOwner().getDishesViewModel().setDishesName(it)
                                }
                                geminiDishesImageResponse.ingredients.let {
                                    ViewModelOwner().getDishesViewModel().setDishesIngredients(it)
                                }
                                geminiDishesImageResponse.calorie.let {
                                    if(it == null) {
                                        ViewModelOwner().getDishesViewModel().setDishesCalorie(0f)
                                    }
                                    else {
                                        ViewModelOwner().getDishesViewModel().setDishesCalorie(it)
                                    }
                                }

                                isLoading = false
                            }
                        } catch(ex: Exception) {
                            showLog("AI分析失敗 ERROR: ${ex.message}")
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "AI分析ボタン",
                            modifier = Modifier
                                .size(30.dp),
                            tint = Color.White
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(id = R.string.confirmphotoview_ai_analyze_string),
                            color = Color.White,
                            fontSize = 22.sp
                        )
                    },
                    backgroundColor = colorResource(id = R.color.cornflower_blue),
                    modifier = Modifier
                        .width(380.dp)
                        .height(55.dp),
                    shape = RoundedCornerShape(10.dp),
                )
            }

            CustomOutlinedTextField(
                value = dishesName.joinToString(", "),
                onValueChange = { newValue ->
                    isTappedNextButton = false
                    val newList = ArrayList(newValue.split(", ").filter { it.isNotBlank() })
                    ViewModelOwner().getDishesViewModel().setDishesName(newList)
                },
                label = "料理",
                isNotEmpty = dishesName.isNotEmpty(),
                isTappedNextButton = isTappedNextButton
            )

            CustomOutlinedTextField(
                value = dishesIngredients.joinToString(", "),
                onValueChange = { newValue ->
                    isTappedNextButton = false
                    val newList = ArrayList(newValue.split(", ").filter { it.isNotBlank() })
                    ViewModelOwner().getDishesViewModel().setDishesIngredients(newList)
                },
                label = "使用材料",
                isNotEmpty = dishesIngredients.isNotEmpty(),
                isTappedNextButton = isTappedNextButton
            )

            CustomOutlinedTextField(
                value = dishesCalorie.toString(),
                onValueChange = { newValue ->
                    isTappedNextButton = false
                    val newCalorie = newValue.toFloatOrNull() ?: 0f
                    ViewModelOwner().getDishesViewModel().setDishesCalorie(newCalorie)
                },
                label = "総カロリー",
                isNotEmpty = dishesCalorie != 0f,
                isTappedNextButton = isTappedNextButton
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        isTappedNextButton = true

                        if(dishesName.isNotEmpty() && dishesIngredients.isNotEmpty() && dishesCalorie != 0f || dishesCalorie != null) {

                            ViewModelOwner().getDishesViewModel().saveDishesResult(databaseRepository)

                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(navController.graph.startDestinationId, true)
                                .build()

                            navController.navigate("Home", navOptions)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "保存",
                            modifier = Modifier
                                .size(30.dp),
                            tint = Color.White
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(id = R.string.confirmphotoview_save_string),
                            color = Color.White,
                            fontSize = 22.sp
                        )
                    },
                    backgroundColor = colorResource(id = R.color.cornflower_blue),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 0.dp)
                        .width(150.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                )
            }
        }
        LoadingIndicator(isLoading = isLoading)
    }
}

@Composable
fun LoadingIndicator(isLoading: Boolean) {
    Box(
        modifier = Modifier
            .padding(top = 140.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.cornflower_blue),
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isNotEmpty: Boolean,
    isTappedNextButton: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedLabelColor = colorResource(id = R.color.cornflower_blue),
            focusedLabelColor = colorResource(id = R.color.cornflower_blue),
            focusedIndicatorColor = colorResource(id = R.color.cornflower_blue),
            unfocusedIndicatorColor = colorResource(id = R.color.cornflower_blue),
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .width(350.dp)
            .padding(start = 15.dp, top = 30.dp),
        singleLine = true,
        trailingIcon = {
            if("見つかりません" in value || "みつかりません" in value) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "食べものがみつかりません",
                    tint = Color.Red
                )
            }
            else if(isTappedNextButton == false && isNotEmpty) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "入力完了",
                    tint = colorResource(id = R.color.cornflower_blue)
                )
            }
            else if(isTappedNextButton && isNotEmpty == false){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "入力未完了",
                    tint = Color.Red
                )
            }
        }
    )
}

private fun showLog(logMessage: String) {
    Log.d(TAG, logMessage)
}