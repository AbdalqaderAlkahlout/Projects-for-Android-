package com.example.recipeapplication.Screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipeapplication.Api.MealDto
import com.example.recipeapplication.data.RecipeViewModel
import com.example.recipeapplication.data.ScreenStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onMealClick:(MealDto)-> Unit = {},viewModel: RecipeViewModel = viewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val uiState  by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Text(text = "Recipe Meals ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.surfaceContainer)
        }
    ) {paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
           OutlinedTextField(
               value = searchQuery,
               onValueChange = {viewModel.onSearchQueryChange(it)},
               modifier = Modifier.fillMaxWidth().padding(16.dp),
               placeholder = { Text("ابحث عن وجبة (مثال: Chicken)...") },
               leadingIcon = {
                   Icon(imageVector = Icons.Default.Search, contentDescription = null)
               },
               trailingIcon = {
                   AnimatedVisibility(
                       visible = searchQuery.isNotEmpty(),
                       enter = fadeIn(),
                       exit = fadeOut()
                   ) {
                       IconButton(onClick = {
                           viewModel.onSearchQueryChange("")
                       }) {
                           Icon(imageVector = Icons.Default.Clear , contentDescription = "clear search")

                       }
                   }
               },
               singleLine = true,
               shape = RoundedCornerShape(16.dp)

               )
            Box(modifier = Modifier.fillMaxSize().weight(1f),
                contentAlignment = Alignment.Center){
                when(val state = uiState){
                    is ScreenStatus.Loading ->{
                        CircularProgressIndicator()
                    }
                    is ScreenStatus.Success ->{
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(state.meals, key = {it.id}){
                                meal ->
                                ItemCard(meal = meal, onClick = {onMealClick(meal)})
                            }
                        }
                    }
                    is ScreenStatus.Error ->{
                        Text(text = state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error)
                    }
                    is ScreenStatus.Idle ->{
                        Text(
                            text = "ابدأ بفرز أو كتابة اسم الوجبة للبحث",
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }

}