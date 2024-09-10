package com.example.barlibrary

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

fun Int.dp2px(context: Context?): Int {
    if (context == null) return 0
    return YTSizeUtil.dp2px(context, this.toFloat()).toInt()
}

fun Int.px2dp(context: Context): Float {
    return YTSizeUtil.px2dp(context, this.toFloat())
}

fun Float.dp2px(context: Context): Int {
    return YTSizeUtil.dp2px(context, this).toInt()
}

fun Float.px2dp(context: Context): Float {
    return YTSizeUtil.px2dp(context, this)
}

    fun Float.dp2px(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        ).toInt()
    }
/**
 * Created by hugh on 2017/8/28.
 */
class YTSizeUtil {

    companion object {
        val RATIO_16_9 = 16f / 9
        val RATIO_16_10 = 16f / 10

        /**
         * dp转px
         *
         * @param context 上下文
         * @param dpValue dp值
         * @return px值
         */
        fun dp2px(context: Context, dpValue: Float): Float {
            val scale = context.resources.displayMetrics.density
            return dpValue * scale + 0.5f
        }

        fun dp2px(dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                Resources.getSystem().displayMetrics
            ).toInt()
        }

        /**
         * px转dp
         *
         * @param context 上下文
         * @param pxValue px值
         * @return dp值
         */
        fun px2dp(context: Context, pxValue: Float): Float {
            val scale = context.resources.displayMetrics.density
            return pxValue / scale + 0.5f
        }

        /**
         * sp转px
         *
         * @param context 上下文
         * @param spValue sp值
         * @return px值
         */
        fun sp2px(context: Context, spValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        /**
         * px转sp
         * @param context 上下文
         * @param pxValue px值
         * @return sp值
         */
        fun px2sp(context: Context, pxValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }

        /**
         * 各种单位转换
         *
         * 该方法存在于TypedValue

         * @param unit    单位
         * *
         * @param value   值
         * *
         * @param metrics DisplayMetrics
         * *
         * @return 转换结果
         */
        fun applyDimension(unit: Int, value: Float, metrics: DisplayMetrics): Float {
            when (unit) {
                TypedValue.COMPLEX_UNIT_PX -> return value
                TypedValue.COMPLEX_UNIT_DIP -> return value * metrics.density
                TypedValue.COMPLEX_UNIT_SP -> return value * metrics.scaledDensity
                TypedValue.COMPLEX_UNIT_PT -> return value * metrics.xdpi * (1.0f / 72)
                TypedValue.COMPLEX_UNIT_IN -> return value * metrics.xdpi
                TypedValue.COMPLEX_UNIT_MM -> return value * metrics.xdpi * (1.0f / 25.4f)
            }
            return 0f
        }
    }
}