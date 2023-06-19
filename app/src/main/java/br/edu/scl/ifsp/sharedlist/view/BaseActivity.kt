package br.edu.scl.ifsp.sharedlist.view

import androidx.appcompat.app.AppCompatActivity

sealed class BaseActivity : AppCompatActivity() {
    protected val EXTRA_TASK = "Task"
    protected val EXTRA_VIEW_TASK = "ViewTask"
}
