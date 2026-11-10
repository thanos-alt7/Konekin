package com.example.konekin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konekin.databinding.ActivityMainBinding
import com.example.konekin.model.Users
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val client = ApiClient.getInstance()
        val response = client.getAllUsers()

        response.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                val dataList = response.body()?.data ?: return
                val userList = ArrayList<String>()

                for (i in dataList) {
                    userList.add(i.employeeName)
                }

                val listAdapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    userList
                )
                binding.lvNama.adapter = listAdapter

                binding.lvNama.setOnItemClickListener { _, _, position, _ ->
                    val clickedEmployeeId = dataList[position].id
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("EMPLOYEE_ID", clickedEmployeeId)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity, "Koneksi error",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}