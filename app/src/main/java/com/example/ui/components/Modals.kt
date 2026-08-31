package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MergeType
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.PdfTool
import com.example.ui.theme.HindGoldLight
import com.example.ui.theme.HindGoldPro
import com.example.ui.theme.HindGoldProDark
import com.example.ui.theme.HindRedDark
import com.example.ui.theme.HindRedLight
import com.example.ui.theme.HindRedPrimary

@Composable
fun PaywallModal(
  title: String,
  onDismiss: () -> Unit,
  onActivateTrial: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .clip(RoundedCornerShape(24.dp))
        .testTag("paywall_modal"),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(HindGoldLight),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "⭐", fontSize = 26.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Upgrade to HindPDF Pro",
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurface
        )

        Text(
          text = "Unlock studio-grade quality, OCR recognition, lossless document security & unlimited daily conversions.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(vertical = 8.dp)
        )

        // Pricing Card
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(16.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "HindPDF Pro Unlimited",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Text(
                text = "Full 30+ Tools + Priority Engine",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Text(
              text = "$9/mo",
              fontWeight = FontWeight.Black,
              fontSize = 18.sp,
              color = HindGoldProDark
            )
          }
        }

        // Perks list
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          PerkItem("Unlimited conversions (No 5-document daily limit)")
          PerkItem("OCR AI text recognition for scans and images")
          PerkItem("Studio Pro Lossless 300+ DPI PDF export")
          PerkItem("Bank-grade 256-bit AES encryption & Redaction")
        }

        Button(
          onClick = onActivateTrial,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("activate_pro_trial_btn"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = HindGoldProDark)
        ) {
          Text(
            text = "Start 7-Day Free Trial",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.White
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "Continue with Free Tier",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Composable
private fun PerkItem(text: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(
      imageVector = Icons.Default.Check,
      contentDescription = null,
      tint = HindGoldProDark,
      modifier = Modifier.size(16.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = text,
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}

@Composable
fun SignaturePadModal(
  onDismiss: () -> Unit,
  onSaveSignature: (List<List<Pair<Float, Float>>>) -> Unit
) {
  val strokes = remember { mutableStateListOf<List<Pair<Float, Float>>>() }
  var currentStroke by remember { mutableStateOf<List<Pair<Float, Float>>>(emptyList()) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .testTag("signature_modal"),
      color = MaterialTheme.colorScheme.surface
    ) {
      Column(
        modifier = Modifier.padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Draw Digital Signature",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Text(
          text = "Use your finger or stylus to sign below:",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(bottom = 12.dp)
        )

        // Canvas Drawing Area
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.5.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
              detectDragGestures(
                onDragStart = { offset ->
                  currentStroke = listOf(Pair(offset.x, offset.y))
                },
                onDrag = { change, _ ->
                  change.consume()
                  currentStroke = currentStroke + Pair(change.position.x, change.position.y)
                },
                onDragEnd = {
                  if (currentStroke.isNotEmpty()) {
                    strokes.add(currentStroke)
                    currentStroke = emptyList()
                  }
                },
                onDragCancel = {
                  currentStroke = emptyList()
                }
              )
            }
        ) {
          Canvas(modifier = Modifier.matchParentSize()) {
            // Draw previous strokes
            strokes.forEach { stroke ->
              if (stroke.size > 1) {
                val path = Path().apply {
                  moveTo(stroke.first().first, stroke.first().second)
                  for (i in 1 until stroke.size) {
                    lineTo(stroke[i].first, stroke[i].second)
                  }
                }
                drawPath(
                  path = path,
                  color = Color(0xFF1E293B),
                  style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
              }
            }
            // Draw current active stroke
            if (currentStroke.size > 1) {
              val activePath = Path().apply {
                moveTo(currentStroke.first().first, currentStroke.first().second)
                for (i in 1 until currentStroke.size) {
                  lineTo(currentStroke[i].first, currentStroke[i].second)
                }
              }
              drawPath(
                path = activePath,
                color = Color(0xFF1E293B),
                style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          OutlinedButton(
            onClick = {
              strokes.clear()
              currentStroke = emptyList()
            },
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Clear ↺")
          }

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onDismiss) {
              Text("Cancel")
            }
            Button(
              onClick = {
                onSaveSignature(strokes.toList())
              },
              colors = ButtonDefaults.buttonColors(containerColor = HindRedPrimary),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("apply_signature_btn")
            ) {
              Text("Apply Signature")
            }
          }
        }
      }
    }
  }
}

@Composable
fun DeleteAccountModal(
  onDismiss: () -> Unit,
  onConfirmDelete: (confirmText: String, pass: String) -> Unit
) {
  var deleteText by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .testTag("delete_account_modal"),
      color = MaterialTheme.colorScheme.surface
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = HindRedPrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Permanent Account Deletion",
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
            color = HindRedPrimary
          )
        }

        Text(
          text = "This action is irreversible. Your membership, activity history, and settings will be permanently wiped.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(vertical = 10.dp)
        )

        Text(
          text = "1. Type 'DELETE' in all caps:",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
          value = deleteText,
          onValueChange = { deleteText = it },
          placeholder = { Text("DELETE") },
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("input_confirm_delete_text")
        )

        Text(
          text = "2. Enter account password:",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
          value = password,
          onValueChange = { password = it },
          placeholder = { Text("••••••••••••") },
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("input_confirm_delete_password")
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(onClick = onDismiss) {
            Text("Cancel")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = { onConfirmDelete(deleteText, password) },
            colors = ButtonDefaults.buttonColors(containerColor = HindRedPrimary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("confirm_delete_btn")
          ) {
            Text("Permanently Delete")
          }
        }
      }
    }
  }
}

fun getToolIconVector(toolId: String): ImageVector {
  return when (toolId) {
    "merge" -> Icons.Default.MergeType
    "split" -> Icons.Default.ContentCut
    "compress" -> Icons.Default.Compress
    "pdf-to-jpg", "pdf-to-png" -> Icons.Default.Image
    "jpg-to-pdf", "png-to-pdf" -> Icons.Default.PictureAsPdf
    "word-to-pdf", "pdf-to-word" -> Icons.Default.Description
    "excel-to-pdf", "pdf-to-excel" -> Icons.Default.TableChart
    "rotate" -> Icons.Default.RotateRight
    "delete-pages" -> Icons.Default.Delete
    "extract-pages" -> Icons.Default.ContentCut
    "reorder-pages" -> Icons.Default.Reorder
    "page-numbers" -> Icons.Default.TextFields
    "edit-pdf" -> Icons.Default.Edit
    "add-text" -> Icons.Default.TextFields
    "add-watermark" -> Icons.Default.FormatPaint
    "protect-password" -> Icons.Default.Lock
    "unlock" -> Icons.Default.LockOpen
    "sign" -> Icons.Default.Draw
    "redact" -> Icons.Default.Security
    "scan-to-pdf" -> Icons.Default.CameraAlt
    "ocr" -> Icons.Default.AutoAwesome
    "pdf-reader" -> Icons.Default.Visibility
    else -> Icons.Default.PictureAsPdf
  }
}
