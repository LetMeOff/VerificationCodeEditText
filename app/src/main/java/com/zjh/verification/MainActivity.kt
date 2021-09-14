package com.zjh.verification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zjh.verification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        //输入完成监听
        binding.verificationEdit.setOnEditCompleteListener(object :
            VerificationCodeEditText.OnEditCompleteListener {
            override fun onEditComplete(text: String) {
                Log.d(TAG, "输入完成 : $text")
            }
        })

    }

    companion object{
        const val TAG = "VerificationEdit"
    }
}