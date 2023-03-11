package de.xxlstrandkorbverleih.foodpricecalculator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class ScreenType {
    Summary,
    Detail,
    SummaryWithDetail
}

@Composable
fun HomeScreen(windowSize: WindowSize) {
    val isExpanded = windowSize == WindowSize.Expanded
    var index by remember {
        mutableStateOf(0)
    }
    var isItemOpen by remember {
        mutableStateOf(false)
    }
    val homeScreenType = getScreenType(
        isExpanded = isExpanded,
        isDetailOpend = isItemOpen
    )

    val color = listOf(
        Color.Blue,
        Color.Red,
        Color.Green,
        Color.Yellow,
        Color.Cyan,
        Color.Magenta,
        Color.Gray,
        Color.LightGray,
        Color.DarkGray
    )

    when(homeScreenType) {
        ScreenType.Detail -> {
            DetailedScreen(
                color = color[index]) {
                isItemOpen = false
            }
        }
        ScreenType.Summary -> {
            SummaryScreen(
                items = color,
                onItemSelected = {
                    index = it
                    isItemOpen = true
                }
            )
        }
        ScreenType.SummaryWithDetail -> {
            SummaryWithDetailScreen(
                items = color,
                index = index,
                onIndexChange = {
                    index = it
                }
            )
        }
    }
}
@Composable
fun SummaryScreen(
    items: List<Color>,
    onItemSelected: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(contentPadding = PaddingValues(8.dp), modifier = modifier) {
        itemsIndexed(items) { index, item ->
            SummaryItem(color = item) {
                onItemSelected.invoke(index)
            }
        }
    }
}

@Composable
fun DetailedScreen(
    color: Color,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
){
    Column(modifier = modifier.fillMaxSize()) {
        DetailedItem(color = color)
    }
    BackHandler {
        onBackPressed.invoke()
    }
}

@Composable
fun SummaryWithDetailScreen(
    items: List<Color>,
    index: Int,
    onIndexChange:(Int)->Unit
)
{
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        SummaryScreen(
            items = items,
            onItemSelected = onIndexChange,
            modifier = Modifier.weight(1f)
        )
        DetailedScreen(
            color = items[index],
            modifier = Modifier.weight(3f)
        ) {

        }
    }
}
@Composable
fun DetailedItem(color: Color) {
    Surface(
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp)
    ) {
    }
}

@Composable
fun SummaryItem(color: Color, onItemSelected: () -> Unit) {
    Surface(
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(8.dp)
            .clickable {
                onItemSelected.invoke()
            },
    ) {
    }
}

@Composable
fun getScreenType(
    isExpanded: Boolean,
    isDetailOpend: Boolean
): ScreenType = when(isExpanded) {
    false -> {
        if(isDetailOpend) {
            ScreenType.Detail
        } else {
            ScreenType.Summary
        }
    }
    true -> {
        ScreenType.SummaryWithDetail
    }
}