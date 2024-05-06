@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)

package com.example.wishlistapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.material3.DismissState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.example.wishlistapp.data.Wish
import kotlinx.coroutines.delay


@Composable
fun HomeView(
    navController: NavController,
    viewModel: WishViewModel
){
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBarView(title = "WishList")},
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                backgroundColor = Color.Black,
                onClick = {
                    navController.navigate(Screen.AddScreen.route + "/0L")
                }){
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White)
            }

        },
        backgroundColor = colorResource(id = R.color.background_color)

    ){
        val wishList = viewModel.getAllWishes.collectAsState(initial = listOf())
        LazyColumn (modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            items(wishList.value, key = {wish -> wish.id}){
                wish ->
                SwipeToEditDeleteContainer(
                    wish = wish,
                    onDelete = {viewModel.deleteWish(wish)},
                ){
                    WishItem(wish = wish) {
                        val id = wish.id
                        navController.navigate(Screen.AddScreen.route + "/$id")
                    }
                }
            }
        }
    }
}

@Composable
fun WishItem(wish: Wish, onClick: () -> Unit){
   Card(modifier = Modifier
       .fillMaxWidth()
       .padding(top = 8.dp, start = 8.dp, end = 8.dp)
       .clickable {
           onClick()
       },
       elevation = 10.dp,
       backgroundColor = Color.White
   ){
       Column(modifier = Modifier.padding(16.dp)) {
           Text(text = wish.title, fontWeight = FontWeight.ExtraBold)
           Text(text = wish.description)
       }
   }
}

@Composable
fun <T> SwipeToEditDeleteContainer(
    wish: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit,
){
    var isRemoved by remember{
        mutableStateOf(false)
    }

    val state = rememberDismissState(
        confirmValueChange = {value ->
            if(value == DismissValue.DismissedToStart){
                isRemoved = true
                true
            }else{
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved){
            delay(animationDuration.toLong())
            onDelete(wish)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
        ) {
        SwipeToDismiss(
            state = state,
            background = {
                DeleteBackground(swipeDismissState = state)
            },
            dismissContent = {content(wish)},
            directions = setOf(DismissDirection.EndToStart)
        )
    }
}

@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
) {
    val color = if(swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
         Color.Red
        }else Color.Transparent


    Box(modifier = Modifier
        .fillMaxSize()
        .background(color)
        .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ){
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

