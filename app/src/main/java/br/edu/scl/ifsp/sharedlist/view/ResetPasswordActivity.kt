package br.edu.scl.ifsp.sharedlist.view

import android.os.Bundle
import android.widget.Toast
import br.edu.scl.ifsp.sharedlist.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : BaseActivity() {

    private val arpb : ActivityResetPasswordBinding by lazy {
        ActivityResetPasswordBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(arpb.root)

        //when user press the "Reset" btn
        arpb.sendEmailBt.setOnClickListener {
            val email = arpb.recoveryPasswordEmailEt.text.toString() //getting input

            //sending reset password based on email input
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { resultado ->

                //if email exists & request worked
                if(resultado.isSuccessful) {
                    //shows success msg
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Email de recuperação enviado para $email",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish() //.. and finishes activity

                    //.. otherwise ..
                } else {
                    //shows failure msg and does nothing
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Falha no envio do email de recuperação.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}