package com.lark.waterripplediffuseview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lark.diffuseview.widget.listener.OnCoreImageClickListener
import com.lark.waterripplediffuseview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        binding.diffuseView.setOnCoreImageClickListener(object : OnCoreImageClickListener {
            override fun onClickCoreImage(view: View) {
                Toast.makeText(this@MainActivity, "点击了中心图片", Toast.LENGTH_SHORT).show()
            }

        })
        binding.diffuseView.start()

    }

}