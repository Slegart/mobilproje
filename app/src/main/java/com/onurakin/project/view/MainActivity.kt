package com.sefikonurakin_hw2.view

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.SeekBar
import android.widget.TextView
import androidx.room.Room
import com.sefikonurakin_hw2.databinding.ActivityMainBinding
import com.sefikonurakin_hw2.db.Tasks
import com.sefikonurakin_hw2.db.TaskRoomDatabase
import com.sefikonurakin_hw2.util.Constants
import com.google.android.material.snackbar.Snackbar
import com.sefikonurakin_hw2.R
import com.sefikonurakin_hw2.adapter.CustomRecyclerViewAdapter
import java.util.Collections


class MainActivity : AppCompatActivity(), CustomRecyclerViewAdapter.OnTaskItemClickListener {

    lateinit var binding:ActivityMainBinding
    lateinit var customDialog: Dialog

    var DeletedTasks = mutableListOf<Tasks>()

    var TaskTemp =""
    var DateTemp =""
    var PriorityTemp = 0

    var adapter: CustomRecyclerViewAdapter?=null

    private val TaskDB: TaskRoomDatabase by lazy {
        Room.databaseBuilder(this, TaskRoomDatabase::class.java, Constants.DATABASENAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    //date should be date type
    var Task1 = Tasks(1,"asd","10.23.2021",1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerCustomer.setLayoutManager(LinearLayoutManager(this))

        getData()

        binding.apply {
            fabAdd.setOnClickListener{

                var TasktoAdd = Tasks(666,"Asya","10.23.2021",1)

                CreateDialog(TasktoAdd,"Add")

                TaskDB.TasksDAO().insertTask(TasktoAdd)

                Snackbar.make(it, "Customer inserted", Snackbar.LENGTH_LONG).show()
                getData()
            }

            ReturnButton.setOnClickListener {
                if (DeletedTasks.isNotEmpty()) {

                    // Create an Intent to start the second activity
                    val intent = Intent(this@MainActivity, SecondActivity::class.java)

                    // Add the deleted tasks to the intent as a ParcelableArrayList
                    intent.putParcelableArrayListExtra("deletedTasks", DeletedTasks as ArrayList<Tasks>)

                    // Start the second activity
                    startActivity(intent)
                }
            }






        }


    }

    fun CreateDialog(task: Tasks, operation: String) {
        customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog)

        var taskName = customDialog.findViewById<TextView>(R.id.TaskNameDialog)
        var DateDialog = customDialog.findViewById<TextView>(R.id.DateDialog)
        var PrioritySeekBar = customDialog.findViewById<SeekBar>(R.id.Priority)

        var btnSave = customDialog.findViewById<TextView>(R.id.btnSave)
        var btnCancel = customDialog.findViewById<TextView>(R.id.btnCancel)
        var btnDelete = customDialog.findViewById<TextView>(R.id.Delete_button)

        taskName.text = task.TaskName
        DateDialog.text = task.Date
        PrioritySeekBar.progress = task.Priority

        btnDelete.setOnClickListener {
            DeletedTasks.add(task)
            TaskDB.TasksDAO().deleteTask(task)
            getData()
            customDialog!!.dismiss()
        }

        btnSave.setOnClickListener {
            val taskNameText = taskName.text.toString().trim()
            val dateText = DateDialog.text.toString().trim()

            if (isValidTaskName(taskNameText) && isValidDateFormat(dateText)) {
                if (operation == "Add") {
                    TaskDB.TasksDAO().insertTask(Tasks(0, taskNameText, dateText, PrioritySeekBar.progress))
                    Snackbar.make(it, "Task inserted", Snackbar.LENGTH_LONG).show()
                } else {
                    TaskTemp = taskNameText
                    DateTemp = dateText
                    PriorityTemp = PrioritySeekBar.progress
                    TaskDB.TasksDAO().updateTask(Tasks(task.id, TaskTemp, DateTemp, PriorityTemp))
                    Snackbar.make(it, "Task updated", Snackbar.LENGTH_LONG).show()
                }
                getData()
                customDialog!!.dismiss()
            } else {
                Snackbar.make(it, "Invalid input. Please check task name and date format.", Snackbar.LENGTH_LONG).show()
            }
        }

        btnCancel.setOnClickListener {
            customDialog!!.dismiss()
        }

        customDialog.show()
    }

    private fun isValidTaskName(name: String): Boolean {
        // Check if the task name contains only letters and spaces
        return name.matches(Regex("^[a-zA-Z ]+\$"))
    }

    private fun isValidDateFormat(date: String): Boolean {
        // Check if the date has the format "01.01.0101"
        return date.matches(Regex("^\\d{2}\\.\\d{2}\\.\\d{4}\$"))
    }


    private fun displayTasks(Tasks: MutableList<Tasks>) {
        adapter = CustomRecyclerViewAdapter(this, Tasks, this)

        binding.recyclerCustomer.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    fun getData(){
        if(TaskDB.TasksDAO().getAllTasks().isNotEmpty()){
            displayTasks(TaskDB.TasksDAO().getAllTasks())
        }
        else{
            binding.recyclerCustomer.adapter = null
        }
    }
    fun prepareData(){
        var customers=ArrayList<Tasks>()
        Collections.addAll(customers,
            Task1,
            Tasks(148, "task 1", "10.10.2023", 2),
            Tasks(897, "task 2", "11.11.2023", 3),
            Tasks(333, "task 3", "12.12.2023", 1))

        TaskDB.TasksDAO().insertAllTasks(customers)

    }

    override fun onTaskItemClick(task: Tasks , operation: String) {
        // Call CreateDialog with the selected task object
        CreateDialog(task,operation)
    }
}