package com.example.graduationproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.graduationproject.R
import com.example.graduationproject.api.RetrofitClient
import com.example.graduationproject.models.Response
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        logInButton.setOnClickListener {

            val email = emailEditText.text.toString().trim()
            val mobileNumber = mobileNumberEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()


            if(email.isEmpty()) {
                emailEditText.requestFocus()
                emailEditText.error = "email can't be empty"
                return@setOnClickListener
            }

            if(mobileNumber.isEmpty()) {
                mobileNumberEditText.requestFocus()
                mobileNumberEditText.error = "email can't be empty"
                return@setOnClickListener
            }

            if(password.isEmpty()) {
                passwordEditText.requestFocus()
                passwordEditText.error = "email can't be empty"
                return@setOnClickListener
            }

            RetrofitClient.retrofitInstance.logIn(email, mobileNumber, password)
                .enqueue(object : Callback<Response> {
                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                        if(response.body()?.status==1){
                            // TODO: Launch the next activity and clear history while printing the success toast message
                            Toast.makeText(applicationContext,"successful",Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(applicationContext,response.body()?.status.toString(),
                                Toast.LENGTH_LONG).show()
                        }
                    }

                })



        }
    }


}
