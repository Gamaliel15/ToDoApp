package com.gamaliel.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamaliel.todoapp.R
import com.gamaliel.todoapp.databinding.FragmentTodoListBinding
import com.gamaliel.todoapp.viewmodel.ListTodoViewModel

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding
    private lateinit var viewModel: ListTodoViewModel
    private var adapter = TodoListAdapter(arrayListOf(), { todo -> viewModel.markTaskAsDone(todo) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListTodoViewModel::class.java)
        viewModel.refresh()
        binding.recViewTodo.layoutManager = LinearLayoutManager(context)
        binding.recViewTodo.adapter = adapter

        binding.btnFAB.setOnClickListener {
            val action = TodoListFragmentDirections.actionCreateTodo()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            adapter.updateTodoList(it)
            if (it.isEmpty()) {
                binding.recViewTodo.visibility = View.GONE
                binding.txtError.text = "Your todo list is empty."
                binding.txtError.visibility = View.VISIBLE
            } else {
                binding.recViewTodo.visibility = View.VISIBLE
                binding.txtError.visibility = View.GONE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.todoLoadErrorLD.observe(viewLifecycleOwner, Observer {
            binding.txtError.visibility = if (it) View.VISIBLE else View.GONE
            binding.txtError.text = "Error loading todos."
        })
    }
}
