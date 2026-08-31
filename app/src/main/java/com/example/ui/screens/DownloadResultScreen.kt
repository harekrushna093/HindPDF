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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProcessingOutput
import com.example.data.model.ToolsCatalog
import com.example.data.model.UserProfileData
import com.example.ui.components.getToolIconVector
import com.example.ui.theme.HindGoldLight
import com.example.ui.theme.HindGoldPro
import com.example.ui.theme.HindGoldProDark
import com.example.ui.theme.HindGreenBg
import com.example.ui.theme.HindGreenSuccess
import com.example.ui.theme.HindRedDark
import com.example.ui.theme.HindRedLight
import com.example.ui.theme.HindRedPrimary

@Composable
fun DownloadResultScreen(
  result: ProcessingOutput?,
  userProfile: UserProfileData,
  onFileNameChanged: (String) -> Unit,
  onDownloadStandard: () -> Unit,
  onDownloadPro: () -> Unit,
  onShareLink: () -> Unit,
  onProcessAnother: () -> Unit,
  onNavigateToTool: (String) -> Unit,
  onOpenPaywall: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  if (result == null) {
    Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Text("No processed document found. Please select a tool.")
    }
    return
  }

  var previewZoom by remember { mutableFloatStateOf(1f) }
  var previewRotation by remember { mutableIntStateOf(0) }
  var currentPreviewPage by remember { mutableIntStateOf(1) }

  val stdSizeFormatted = "${String.format("%.2f", result.standardSizeBytes.toDouble() / (1024 * 1024))} MB"
  val proSizeFormatted = "${String.format("%.2f", result.proSizeBytes.toDouble() / (1024 * 1024))} MB"

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
      .testTag("download_result_column")
  ) {
    item {
      Spacer(modifier = Modifier.height(16.dp))

      // DOCUMENT PREVIEW CARD
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("preview_panel_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Preview toolbar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Document Preview",
              fontWeight = FontWeight.Black,
              fontSize = 16.sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              IconButton(
                onClick = { previewZoom = (previewZoom * 1.15f).coerceAtMost(2.0f) },
                modifier = Modifier.size(30.dp)
              ) {
                Icon(Icons.Default.ZoomIn, contentDescription = "Zoom In", modifier = Modifier.size(18.dp))
              }
              IconButton(
                onClick = { previewZoom = (previewZoom * 0.85f).coerceAtLeast(0.6f) },
                modifier = Modifier.size(30.dp)
              ) {
                Icon(Icons.Default.ZoomOut, contentDescription = "Zoom Out", modifier = Modifier.size(18.dp))
              }
              IconButton(
                onClick = { previewRotation = (previewRotation + 90) % 360 },
                modifier = Modifier.size(30.dp)
              ) {
                Icon(Icons.Default.RotateRight, contentDescription = "Rotate", modifier = Modifier.size(18.dp))
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Preview Canvas simulator
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFF8FAFC))
              .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
              .padding(12.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .scale(previewZoom)
                .rotate(previewRotation.toFloat())
            ) {
              Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = HindRedPrimary,
                modifier = Modifier.size(40.dp)
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = result.fileName,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFF0F172A)
              )
              Text(
                text = "Page $currentPreviewPage of ${result.pageCount} • Verified Output",
                fontSize = 10.sp,
                color = Color(0xFF64748B)
              )
              Text(
                text = "✓ 256-Bit SSL Client Encryption Applied",
                fontSize = 9.sp,
                color = HindGreenSuccess,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          // Pagination bar
          if (result.pageCount > 1) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              TextButton(
                onClick = { if (currentPreviewPage > 1) currentPreviewPage-- },
                enabled = currentPreviewPage > 1
              ) {
                Text("← Previous", fontSize = 11.sp)
              }

              Text(
                text = "Page $currentPreviewPage of ${result.pageCount}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )

              TextButton(
                onClick = { if (currentPreviewPage < result.pageCount) currentPreviewPage++ },
                enabled = currentPreviewPage < result.pageCount
              ) {
                Text("Next →", fontSize = 11.sp)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // TELEMETRY & QUALITY DASHBOARD
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "⚡ Processing Telemetry & Quality",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 10.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Engine item
            TelemetryItem(
              title = "Engine",
              value = "Verified",
              sub = "Client-Safe",
              subColor = HindGreenSuccess,
              modifier = Modifier.weight(1f)
            )

            // Standard item
            TelemetryItem(
              title = "Standard",
              value = stdSizeFormatted,
              sub = "Balanced",
              subColor = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.weight(1f)
            )

            // Studio Pro item
            TelemetryItem(
              title = "Studio Pro",
              value = proSizeFormatted,
              sub = "Lossless HD",
              subColor = HindGoldProDark,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // DOWNLOAD ACTIONS CARD
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("download_actions_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Output File Name",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          OutlinedTextField(
            value = result.fileName,
            onValueChange = onFileNameChanged,
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp)
              .testTag("output_file_name_input"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Standard Download Button
          Button(
            onClick = onDownloadStandard,
            colors = ButtonDefaults.buttonColors(containerColor = HindRedPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("download_standard_btn")
          ) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Download Standard Output", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Studio Pro HD Download Button
          Button(
            onClick = {
              if (userProfile.isPro) onDownloadPro()
              else onOpenPaywall("Studio Lossless 300+ DPI Output")
            },
            colors = ButtonDefaults.buttonColors(containerColor = HindGoldProDark),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("download_pro_hd_btn")
          ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("⭐ Download Pro (Lossless HD • 300 DPI)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedButton(
            onClick = onProcessAnother,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("process_another_btn")
          ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("↻ Process Another Document", fontSize = 12.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // SHARE DOCUMENT CARD
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "🔗 Share Document",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 10.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            ShareButton("Copy Link", Icons.Default.ContentCopy, modifier = Modifier.weight(1f)) { onShareLink() }
            ShareButton("Email", Icons.Default.Email, modifier = Modifier.weight(1f)) { onShareLink() }
            ShareButton("Slack", Icons.Default.Message, modifier = Modifier.weight(1f)) { onShareLink() }
            ShareButton("Share", Icons.Default.Share, modifier = Modifier.weight(1f)) { onShareLink() }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // RECOMMENDED NEXT STEPS
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "🛠 Recommended Next Steps",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 10.dp)
          )

          val recTools = listOf(
            ToolsCatalog.getToolById("compress"),
            ToolsCatalog.getToolById("protect-password"),
            ToolsCatalog.getToolById("ocr")
          )

          recTools.forEach { recTool ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable { onNavigateToTool(recTool.id) },
              color = MaterialTheme.colorScheme.surfaceVariant
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(recTool.color)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = getToolIconVector(recTool.id),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                  )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                  Text(
                    text = recTool.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  )
                  Text(
                    text = recTool.desc,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
fun TelemetryItem(
  title: String,
  value: String,
  sub: String,
  subColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.clip(RoundedCornerShape(10.dp)),
    color = MaterialTheme.colorScheme.surfaceVariant
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        text = title,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = value,
        fontSize = 13.sp,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = sub,
        fontSize = 9.sp,
        fontWeight = FontWeight.SemiBold,
        color = subColor
      )
    }
  }
}

@Composable
fun ShareButton(
  label: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  OutlinedButton(
    onClick = onClick,
    modifier = modifier,
    shape = RoundedCornerShape(8.dp)
  ) {
    Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp))
  }
}
