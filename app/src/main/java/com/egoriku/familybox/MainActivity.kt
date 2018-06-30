package com.egoriku.familybox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.egoriku.familybox.barcode.BarcodeScanningActivity
import com.egoriku.familybox.fragment.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 101
        const val BARCODE_VALUE = "BARCODE_VALUE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            startActivityForResult(
                    Intent(this, BarcodeScanningActivity::class.java),
                    REQUEST_CODE)
        }

        bottomAppBar.setNavigationOnClickListener {
            RoundedBottomSheetDialogFragment().apply {
                show(supportFragmentManager, this.tag)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            for (nestedFragment in fragment.childFragmentManager.fragments) {
                nestedFragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}