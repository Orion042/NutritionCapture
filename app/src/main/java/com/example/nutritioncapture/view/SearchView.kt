package com.example.nutritioncapture.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nutritioncapture.R
import com.example.nutritioncapture.viewmodel.ViewModelOwner
import kotlin.math.log

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(navController: NavController) {

    val searchViewModel = ViewModelOwner().getSearchViewModel()

    val searchText by searchViewModel.searchText.collectAsState()
    val isSearching by searchViewModel.isSearching.collectAsState()

    LaunchedEffect(searchText) {
        snapshotFlow {
        }.collect {
            showLog("searchText: $searchText")
        }
    }

    LaunchedEffect(isSearching) {
        showLog("isSearching: $isSearching")
    }

    Scaffold(
        topBar = {
            CustomSearchBar(
                searchText = searchText,
                isSearching = isSearching
            )
        }
    ) { paddingValues ->
        SearchResult()
    }
}

@Composable
fun SearchResult() {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    searchText: String,
    isSearching: Boolean
) {
    val searchViewModel = ViewModelOwner().getSearchViewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.searchview_search_string),
            fontSize = 24.sp,
            color = colorResource(id = R.color.cornflower_blue),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        SearchBar(
            query = searchText,
            onQueryChange = searchViewModel::onSearchTextChange,
            onSearch = {
                // TODO: サーバー作成後作成
            },
            active = isSearching,
            onActiveChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = {
                        searchViewModel.onSearchTextChange("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Icon"
                        )
                    }
                }
            }
        ) {

        }
    }
}

private fun showLog(logMessage: String) {
    Log.d("SearchView", logMessage)
}