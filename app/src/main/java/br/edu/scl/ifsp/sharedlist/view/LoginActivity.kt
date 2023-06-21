package br.edu.scl.ifsp.sharedlist.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.sharedlist.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : BaseActivity() {

    private val alb : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var gsarl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        //starting 'CreateAccount' activity on btn click
        alb.createAccountBt.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        //'' 'ResetPassword' ''
        alb.passwordResetBt.setOnClickListener {
            //sends over to ResetPassword's page
            // \-> Intent(where it's from, where it's going)
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        // '' 'Login' ''
        alb.loginBt.setOnClickListener {

            //getting inputs
            val email = alb.emailEt.text.toString()
            val password = alb.passwordEt.text.toString()

            //sending creatorEmail info to authenticator...
            // ...and if it works...
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener {
                //shows success msg
                Toast.makeText(
                    this,
                    "Usuário $email autenticado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()

                //and then starts 'Main'
                openMainActivity()

                //...otherwise ...
            }.addOnFailureListener {
                //send fail msg
                Toast.makeText(
                    this,
                    "Falha na autenticação do usuário.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Login Google
        alb.loginGoogleBt.setOnClickListener {
            val gsa: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
            if (gsa == null) {
                // Solicitar conta google
                gsarl.launch(googleSignInClient.signInIntent)
            } else {
                // Conta google logada
                openMainActivity()
            }
        }

        // Google Sign-in Result Launcher
        gsarl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val gsa: GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(gsa.idToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener {
                    Toast.makeText(this, "Usuário ${gsa.email} autenticado com sucesso", Toast.LENGTH_SHORT).show()
                    openMainActivity()
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Falha na autenticação do usuário ${gsa.email}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Falha na autenticação do usuário por meio da Google.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //when user goes back to app bar
    override fun onStart() {
        super.onStart()

        //if user is already logged in
        if(FirebaseAuth.getInstance().currentUser != null) {
            openMainActivity()
        }

    }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))   // goes straight to 'Main'

        //then closes 'Login' page
        finish()
    }
}