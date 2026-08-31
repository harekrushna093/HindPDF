package com.example.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class ToolTier {
  FREE,
  PRO,
  FREEMIUM
}

enum class ToolCategory(val title: String, val emoji: String) {
  CORE("Core PDF Tools", "📄"),
  ORGANIZE("Organize PDF", "🟠"),
  EDIT_SECURITY("Edit & Security", "🔵"),
  SCAN_SMART("Scan & Smart Tools", "🟢")
}

data class PdfTool(
  val id: String,
  val name: String,
  val desc: String,
  val category: ToolCategory,
  val tier: ToolTier,
  val color: Long,
  val filePrefix: String,
  val fileExt: String,
  val mimeType: String,
  val acceptedExtensions: List<String> = listOf("pdf")
)

data class UploadedDoc(
  val id: String,
  val name: String,
  val sizeFormatted: String,
  val rawBytesCount: Long,
  val timeFormatted: String = "Added just now",
  val pageCount: Int = 1
)

data class VisualPdfPage(
  val uid: String,
  val fileId: String,
  val fileName: String,
  val pageNumber: Int,
  val rotationDegrees: Int = 0,
  val isDeleted: Boolean = false
)

data class ProcessingOutput(
  val toolId: String,
  val fileName: String,
  val standardSizeBytes: Long,
  val proSizeBytes: Long,
  val formatCategory: String, // "pdf", "image", "text"
  val sampleText: String = "",
  val pageCount: Int = 1
)

data class UserProfileData(
  val uid: String = "guest_user",
  val email: String = "guest@hindpdf.com",
  val displayName: String = "Guest User",
  val isPro: Boolean = false,
  val authProvider: String = "Email Account",
  val dailyUsageCount: Int = 0,
  val dailyLimit: Int = 5,
  val notificationEnabled: Boolean = true,
  val zeroRetentionEnabled: Boolean = true,
  val promoUpdatesEnabled: Boolean = false,
  val proRenewalDate: String = "September 29, 2026",
  val billingMethod: String = "•••• 4242 (Visa)"
)
