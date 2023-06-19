package br.edu.scl.ifsp.sharedlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.sharedlist.R
import br.edu.scl.ifsp.sharedlist.adapter.OnTaskClickListener
import br.edu.scl.ifsp.sharedlist.adapter.TaskRvAdapter
import br.edu.scl.ifsp.sharedlist.controller.TaskController
import br.edu.scl.ifsp.sharedlist.databinding.ActivityMainBinding

class MainActivity : BaseActivity(), OnTaskClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val taskList: MutableList<br.edu.scl.ifsp.sharedlist.model.Task> = mutableListOf()

    // Adapter
    private val taskAdapter: TaskRvAdapter by lazy {
        TaskRvAdapter(taskList, this)
    }

    private lateinit var tarl: ActivityResultLauncher<Intent>

    // Controller
    private val taskController: TaskController by lazy {
        TaskController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = getString(R.string.task_list)

        taskController.getTasks()
        amb.tasksRv.layoutManager = LinearLayoutManager(this)
        amb.tasksRv.adapter = taskAdapter

        tarl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_TASK, br.edu.scl.ifsp.sharedlist.model.Task::class.java)
                } else {
                    result.data?.getParcelableExtra(EXTRA_TASK)
                }
                task?.let { _task ->
                    val position = taskList.indexOfFirst { it.id == _task.id }
                    if (position != -1) {
                        taskList[position] = _task
                        taskController.editTask(_task)
                        Toast.makeText(this, "Atividade editada!", Toast.LENGTH_SHORT).show()
                    } else {
                        taskController.insertTask(_task)
                        Toast.makeText(this, "Atividade adicionada!", Toast.LENGTH_SHORT).show()
                    }
                    taskController.getTasks()
                    taskAdapter.notifyDataSetChanged()
                }
            }
        }

        //auto-refreshes taskList each 3s
        Thread {
            Thread.sleep(3000)
            taskController.getTasks()
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addTaskMi -> {
                tarl.launch(Intent(this, TaskActivity::class.java))
                true
            }

            R.id.refreshTasksMi -> {
                taskController.getTasks()
                true
            }

            else -> false
        }
    }

    fun updateTaskList(_taskList: MutableList<br.edu.scl.ifsp.sharedlist.model.Task>) {
        taskList.clear()
        taskList.addAll(_taskList)
        taskAdapter.notifyDataSetChanged()
    }

    // Funções que serão chamados sempre que uma célula for clicada no RecyclerView.
    // A associação entre célula e função será feita no TaskRvAdapter.
    override fun onTileTaskClick(position: Int) {
        val task = taskList[position]
        val taskIntent = Intent(this@MainActivity, TaskActivity::class.java)
        taskIntent.putExtra(EXTRA_TASK, task)
        taskIntent.putExtra(EXTRA_VIEW_TASK, true)
        startActivity(taskIntent)
    }

    override fun onEditMenuItemClick(position: Int) {
        val task = taskList[position]
        val taskIntent = Intent(this, TaskActivity::class.java)
        taskIntent.putExtra(EXTRA_TASK, task)
        tarl.launch(taskIntent)
    }

    override fun onRemoveMenuItemClick(position: Int) {
        val task = taskList[position]
        taskList.removeAt(position)
        taskController.removeTask(task)
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Atividade removida!", Toast.LENGTH_SHORT).show()
    }
}