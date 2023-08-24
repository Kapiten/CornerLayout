package com.cbm.android.corneringlayout

import android.content.DialogInterface
import android.content.res.TypedArray
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import com.cbm.android.corneringlayout.databinding.ActivityMainBinding
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ll.addView(EditText(this), ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        val cl = CornerLayout(this);
        cl.setPadding(8)
        cl.createTextDefault("Sample", "Hint Sample")

        binding.ll.addView(cl, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        binding.clConditionalSimpleText.getContentText()!!.et!!.addTextChangedListener {
            binding.clConditionalSimpleText
                .setErrorCondition(it!!.length<3, "Must be at least 3 characters")
            if(it!!.length<3){binding.clConditionalSimpleText.setFilling(Color.RED)}
            else {binding.clConditionalSimpleText.setFilling(Color.BLACK)}
        }

        binding.clConditionalSimpleText.getContentText()!!.setOnOptionsClick(View.OnClickListener {
            AlertDialog.Builder(it.context)
                .setTitle("Test")
                .setMessage("Options are awesome.")
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->

                })
                .create().show()
        })

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