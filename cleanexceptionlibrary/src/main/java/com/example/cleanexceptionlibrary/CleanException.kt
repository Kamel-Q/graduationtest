package com.example.cleanexceptionlibrary

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import android.util.Log
import android.widget.Toast
import com.example.cleanexceptionlibrary.api.RetrofitClient

object CleanException {

    val ApiKey= "5d9853aaeb074c20fa135b90080485b3f35d83566c23cf4f4fafc895e394d4a1"
    val ApiSecret="7f453dd1b6c12eb143ff59ac94f10df12dfdc24da08063e26e8b19092c528e01"

    fun sendExceptionError(exception: Exception, context: Context, title: String, description: String){

        var mPackage = context.packageName
        var path=Utilities.getDataDir(context, mPackage)

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
                        context.applicationContext,
                        "error: " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context.applicationContext, "Successful", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context.applicationContext, "Error occurred", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            })
    }

    fun getClassName(fullyQualifiedClassName: String): String{
        val className=fullyQualifiedClassName.split(".")
        return className[className.size-1]
    }

    fun isNotFactoryPackage(stackTraceElement: String, packageName: String): Boolean {
        return stackTraceElement.startsWith(packageName,true)
    }
}