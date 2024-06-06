package com.gamaliel.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gamaliel.todoapp.model.Todo
import com.gamaliel.todoapp.model.TodoDatabase
import com.gamaliel.todoapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailTodoViewModel(application: Application)
    : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    val todoLD = MutableLiveData<Todo>()

    fun addTodo(todo: Todo) { //bisa pake list
        launch {
            val db = buildDb(
                getApplication()
            )
            db.todoDao().insertAll(todo)
        }
    }

    fun fetch(uuid:Int) {
        launch {
            val db = buildDb(getApplication())
            todoLD.postValue(db.todoDao().selectTodo(uuid))
        }
    }

    fun update(todo: Todo) {
        launch {
            buildDb(getApplication()).todoDao().updateTodo(todo)
        }
    }



    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

}
