package com.example.notesapplication.ui.theme.screens.add_edit_notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notesapplication.preferences.SortOrder
import com.example.notesapplication.viewModel.NotesViewModelList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    viewModel: NotesViewModelList,
    onAddNewNoteClick: ()-> Unit,
    onEditsNotesClick:(Int)-> Unit
){
    val state by viewModel.stateUi.collectAsStateWithLifecycle()
    var isSortMenuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = "لملاحظاة المهام ")},
                actions = {
                    IconButton(
                        onClick = { viewModel.onToggleDarkTheme(!state.isDarkUi) }
                    ) {
                        Icon(imageVector = if (state.isDarkUi) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle themes")
                    }
                    Box {
                        IconButton(onClick = {isSortMenuExpanded = true}) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Sort, contentDescription = "sort notes")

                        }
                        DropdownMenu(expanded = isSortMenuExpanded,
                            onDismissRequest = {isSortMenuExpanded= true}) {
                            DropdownMenuItem(
                              text =  {Text(text = "الترتيب حسب التاريخ ")},
                                onClick = {viewModel.onSortOrderChange(SortOrder.BY_DATE)
                                isSortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {Text(text = "الترتيب حسب الأولوية  ")},
                                onClick = {
                                    viewModel.onSortOrderChange(SortOrder.BY_PRIORITY)
                                    isSortMenuExpanded = false
                                }
                            )
                        }
                    }
                }

            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNewNoteClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(imageVector = Icons.Filled.Add , contentDescription = "Add Notse")
            }
        }

    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = {viewModel.onSearchChange(it)},
                placeholder = {Text(text = "Search")},
                leadingIcon = {Icon(imageVector = Icons.Filled.Search, contentDescription = "Searching ")},
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.onSearchChange("")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear Search"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (state.notes.isEmpty()&& state.isLoading){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = if (state.searchQuery.isEmpty()) "لا توجد ملاحظات  الان , اضف اول ملاحظة "
                    else "لا توجد نتائج مطابقة للبحث \uD83D\uDD0D",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline)
                }
            }else{
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = state.notes,
                        key = {note -> note.id}
                    ){notes ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            NoteItem(
                                note = notes,
                                onToggleCompletion = {viewModel.toggleNoteCompletion(it)},
                                onDelete = {viewModel.onToggleDelete(it)},
                                onClickCard = {onEditsNotesClick(it.id)},
                                modifier = Modifier.animateItem()

                            )
                        }
                    }
                }
            }

        }
    }

}