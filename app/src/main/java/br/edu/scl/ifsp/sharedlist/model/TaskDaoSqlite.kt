package br.edu.scl.ifsp.sharedlist.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.scl.ifsp.sharedlist.R

class TaskDaoSqlite (context: Context) : TaskDao {
    companion object Constants {
        private const val TASK_DATABASE_FILE = "tasks"
        private const val TASK_TABLE = "task"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "title"
        private const val ADDRESS_COLUMN = "description"
        private const val PHONE_COLUMN = "dateOfConclusion"
        private const val EMAIL_COLUMN = "creatorEmail"

        private const val CREATE_TASK_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $TASK_TABLE (" +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NAME_COLUMN TEXT NOT NULL, " +
                    "$ADDRESS_COLUMN TEXT NOT NULL, " +
                    "$PHONE_COLUMN TEXT NOT NULL, " +
                    "$EMAIL_COLUMN TEXT NOT NULL );"
    }

    // Referência para o banco de dados
    private val taskSqliteDatabase: SQLiteDatabase

    init {
        // Criando ou abrindo o banco
        taskSqliteDatabase =
            context.openOrCreateDatabase(TASK_DATABASE_FILE, Context.MODE_PRIVATE, null)
        try {
            taskSqliteDatabase.execSQL(CREATE_TASK_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun createTask(task: Task) {
        taskSqliteDatabase.insert(TASK_TABLE, null, task.toContentValues()).toInt()
    }

    override fun retrieveTask(id: Int): Task? {
        val cursor = taskSqliteDatabase.rawQuery(
            "SELECT * FROM $TASK_TABLE WHERE $ID_COLUMN = ?",
            arrayOf(id.toString())
        )

        val task = if (cursor.moveToFirst()) {
            // preencher task com dados da posição atual do cursor
            cursor.rowToContact()
        } else {
            null
        }
        cursor.close()

        return task
    }

    override fun retrieveTasks(): MutableList<Task> {
        val taskList = mutableListOf<Task>()

        val cursor = taskSqliteDatabase.rawQuery(
            "SELECT * FROM $TASK_TABLE ORDER BY $NAME_COLUMN",
            null
        )

        while (cursor.moveToNext()) {
            taskList.add(cursor.rowToContact())
        }
        cursor.close()

        return taskList
    }

    override fun updateTask(task: Task) =
        taskSqliteDatabase.update(
            TASK_TABLE,
            task.toContentValues(),
            "$ID_COLUMN = ?",
            arrayOf(task.id.toString())
        )

    override fun deleteTask(task: Task) =
        taskSqliteDatabase.delete(
            TASK_TABLE,
            "$ID_COLUMN = ?",
            arrayOf(task.id.toString())
        )

    private fun Task.toContentValues() = with(ContentValues()) {
        put(NAME_COLUMN, title)
        put(ADDRESS_COLUMN, description)
        put(PHONE_COLUMN, dateOfConclusion)
        put(EMAIL_COLUMN, creatorEmail)
        this
    }

    private fun Cursor.rowToContact() = Task(
        id = getInt(getColumnIndexOrThrow(ID_COLUMN)),
        title = getString(getColumnIndexOrThrow(NAME_COLUMN)),
        description = getString(getColumnIndexOrThrow(ADDRESS_COLUMN)),
        dateOfConclusion = getString(getColumnIndexOrThrow(PHONE_COLUMN)),
        creatorEmail = getString(getColumnIndexOrThrow(EMAIL_COLUMN)),
    )
}