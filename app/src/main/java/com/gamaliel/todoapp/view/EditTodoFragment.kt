package com.gamaliel.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gamaliel.todoapp.R
import com.gamaliel.todoapp.databinding.FragmentCreateTodoBinding
import com.gamaliel.todoapp.databinding.FragmentEditTodoBinding
import com.gamaliel.todoapp.model.Todo
import com.gamaliel.todoapp.viewmodel.DetailTodoViewModel


class EditTodoFragment : Fragment(),RadioClickListener, TodoEditClickListener {

    private lateinit var binding: FragmentEditTodoBinding
    private lateinit var viewModel: DetailTodoViewModel
    private lateinit var todo:Todo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTodoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)

        binding.txtJudulTodo.text = "Edit Todo"
        binding.btnAdd.text = "Save Changes"

        val uuid = EditTodoFragmentArgs.fromBundle(requireArguments()).uuid
        viewModel.fetch(uuid)

        binding.radiolistener = this
        binding.submitlistener = this

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            binding.todo = it
//            binding.txtTitle.setText(it.title)
//            binding.txtNotes.setText(it.notes)
//
//            when(it.priority){
//                1 -> binding.radioLow.isChecked = true
//                2 -> binding.radioMedium.isChecked = true
//                3 -> binding.radioHigh.isChecked = true //         }
        })
    }

    override fun onRadioClick(v: View) {

        binding.todo!!.priority = v.tag.toString().toInt()//ngambil priority dari tag yang ada di layout

    }

    override fun onTodoEditClick(v: View) {
        viewModel.update(binding.todo!!)
        Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT)
    }


}