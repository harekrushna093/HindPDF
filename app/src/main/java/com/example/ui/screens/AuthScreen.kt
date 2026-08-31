package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.HindPdfRepository
import com.example.ui.theme.HindGreenDark
import com.example.ui.theme.HindGreenLight
import com.example.ui.theme.HindRedDark
import com.example.ui.theme.HindRedLight
import com.example.ui.theme.HindRedPrimary
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
  repository: HindPdfRepository,
  onAuthSuccess: () -> Unit,
  onContinueAsGuest: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0 = Log In, 1 = Sign Up
  var name by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var isPasswordVisible by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var successMessage by remember { mutableStateOf<String?>(null) }
  var isLoading by remember { mutableStateOf(false) }
  var showForgotPasswordDialog by remember { mutableStateOf(false) }
  var resetEmailInput by remember { mutableStateOf("") }

  val scope = rememberCoroutineScope()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    item {
      Spacer(modifier = Modifier.height(20.dp))

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("auth_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(24.dp)) {
          // Brand Header inside Auth
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Brush.linearGradient(listOf(HindRedPrimary, HindRedDark))),
              contentAlignment = Alignment.Center
            ) {
              Text("PDF", color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Hind",
              fontWeight = FontWeight.Black,
              fontSize = 22.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "PDF",
              fontWeight = FontWeight.Black,
              fontSize = 22.sp,
              color = HindRedPrimary
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Tab Switcher (Log in / Sign up)
          TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .testTag("auth_tab_row")
          ) {
            Tab(
              selected = selectedTab == 0,
              onClick = {
                selectedTab = 0
                errorMessage = null
                successMessage = null
              },
              text = { Text("Log In", fontWeight = FontWeight.Bold, fontSize = 14.sp) }
            )
            Tab(
              selected = selectedTab == 1,
              onClick = {
                selectedTab = 1
                errorMessage = null
                successMessage = null
              },
              text = { Text("Create Account", fontWeight = FontWeight.Bold, fontSize = 14.sp) }
            )
          }

          Spacer(modifier = Modifier.height(18.dp))

          Text(
            text = if (selectedTab == 0) "Welcome back to HindPDF" else "Create your free account",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
          )
          Text(
            text = if (selectedTab == 0)
              "Access all 30+ PDF tools, offline sync, and your cloud history."
            else
              "Get instant access to secure PDF tools, fast processing, and cloud backup.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
          )

          // Success message banner
          AnimatedVisibility(visible = successMessage != null) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(HindGreenLight)
                .padding(12.dp)
            ) {
              Text(
                text = successMessage ?: "",
                color = HindGreenDark,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          // Error banner
          AnimatedVisibility(visible = errorMessage != null) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(HindRedLight)
                .padding(12.dp)
            ) {
              Text(
                text = errorMessage ?: "",
                color = HindRedDark,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          // Full name input if signing up
          if (selectedTab == 1) {
            Text("Full Name", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
              value = name,
              onValueChange = { name = it },
              placeholder = { Text("e.g. Harekrushna") },
              leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
              },
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .testTag("auth_name_input"),
              shape = RoundedCornerShape(10.dp),
              singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
          }

          // Email input
          Text("Email Address", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("you@example.com") },
            leadingIcon = {
              Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            },
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .testTag("auth_email_input"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Password input with visibility toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Password", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            if (selectedTab == 0) {
              Text(
                text = "Forgot password?",
                fontSize = 11.sp,
                color = HindRedPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                  resetEmailInput = email.ifBlank { "" }
                  showForgotPasswordDialog = true
                }
              )
            }
          }

          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("••••••••••••") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
              Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            },
            trailingIcon = {
              IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                  imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                  contentDescription = "Toggle password visibility",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .testTag("auth_password_input"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(18.dp))

          // Submit Button
          Button(
            onClick = {
              if (email.isBlank() || password.isBlank()) {
                errorMessage = "Please enter both your email address and password."
                return@Button
              }
              if (selectedTab == 1 && password.length < 6) {
                errorMessage = "Password must be at least 6 characters."
                return@Button
              }
              isLoading = true
              errorMessage = null
              scope.launch {
                val res = if (selectedTab == 0) {
                  repository.signIn(email.trim(), password)
                } else {
                  repository.signUp(name.trim(), email.trim(), password)
                }
                isLoading = false
                if (res.isSuccess) {
                  onAuthSuccess()
                } else {
                  errorMessage = res.exceptionOrNull()?.message ?: "Authentication failed."
                }
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = HindRedPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("auth_submit_btn"),
            enabled = !isLoading
          ) {
            if (isLoading) {
              CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
              Text(
                text = if (selectedTab == 0) "Log In to HindPDF" else "Create My Account",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Divider
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Text(
              text = "  or sign in with  ",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Social Login Buttons: Google & Facebook
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Google Sign In
            OutlinedButton(
              onClick = {
                scope.launch {
                  isLoading = true
                  val res = repository.signInWithGoogle(
                    emailOrToken = if (email.contains("@")) email.trim() else "harekrushna093@gmail.com",
                    name = if (name.isNotBlank()) name else "Harekrushna"
                  )
                  isLoading = false
                  if (res.isSuccess) {
                    onAuthSuccess()
                  } else {
                    errorMessage = res.exceptionOrNull()?.message ?: "Google Sign-In failed"
                  }
                }
              },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .testTag("google_auth_btn")
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔴 Google", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
            }

            // Facebook Sign In
            OutlinedButton(
              onClick = {
                scope.launch {
                  isLoading = true
                  val res = repository.signInWithFacebook(
                    emailOrToken = if (email.contains("@")) email.trim() else "harekrushna.fb@hindpdf.com",
                    name = if (name.isNotBlank()) name else "Harekrushna Facebook"
                  )
                  isLoading = false
                  if (res.isSuccess) {
                    onAuthSuccess()
                  } else {
                    errorMessage = res.exceptionOrNull()?.message ?: "Facebook Sign-In failed"
                  }
                }
              },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .testTag("facebook_auth_btn")
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔵 Facebook", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1877F2))
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Continue as Guest Button
          TextButton(
            onClick = onContinueAsGuest,
            modifier = Modifier.fillMaxWidth().testTag("continue_as_guest_btn")
          ) {
            Text(
              text = "Skip for now (Continue as Guest)",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }

  // Forgot password dialog
  if (showForgotPasswordDialog) {
    AlertDialog(
      onDismissRequest = { showForgotPasswordDialog = false },
      title = { Text("Reset Password") },
      text = {
        Column {
          Text(
            "Enter your registered email address. We'll send you instructions to reset your password.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = resetEmailInput,
            onValueChange = { resetEmailInput = it },
            placeholder = { Text("you@example.com") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (resetEmailInput.isBlank()) return@Button
            scope.launch {
              repository.sendPasswordReset(resetEmailInput.trim())
              showForgotPasswordDialog = false
              successMessage = "Password reset instructions sent to $resetEmailInput"
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = HindRedPrimary)
        ) {
          Text("Send Reset Link")
        }
      },
      dismissButton = {
        TextButton(onClick = { showForgotPasswordDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
