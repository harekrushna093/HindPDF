package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PdfTool
import com.example.data.model.ToolTier
import com.example.data.model.UploadedDoc
import com.example.data.model.UserProfileData
import com.example.data.model.VisualPdfPage
import com.example.ui.components.getToolIconVector
import com.example.ui.theme.HindGoldLight
import com.example.ui.theme.HindGoldPro
import com.example.ui.theme.HindGoldProDark
import com.example.ui.theme.HindGreenBg
import com.example.ui.theme.HindGreenSuccess
import com.example.ui.theme.HindRedDark
import com.example.ui.theme.HindRedLight
import com.example.ui.theme.HindRedPrimary
import com.example.ui.viewmodel.ToolSettingsState

@Composable
fun ToolWorkbenchScreen(
  tool: PdfTool,
  userProfile: UserProfileData,
  uploadedFiles: List<UploadedDoc>,
  visualPages: List<VisualPdfPage>,
  toolSettings: ToolSettingsState,
  isProcessing: Boolean,
  processingProgress: Int,
  statusText: String,
  onAddSampleDoc: (name: String, sizeMb: Double, pageCount: Int) -> Unit,
  onRemoveDoc: (id: String) -> Unit,
  onClearAllDocs: () -> Unit,
  onSortDocs: () -> Unit,
  onMovePage: (fromIndex: Int, direction: Int) -> Unit,
  onRotatePage: (index: Int) -> Unit,
  onToggleDeletePage: (index: Int) -> Unit,
  onRotateAllPages: (degrees: Int) -> Unit,
  onUpdateSettings: ((ToolSettingsState) -> ToolSettingsState) -> Unit,
  onOpenSignatureDialog: () -> Unit,
  onExecuteTool: () -> Unit,
  onOpenPaywall: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
      .testTag("tool_workbench_column")
  ) {
    // HEADER
    item {
      Spacer(modifier = Modifier.height(16.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(tool.color)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = getToolIconVector(tool.id),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
              text = tool.name,
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.onSurface
            )

            if (tool.tier == ToolTier.PRO) {
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(Brush.linearGradient(listOf(HindGoldPro, HindGoldProDark)))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text("PRO", color = Color.White, fontWeight = FontWeight.Black, fontSize = 10.sp)
              }
            }
          }

          Text(
            text = tool.desc,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp, bottom = 10.dp)
          )

          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(HindGreenBg)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "🔒 Client-Side Encrypted",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = HindGreenSuccess
              )
            }

            if (userProfile.isPro) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(HindGoldLight)
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text(
                  text = "⚡ Pro Unlimited Active",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = HindGoldProDark
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // DROPZONE / FILE SELECTOR
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("file_dropzone_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Dropzone Dashed Container
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(if (uploadedFiles.isEmpty()) Color(0xFFFFF7F7) else MaterialTheme.colorScheme.surfaceVariant)
              .border(1.5.dp, if (uploadedFiles.isEmpty()) HindRedPrimary.copy(alpha = 0.5f) else Color(0xFFCBD5E1), RoundedCornerShape(14.dp))
              .padding(16.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(HindRedPrimary),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.UploadFile,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(24.dp)
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = if (uploadedFiles.isEmpty()) "Select or Import Documents" else "Add More Documents",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )

              Text(
                text = "Pick file on device or tap quick preset sample below",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Sample presets
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
              ) {
                Button(
                  onClick = {
                    onAddSampleDoc("Project_Proposal_2026.pdf", 2.45, 3)
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = HindRedPrimary),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.testTag("import_sample_pdf_btn")
                ) {
                  Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Add Document", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                  onClick = {
                    onAddSampleDoc("Scanned_Contract_${uploadedFiles.size + 1}.pdf", 1.80, 2)
                  },
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.testTag("import_second_sample_btn")
                ) {
                  Text("+ Add 2nd File", fontSize = 12.sp)
                }
              }
            }
          }

          // File List Actions
          if (uploadedFiles.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Imported Documents (${uploadedFiles.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )

              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                  onClick = onSortDocs,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text("A-Z ⌵", fontSize = 10.sp)
                }
                OutlinedButton(
                  onClick = onClearAllDocs,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text("Clear 🗑", fontSize = 10.sp, color = HindRedPrimary)
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Document Rows
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              uploadedFiles.forEach { doc ->
                Surface(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                  color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.weight(1f)
                    ) {
                      Box(
                        modifier = Modifier
                          .size(28.dp)
                          .clip(RoundedCornerShape(4.dp))
                          .background(HindRedLight),
                        contentAlignment = Alignment.Center
                      ) {
                        Text(
                          text = "DOC",
                          color = HindRedPrimary,
                          fontWeight = FontWeight.Black,
                          fontSize = 9.sp
                        )
                      }
                      Spacer(modifier = Modifier.width(8.dp))
                      Column {
                        Text(
                          text = doc.name,
                          fontWeight = FontWeight.Bold,
                          fontSize = 12.sp,
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis
                        )
                        Text(
                          text = "${doc.sizeFormatted} • ${doc.pageCount} pages",
                          fontSize = 10.sp,
                          color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                      }
                    }

                    IconButton(
                      onClick = { onRemoveDoc(doc.id) },
                      modifier = Modifier.size(28.dp)
                    ) {
                      Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = HindRedPrimary,
                        modifier = Modifier.size(16.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // VISUAL PAGES GRID (for rearrange, per-page rotate, per-page delete)
    if (visualPages.isNotEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("visual_pages_card"),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          shape = RoundedCornerShape(16.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Document Page Tiles (${visualPages.size})",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
              )

              OutlinedButton(
                onClick = { onRotateAllPages(90) },
                shape = RoundedCornerShape(6.dp)
              ) {
                Text("Rotate All ↻", fontSize = 10.sp)
              }
            }

            Text(
              text = "Tap arrows to move, ↻ to rotate, or 🗑 to exclude page.",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(bottom = 10.dp)
            )

            // Grid of visual pages
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              for (i in visualPages.indices step 2) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  VisualPageTile(
                    page = visualPages[i],
                    index = i,
                    onMoveLeft = { onMovePage(i, -1) },
                    onMoveRight = { onMovePage(i, 1) },
                    onRotate = { onRotatePage(i) },
                    onDelete = { onToggleDeletePage(i) },
                    modifier = Modifier.weight(1f)
                  )

                  if (i + 1 < visualPages.size) {
                    VisualPageTile(
                      page = visualPages[i + 1],
                      index = i + 1,
                      onMoveLeft = { onMovePage(i + 1, -1) },
                      onMoveRight = { onMovePage(i + 1, 1) },
                      onRotate = { onRotatePage(i + 1) },
                      onDelete = { onToggleDeletePage(i + 1) },
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

        Spacer(modifier = Modifier.height(14.dp))
      }
    }

    // DYNAMIC TOOL SETTINGS
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("tool_settings_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Tool Options & Settings",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 10.dp)
          )

          when (tool.id) {
            "protect-password" -> {
              Text(
                text = "Encryption Password (Min 4 chars)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              OutlinedTextField(
                value = toolSettings.protectPassword,
                onValueChange = { p -> onUpdateSettings { it.copy(protectPassword = p) } },
                placeholder = { Text("Enter lock password") },
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .testTag("input_protect_password"),
                singleLine = true
              )
            }
            "add-watermark" -> {
              Text(
                text = "Watermark Text Phrase",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              OutlinedTextField(
                value = toolSettings.watermarkText,
                onValueChange = { w -> onUpdateSettings { it.copy(watermarkText = w) } },
                placeholder = { Text("e.g. CONFIDENTIAL / DRAFT") },
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .testTag("input_watermark_text"),
                singleLine = true
              )
            }
            "add-text", "edit-pdf" -> {
              Text(
                text = "Custom Text to Overlay",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              OutlinedTextField(
                value = toolSettings.customTextStamp,
                onValueChange = { t -> onUpdateSettings { it.copy(customTextStamp = t) } },
                placeholder = { Text("e.g. APPROVED DOCUMENT") },
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .testTag("input_custom_text"),
                singleLine = true
              )
            }
            "sign" -> {
              Button(
                onClick = onOpenSignatureDialog,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("open_signature_dialog_btn")
              ) {
                Icon(
                  imageVector = Icons.Default.Draw,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (toolSettings.hasSignature) "✅ Signature Captured (Tap to Redraw)" else "✍️ Draw Digital Signature",
                  color = MaterialTheme.colorScheme.onSurface,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            "split", "extract-pages" -> {
              Text(
                text = "Page Range to Extract",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              OutlinedTextField(
                value = toolSettings.extractPageRange,
                onValueChange = { r -> onUpdateSettings { it.copy(extractPageRange = r) } },
                placeholder = { Text("e.g. 1-3, 5") },
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .testTag("input_extract_range"),
                singleLine = true
              )
            }
            else -> {
              Text(
                text = "Standard client-side processing enabled. Zero server uploads.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // IN-BUTTON ANIMATED PROGRESS BAR & EXECUTE BUTTON
          val animatedProgress by animateFloatAsState(targetValue = processingProgress / 100f, label = "progress")

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (userProfile.isPro) HindGoldProDark else HindRedPrimary)
              .clickable(enabled = !isProcessing && uploadedFiles.isNotEmpty()) {
                onExecuteTool()
              }
              .testTag("execute_workbench_btn"),
            contentAlignment = Alignment.Center
          ) {
            // Fill background
            if (isProcessing) {
              Box(
                modifier = Modifier
                  .fillMaxHeight()
                  .fillMaxWidth(animatedProgress)
                  .background(if (userProfile.isPro) HindGoldPro else HindRedDark)
                  .align(Alignment.CenterStart)
              )
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              if (isProcessing) {
                CircularProgressIndicator(
                  color = Color.White,
                  strokeWidth = 2.5.dp,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "$statusText ($processingProgress%)",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              } else {
                Text(
                  text = if (uploadedFiles.isEmpty()) "Add Document to Process" else "Process ${tool.name}",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
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
fun VisualPageTile(
  page: VisualPdfPage,
  index: Int,
  onMoveLeft: () -> Unit,
  onMoveRight: () -> Unit,
  onRotate: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .border(
        width = 1.5.dp,
        color = if (page.isDeleted) HindRedPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        shape = RoundedCornerShape(10.dp)
      ),
    colors = CardDefaults.cardColors(
      containerColor = if (page.isDeleted) HindRedLight.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
    )
  ) {
    Column(
      modifier = Modifier.padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Mock page preview
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(100.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(Color(0xFFF1F5F9))
          .rotate(page.rotationDegrees.toFloat()),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            tint = HindRedPrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Page ${page.pageNumber}",
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = Color(0xFF1E293B)
          )
          Text(
            text = "${page.rotationDegrees}°",
            fontSize = 9.sp,
            color = Color(0xFF64748B)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "#${index + 1} (${page.fileName})",
          fontSize = 9.sp,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f)
        )

        IconButton(
          onClick = onDelete,
          modifier = Modifier.size(22.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = if (page.isDeleted) HindRedPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
          )
        }
      }

      // Action controls
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        IconButton(onClick = onMoveLeft, modifier = Modifier.size(24.dp)) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Move Left", modifier = Modifier.size(14.dp))
        }
        IconButton(onClick = onRotate, modifier = Modifier.size(24.dp)) {
          Icon(Icons.Default.RotateRight, contentDescription = "Rotate", modifier = Modifier.size(14.dp))
        }
        IconButton(onClick = onMoveRight, modifier = Modifier.size(24.dp)) {
          Icon(Icons.Default.ArrowForward, contentDescription = "Move Right", modifier = Modifier.size(14.dp))
        }
      }
    }
  }
}
