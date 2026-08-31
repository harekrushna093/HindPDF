package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.ActivityLogEntity
import com.example.data.model.PdfTool
import com.example.data.model.ProcessingOutput
import com.example.data.model.ToolTier
import com.example.data.model.ToolsCatalog
import com.example.data.model.UploadedDoc
import com.example.data.model.UserProfileData
import com.example.data.model.VisualPdfPage
import com.example.data.repository.HindPdfRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

sealed class ScreenDestination {
  data object Home : ScreenDestination()
  data object AllTools : ScreenDestination()
  data object History : ScreenDestination()
  data class ToolWorkbench(val toolId: String) : ScreenDestination()
  data object DownloadResult : ScreenDestination()
  data object Auth : ScreenDestination()
  data object Profile : ScreenDestination()
  data object PrivacyPolicy : ScreenDestination()
  data object TermsOfService : ScreenDestination()
}

data class ToolSettingsState(
  val protectPassword: String = "",
  val protectPasswordConfirm: String = "",
  val watermarkText: String = "CONFIDENTIAL",
  val customTextStamp: String = "APPROVED DOCUMENT",
  val extractPageRange: String = "1-3",
  val hasSignature: Boolean = false,
  val signatureStrokePoints: List<List<Pair<Float, Float>>> = emptyList(),
  val globalRotation: Int = 0
)

class HindPdfViewModel(
  private val repository: HindPdfRepository
) : ViewModel() {

  private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Home)
  val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

  val isOnline: StateFlow<Boolean> = repository.isOnlineFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedTool = MutableStateFlow<PdfTool>(ToolsCatalog.allTools.first())
  val selectedTool: StateFlow<PdfTool> = _selectedTool.asStateFlow()

  private val _toolSettings = MutableStateFlow(ToolSettingsState())
  val toolSettings: StateFlow<ToolSettingsState> = _toolSettings.asStateFlow()

  private val _uploadedFiles = MutableStateFlow<List<UploadedDoc>>(emptyList())
  val uploadedFiles: StateFlow<List<UploadedDoc>> = _uploadedFiles.asStateFlow()

  private val _visualPages = MutableStateFlow<List<VisualPdfPage>>(emptyList())
  val visualPages: StateFlow<List<VisualPdfPage>> = _visualPages.asStateFlow()

  private val _isProcessing = MutableStateFlow(false)
  val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

  private val _processingProgress = MutableStateFlow(0)
  val processingProgress: StateFlow<Int> = _processingProgress.asStateFlow()

  private val _processingStatusText = MutableStateFlow("Ready")
  val processingStatusText: StateFlow<String> = _processingStatusText.asStateFlow()

  private val _processingResult = MutableStateFlow<ProcessingOutput?>(null)
  val processingResult: StateFlow<ProcessingOutput?> = _processingResult.asStateFlow()

  private val _userProfile = MutableStateFlow(UserProfileData())
  val userProfile: StateFlow<UserProfileData> = _userProfile.asStateFlow()

  val activityLogs: StateFlow<List<ActivityLogEntity>> = repository.activityLogs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _showPaywallModal = MutableStateFlow(false)
  val showPaywallModal: StateFlow<Boolean> = _showPaywallModal.asStateFlow()

  private val _paywallFeatureTitle = MutableStateFlow("HindPDF Pro Unlimited")
  val paywallFeatureTitle: StateFlow<String> = _paywallFeatureTitle.asStateFlow()

  private val _showSignatureModal = MutableStateFlow(false)
  val showSignatureModal: StateFlow<Boolean> = _showSignatureModal.asStateFlow()

  private val _showDeleteAccountModal = MutableStateFlow(false)
  val showDeleteAccountModal: StateFlow<Boolean> = _showDeleteAccountModal.asStateFlow()

  private val _toastEvent = MutableSharedFlow<String>()
  val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

  init {
    viewModelScope.launch {
      repository.authUserFlow.collect { firebaseUser ->
        if (firebaseUser != null) {
          _userProfile.value = _userProfile.value.copy(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "user@hindpdf.com",
            displayName = firebaseUser.displayName ?: "HindPDF Member",
            authProvider = if (firebaseUser.providerData.any { it.providerId == "google.com" }) "Google Account"
            else if (firebaseUser.providerData.any { it.providerId == "facebook.com" }) "Facebook Account"
            else "Email & Password"
          )
          // Observe Firestore with automatic offline cache support
          repository.observeCloudProfile(firebaseUser.uid).collect { cloudData ->
            if (cloudData != null) {
              _userProfile.value = cloudData
            }
          }
        }
      }
    }
  }

  fun navigateTo(destination: ScreenDestination) {
    if (destination is ScreenDestination.ToolWorkbench) {
      val tool = ToolsCatalog.getToolById(destination.toolId)
      _selectedTool.value = tool
      _uploadedFiles.value = emptyList()
      _visualPages.value = emptyList()
      _toolSettings.value = ToolSettingsState()
      _processingProgress.value = 0
      _isProcessing.value = false
    }
    _currentScreen.value = destination
  }

  fun onSearchQueryChanged(q: String) {
    _searchQuery.value = q
  }

  fun addSampleDocument(name: String, sizeMb: Double, pageCount: Int) {
    val newDoc = UploadedDoc(
      id = UUID.randomUUID().toString(),
      name = name,
      sizeFormatted = "${String.format("%.2f", sizeMb)} MB",
      rawBytesCount = (sizeMb * 1024 * 1024).toLong(),
      pageCount = pageCount
    )
    _uploadedFiles.value = _uploadedFiles.value + newDoc

    // Generate visual pages for preview & rearrangement
    val pages = mutableListOf<VisualPdfPage>()
    _uploadedFiles.value.forEach { doc ->
      for (p in 1..doc.pageCount) {
        pages.add(
          VisualPdfPage(
            uid = "page_${doc.id}_$p",
            fileId = doc.id,
            fileName = doc.name,
            pageNumber = p
          )
        )
      }
    }
    _visualPages.value = pages
    emitToast("Added ${newDoc.name}")
  }

  fun removeFile(docId: String) {
    _uploadedFiles.value = _uploadedFiles.value.filterNot { it.id == docId }
    _visualPages.value = _visualPages.value.filterNot { it.fileId == docId }
  }

  fun clearAllFiles() {
    _uploadedFiles.value = emptyList()
    _visualPages.value = emptyList()
  }

  fun sortFilesAlphabetically() {
    _uploadedFiles.value = _uploadedFiles.value.sortedBy { it.name }
    val pages = mutableListOf<VisualPdfPage>()
    _uploadedFiles.value.forEach { doc ->
      for (p in 1..doc.pageCount) {
        pages.add(
          VisualPdfPage(
            uid = "page_${doc.id}_$p",
            fileId = doc.id,
            fileName = doc.name,
            pageNumber = p
          )
        )
      }
    }
    _visualPages.value = pages
    emitToast("Sorted documents A-Z")
  }

  fun movePage(fromIndex: Int, direction: Int) {
    val toIndex = fromIndex + direction
    val current = _visualPages.value.toMutableList()
    if (toIndex in 0 until current.size) {
      val item = current.removeAt(fromIndex)
      current.add(toIndex, item)
      _visualPages.value = current
    }
  }

  fun rotatePage(index: Int) {
    val current = _visualPages.value.toMutableList()
    if (index in 0 until current.size) {
      val item = current[index]
      current[index] = item.copy(rotationDegrees = (item.rotationDegrees + 90) % 360)
      _visualPages.value = current
    }
  }

  fun toggleDeletePage(index: Int) {
    val current = _visualPages.value.toMutableList()
    if (index in 0 until current.size) {
      val item = current[index]
      current[index] = item.copy(isDeleted = !item.isDeleted)
      _visualPages.value = current
    }
  }

  fun rotateAllPages(degrees: Int = 90) {
    _visualPages.value = _visualPages.value.map {
      it.copy(rotationDegrees = (it.rotationDegrees + degrees) % 360)
    }
    emitToast("Rotated all pages +$degrees°")
  }

  fun updateSettings(update: (ToolSettingsState) -> ToolSettingsState) {
    _toolSettings.value = update(_toolSettings.value)
  }

  fun setSignaturePoints(points: List<List<Pair<Float, Float>>>) {
    _toolSettings.value = _toolSettings.value.copy(
      signatureStrokePoints = points,
      hasSignature = points.isNotEmpty()
    )
    _showSignatureModal.value = false
    emitToast("Digital signature saved!")
  }

  fun openPaywall(feature: String = "HindPDF Pro Unlimited") {
    _paywallFeatureTitle.value = feature
    _showPaywallModal.value = true
  }

  fun closePaywall() {
    _showPaywallModal.value = false
  }

  fun openSignatureDialog() {
    _showSignatureModal.value = true
  }

  fun closeSignatureDialog() {
    _showSignatureModal.value = false
  }

  fun openDeleteAccountDialog() {
    _showDeleteAccountModal.value = true
  }

  fun closeDeleteAccountDialog() {
    _showDeleteAccountModal.value = false
  }

  fun activateProTrial() {
    _userProfile.value = _userProfile.value.copy(isPro = true)
    _showPaywallModal.value = false
    viewModelScope.launch {
      repository.updateCloudProfile(_userProfile.value)
    }
    emitToast("👑 Welcome to HindPDF Pro! Studio status activated.")
  }

  fun cancelProSubscription() {
    _userProfile.value = _userProfile.value.copy(isPro = false)
    viewModelScope.launch {
      repository.updateCloudProfile(_userProfile.value)
    }
    emitToast("Subscription cancelled. Reverted to Free Tier.")
  }

  fun toggleNotification(enabled: Boolean) {
    _userProfile.value = _userProfile.value.copy(notificationEnabled = enabled)
    viewModelScope.launch { repository.updateCloudProfile(_userProfile.value) }
    emitToast(if (enabled) "Notifications enabled!" else "Notifications disabled.")
  }

  fun toggleZeroRetention(enabled: Boolean) {
    _userProfile.value = _userProfile.value.copy(zeroRetentionEnabled = enabled)
    viewModelScope.launch { repository.updateCloudProfile(_userProfile.value) }
    emitToast("Zero Client Retention updated.")
  }

  fun togglePromoUpdates(enabled: Boolean) {
    _userProfile.value = _userProfile.value.copy(promoUpdatesEnabled = enabled)
    viewModelScope.launch { repository.updateCloudProfile(_userProfile.value) }
    emitToast("Product updates preference saved.")
  }

  fun updateProfileName(newName: String) {
    if (newName.isBlank()) return
    _userProfile.value = _userProfile.value.copy(displayName = newName)
    viewModelScope.launch {
      repository.updateName(newName)
      repository.updateCloudProfile(_userProfile.value)
      emitToast("Display name updated!")
    }
  }

  fun changePassword(newPass: String) {
    if (newPass.length < 6) {
      emitToast("Password must be at least 6 characters.")
      return
    }
    viewModelScope.launch {
      val res = repository.updatePassword(newPass)
      if (res.isSuccess) emitToast("Password updated successfully!")
      else emitToast("Failed: ${res.exceptionOrNull()?.message}")
    }
  }

  fun executeDeleteAccount(confirmText: String, pass: String) {
    if (confirmText != "DELETE") {
      emitToast("Verification failed: Type 'DELETE' in all caps.")
      return
    }
    viewModelScope.launch {
      repository.deleteAccount()
      _userProfile.value = UserProfileData()
      _showDeleteAccountModal.value = false
      _currentScreen.value = ScreenDestination.Home
      emitToast("Account deleted.")
    }
  }

  fun executeTool() {
    val tool = _selectedTool.value
    if (_uploadedFiles.value.isEmpty()) {
      emitToast("Please select or add at least one document.")
      return
    }

    if (tool.tier == ToolTier.PRO && !_userProfile.value.isPro) {
      openPaywall("${tool.name} (Pro Only)")
      return
    }

    if (!_userProfile.value.isPro && _userProfile.value.dailyUsageCount >= _userProfile.value.dailyLimit) {
      openPaywall("Daily Conversion Limit Exceeded")
      return
    }

    val primaryDoc = _uploadedFiles.value.first()
    val baseName = primaryDoc.name.substringBeforeLast(".")
    val outputName = "${tool.filePrefix}${baseName}${tool.fileExt}"

    viewModelScope.launch {
      _isProcessing.value = true
      _processingProgress.value = 10
      _processingStatusText.value = "Analyzing Document Streams..."
      delay(300)

      _processingProgress.value = 35
      _processingStatusText.value = "Executing ${tool.name} Engine..."
      delay(400)

      _processingProgress.value = 70
      _processingStatusText.value = "Optimizing 256-bit Vector Clarity..."
      delay(350)

      _processingProgress.value = 95
      _processingStatusText.value = "Finalizing Output File..."
      delay(200)

      _processingProgress.value = 100
      _processingStatusText.value = "Completed!"
      _isProcessing.value = false

      // Calculate sizes
      val stdSize = (primaryDoc.rawBytesCount * 0.72).toLong().coerceAtLeast(120 * 1024)
      val proSize = (primaryDoc.rawBytesCount * 0.45).toLong().coerceAtLeast(85 * 1024)

      val result = ProcessingOutput(
        toolId = tool.id,
        fileName = outputName,
        standardSizeBytes = stdSize,
        proSizeBytes = proSize,
        formatCategory = when {
          tool.fileExt.contains("zip") || tool.fileExt.contains("jpg") || tool.fileExt.contains("png") -> "image"
          tool.fileExt.contains("txt") || tool.fileExt.contains("csv") -> "text"
          else -> "pdf"
        },
        sampleText = "HindPDF Document Extracted Contents:\n--- Section 1: Standard Verified ---\nAll PDF objects, annotations, and vector fonts processed securely with zero server retention.",
        pageCount = _visualPages.value.count { !it.isDeleted }.coerceAtLeast(1)
      )
      _processingResult.value = result

      // Update daily usage
      val newCount = _userProfile.value.dailyUsageCount + 1
      _userProfile.value = _userProfile.value.copy(dailyUsageCount = newCount)
      repository.updateCloudProfile(_userProfile.value)

      // Save to Room DB & Cloud Firestore (with offline queue)
      val sizeFormatted = "${String.format("%.2f", stdSize.toDouble() / (1024 * 1024))} MB"
      repository.recordActivity(tool.name, outputName, sizeFormatted, _userProfile.value.uid)

      // Send Push Notification
      if (_userProfile.value.notificationEnabled) {
        repository.notifyTaskDone(tool.name, outputName, _userProfile.value.isPro)
      }

      _currentScreen.value = ScreenDestination.DownloadResult
    }
  }

  fun updateResultFileName(newName: String) {
    val current = _processingResult.value ?: return
    val ext = _selectedTool.value.fileExt
    val clean = if (newName.endsWith(ext)) newName else "$newName$ext"
    _processingResult.value = current.copy(fileName = clean)
  }

  fun clearActivityLogs() {
    viewModelScope.launch {
      repository.clearHistory()
      emitToast("Activity history cleared.")
    }
  }

  fun logout() {
    repository.signOut()
    _userProfile.value = UserProfileData()
    _currentScreen.value = ScreenDestination.Home
    emitToast("Signed out successfully.")
  }

  private fun emitToast(msg: String) {
    viewModelScope.launch {
      _toastEvent.emit(msg)
    }
  }
}

class HindPdfViewModelFactory(
  private val repository: HindPdfRepository
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(HindPdfViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return HindPdfViewModel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
