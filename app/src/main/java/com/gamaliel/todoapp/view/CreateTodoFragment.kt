package com.gamaliel.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.gamaliel.todoapp.databinding.FragmentCreateTodoBinding
import com.gamaliel.todoapp.model.Todo
import com.gamaliel.todoapp.viewmodel.DetailTodoViewModel


class CreateTodoFragment : Fragment() {
    private lateinit var binding:FragmentCreateTodoBinding
    private lateinit var viewModel: DetailTodoViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTodoBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)

        binding.btnAdd.setOnClickListener {
            val radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
            var todo = Todo(
                binding.txtTitle.text.toString(),
                binding.txtNotes.text.toString(),
                radio.tag.toString().toInt()
            )
            viewModel.addTodo(todo)
            Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
            Navigation.findNavController(it).popBackStack()
        }


    }
}