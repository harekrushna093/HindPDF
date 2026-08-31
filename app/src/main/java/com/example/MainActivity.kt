package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.data.firebase.FirebaseSyncManager
import com.example.data.firebase.NetworkMonitor
import com.example.data.firebase.NotificationHelper
import com.example.data.local.HindPdfDatabase
import com.example.data.repository.HindPdfRepository
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.AppNavigationDrawer
import com.example.ui.components.DeleteAccountModal
import com.example.ui.components.PaywallModal
import com.example.ui.components.SignaturePadModal
import com.example.ui.components.TopNavBar
import com.example.ui.screens.AllToolsScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DownloadResultScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LegalScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ToolWorkbenchScreen
import com.example.ui.theme.HindPDFTheme
import com.example.ui.viewmodel.HindPdfViewModel
import com.example.ui.viewmodel.HindPdfViewModelFactory
import com.example.ui.viewmodel.ScreenDestination
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val repository by lazy {
    val database = HindPdfDatabase.getDatabase(applicationContext)
    val firebaseSync = FirebaseSyncManager()
    val notificationHelper = NotificationHelper(applicationContext)
    val networkMonitor = NetworkMonitor(applicationContext)
    HindPdfRepository(
      activityLogDao = database.activityLogDao(),
      firebaseSync = firebaseSync,
      notificationHelper = notificationHelper,
      networkMonitor = networkMonitor
    )
  }

  private val viewModel: HindPdfViewModel by viewModels {
    HindPdfViewModelFactory(repository)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      HindPDFTheme {
        HindPdfApp(
          viewModel = viewModel,
          repository = repository,
          onShowToast = { msg ->
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
          }
        )
      }
    }
  }
}

@Composable
fun HindPdfApp(
  viewModel: HindPdfViewModel,
  repository: HindPdfRepository,
  onShowToast: (String) -> Unit
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()
  val selectedTool by viewModel.selectedTool.collectAsState()
  val uploadedFiles by viewModel.uploadedFiles.collectAsState()
  val visualPages by viewModel.visualPages.collectAsState()
  val toolSettings by viewModel.toolSettings.collectAsState()
  val isProcessing by viewModel.isProcessing.collectAsState()
  val processingProgress by viewModel.processingProgress.collectAsState()
  val processingStatusText by viewModel.processingStatusText.collectAsState()
  val processingResult by viewModel.processingResult.collectAsState()
  val activityLogs by viewModel.activityLogs.collectAsState()
  val isOnline by viewModel.isOnline.collectAsState()

  val showPaywallModal by viewModel.showPaywallModal.collectAsState()
  val paywallFeatureTitle by viewModel.paywallFeatureTitle.collectAsState()
  val showSignatureModal by viewModel.showSignatureModal.collectAsState()
  val showDeleteAccountModal by viewModel.showDeleteAccountModal.collectAsState()

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  // Handle toast events
  LaunchedEffect(Unit) {
    viewModel.toastEvent.collect { message ->
      onShowToast(message)
    }
  }

  // Handle back presses
  BackHandler(enabled = currentScreen !is ScreenDestination.Home || drawerState.isOpen) {
    if (drawerState.isOpen) {
      scope.launch { drawerState.close() }
    } else {
      when (currentScreen) {
        is ScreenDestination.ToolWorkbench,
        is ScreenDestination.DownloadResult,
        is ScreenDestination.AllTools,
        is ScreenDestination.History,
        is ScreenDestination.Auth,
        is ScreenDestination.Profile,
        is ScreenDestination.PrivacyPolicy,
        is ScreenDestination.TermsOfService -> {
          viewModel.navigateTo(ScreenDestination.Home)
        }
        else -> {}
      }
    }
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      AppNavigationDrawer(
        userProfile = userProfile,
        currentScreen = currentScreen,
        isOnline = isOnline,
        onNavigate = { dest -> viewModel.navigateTo(dest) },
        onOpenPaywall = { viewModel.openPaywall("Navigation Drawer Upgrade") },
        onCloseDrawer = { scope.launch { drawerState.close() } },
        onSignOut = { viewModel.logout() }
      )
    }
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        TopNavBar(
          userProfile = userProfile,
          currentScreen = currentScreen,
          isOnline = isOnline,
          onOpenDrawer = {
            scope.launch {
              if (drawerState.isOpen) drawerState.close() else drawerState.open()
            }
          },
          onNavigate = { dest -> viewModel.navigateTo(dest) },
          onOpenPaywall = { viewModel.openPaywall("Navigation Bar Upgrade") }
        )
      },
      bottomBar = {
        AppBottomNavBar(
          currentScreen = currentScreen,
          historyCount = activityLogs.size,
          userProfile = userProfile,
          onNavigate = { dest -> viewModel.navigateTo(dest) }
        )
      },
      snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
      val modifier = Modifier.padding(innerPadding)

      when (val screen = currentScreen) {
        is ScreenDestination.Home -> {
          HomeScreen(
            userProfile = userProfile,
            onNavigateToTool = { toolId ->
              viewModel.navigateTo(ScreenDestination.ToolWorkbench(toolId))
            },
            onNavigateToAllTools = {
              viewModel.navigateTo(ScreenDestination.AllTools)
            },
            onOpenPaywall = { title ->
              viewModel.openPaywall(title)
            },
            modifier = modifier
          )
        }

        is ScreenDestination.AllTools -> {
          AllToolsScreen(
            onNavigateToTool = { toolId ->
              viewModel.navigateTo(ScreenDestination.ToolWorkbench(toolId))
            },
            modifier = modifier
          )
        }

        is ScreenDestination.History -> {
          HistoryScreen(
            activityLogs = activityLogs,
            isOnline = isOnline,
            onNavigateToTool = { toolId ->
              viewModel.navigateTo(ScreenDestination.ToolWorkbench(toolId))
            },
            onNavigateToAllTools = {
              viewModel.navigateTo(ScreenDestination.AllTools)
            },
            onClearHistory = {
              viewModel.clearActivityLogs()
            },
            onShowToast = onShowToast,
            modifier = modifier
          )
        }

        is ScreenDestination.ToolWorkbench -> {
          ToolWorkbenchScreen(
            tool = selectedTool,
            userProfile = userProfile,
            uploadedFiles = uploadedFiles,
            visualPages = visualPages,
            toolSettings = toolSettings,
            isProcessing = isProcessing,
            processingProgress = processingProgress,
            statusText = processingStatusText,
            onAddSampleDoc = { name, sizeMb, pageCount ->
              viewModel.addSampleDocument(name, sizeMb, pageCount)
            },
            onRemoveDoc = { id -> viewModel.removeFile(id) },
            onClearAllDocs = { viewModel.clearAllFiles() },
            onSortDocs = { viewModel.sortFilesAlphabetically() },
            onMovePage = { from, dir -> viewModel.movePage(from, dir) },
            onRotatePage = { idx -> viewModel.rotatePage(idx) },
            onToggleDeletePage = { idx -> viewModel.toggleDeletePage(idx) },
            onRotateAllPages = { deg -> viewModel.rotateAllPages(deg) },
            onUpdateSettings = { update -> viewModel.updateSettings(update) },
            onOpenSignatureDialog = { viewModel.openSignatureDialog() },
            onExecuteTool = { viewModel.executeTool() },
            onOpenPaywall = { title -> viewModel.openPaywall(title) },
            modifier = modifier
          )
        }

        is ScreenDestination.DownloadResult -> {
          DownloadResultScreen(
            result = processingResult,
            userProfile = userProfile,
            onFileNameChanged = { name -> viewModel.updateResultFileName(name) },
            onDownloadStandard = {
              onShowToast("📥 Saved ${processingResult?.fileName ?: "file.pdf"} to Downloads folder")
            },
            onDownloadPro = {
              onShowToast("⭐ Downloaded Studio Lossless HD 300+ DPI PDF!")
            },
            onShareLink = {
              onShowToast("🔗 Share link generated and copied to clipboard!")
            },
            onProcessAnother = {
              viewModel.navigateTo(ScreenDestination.ToolWorkbench(selectedTool.id))
            },
            onNavigateToTool = { toolId ->
              viewModel.navigateTo(ScreenDestination.ToolWorkbench(toolId))
            },
            onOpenPaywall = { title ->
              viewModel.openPaywall(title)
            },
            modifier = modifier
          )
        }

        is ScreenDestination.Profile -> {
          ProfileScreen(
            userProfile = userProfile,
            activityLogs = activityLogs,
            onUpdateName = { name -> viewModel.updateProfileName(name) },
            onChangePassword = { pass -> viewModel.changePassword(pass) },
            onToggleNotification = { enabled -> viewModel.toggleNotification(enabled) },
            onToggleZeroRetention = { enabled -> viewModel.toggleZeroRetention(enabled) },
            onTogglePromo = { enabled -> viewModel.togglePromoUpdates(enabled) },
            onOpenPaywall = { title -> viewModel.openPaywall(title) },
            onCancelPro = { viewModel.cancelProSubscription() },
            onClearHistory = { viewModel.clearActivityLogs() },
            onOpenDeleteModal = { viewModel.openDeleteAccountDialog() },
            onNavigateToAllTools = { viewModel.navigateTo(ScreenDestination.AllTools) },
            onSignOut = { viewModel.logout() },
            modifier = modifier
          )
        }

        is ScreenDestination.Auth -> {
          AuthScreen(
            repository = repository,
            onAuthSuccess = {
              onShowToast("Welcome to HindPDF!")
              viewModel.navigateTo(ScreenDestination.Home)
            },
            onContinueAsGuest = {
              viewModel.navigateTo(ScreenDestination.Home)
            },
            modifier = modifier
          )
        }

        is ScreenDestination.PrivacyPolicy -> {
          LegalScreen(isPrivacyPolicy = true, modifier = modifier)
        }

        is ScreenDestination.TermsOfService -> {
          LegalScreen(isPrivacyPolicy = false, modifier = modifier)
        }
      }
    }
  }

  // Modals / Overlays
  if (showPaywallModal) {
    PaywallModal(
      title = paywallFeatureTitle,
      onDismiss = { viewModel.closePaywall() },
      onActivateTrial = { viewModel.activateProTrial() }
    )
  }

  if (showSignatureModal) {
    SignaturePadModal(
      onDismiss = { viewModel.closeSignatureDialog() },
      onSaveSignature = { points -> viewModel.setSignaturePoints(points) }
    )
  }

  if (showDeleteAccountModal) {
    DeleteAccountModal(
      onDismiss = { viewModel.closeDeleteAccountDialog() },
      onConfirmDelete = { confirmText, pass ->
        viewModel.executeDeleteAccount(confirmText, pass)
      }
    )
  }
}
