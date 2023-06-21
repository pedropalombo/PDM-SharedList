package br.edu.scl.ifsp.sharedlist.adapter

interface OnTaskClickListener {
    fun onTileTaskClick(position: Int)
    fun onEditMenuItemClick(position: Int)
    fun onRemoveMenuItemClick(position: Int)
    fun onStatusMenuItemClick(position: Int)
}