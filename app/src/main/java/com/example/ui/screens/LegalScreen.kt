package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LegalScreen(
  isPrivacyPolicy: Boolean,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
      .testTag("legal_screen_column")
  ) {
    item {
      Spacer(modifier = Modifier.height(16.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = if (isPrivacyPolicy) "Privacy Policy for HindPDF" else "Terms of Service for HindPDF",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Last updated: August 30, 2026",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
          )

          HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))

          if (isPrivacyPolicy) {
            LegalSection("1. Overview", "HindPDF provides native tools to convert, compress, edit, sign, OCR, and process PDF files on your Android device. A core design principle is that your document files are processed locally on your device and are never uploaded to our servers.")
            LegalSection("2. Information We Collect", "Account info: Email, Name, and password managed securely via Firebase Authentication. Document content: Files you select are rendered and processed in memory on device; results are generated locally with zero cloud retention.")
            LegalSection("3. Third-Party Services", "Firebase Authentication (Google) handles account sync, tokens, and credentials securely.")
            LegalSection("4. Contact Us", "If you have questions about this Privacy Policy, please contact: support@hindpdf.com")
          } else {
            LegalSection("1. The Service", "HindPDF provides native tools for working with PDF documents: merging, splitting, compressing, converting, rotating, editing, watermarking, password protection, unlocking, e-signing, redacting, and OCR.")
            LegalSection("2. Free & Pro Plans", "Free Plan: Standard daily conversions with zero watermarks.\nPro Plan: Unlimited daily conversions with 300+ DPI HD vector clarity and OCR.")
            LegalSection("3. Your Content", "You retain all ownership rights to files processed through the Service. We do not view, store, or transmit your documents.")
            LegalSection("4. Governing Law", "These Terms are governed by the laws of India. Contact: support@hindpdf.com")
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
private fun LegalSection(title: String, body: String) {
  Column(modifier = Modifier.padding(vertical = 6.dp)) {
    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
    Text(text = body, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp, modifier = Modifier.padding(top = 2.dp))
  }
}
