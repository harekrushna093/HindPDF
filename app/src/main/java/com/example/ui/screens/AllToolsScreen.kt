package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ToolCategory
import com.example.data.model.ToolsCatalog
import com.example.ui.theme.HindRedPrimary

@Composable
fun AllToolsScreen(
  onNavigateToTool: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }

  val filteredTools = remember(searchQuery) {
    if (searchQuery.isBlank()) ToolsCatalog.allTools
    else ToolsCatalog.allTools.filter {
      it.name.contains(searchQuery, ignoreCase = true) ||
      it.desc.contains(searchQuery, ignoreCase = true) ||
      it.id.contains(searchQuery, ignoreCase = true)
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
      .testTag("all_tools_screen_column")
  ) {
    item {
      Spacer(modifier = Modifier.height(16.dp))

      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "All PDF Tools & Utilities",
          fontSize = 22.sp,
          fontWeight = FontWeight.Black,
          color = MaterialTheme.colorScheme.onBackground
        )

        Text(
          text = "Explore 30+ native Android tools to convert, edit, organize, and secure your documents.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        // Search Bar
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 2.dp
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Filter 30+ tools (e.g. OCR, Compress, Sign)...", fontSize = 13.sp) },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = HindRedPrimary,
                modifier = Modifier.size(20.dp)
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("all_tools_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color.Transparent,
              unfocusedBorderColor = Color.Transparent
            ),
            singleLine = true
          )
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }

    // Categories
    ToolCategory.entries.forEach { category ->
      val categoryTools = filteredTools.filter { it.category == category }
      if (categoryTools.isNotEmpty()) {
        item {
          Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(bottom = 6.dp)
            ) {
              Text(
                text = "${category.emoji} ${category.title}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
              )
            }
            HorizontalDivider(
              modifier = Modifier.padding(bottom = 12.dp),
              color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              for (i in categoryTools.indices step 2) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  ToolCardItem(
                    tool = categoryTools[i],
                    onClick = { onNavigateToTool(categoryTools[i].id) },
                    modifier = Modifier.weight(1f)
                  )
                  if (i + 1 < categoryTools.size) {
                    ToolCardItem(
                      tool = categoryTools[i + 1],
                      onClick = { onNavigateToTool(categoryTools[i + 1].id) },
                      modifier = Modifier.weight(1f)
                    )
                  } else {
                    Spacer(modifier = Modifier.weight(1f))
                  }
                }
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
