package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PdfTool
import com.example.data.model.ToolCategory
import com.example.data.model.ToolTier
import com.example.data.model.ToolsCatalog
import com.example.data.model.UserProfileData
import com.example.ui.components.getToolIconVector
import com.example.ui.theme.HindGoldBorder
import com.example.ui.theme.HindGoldLight
import com.example.ui.theme.HindGoldPro
import com.example.ui.theme.HindGoldProDark
import com.example.ui.theme.HindGreenBg
import com.example.ui.theme.HindGreenSuccess
import com.example.ui.theme.HindRedDark
import com.example.ui.theme.HindRedLight
import com.example.ui.theme.HindRedPrimary
import com.example.ui.viewmodel.ScreenDestination
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
  userProfile: UserProfileData,
  onNavigateToTool: (String) -> Unit,
  onNavigateToAllTools: () -> Unit,
  onOpenPaywall: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }

  // Typewriter placeholder animation
  val searchSuggestions = listOf(
    "Search tools: Compress PDF...",
    "Search tools: OCR Scanned PDF...",
    "Search tools: Merge multiple PDFs...",
    "Search tools: PDF to Word...",
    "Search tools: Split PDF...",
    "Search tools: Protect with Password...",
    "Search tools: Sign PDF..."
  )
  var promptIndex by remember { mutableIntStateOf(0) }
  var typewriterText by remember { mutableStateOf("") }

  LaunchedEffect(promptIndex) {
    val fullPhrase = searchSuggestions[promptIndex]
    // Type forward
    for (i in 1..fullPhrase.length) {
      typewriterText = fullPhrase.substring(0, i)
      delay(40)
    }
    delay(1800)
    // Delete backward
    for (i in fullPhrase.length downTo 0) {
      typewriterText = fullPhrase.substring(0, i)
      delay(20)
    }
    delay(300)
    promptIndex = (promptIndex + 1) % searchSuggestions.size
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("home_screen_column")
  ) {
    // HERO SECTION
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(
                if (userProfile.isPro) HindGoldLight.copy(alpha = 0.4f) else HindRedLight.copy(alpha = 0.35f),
                MaterialTheme.colorScheme.background
              )
            )
          )
          .padding(horizontal = 20.dp, vertical = 24.dp)
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "All Your PDF Tools in One Place",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 30.sp
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Merge, split, compress, convert and secure your PDFs free — fast, private, and native on Android. No cloud uploads required.",
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp)
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Search Box
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp)),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            shadowElevation = 4.dp
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                  Text(
                    text = typewriterText.ifEmpty { "Search 30+ PDF tools..." },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                  )
                },
                leadingIcon = {
                  Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = HindRedPrimary,
                    modifier = Modifier.size(20.dp)
                  )
                },
                modifier = Modifier
                  .weight(1f)
                  .testTag("home_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color.Transparent,
                  unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true
              )

              Button(
                onClick = {
                  val query = searchQuery.trim().lowercase()
                  val match = ToolsCatalog.allTools.find { it.name.lowercase().contains(query) || it.id.contains(query) }
                  if (match != null) onNavigateToTool(match.id)
                  else onNavigateToAllTools()
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (userProfile.isPro) HindGoldProDark else HindRedPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("home_search_button")
              ) {
                Text("Search", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Popular quick chips
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = "Popular: ",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(top = 4.dp, end = 4.dp)
            )
            QuickTagChip("Compress PDF") { onNavigateToTool("compress") }
            QuickTagChip("Merge PDF") { onNavigateToTool("merge") }
            QuickTagChip("OCR PDF ⭐") { onNavigateToTool("ocr") }
            QuickTagChip("PDF to Word") { onNavigateToTool("pdf-to-word") }
          }
        }
      }
    }

    // POPULAR TOOLS SECTION
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Popular Tools",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
          )

          TextButton(onClick = onNavigateToAllTools) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "View all 30+ tools",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = HindRedPrimary
              )
              Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = HindRedPrimary,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid of popular tools
        val popularList = ToolsCatalog.popularTools
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          for (i in popularList.indices step 2) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              ToolCardItem(
                tool = popularList[i],
                onClick = { onNavigateToTool(popularList[i].id) },
                modifier = Modifier.weight(1f)
              )
              if (i + 1 < popularList.size) {
                ToolCardItem(
                  tool = popularList[i + 1],
                  onClick = { onNavigateToTool(popularList[i + 1].id) },
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

    // HOW IT WORKS SECTION
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "How It Works",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
          )
          Text(
            text = "Simple, private, and fast in 3 native steps.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
          )

          StepRowItem(
            stepNum = "1",
            icon = Icons.Default.UploadFile,
            title = "Select or Import Files",
            desc = "Pick PDF documents, Word files, or images directly on device."
          )

          Spacer(modifier = Modifier.height(14.dp))

          StepRowItem(
            stepNum = "2",
            icon = Icons.Default.Bolt,
            title = "Process Instantly",
            desc = "Local high-performance engine converts & edits with zero cloud upload."
          )

          Spacer(modifier = Modifier.height(14.dp))

          StepRowItem(
            stepNum = "3",
            icon = Icons.Default.Download,
            title = "Download Output",
            desc = "Save verified Standard or Studio Pro Lossless HD documents."
          )
        }
      }
    }

    // WHY CHOOSE US
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Text(
          text = "Why Choose HindPDF?",
          fontSize = 18.sp,
          fontWeight = FontWeight.Black,
          modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          FeatureCard(
            icon = Icons.Default.Security,
            iconBg = HindRedLight,
            iconTint = HindRedPrimary,
            title = "100% Client Privacy",
            desc = "Your files are never transmitted to external servers.",
            modifier = Modifier.weight(1f)
          )
          FeatureCard(
            icon = Icons.Default.Speed,
            iconBg = HindGoldLight,
            iconTint = HindGoldProDark,
            title = "Blazing Fast",
            desc = "Instant on-device processing powered by native Kotlin.",
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // PRO UPGRADE BANNER (if not pro)
    if (!userProfile.isPro) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("home_pro_banner"),
          colors = CardDefaults.cardColors(containerColor = HindRedPrimary),
          shape = RoundedCornerShape(18.dp)
        ) {
          Column(
            modifier = Modifier.padding(20.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = "⭐", fontSize = 20.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Unlock Full HindPDF Pro Potential",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
              )
            }

            Text(
              text = "Unlimited daily conversions, OCR text extraction, secure password encryption & 300+ DPI vector clarity.",
              color = Color.White.copy(alpha = 0.9f),
              fontSize = 12.sp,
              modifier = Modifier.padding(vertical = 10.dp)
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = { onOpenPaywall("HindPDF Pro Unlimited") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text(
                  text = "Upgrade to Pro",
                  color = HindRedPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }

              OutlinedButton(
                onClick = onNavigateToAllTools,
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(Color.White, Color.White))),
                modifier = Modifier.weight(1f)
              ) {
                Text(
                  text = "Explore 30+ Tools",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun QuickTagChip(text: String, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .clickable { onClick() }
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    Text(
      text = text,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
fun ToolCardItem(
  tool: PdfTool,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clip(RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .testTag("tool_card_${tool.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
    shape = RoundedCornerShape(14.dp)
  ) {
    Column(
      modifier = Modifier.padding(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
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

        when (tool.tier) {
          ToolTier.PRO -> {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Brush.linearGradient(listOf(HindGoldPro, HindGoldProDark)))
                .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
              Text(
                text = "PRO",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 9.sp
              )
            }
          }
          ToolTier.FREEMIUM -> {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(HindGreenBg)
                .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
              Text(
                text = "Free / Pro",
                color = HindGreenSuccess,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
              )
            }
          }
          ToolTier.FREE -> {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(HindGreenBg)
                .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
              Text(
                text = "Free",
                color = HindGreenSuccess,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = tool.name,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Text(
        text = tool.desc,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 14.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(top = 2.dp)
      )
    }
  }
}

@Composable
fun StepRowItem(
  stepNum: String,
  icon: ImageVector,
  title: String,
  desc: String
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(38.dp)
        .clip(CircleShape)
        .background(HindRedLight),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = stepNum,
        color = HindRedPrimary,
        fontWeight = FontWeight.Black,
        fontSize = 16.sp
      )
    }

    Spacer(modifier = Modifier.width(12.dp))

    Column {
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
      )
      Text(
        text = desc,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun FeatureCard(
  icon: ImageVector,
  iconBg: Color,
  iconTint: Color,
  title: String,
  desc: String,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(iconBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
      )
      Text(
        text = desc,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 14.sp,
        modifier = Modifier.padding(top = 2.dp)
      )
    }
  }
}
