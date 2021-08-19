package com.example.graduationproject.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.R
import com.example.graduationproject.Utilites
import com.example.graduationproject.api.RetrofitClient
import com.example.graduationproject.models.Response
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import java.util.*
import java.util.stream.Collectors.toList


class MainActivity : AppCompatActivity() {

    val ApiKey= "5d9853aaeb074c20fa135b90080485b3f35d83566c23cf4f4fafc895e394d4a1"
    val ApiSecret="7f453dd1b6c12eb143ff59ac94f10df12dfdc24da08063e26e8b19092c528e01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            a() // throw new IOException();
        } catch (ex: Exception) {
            sendExceptionError(ex,this,"Testing Logic","IOException")
        }

        loginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        signUpButton.setOnClickListener {

            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val mobileNumber = mobileNumberEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword= confirmPasswordEditText.text.toString().trim()

            if(firstName.isEmpty()){
                firstNameEditText.error="first name should not be empty"
                firstNameEditText.requestFocus()
                return@setOnClickListener
            }

            if(lastName.isEmpty()){
                lastNameEditText.error="last name should not be empty"
                lastNameEditText.requestFocus()
                return@setOnClickListener
            }

            if(mobileNumber.isEmpty()){
                mobileNumberEditText.error="mobile number should not be empty"
                mobileNumberEditText.requestFocus()
                return@setOnClickListener
            }

            if(email.isEmpty()){
                emailEditText.error="Email should not be empty"
                emailEditText.requestFocus()
                return@setOnClickListener
            }


            if(password.isEmpty()){
                passwordEditText.error="password should not be empty"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if(confirmPassword.isEmpty()){
                confirmPasswordEditText.error="Re-type your password"
                confirmPasswordEditText.requestFocus()
                return@setOnClickListener
            }

            if(password != confirmPassword){
                confirmPasswordEditText.error="Passwords should match"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }


            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailEditText.error= "Enter a valid Email"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.retrofitInstance.signUp(
                firstName,
                lastName,
                email,
                mobileNumber,
                password
            )
                    .enqueue(object : Callback<Response> {
                        override fun onFailure(call: Call<Response>, t: Throwable) {
                            Toast.makeText(
                                applicationContext,
                                "error: " + t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<Response>,
                            response: retrofit2.Response<Response>
                        ) {
                            if (response.body()?.status == 1) {
                                Toast.makeText(
                                    applicationContext,
                                    "Registration Successful!\n" + response.body()?.token,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Registration Failed!",
                                    Toast.LENGTH_LONG
                                ).show()
                                //                       startActivity(Intent(this,LoginActivity::class.java))
                            }
                        }

                    })

        }
    }

    @kotlin.jvm.Throws(IOException::class)
    fun a() {
        b()
    }

    @kotlin.jvm.Throws(IOException::class)
    fun b() {
        throw IOException()
    }

    fun sendExceptionError(exception: Exception, context: Context, title: String, description: String){

        var mPackage = context.packageName
        var path=Utilites.getDataDir(context, mPackage)

        val stackTraceElements = exception.stackTrace

        var cleanStackTrace=stackTraceElements.filter { isNotFactoryPackage(it.className,mPackage) }
            //.map { getClassName(it.className) }

        var i = 0
        while (i < cleanStackTrace.size) {

            val elementClassName=cleanStackTrace[i].className.toString()
            if(isNotFactoryPackage(elementClassName,mPackage)) {

                var className= getClassName(cleanStackTrace[i].className)
                Log.e("Error",
                    " Exception thrown from line " + cleanStackTrace[i].lineNumber
                            + " from method " + cleanStackTrace[i].methodName
                            + " of Class " + className
                            + " from file " + cleanStackTrace[i].fileName + "\n")
            }
            i++
        }

        Log.i("cleanStackTrace is: ",cleanStackTrace.toString())

        RetrofitClient.retrofitInstance.Errors(ApiKey,
            ApiSecret,
            "00000000-0000-0000-0000-000000000000",
            0,
            stackTraceElements[cleanStackTrace.size-1].lineNumber,
            stackTraceElements[cleanStackTrace.size-1].fileName,
            path,
            description,
            title
        )
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "error: " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Successful", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(applicationContext, "Error occurred", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            })
    }

    fun getClassName(fullyQualifiedClassName: String): String{
        var className=fullyQualifiedClassName.split(".")
        return className[className.size-1]
    }

    fun isNotFactoryPackage(stackTraceElement: String, packageName: String): Boolean {
        return stackTraceElement.startsWith(packageName,true)
    }

}
