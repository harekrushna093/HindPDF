package com.example.data.repository

import com.example.data.firebase.FirebaseSyncManager
import com.example.data.firebase.NetworkMonitor
import com.example.data.firebase.NotificationHelper
import com.example.data.local.ActivityLogDao
import com.example.data.local.ActivityLogEntity
import com.example.data.model.UserProfileData
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HindPdfRepository(
  private val activityLogDao: ActivityLogDao,
  private val firebaseSync: FirebaseSyncManager,
  private val notificationHelper: NotificationHelper,
  private val networkMonitor: NetworkMonitor
) {
  val activityLogs: Flow<List<ActivityLogEntity>> = activityLogDao.getAllActivityLogs()
  val authUserFlow: Flow<FirebaseUser?> = firebaseSync.authStateFlow()
  val isOnlineFlow: Flow<Boolean> = networkMonitor.isOnlineFlow

  fun observeCloudProfile(uid: String): Flow<UserProfileData?> =
    firebaseSync.observeUserProfile(uid)

  suspend fun recordActivity(toolName: String, fileName: String, outputSize: String = "1.2 MB", uid: String = "") {
    val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    val formattedTime = timeFormatter.format(Date())

    val log = ActivityLogEntity(
      toolName = toolName,
      fileName = fileName,
      timeFormatted = formattedTime,
      outputSize = outputSize
    )
    activityLogDao.insertLog(log)
    // Also sync to cloud firestore (handled offline automatically by Firestore disk cache)
    if (uid.isNotBlank()) {
      firebaseSync.syncActivityToCloud(uid, log)
    }
  }

  suspend fun clearHistory() {
    activityLogDao.clearAllLogs()
  }

  fun notifyTaskDone(toolName: String, fileName: String, isPro: Boolean) {
    notificationHelper.sendTaskCompletedNotification(toolName, fileName, isPro)
  }

  suspend fun updateCloudProfile(profile: UserProfileData) {
    firebaseSync.saveUserProfileToCloud(profile)
  }

  suspend fun signIn(email: String, pass: String) = firebaseSync.signInWithEmail(email, pass)
  suspend fun signUp(name: String, email: String, pass: String) = firebaseSync.signUpWithEmail(name, email, pass)
  suspend fun signInWithGoogle(emailOrToken: String, name: String) = firebaseSync.signInWithGoogle(emailOrToken, name)
  suspend fun signInWithFacebook(emailOrToken: String, name: String) = firebaseSync.signInWithFacebook(emailOrToken, name)
  suspend fun sendPasswordReset(email: String) = firebaseSync.sendPasswordReset(email)
  suspend fun updateName(name: String) = firebaseSync.updateDisplayName(name)
  suspend fun updatePassword(pass: String) = firebaseSync.updatePassword(pass)
  suspend fun deleteAccount() = firebaseSync.deleteAccount()
  fun signOut() = firebaseSync.signOut()
}
