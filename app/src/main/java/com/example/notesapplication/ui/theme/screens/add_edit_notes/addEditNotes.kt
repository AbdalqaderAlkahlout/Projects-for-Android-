package com.example.notesapplication.ui.theme.screens.add_edit_notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesapplication.ui.theme.util.Category
import com.example.notesapplication.ui.theme.util.Priority
import com.example.notesapplication.viewModel.AddEditNotesViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddNotesEdit(
    viewModel: AddEditNotesViewModel,
    popBackStack: ()-> Unit
){
  val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is AddEditNotesViewModel.UiEvent.SaveSuccess -> popBackStack()
                is AddEditNotesViewModel.UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "ملاحظة جديدة / تعديل ") },
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {viewModel.saveNote()},
                containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Save Note")
            }

        }

    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = {viewModel.onTitleChange(it)},
                label = {Text(text = "عنوان الملاحظة ")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )
            Text(text = "الأولوية " , style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Priority.entries.forEach { priorityItem ->
                    FilterChip(
                        selected = viewModel.priority ==priorityItem,
                        onClick = { viewModel.onPriorityChange(priorityItem) },
                        label = { Text(text = priorityItem.name) },

                    )
                }
            }
            Text(text = "الفئة " , style = MaterialTheme.typography.titleMedium)

            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Category.entries.forEach {categoryItem ->
                    FilterChip(
                        selected = viewModel.category == categoryItem,
                        onClick = {viewModel.onCategoryChange(categoryItem)},
                        label = {Text(text = categoryItem.name)}
                    )
                }

            }
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("التفاصيل والملاحظات...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                maxLines = 10,
                shape = MaterialTheme.shapes.medium
            )

        }
    }

}