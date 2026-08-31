package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ActivityLogEntity
import com.example.ui.theme.HindGoldPro
import com.example.ui.theme.HindGreenDark
import com.example.ui.theme.HindGreenLight
import com.example.ui.theme.HindRedDark
import com.example.ui.theme.HindRedLight
import com.example.ui.theme.HindRedPrimary

@Composable
fun HistoryScreen(
  activityLogs: List<ActivityLogEntity>,
  isOnline: Boolean,
  onNavigateToTool: (String) -> Unit,
  onNavigateToAllTools: () -> Unit,
  onClearHistory: () -> Unit,
  onShowToast: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }
  var showClearConfirmDialog by remember { mutableStateOf(false) }

  val categories = listOf("All", "Merge & Split", "Convert", "Security", "Edit & Scan")

  val filteredLogs = activityLogs.filter { log ->
    val matchesSearch = log.fileName.contains(searchQuery, ignoreCase = true) ||
        log.toolName.contains(searchQuery, ignoreCase = true)

    val matchesCategory = when (selectedCategory) {
      "Merge & Split" -> log.toolName.contains("Merge", ignoreCase = true) || log.toolName.contains("Split", ignoreCase = true)
      "Convert" -> log.toolName.contains("to", ignoreCase = true) || log.toolName.contains("PDF", ignoreCase = true)
      "Security" -> log.toolName.contains("Protect", ignoreCase = true) || log.toolName.contains("Unlock", ignoreCase = true) || log.toolName.contains("Sign", ignoreCase = true)
      "Edit & Scan" -> log.toolName.contains("Edit", ignoreCase = true) || log.toolName.contains("Scan", ignoreCase = true) || log.toolName.contains("OCR", ignoreCase = true)
      else -> true
    }
    matchesSearch && matchesCategory
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))

      // Header with Title and Clear Action
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Recent Activity & Cache",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Offline-first conversion logs & cached files",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        if (activityLogs.isNotEmpty()) {
          IconButton(
            onClick = { showClearConfirmDialog = true },
            modifier = Modifier.testTag("clear_history_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Clear History",
              tint = MaterialTheme.colorScheme.error
            )
          }
        }
      }
    }

    // Offline / Online Status Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth().testTag("sync_status_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isOnline) HindGreenLight.copy(alpha = 0.5f) else HindRedLight.copy(alpha = 0.5f)
        )
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(if (isOnline) HindGreenDark else HindRedDark),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isOnline) Icons.Default.CloudDone else Icons.Default.CloudOff,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (isOnline) "🟢 Real-time Cloud Sync Active" else "⚡ Offline Persistence Mode Active",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = if (isOnline) HindGreenDark else HindRedDark
            )
            Text(
              text = if (isOnline)
                "All conversion logs & settings are synced with Firebase Firestore."
              else
                "Working offline with local disk cache. Changes will auto-sync once connection is restored.",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 15.sp
            )
          }
        }
      }
    }

    // Search bar
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search conversion logs or files...") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("history_search_input"),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
      )
    }

    // Filter Chips
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(categories) { category ->
          FilterChip(
            selected = selectedCategory == category,
            onClick = { selectedCategory = category },
            label = { Text(category, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = HindRedPrimary,
              selectedLabelColor = Color.White
            )
          )
        }
      }
    }

    // List of Logs
    if (filteredLogs.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .testTag("history_empty_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
              text = if (searchQuery.isNotBlank()) "No records match '$searchQuery'" else "No conversion history yet",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Convert, merge, or protect PDFs to see your history and offline cache here.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
            )
            Button(
              onClick = { onNavigateToAllTools() },
              colors = ButtonDefaults.buttonColors(containerColor = HindRedPrimary),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.GridView, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Explore 30+ PDF Tools", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    } else {
      items(filteredLogs, key = { it.id }) { log ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("history_item_${log.id}"),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Document Icon
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(HindRedLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = HindRedDark,
                modifier = Modifier.size(24.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = log.toolName,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(HindGreenLight)
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "Cached",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = HindGreenDark
                  )
                }
              }

              Text(
                text = log.fileName,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )

              Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "🕒 ${log.timeFormatted}",
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "📦 ${log.outputSize}",
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            // Action: Re-download / Open
            IconButton(
              onClick = {
                onShowToast("📥 Exported ${log.fileName} from offline cache")
              }
            ) {
              Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Export file",
                tint = HindRedPrimary
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // Clear confirmation dialog
  if (showClearConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showClearConfirmDialog = false },
      title = { Text("Clear All History?") },
      text = { Text("This will permanently clear your local conversion log history. Your original files will not be affected.") },
      confirmButton = {
        Button(
          onClick = {
            onClearHistory()
            showClearConfirmDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Clear All")
        }
      },
      dismissButton = {
        TextButton(onClick = { showClearConfirmDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
