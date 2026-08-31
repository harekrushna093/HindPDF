package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileData
import com.example.ui.theme.HindGoldLight
import com.example.ui.theme.HindGoldPro
import com.example.ui.theme.HindGoldProDark
import com.example.ui.theme.HindGreenDark
import com.example.ui.theme.HindGreenLight
import com.example.ui.theme.HindRedDark
import com.example.ui.theme.HindRedLight
import com.example.ui.theme.HindRedPrimary
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun AppNavigationDrawer(
  userProfile: UserProfileData,
  currentScreen: ScreenDestination,
  isOnline: Boolean,
  onNavigate: (ScreenDestination) -> Unit,
  onOpenPaywall: () -> Unit,
  onCloseDrawer: () -> Unit,
  onSignOut: () -> Unit,
  modifier: Modifier = Modifier
) {
  ModalDrawerSheet(
    modifier = modifier
      .width(320.dp)
      .fillMaxHeight()
      .testTag("app_navigation_drawer"),
    drawerContainerColor = MaterialTheme.colorScheme.surface
  ) {
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
    ) {
      // Drawer Header
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                MaterialTheme.colorScheme.surface
              )
            )
          )
          .padding(20.dp)
      ) {
        Column {
          // Brand Logo
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Brush.linearGradient(listOf(HindRedPrimary, HindRedDark))),
              contentAlignment = Alignment.Center
            ) {
              Text("PDF", color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Hind", fontWeight = FontWeight.Black, fontSize = 18.sp)
                Text("PDF", fontWeight = FontWeight.Black, fontSize = 18.sp, color = HindRedPrimary)
                if (userProfile.isPro) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(4.dp))
                      .background(HindGoldPro)
                      .padding(horizontal = 4.dp, vertical = 2.dp)
                  ) {
                    Text("PRO", color = Color.White, fontWeight = FontWeight.Black, fontSize = 8.sp)
                  }
                }
              }
              Text("Native Android Suite", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // User Card inside Drawer
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                onCloseDrawer()
                if (userProfile.uid == "guest_user") {
                  onNavigate(ScreenDestination.Auth)
                } else {
                  onNavigate(ScreenDestination.Profile)
                }
              },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(if (userProfile.isPro) HindGoldPro else HindRedPrimary),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = userProfile.displayName.firstOrNull()?.uppercase() ?: "U",
                  color = Color.White,
                  fontWeight = FontWeight.Black,
                  fontSize = 16.sp
                )
              }

              Spacer(modifier = Modifier.width(10.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = userProfile.displayName,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = userProfile.email,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Connection status pill
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (isOnline) HindGreenLight else HindRedLight)
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (isOnline) Icons.Default.CloudDone else Icons.Default.CloudOff,
              contentDescription = null,
              tint = if (isOnline) HindGreenDark else HindRedDark,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isOnline) "Firestore Online Sync" else "Offline Cache Active",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = if (isOnline) HindGreenDark else HindRedDark
            )
          }
        }
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

      // Primary Navigation Section
      Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        Text(
          text = "NAVIGATION",
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )

        NavigationDrawerItem(
          icon = { Icon(Icons.Default.Home, contentDescription = null) },
          label = { Text("Home & Studio", fontWeight = FontWeight.SemiBold) },
          selected = currentScreen is ScreenDestination.Home,
          onClick = {
            onNavigate(ScreenDestination.Home)
            onCloseDrawer()
          },
          colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = HindRedLight.copy(alpha = 0.5f),
            selectedIconColor = HindRedPrimary,
            selectedTextColor = HindRedPrimary
          ),
          modifier = Modifier.padding(vertical = 2.dp)
        )

        NavigationDrawerItem(
          icon = { Icon(Icons.Default.GridView, contentDescription = null) },
          label = { Text("All 30+ PDF Tools", fontWeight = FontWeight.SemiBold) },
          selected = currentScreen is ScreenDestination.AllTools,
          onClick = {
            onNavigate(ScreenDestination.AllTools)
            onCloseDrawer()
          },
          colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = HindRedLight.copy(alpha = 0.5f),
            selectedIconColor = HindRedPrimary,
            selectedTextColor = HindRedPrimary
          ),
          modifier = Modifier.padding(vertical = 2.dp)
        )

        NavigationDrawerItem(
          icon = { Icon(Icons.Default.History, contentDescription = null) },
          label = { Text("Recent Activity & Cache", fontWeight = FontWeight.SemiBold) },
          selected = currentScreen is ScreenDestination.History,
          onClick = {
            onNavigate(ScreenDestination.History)
            onCloseDrawer()
          },
          colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = HindRedLight.copy(alpha = 0.5f),
            selectedIconColor = HindRedPrimary,
            selectedTextColor = HindRedPrimary
          ),
          modifier = Modifier.padding(vertical = 2.dp)
        )

        NavigationDrawerItem(
          icon = { Icon(Icons.Default.Person, contentDescription = null) },
          label = { Text("Profile & Settings", fontWeight = FontWeight.SemiBold) },
          selected = currentScreen is ScreenDestination.Profile,
          onClick = {
            if (userProfile.uid == "guest_user") {
              onNavigate(ScreenDestination.Auth)
            } else {
              onNavigate(ScreenDestination.Profile)
            }
            onCloseDrawer()
          },
          colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = HindRedLight.copy(alpha = 0.5f),
            selectedIconColor = HindRedPrimary,
            selectedTextColor = HindRedPrimary
          ),
          modifier = Modifier.padding(vertical = 2.dp)
        )
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 6.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
      )

      // Quick Tool Shortcuts
      Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Text(
          text = "POPULAR TOOLS",
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )

        NavigationDrawerItem(
          icon = { Icon(Icons.Default.Description, contentDescription = null, tint = HindRedPrimary) },
          label = { Text("Merge & Split PDF") },
          selected = false,
          onClick = {
            onNavigate(ScreenDestination.ToolWorkbench("merge-pdf"))
            onCloseDrawer()
          },
          modifier = Modifier.padding(vertical = 2.dp)
        )

        NavigationDrawerItem(
          icon = { Icon(Icons.Default.Transform, contentDescription = null, tint = Color(0xFF2563EB)) },
          label = { Text("PDF to Word & Excel") },
          selected = false,
          onClick = {
            onNavigate(ScreenDestination.ToolWorkbench("pdf-to-word"))
            onCloseDrawer()
          },
          modifier = Modifier.padding(vertical = 2.dp)
        )

        NavigationDrawerItem(
          icon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFD97706)) },
          label = { Text("Password Protect & Sign") },
          selected = false,
          onClick = {
            onNavigate(ScreenDestination.ToolWorkbench("protect-pdf"))
            onCloseDrawer()
          },
          modifier = Modifier.padding(vertical = 2.dp)
        )
      }

      // Pro Upgrade Banner if not pro
      if (!userProfile.isPro) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                onCloseDrawer()
                onOpenPaywall()
              },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = HindGoldLight)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = HindGoldProDark)
                Spacer(modifier = Modifier.width(6.dp))
                Text("HindPDF Pro", fontWeight = FontWeight.Black, fontSize = 13.sp, color = HindGoldProDark)
              }
              Text(
                text = "Unlimited conversions, OCR, 300+ DPI & zero queue times.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 6.dp)
              )
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(HindGoldPro)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text("Upgrade for $9/mo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
      )

      // Legal Links & Auth
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          TextButton(
            onClick = {
              onNavigate(ScreenDestination.PrivacyPolicy)
              onCloseDrawer()
            }
          ) {
            Icon(Icons.Default.PrivacyTip, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Privacy", fontSize = 11.sp)
          }

          TextButton(
            onClick = {
              onNavigate(ScreenDestination.TermsOfService)
              onCloseDrawer()
            }
          ) {
            Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Terms", fontSize = 11.sp)
          }
        }

        if (userProfile.uid != "guest_user") {
          NavigationDrawerItem(
            icon = { Icon(Icons.Default.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            label = { Text("Sign Out", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) },
            selected = false,
            onClick = {
              onCloseDrawer()
              onSignOut()
            }
          )
        } else {
          NavigationDrawerItem(
            icon = { Icon(Icons.Default.Login, contentDescription = null, tint = HindRedPrimary) },
            label = { Text("Sign In or Register", color = HindRedPrimary, fontWeight = FontWeight.Bold) },
            selected = false,
            onClick = {
              onCloseDrawer()
              onNavigate(ScreenDestination.Auth)
            }
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
