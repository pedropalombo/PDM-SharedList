package br.edu.scl.ifsp.sharedlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.sharedlist.R
import br.edu.scl.ifsp.sharedlist.databinding.TileTaskBinding
import br.edu.scl.ifsp.sharedlist.model.Task

class TaskAdapter (
    context: Context,
    private val taskList: MutableList<Task>
) : ArrayAdapter<Task>(context, R.layout.tile_task, taskList) {
    private lateinit var ttb: TileTaskBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task: Task = taskList[position]

        var tileTaskView = convertView
        if (tileTaskView == null) {
            // infla um nova célula
            ttb = TileTaskBinding.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            tileTaskView = ttb.root

            // criando um holder e guardando referência para os TextViews
            val ttvh = TileTaskViewHolder(ttb.nameTv, ttb.emailTv)

            // armazenando ViewHolder na célula
            tileTaskView.tag = ttvh
        }

        // substituir os valores
        with(tileTaskView.tag as TileTaskViewHolder) {
            nomeTv.text = task.name
            emailTv.text = task.email
        }

        return tileTaskView
    }

    private data class TileTaskViewHolder(
        val nomeTv: TextView,
        val emailTv: TextView
    )
}