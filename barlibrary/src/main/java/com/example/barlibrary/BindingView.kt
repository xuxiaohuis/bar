package com.example.barlibrary

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter


/*@BindingAdapter("backgroundColor")
fun CustomRoundTextView.backgroundColor(color:(Int) ){
   setBackgroundColor(color)
}*/

@BindingAdapter("showByStatus")
fun View.showByStatus(isVisible:Boolean){
   visibility = if(isVisible){
      View.VISIBLE
   }else{
      View.GONE
   }
}

@BindingAdapter("isBold")
fun TextView.isBold(isBold:Boolean){
   paint.isFakeBoldText = isBold
}

/*@BindingAdapter("mSrc")
fun SimpleDraweeView.mSrc(iconUrl:String?){
   FrescoUtils.displayNetImage(this, iconUrl)
}*/

/*@BindingAdapter("textColor")
fun TextView.textColor(style:Int){
   when(style){
      1-> this.setTextColor(resources.getColor(com.ingtube.util.R.color.yt_color_text_yellow,null))
      2-> this.setTextColor(resources.getColor(com.ingtube.util.R.color.yt_color_blue_4663FF,null))
      3-> this.setTextColor(resources.getColor(com.ingtube.util.R.color.yt_color_black,null))
   }
}*/
