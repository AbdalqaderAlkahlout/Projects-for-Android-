package com.example.notesapplication.ui.theme.screens.add_edit_notes

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notesapplication.local.NoteEntity
import com.example.notesapplication.ui.theme.util.Priority
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NoteItem(
    note: NoteEntity,
    onToggleCompletion: (NoteEntity) -> Unit,
    onDelete: (NoteEntity) -> Unit,
    onClickCard: (NoteEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val priorityColor = when (note.priority) {
        Priority.HIGH -> Color(0xFFE53935)
        Priority.MEDIUM -> Color(0xFFFB8C00)
        Priority.LOW -> Color(0xFF43A047)
    }
    val colorAlpha by animateColorAsState(
        targetValue =  if (note.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        else MaterialTheme.colorScheme.surfaceVariant,
        label = "color alpha"
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickCard(note) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorAlpha
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                onClick = {onToggleCompletion(note)}
            ) {
                Icon(imageVector = if (note.isCompleted) Icons.Outlined.Circle
                else Icons.Filled.CheckCircleOutline ,
                    contentDescription = "Check Complete",
                    tint = if (note.isCompleted)MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(10.dp),
                        shape = RoundedCornerShape(50.dp),
                        color = priorityColor
                    ) { }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = note.category.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary

                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = note.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            textDecoration = if (!note.isCompleted) TextDecoration.LineThrough
                            else TextDecoration.None
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis)





                }
                Spacer(modifier = Modifier.height(6.dp))
                Row {  if (note.description.isNotEmpty()) {
                    Text(
                        text = note.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }}
                Spacer(modifier = Modifier.height(6.dp))
                val formattedDate = SimpleDateFormat(
                    "dd MMM, yyyy - hh:mm a",
                    Locale.getDefault()
                )
                    .format(Date(note.createdAt))

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                // 🗑️ زر الحذف
                IconButton(onClick = { onDelete(note) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Note",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                }
            }

        }
    }
