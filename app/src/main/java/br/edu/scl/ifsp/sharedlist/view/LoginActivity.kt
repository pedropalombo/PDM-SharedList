package br.edu.scl.ifsp.sharedlist.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.sharedlist.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val alb : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

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