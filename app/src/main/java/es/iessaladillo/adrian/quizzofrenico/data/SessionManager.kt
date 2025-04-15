package es.iessaladillo.adrian.quizzofrenico.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    var userId: String?
        get() = prefs.getString("user_id", null)
        set(value) {
            prefs.edit { putString("user_id", value) }
        }

    fun logout() {
        userId = null
        FirebaseAuth.getInstance().signOut()
    }
}