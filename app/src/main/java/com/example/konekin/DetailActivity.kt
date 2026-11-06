package com.example.konekin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konekin.databinding.ActivityDetailBinding
import com.example.konekin.model.UserDetail
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val employeeId = intent.getIntExtra("EMPLOYEE_ID", -1)

        if (employeeId != -1) {
            getEmployeeDetail(employeeId)
        } else {
            Toast.makeText(this, "ID Employee tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getEmployeeDetail(id: Int) {
        val client = ApiClient.getInstance()
        client.getEmployeeById(id).enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.isSuccessful && response.body() != null) {
                    val userDetail = response.body()!!
                    val employee = userDetail.data

                    binding.tvStatus.text = "Status: ${userDetail.status}"
                    binding.tvMessage.text = "Message: ${userDetail.message ?: "-"}"

                    if (employee != null) {
                        binding.tvId.text = "ID: ${employee.id}"
                        binding.tvName.text = "Name: ${employee.employeeName}"
                        binding.tvSalary.text = "Salary: $${employee.employeeSalary}"
                        binding.tvAge.text = "Age: ${employee.employeeAge}"
                    }
                } else {
                    Toast.makeText(this@DetailActivity, "Gagal mengambil data detail", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Koneksi Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}