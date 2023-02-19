package com.example.customview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list: MutableList<String> = ArrayList()

        list.add("")

        binding.customView.apply {
            setData(list)
        }

        binding.btnSubmit.setOnClickListener {
            Toast.makeText(this, "show result ".plus(binding.customView.getSelectedData().toString()), Toast.LENGTH_SHORT).show()
        }
    }
}