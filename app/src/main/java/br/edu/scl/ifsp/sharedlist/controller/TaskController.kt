package br.edu.scl.ifsp.sharedlist.controller

import br.edu.scl.ifsp.sharedlist.model.Task
import br.edu.scl.ifsp.sharedlist.model.TaskDao
import br.edu.scl.ifsp.sharedlist.model.TaskDaoRtDbFb
import br.edu.scl.ifsp.sharedlist.view.MainActivity

class TaskController (private val mainActivity: MainActivity) {

    //using Firebase node as a db implementer
    private val taskDaoImpl:TaskDao = TaskDaoRtDbFb()

    fun insertTask(task: Task) {
        Thread {
            taskDaoImpl.createTask(task)
        }.start()
    }

    fun getTask(id: Int) = taskDaoImpl.retrieveTask(id)
    fun getTasks() {
        Thread {
            val list = taskDaoImpl.retrieveTasks()
            mainActivity.runOnUiThread {
                mainActivity.updateTaskList(list)
            }
        }.start()
    }

    fun editTask(task: Task) {
        Thread {
            taskDaoImpl.updateTask(task)
        }.start()
    }

    fun removeTask(task: Task) {
        Thread {
            taskDaoImpl.deleteTask(task)
        }
    }
}