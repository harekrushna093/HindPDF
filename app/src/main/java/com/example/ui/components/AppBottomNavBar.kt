package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileData
import com.example.ui.theme.HindRedLight
import com.example.ui.theme.HindRedPrimary
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun AppBottomNavBar(
  currentScreen: ScreenDestination,
  historyCount: Int,
  userProfile: UserProfileData,
  onNavigate: (ScreenDestination) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier.testTag("app_bottom_nav_bar"),
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 8.dp
  ) {
    // 1. Home
    NavigationBarItem(
      selected = currentScreen is ScreenDestination.Home,
      onClick = { onNavigate(ScreenDestination.Home) },
      icon = {
        Icon(
          imageVector = if (currentScreen is ScreenDestination.Home) Icons.Filled.Home else Icons.Outlined.Home,
          contentDescription = "Home",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Home",
          fontWeight = if (currentScreen is ScreenDestination.Home) FontWeight.Bold else FontWeight.Normal,
          fontSize = 11.sp
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = HindRedPrimary,
        selectedTextColor = HindRedPrimary,
        indicatorColor = HindRedLight.copy(alpha = 0.5f)
      ),
      modifier = Modifier.testTag("bottom_nav_home")
    )

    // 2. All Tools
    NavigationBarItem(
      selected = currentScreen is ScreenDestination.AllTools,
      onClick = { onNavigate(ScreenDestination.AllTools) },
      icon = {
        Icon(
          imageVector = if (currentScreen is ScreenDestination.AllTools) Icons.Filled.GridView else Icons.Outlined.GridView,
          contentDescription = "All Tools",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Tools",
          fontWeight = if (currentScreen is ScreenDestination.AllTools) FontWeight.Bold else FontWeight.Normal,
          fontSize = 11.sp
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = HindRedPrimary,
        selectedTextColor = HindRedPrimary,
        indicatorColor = HindRedLight.copy(alpha = 0.5f)
      ),
      modifier = Modifier.testTag("bottom_nav_tools")
    )

    // 3. History & Cache
    NavigationBarItem(
      selected = currentScreen is ScreenDestination.History,
      onClick = { onNavigate(ScreenDestination.History) },
      icon = {
        BadgedBox(
          badge = {
            if (historyCount > 0) {
              Badge(
                containerColor = HindRedPrimary,
                contentColor = androidx.compose.ui.graphics.Color.White
              ) {
                Text(text = if (historyCount > 99) "99+" else "$historyCount", fontSize = 9.sp)
              }
            }
          }
        ) {
          Icon(
            imageVector = if (currentScreen is ScreenDestination.History) Icons.Filled.History else Icons.Outlined.History,
            contentDescription = "History",
            modifier = Modifier.size(24.dp)
          )
        }
      },
      label = {
        Text(
          text = "Activity",
          fontWeight = if (currentScreen is ScreenDestination.History) FontWeight.Bold else FontWeight.Normal,
          fontSize = 11.sp
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = HindRedPrimary,
        selectedTextColor = HindRedPrimary,
        indicatorColor = HindRedLight.copy(alpha = 0.5f)
      ),
      modifier = Modifier.testTag("bottom_nav_history")
    )

    // 4. Profile / Account
    NavigationBarItem(
      selected = currentScreen is ScreenDestination.Profile || currentScreen is ScreenDestination.Auth,
      onClick = {
        if (userProfile.uid == "guest_user") {
          onNavigate(ScreenDestination.Auth)
        } else {
          onNavigate(ScreenDestination.Profile)
        }
      },
      icon = {
        Icon(
          imageVector = if (currentScreen is ScreenDestination.Profile || currentScreen is ScreenDestination.Auth)
            Icons.Filled.Person
          else
            Icons.Outlined.Person,
          contentDescription = "Profile",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = if (userProfile.uid == "guest_user") "Log In" else "Account",
          fontWeight = if (currentScreen is ScreenDestination.Profile || currentScreen is ScreenDestination.Auth) FontWeight.Bold else FontWeight.Normal,
          fontSize = 11.sp
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = HindRedPrimary,
        selectedTextColor = HindRedPrimary,
        indicatorColor = HindRedLight.copy(alpha = 0.5f)
      ),
      modifier = Modifier.testTag("bottom_nav_profile")
    )
  }
}
