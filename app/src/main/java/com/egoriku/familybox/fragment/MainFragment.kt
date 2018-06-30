package com.egoriku.familybox.fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.egoriku.familybox.MainActivity
import com.egoriku.familybox.R
import com.egoriku.familybox.barcode.encoder.BarcodeEncoder
import com.google.zxing.BarcodeFormat
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val encoder: BarcodeEncoder = BarcodeEncoder()
    private var state = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.fragment_main, container, false)

        inflate.cardView.setOnClickListener {
            when (state) {
                false -> {
                    textView.visibility = View.VISIBLE
                    imageView.visibility = View.VISIBLE
                    state = true
                }

                true -> {
                    textView.visibility = View.GONE
                    imageView.visibility = View.GONE
                    state = false
                }
            }
        }

        return inflate
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MainActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

            val stringExtra = data.getStringExtra(MainActivity.BARCODE_VALUE)
            val width = constraint.width
            val height = constraint.height
            val encodeBitmap = encoder.encodeBitmap(stringExtra, BarcodeFormat.CODE_128, width, height / 5)
            imageView.setImageBitmap(encodeBitmap)
            textView.text = stringExtra
            cardView.visibility = View.VISIBLE
        }
    }

}
