package com.cdsy.aichat.manager.api

import android.content.Context
import android.content.Intent
import com.cdsy.aichat.util.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.reactivex.Single

class GoogleSignInService(private val context: Context) {

    companion object {
        const val RC_SIGN_IN = 9001
    }

    private lateinit var googleSignInClient: GoogleSignInClient

    init {
        setupGoogleSignIn()
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // 与旧项目保持一致：要 idToken + email + id
            .requestIdToken(Constants.GOOGLE_CLIENT_ID)
            .requestEmail()
            .requestId()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun handleSignInResult(data: Intent?): Single<GoogleSignInAccount> {
        return Single.create { emitter ->
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                emitter.onSuccess(account)
            } catch (e: ApiException) {
                emitter.onError(e)
            }
        }
    }

    fun signOut(): Single<Unit> {
        return Single.create { emitter ->
            googleSignInClient.signOut()
                .addOnCompleteListener {
                    emitter.onSuccess(Unit)
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    fun getCurrentAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(context) != null
    }
}
