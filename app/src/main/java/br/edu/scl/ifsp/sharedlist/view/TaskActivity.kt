package br.edu.scl.ifsp.sharedlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import br.edu.scl.ifsp.sharedlist.R
import br.edu.scl.ifsp.sharedlist.databinding.ActivityTaskBinding
import br.edu.scl.ifsp.sharedlist.model.Task
import java.util.*


class TaskActivity : BaseActivity() {
    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atb.root)
        supportActionBar?.subtitle = getString(R.string.task_info)

        // Se receber uma task da MainActivity, preenche os campos do formulário
        val receivedTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TASK)
        }
        receivedTask?.let { _receivedTask ->
            val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
            with(atb) {
                with(_receivedTask) {
                    nameEt.setText(name)
                    addressEt.setText(address)
                    phoneEt.setText(phone)
                    emailEt.setText(email)

                    // alterando visibilidade, se necessário
                    nameEt.isEnabled = !viewTask
                    addressEt.isEnabled = !viewTask
                    phoneEt.isEnabled = !viewTask
                    emailEt.isEnabled = !viewTask
                    saveBt.visibility = if (viewTask) View.GONE else View.VISIBLE
                }
            }
        }

        atb.saveBt.setOnClickListener {
            val task = Task(
                id = receivedTask?.id,
                name = atb.nameEt.text.toString(),
                address = atb.addressEt.text.toString(),
                phone = atb.phoneEt.text.toString(),
                email = atb.emailEt.text.toString()
            )

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_TASK, task)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun generateId(): Int {
        val random = Random(System.currentTimeMillis())
        return random.nextInt()
    }
}