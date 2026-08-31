package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
fun TopNavBar(
  userProfile: UserProfileData,
  currentScreen: ScreenDestination,
  isOnline: Boolean,
  onOpenDrawer: () -> Unit,
  onNavigate: (ScreenDestination) -> Unit,
  onOpenPaywall: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("top_nav_bar"),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    shadowElevation = 3.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left: Drawer Hamburger button + Brand Logo
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onOpenDrawer,
          modifier = Modifier
            .size(38.dp)
            .testTag("drawer_menu_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Open Navigation Drawer",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onNavigate(ScreenDestination.Home) }
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .testTag("brand_logo_button"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(7.dp))
              .background(Brush.linearGradient(listOf(HindRedPrimary, HindRedDark))),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "PDF",
              color = Color.White,
              fontWeight = FontWeight.Black,
              fontSize = 10.sp
            )
          }

          Spacer(modifier = Modifier.width(7.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Hind",
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "PDF",
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                color = HindRedPrimary
              )

              if (userProfile.isPro) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Brush.linearGradient(listOf(HindGoldPro, HindGoldProDark)))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                  Text(
                    text = "PRO",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 8.sp
                  )
                }
              }
            }
          }
        }
      }

      // Right: Offline status icon, Pro button, Profile Avatar
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Live Network status indicator
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isOnline) HindGreenLight.copy(alpha = 0.6f) else HindRedLight.copy(alpha = 0.6f))
            .padding(horizontal = 6.dp, vertical = 4.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = if (isOnline) Icons.Default.CloudDone else Icons.Default.CloudOff,
              contentDescription = if (isOnline) "Online" else "Offline",
              tint = if (isOnline) HindGreenDark else HindRedDark,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = if (isOnline) "Sync" else "Offline",
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = if (isOnline) HindGreenDark else HindRedDark
            )
          }
        }

        // Pro Badge / Upgrade Button
        if (userProfile.isPro) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(HindGoldLight)
              .border(1.dp, HindGoldPro, RoundedCornerShape(20.dp))
              .padding(horizontal = 8.dp, vertical = 5.dp)
              .testTag("nav_pro_active_pill")
          ) {
            Text(
              text = "👑 Pro",
              color = HindGoldProDark,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 11.sp
            )
          }
        } else {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(HindRedPrimary)
              .clickable { onOpenPaywall() }
              .padding(horizontal = 9.dp, vertical = 5.dp)
              .testTag("nav_get_pro_btn")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "Pro",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            }
          }
        }

        // Profile Avatar Button
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(if (userProfile.isPro) HindGoldPro else HindRedPrimary)
            .clickable {
              if (userProfile.uid == "guest_user") {
                onNavigate(ScreenDestination.Auth)
              } else {
                onNavigate(ScreenDestination.Profile)
              }
            }
            .testTag("nav_profile_btn"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = userProfile.displayName.firstOrNull()?.uppercase() ?: "U",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp
          )
        }
      }
    }
  }
}
