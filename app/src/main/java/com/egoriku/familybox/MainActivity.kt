package com.egoriku.familybox

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.egoriku.familybox.barcode.BarcodeScanningActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 101
        const val BARCODE_VALUE = "BARCODE_VALUE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            startActivityForResult(
                    Intent(this, BarcodeScanningActivity::class.java),
                    REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            textView.text = data.getStringExtra(BARCODE_VALUE)
        }
    }
}
