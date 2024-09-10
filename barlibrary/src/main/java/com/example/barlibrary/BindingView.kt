package com.example.barlibrary

import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("isBold")
fun TextView.isBold(isBold:Boolean){
   paint.isFakeBoldText = isBold
}
