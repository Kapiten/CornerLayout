package com.cbm.android.corneringlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.cbm.android.corneringlayout.databinding.ActivityMainBinding
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clConditionalSimpleText.getContentText()!!.et!!.addTextChangedListener {
            binding.clConditionalSimpleText
                .setErrorCondition(it!!.length<3, "Must be at least 3 characters")
        }

        binding.clConditionalAdvancedText.getContentText()!!.et!!.addTextChangedListener {
            binding.clConditionalAdvancedText
                .setErrorCondition(it!!.length<3, "Must be at least 3 characters")
        }

        binding.clPasswordSimpleText.getContentText()!!.et!!.addTextChangedListener {
            binding.clPasswordSimpleText
                .setErrorCondition(!Pattern.compile("^(?=(.*\\d)+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))(?=(.*[^\\da-zA-Z])+)").matcher(it!!.toString()).find()||it.toString().length<5, "Must contain letters, numbers, punctuation and al least be 5 characters")
        }

        binding.clPasswordAdvancedText.getContentText()!!.et!!.addTextChangedListener {
            binding.clPasswordAdvancedText
                .setErrorCondition(!Pattern.compile("^(?=(.*\\d)+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))(?=(.*[^\\da-zA-Z])+)").matcher(it!!.toString()).find()||it.toString().length<5, "Must contain letters, numbers, punctuation and al least be 5 characters")
        }
    }
}