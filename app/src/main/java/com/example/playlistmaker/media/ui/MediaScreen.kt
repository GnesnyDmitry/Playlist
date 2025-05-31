package com.example.playlistmaker.media.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.theme.CustomTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaScreen() {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = pagerState.currentPage

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            backgroundColor = CustomTheme.colors.background,
            contentColor = CustomTheme.colors.text,
            title = {
                Text(
                    style = CustomTheme.typography.medium,
                    text = "Медиатека"
                )
            }
        )
        TabRow(
            backgroundColor = CustomTheme.colors.background,
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions ->
                val currentTab = tabPositions[selectedTabIndex]
                Box(
                    Modifier
                        .tabIndicatorOffset(currentTab)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .width(140.dp)
                        .height(2.dp)
                        .background(CustomTheme.colors.secondBackground, shape = RoundedCornerShape(1.5.dp))
                )
            }
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(0) }
                },
                text = {
                    Text(
                        color = CustomTheme.colors.text,
                        style = CustomTheme.typography.secondSmall,
                        text = "Избранные треки"
                    )
                       },
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(1) }
                },
                text = {
                    Text(
                        color = CustomTheme.colors.text,
                        style = CustomTheme.typography.secondSmall,
                        text = "Плейлисты"
                    ) },
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> FavoriteTracksScreen()
                1 -> PlaylistScreen()
            }
        }
    }
}




