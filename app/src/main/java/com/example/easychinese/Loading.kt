package com.example.easychinese

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Loading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

val pd=ProgressDialog(this)

        pd.setMessage("Loading......")
        findViewById<Button>(R.id.st).setOnClickListener {
            pd.show()
        }
        findViewById<Button>(R.id.en).setOnClickListener {
            pd.dismiss()
        }

    }
}