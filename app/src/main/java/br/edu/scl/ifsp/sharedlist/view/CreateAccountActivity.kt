package br.edu.scl.ifsp.sharedlist.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.sharedlist.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : AppCompatActivity() {

    private val acab : ActivityCreateAccountBinding by lazy {
        ActivityCreateAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acab.root)

        acab.createAccountBt.setOnClickListener {

            //getting values set as inputs
            val email = acab.emailEt.text.toString()
            val password = acab.passwordEt.text.toString()
            val password2 = acab.repeatPasswordEt.text.toString()

            //checking if passwords match
            if (password.equals(password2)) {

                //if its all good...
                //... it creates an account on Firebase
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    Toast.makeText(
                        this@CreateAccountActivity,
                        "Usuário $email criado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()

                    //then ends the activity since user has been created
                    finish()

                    //... otherwise ...
                }.addOnFailureListener {

                    //shows error message
                    Toast.makeText(
                        this@CreateAccountActivity,
                        "Erro na criação do usuário",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    this@CreateAccountActivity,
                    "Senhas não coincidem",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}