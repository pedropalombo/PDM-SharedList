package br.edu.scl.ifsp.sharedlist.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.sharedlist.databinding.TileTaskBinding
import br.edu.scl.ifsp.sharedlist.model.Task

class TaskRvAdapter (
    private val taskList: MutableList<Task>,
    private val onTaskClickListener: OnTaskClickListener
) : RecyclerView.Adapter<TaskRvAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(tileTaskBinding: TileTaskBinding) :
        RecyclerView.ViewHolder(tileTaskBinding.root), View.OnCreateContextMenuListener {
        val nameTv: TextView = tileTaskBinding.nameTv
        val emailTv: TextView = tileTaskBinding.emailTv
        var taskPosition = -1 // Para saber qual célula foi clicada
        init {
            tileTaskBinding.root.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, 0, 0, "Editar")?.setOnMenuItemClickListener {
                if (taskPosition != -1) {
                    onTaskClickListener.onEditMenuItemClick(taskPosition)
                }
                true
            }
            menu?.add(Menu.NONE, 1, 1, "Remover")?.setOnMenuItemClickListener {
                if (taskPosition != -1) {
                    onTaskClickListener.onRemoveMenuItemClick(taskPosition)
                }
                true
            }
        }
    }

    // Chamada pelo LayoutManager para buscar a quantidade de dados e preparar a quantidade de células.
    override fun getItemCount(): Int = taskList.size

    // Chamada pelo LayoutManager para criar um nova célula (e consequentemente um novo ViewHolder).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Cria um nova célula
        val tileTaskBinding = TileTaskBinding.inflate(LayoutInflater.from(parent.context))

        // Cria um ViewHolder usando a célula
        val taskViewHolder = TaskViewHolder(tileTaskBinding)

        // Retorna o ViewHolder
        return taskViewHolder
    }

    // Chamada pelo LayoutManager para alterar os valores de uma célula
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Busca a task pela posição no data source
        val task = taskList[position]

        // Altera os valores da célula
        holder.nameTv.text = task.name
        holder.emailTv.text = task.email
        holder.taskPosition = position
        holder.itemView.setOnClickListener {
            onTaskClickListener.onTileTaskClick(position)
        }
    }
}