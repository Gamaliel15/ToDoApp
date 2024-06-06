package com.gamaliel.todoapp.view

import android.view.View
import android.widget.CompoundButton
import com.gamaliel.todoapp.model.Todo

interface TodoCheckChangeListener{
    fun onTodoCheckChange(cb:CompoundButton, isChecked:Boolean, todo: Todo)
}

interface TodoEditClickListener{
    fun onTodoEditClick(v:View)
}

interface RadioClickListener{
    fun onRadioClick(v:View)
}