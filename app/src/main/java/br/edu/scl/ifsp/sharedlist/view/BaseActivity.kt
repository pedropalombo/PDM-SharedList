package br.edu.scl.ifsp.sharedlist.view

import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.sharedlist.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

sealed class BaseActivity : AppCompatActivity() {
    protected val EXTRA_TASK = "Task"
    protected val EXTRA_VIEW_TASK = "ViewTask"

    protected val googleSignInOptions: GoogleSignInOptions by lazy {
        val webApiKey = "930704137234-8ka2k6s5mbdemnim4ul0mdb6877i6dcf.apps.googleusercontent.com";
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webApiKey)
            .requestEmail()
            .build()
    }

    protected val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(this, googleSignInOptions)
    }
}
