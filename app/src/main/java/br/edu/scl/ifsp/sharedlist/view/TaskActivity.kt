package br.edu.scl.ifsp.sharedlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import br.edu.scl.ifsp.sharedlist.R
import br.edu.scl.ifsp.sharedlist.databinding.ActivityTaskBinding
import br.edu.scl.ifsp.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
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
                    titleEt.setText(title)
                    descriptionEt.setText(description)
                    dateOfConclusionEt.setText(dateOfConclusion)
                    creatorEmailEt.setText(creatorEmail)
                    dateOfCreationEt.setText(dateOfCreation)

                    // alterando visibilidade, se necessário
                    titleEt.isEnabled = false
                    descriptionEt.isEnabled = !viewTask
                    dateOfConclusionEt.isEnabled = !viewTask
                    saveBt.visibility = if (viewTask) View.GONE else View.VISIBLE
                }
            }
        } ?: run {
            val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale("pt", "BR"))
            val currentDate = dateFormat.format(Date())

            val user = FirebaseAuth.getInstance().currentUser
            val userEmail = user?.email

            with(atb) {
                creatorEmailEt.setText(userEmail)
                dateOfCreationEt.setText(currentDate)
            }
        }

        atb.saveBt.setOnClickListener {
            val task = Task(
                id = receivedTask?.id,
                title = atb.titleEt.text.toString(),
                description = atb.descriptionEt.text.toString(),
                dateOfConclusion = atb.dateOfConclusionEt.text.toString(),
                creatorEmail = atb.creatorEmailEt.text.toString(),
                dateOfCreation = atb.dateOfCreationEt.text.toString()
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