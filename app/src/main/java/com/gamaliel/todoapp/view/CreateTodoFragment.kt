package com.gamaliel.todoapp.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.gamaliel.todoapp.databinding.FragmentCreateTodoBinding
import com.gamaliel.todoapp.model.Todo
import com.gamaliel.todoapp.util.NotificationHelper
import com.gamaliel.todoapp.viewmodel.DetailTodoViewModel
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit


class CreateTodoFragment : Fragment(), RadioClickListener, TodoEditClickListener, DateClickListener,
                            TimeClickListener, DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    private lateinit var binding:FragmentCreateTodoBinding
    private lateinit var viewModel: DetailTodoViewModel

    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTodoBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),NotificationHelper.REQUEST_NOTIF)
            }
        }

        binding.todo = Todo("","",3,0,0)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
        binding.radiolistener = this
        binding.addlistener = this
        binding.datelistener = this
        binding.timelistener = this


        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode ==NotificationHelper.REQUEST_NOTIF) {
            if(grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                NotificationHelper(requireContext())
                    .createNotification("Todo Created",
                        "A new todo has been created! Stay focus!")
            }
        }
    }

    override fun onTodoEditClick(v: View) {
        val today = Calendar.getInstance()
        val c = Calendar.getInstance()
        c.set(year,month,day,hour,minute,0)

        val delay = (c.timeInMillis/1000L) - (today.timeInMillis/1000L)
        binding.todo!!.todo_date = (c.timeInMillis/1000L).toInt()

        val workRequest = OneTimeWorkRequestBuilder<NotificationHelper.TodoWorker>()
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to "Todo created",
                    "message" to "Stay focus"
                ))
            .build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)

        //slide 63
//        val radio = view?.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//        var todo = Todo(
//            binding.txtTitle.text.toString(),
//            binding.txtNotes.text.toString(),
//            radio?.tag.toString().toInt()
//        )
        viewModel.addTodo(binding.todo!!)
        Toast.makeText(view?.context, "Data added", Toast.LENGTH_LONG).show()
        Navigation.findNavController(v).popBackStack()
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()//ngambil priority dari tag yang ada di layout
    }

    override fun onDateClick(v: View) {
        val c = Calendar.getInstance()
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d  = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), this, y, m, d).show()
        //activity?.let { it1 -> DatePickerDialog(it1, this, year, month, day).show() }
    }

    override fun onTimeClick(v: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        Calendar.getInstance().let {
            it.set(year, month, day)
            binding.txtDate.setText(day.toString().padStart(2,'0') + "-" +
                    (month+1).toString().padStart(2,'0') + "-" + year)
            this.year = year
            this.month = month
            this.day = day
        }
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        binding.txtTime.setText(hour.toString().padStart(2,'0') + ":" +
                minute.toString().padStart(2,'0'))
        this.hour = hour
        this.minute = minute
    }

}