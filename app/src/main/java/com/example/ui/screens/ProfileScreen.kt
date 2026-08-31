package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.ActivityLogEntity
import com.example.data.model.UserProfileData
import com.example.ui.theme.HindGoldLight
import com.example.ui.theme.HindGoldPro
import com.example.ui.theme.HindGoldProDark
import com.example.ui.theme.HindGreenBg
import com.example.ui.theme.HindGreenSuccess
import com.example.ui.theme.HindRedDark
import com.example.ui.theme.HindRedLight
import com.example.ui.theme.HindRedPrimary

@Composable
fun ProfileScreen(
  userProfile: UserProfileData,
  activityLogs: List<ActivityLogEntity>,
  onUpdateName: (String) -> Unit,
  onChangePassword: (String) -> Unit,
  onToggleNotification: (Boolean) -> Unit,
  onToggleZeroRetention: (Boolean) -> Unit,
  onTogglePromo: (Boolean) -> Unit,
  onOpenPaywall: (String) -> Unit,
  onCancelPro: () -> Unit,
  onClearHistory: () -> Unit,
  onOpenDeleteModal: () -> Unit,
  onNavigateToAllTools: () -> Unit,
  onSignOut: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showEditNameDialog by remember { mutableStateOf(false) }
  var showPasswordDialog by remember { mutableStateOf(false) }
  var newNameInput by remember { mutableStateOf(userProfile.displayName) }
  var newPassInput by remember { mutableStateOf("") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
      .testTag("profile_screen_column")
  ) {
    item {
      Spacer(modifier = Modifier.height(16.dp))

      // AREA 1: ACCOUNT INFO CARD
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("profile_account_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Large Avatar
          Box(
            modifier = Modifier
              .size(68.dp)
              .clip(CircleShape)
              .background(if (userProfile.isPro) HindGoldPro else HindRedPrimary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = userProfile.displayName.firstOrNull()?.uppercase() ?: "U",
              color = Color.White,
              fontWeight = FontWeight.Black,
              fontSize = 28.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = userProfile.displayName,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          Text(
            text = userProfile.email,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (userProfile.isPro) HindGoldLight else HindGreenBg)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = if (userProfile.isPro) "PRO TIER ACTIVE 👑" else "FREE TIER",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = if (userProfile.isPro) HindGoldProDark else HindGreenSuccess
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = userProfile.authProvider,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
          ) {
            OutlinedButton(
              onClick = {
                newNameInput = userProfile.displayName
                showEditNameDialog = true
              },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("edit_name_btn")
            ) {
              Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Edit Name", fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
              onClick = { showPasswordDialog = true },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("change_pass_btn")
            ) {
              Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Password", fontSize = 11.sp)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedButton(
            onClick = onSignOut,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("sign_out_btn")
          ) {
            Text("Sign Out", fontSize = 12.sp, color = HindRedPrimary)
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // AREA 2: PLAN STATUS & USAGE CAP TRACKER
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("plan_status_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Plan Status & Usage",
              fontWeight = FontWeight.Black,
              fontSize = 15.sp
            )

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (userProfile.isPro) HindGoldLight else HindGreenBg)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = if (userProfile.isPro) "PRO UNLIMITED" else "FREE TIER",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (userProfile.isPro) HindGoldProDark else HindGreenSuccess
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          if (userProfile.isPro) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Membership:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Active Pro Member 👑", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HindGoldProDark)
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Renewal Cycle:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(userProfile.proRenewalDate, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Daily Limit:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Unlimited (No Daily Cap)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HindGreenSuccess)
              }
            }
          } else {
            // Free Tier Usage Bar
            val count = userProfile.dailyUsageCount.coerceAtMost(userProfile.dailyLimit)
            val progress = (count.toFloat() / userProfile.dailyLimit.toFloat()).coerceIn(0f, 1f)

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Today's Conversions:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              Text("$count / ${userProfile.dailyLimit} Used", fontSize = 12.sp, fontWeight = FontWeight.Black, color = HindRedPrimary)
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = HindRedPrimary,
              trackColor = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = { onOpenPaywall("Profile Upgrade") },
              colors = ButtonDefaults.buttonColors(containerColor = HindGoldProDark),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("profile_upgrade_btn")
            ) {
              Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Upgrade to Pro Unlimited ($9/mo)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // AREA 3: BILLING & INVOICES (if Pro)
    if (userProfile.isPro) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          shape = RoundedCornerShape(18.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text(
              text = "Billing & Payments 💳",
              fontWeight = FontWeight.Black,
              fontSize = 15.sp,
              modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Payment Method:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(userProfile.billingMethod, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Next Invoice:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("$9.00 on ${userProfile.proRenewalDate}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedButton(
                onClick = { /* Invoice receipt simulation */ },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Invoice Receipt", fontSize = 11.sp)
              }

              OutlinedButton(
                onClick = onCancelPro,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text("Cancel Pro", fontSize = 11.sp, color = HindRedPrimary)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
      }
    }

    // AREA 4: ACTIVITY & HISTORY LOG
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("activity_history_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.History, contentDescription = null, tint = HindRedPrimary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Recent Activity Log", fontWeight = FontWeight.Black, fontSize = 15.sp)
            }

            if (activityLogs.isNotEmpty()) {
              TextButton(onClick = onClearHistory) {
                Text("Clear Log", fontSize = 11.sp, color = HindRedPrimary)
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          if (activityLogs.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
              contentAlignment = Alignment.Center
            ) {
              Text("No recent documents processed yet.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              activityLogs.take(10).forEach { log ->
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
                    Column(modifier = Modifier.weight(1f)) {
                      Text(log.toolName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                      Text(log.fileName, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                      Text(log.timeFormatted, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                      TextButton(onClick = onNavigateToAllTools) {
                        Text("⚡ Process", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = HindRedPrimary)
                      }
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

    // AREA 5: SETTINGS, PRIVACY & DANGER ZONE
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("settings_privacy_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text("Settings & Privacy Controls ⚙️", fontWeight = FontWeight.Black, fontSize = 15.sp, modifier = Modifier.padding(bottom = 10.dp))

          // Push Notifications toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Push Notification on Task Done", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              Text("Receive local/push notification when long processing completes", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(
              checked = userProfile.notificationEnabled,
              onCheckedChange = onToggleNotification,
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = HindRedPrimary)
            )
          }

          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

          // Zero Retention toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Zero Client Retention", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              Text("Immediately wipe in-memory buffer upon navigating away", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(
              checked = userProfile.zeroRetentionEnabled,
              onCheckedChange = onToggleZeroRetention,
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = HindRedPrimary)
            )
          }

          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

          // Promotional Updates toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Product & Tool Releases", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              Text("Receive release notes when new tools are added", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(
              checked = userProfile.promoUpdatesEnabled,
              onCheckedChange = onTogglePromo,
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = HindRedPrimary)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Danger Zone
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(HindRedLight.copy(alpha = 0.4f))
              .padding(14.dp)
          ) {
            Column {
              Text("Danger Zone", fontWeight = FontWeight.Black, fontSize = 13.sp, color = HindRedPrimary)
              Text("Permanently erase your credentials and all cloud activity records.", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 4.dp))
              OutlinedButton(
                onClick = onOpenDeleteModal,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("open_delete_modal_btn")
              ) {
                Text("🗑 Delete Account (2-Step Verification)", fontSize = 11.sp, color = HindRedPrimary, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }

  // Edit Name Dialog
  if (showEditNameDialog) {
    Dialog(onDismissRequest = { showEditNameDialog = false }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Edit Full Name", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          OutlinedTextField(
            value = newNameInput,
            onValueChange = { newNameInput = it },
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp)
          )
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { showEditNameDialog = false }) { Text("Cancel") }
            Button(
              onClick = {
                onUpdateName(newNameInput)
                showEditNameDialog = false
              },
              colors = ButtonDefaults.buttonColors(containerColor = HindRedPrimary)
            ) {
              Text("Save")
            }
          }
        }
      }
    }
  }

  // Change Password Dialog
  if (showPasswordDialog) {
    Dialog(onDismissRequest = { showPasswordDialog = false }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Change Password", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          OutlinedTextField(
            value = newPassInput,
            onValueChange = { newPassInput = it },
            placeholder = { Text("New password (min. 6 chars)") },
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp)
          )
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { showPasswordDialog = false }) { Text("Cancel") }
            Button(
              onClick = {
                onChangePassword(newPassInput)
                showPasswordDialog = false
              },
              colors = ButtonDefaults.buttonColors(containerColor = HindRedPrimary)
            ) {
              Text("Update")
            }
          }
        }
      }
    }
  }
}
