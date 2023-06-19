package br.edu.scl.ifsp.sharedlist.model

import com.google.firebase.ktx.Firebase

class TaskDaoRtDbFb  : TaskDao {

    //OBS: don't understand what something is/does ==> debugger

    private val TASK_LIST_ROOT_NODE = "taskList"  //setting node for taskList
    private val taskDaoRtDbFb = Firebase.database.getReference(TASK_LIST_ROOT_NODE)

    private val taskList : MutableList<Task> = mutableListOf()

    init {
        //CRUD for taskList node
        taskDaoRtDbFb.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task: Task? = snapshot.getValue<Task>()

                //checking task existence before adding
                task?.let { _task ->
                    if(!taskList.any{_task.name == it.name}) {
                        taskList.add(_task)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val task: Task? = snapshot.getValue<Task>()

                //checking task existence before editing it
                task?.let { _task ->
                    taskList[taskList.indexOfFirst { _task.name == it.name }] = _task
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val task: Task? = snapshot.getValue<Task>()

                //checking task existence before removing it
                task?.let { _task ->
                    taskList.remove(_task)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //whenever there's a change for a value on bd
        taskDaoRtDbFb.addListenerForSingleValueEvent(object : ValueEventListener {

            //when it's changed
            override fun onDataChange(snapshot : DataSnapshot) {
                val taskHashMap = snapshot.getValue<HashMap<String, Task>>()  //getting hashmap for task |-> since snapshot is an object full of objects

                taskList.clear() //clears list

                //and adds everything back as hashmap
                taskHashMap?.values?.forEach {
                    taskList.add(it)
                }
            }

            override fun onCancelled(error : DatabaseError) {
                // N/A
            }
        })

    }

    // =| GETTERS/SETTERS for tasks |=
    override fun createTask(task: Task) {
        createOrUpdateTask(task)
    }

    override fun retrieveTask(id: Int): Task? {
        //more of an example than an action function
        return taskList[taskList.indexOfFirst { id == it.id }]
    }

    override fun retrieveTasks(): MutableList<Task> {
        return taskList
    }

    override fun updateTask(task: Task): Int {
        createOrUpdateTask(task)
        return 1
    }

    override fun deleteTask(task: Task): Int {
        taskDaoRtDbFb.child(task.name).removeValue()
        return 1
    }
    // =| \ |=

    //if the name's not on the list, it'll add it
    //|-> if object has its name edited, a copy of it will be created with the new name
    private fun createOrUpdateTask(task : Task) {
        taskDaoRtDbFb.child(task.name).setValue(task)
    }

}