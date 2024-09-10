package com.example.barlibrary

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.EditorInfo
import android.widget.*
import java.util.*

class CommonNavBar(context:Context,attributeSet: AttributeSet?=null) : FrameLayout(context,attributeSet),View.OnClickListener{
    var itemView: View? = null

    companion object{
        const val ACTION_LEFT_TEXT = 1          // 左边TextView被点击
        const val ACTION_LEFT_BUTTON = 2        // 左边ImageBtn被点击
        const val ACTION_RIGHT_TEXT = 3         // 右边TextView被点击
        const val ACTION_RIGHT_BUTTON = 4       // 右边ImageBtn被点击
        const val ACTION_CENTER_SEARCH = 5      //中间控件被点击

        private const val TYPE_LEFT_NONE = 0
        private const val TYPE_LEFT_TEXTVIEW = 1
        private const val TYPE_LEFT_IMAGE = 2

        private const val TYPE_RIGHT_NONE = 0
        private const val TYPE_RIGHT_TEXTVIEW = 1
        private const val TYPE_RIGHT_IMAGE = 2

        private const val TYPE_CENTER_NONE = 0
        private const val TYPE_CENTER_TEXTVIEW = 1
        private const val TYPE_CENTER_SEARCH = 2
        private const val TYPE_CENTER_CUSTOM = 3
    }

    private var listener: OnNavClickListener?=null
    private var titleText:String?=null                 // 标题文字
    private var leftText:String?=null                   // 左边文字
    private var leftTextColor:Int = 0
    private var leftTextSize:Float = 0f
    private var rightText:String?=null                  // 右边文字
    private var rightTextColor:Int = 0
    private var rightTextSize:Float = 0f

    private var leftIvSrc: Int=0                        // 左边图片
    private var rightIvSrc: Int=0                       // 右边图片
    private var searchHintText:String?=null             //搜索提示文案

    private var tvLeft: TextView? = null                // 左边TextView
    private var btnLeft: ImageButton? = null            // 左边ImageButton
    private var tvRight : TextView? = null              // 右边TextView
    private var btnRight : ImageButton? = null          // 右边ImageButton

    private var tvCenter:TextView?=null                 // 中间标题
    private var etCenterSearch:EditText?=null           //中间search编辑框
    private var centerCustomViewRes:Int?=null           // 中间自定义布局
    private var centerCustomView:View?=null
    private var centerToEndOfLeft:Boolean = false
    private var centerToStartOfRight:Boolean = false

    private var titleBarColor = 0                       // 标题栏背景颜色

    private var leftType = 0                            // 左边视图类型
    private var rightType = 0                           // 右边视图类型
    private var centerType = 0                          //中间视图类型

    private var rlMain : RelativeLayout? = null         // 主视图


    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CommonNavBar)

        titleBarColor = typedArray.getColor(R.styleable.CommonNavBar_titleBarColor, 0)
        //左侧样式
        leftType = typedArray.getInt(R.styleable.CommonNavBar_leftType, TYPE_LEFT_NONE)
        leftTextColor = typedArray.getInt(R.styleable.CommonNavBar_leftTextColor,resources.getColor(R.color.yt_color_black_252525))
        leftTextSize = typedArray.getDimension(R.styleable.CommonNavBar_leftTextSize,16f)
        when(leftType){
            TYPE_LEFT_TEXTVIEW ->{
                leftText = typedArray.getString(R.styleable.CommonNavBar_leftText)
            }
            TYPE_LEFT_IMAGE ->{
                leftIvSrc = typedArray.getResourceId(
                    R.styleable.CommonNavBar_leftSrc,
                    R.drawable.ic_arrow_left_black
                )
            }
        }

        //右侧样式
        rightType = typedArray.getInt(R.styleable.CommonNavBar_rightType, TYPE_RIGHT_NONE)
        rightTextColor = typedArray.getInt(R.styleable.CommonNavBar_rightTextColor,resources.getColor(R.color.yt_color_black_252525))
        rightTextSize = typedArray.getDimension(R.styleable.CommonNavBar_rightTextSize,16f)
        when(rightType){
            TYPE_RIGHT_TEXTVIEW ->{
                rightText = typedArray.getString(R.styleable.CommonNavBar_rightText)
            }
            TYPE_RIGHT_IMAGE ->{
                rightIvSrc = typedArray.getResourceId(
                    R.styleable.CommonNavBar_rightSrc,
                    0
                )
            }
        }

        //中间样式
        centerType = typedArray.getInt(R.styleable.CommonNavBar_centerType, TYPE_CENTER_NONE)
        centerToEndOfLeft = typedArray.getBoolean(R.styleable.CommonNavBar_centerToEndOfLeft,false)
        centerToStartOfRight = typedArray.getBoolean(R.styleable.CommonNavBar_centerToStartOfRight,false)
        when(centerType){
            TYPE_CENTER_TEXTVIEW ->{
                titleText = typedArray.getString(R.styleable.CommonNavBar_titleText)
            }
            TYPE_CENTER_SEARCH ->{
                searchHintText = typedArray.getString(R.styleable.CommonNavBar_searchHintText)
            }
            TYPE_CENTER_CUSTOM ->{
                centerCustomViewRes = typedArray.getResourceId(R.styleable.CommonNavBar_centerCustomView, 0)
            }
        }
        typedArray.recycle()
        mainLayout()
        initMainViews(context)
    }

    private fun mainLayout(){
        rlMain = RelativeLayout(context)
        rlMain?.setBackgroundColor(titleBarColor)
        val mainParams = RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        mainParams.addRule(RelativeLayout.CENTER_VERTICAL)
        addView(rlMain, mainParams)
    }

    private fun initMainViews(context: Context) {
        if(centerType!= TYPE_CENTER_NONE){
            initCenterView()
        }
        if (leftType != TYPE_LEFT_NONE) {
            initMainLeftViews(context)
        }
        if (rightType != TYPE_RIGHT_NONE) {
            initMainRightViews(context)
        }
    }

    private fun initCenterView(){
        val centerInnerParams = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            addRule(RelativeLayout.CENTER_IN_PARENT)
            when{
                centerToEndOfLeft&&leftType == TYPE_LEFT_TEXTVIEW -> addRule(RelativeLayout.RIGHT_OF,tvLeft?.id?:0)
                centerToEndOfLeft&&leftType == TYPE_LEFT_IMAGE -> addRule(RelativeLayout.RIGHT_OF,btnLeft?.id?:0)
                centerToStartOfRight&&rightType == TYPE_RIGHT_TEXTVIEW -> addRule(RelativeLayout.LEFT_OF,tvRight?.id?:0)
                centerToStartOfRight&&rightType == TYPE_RIGHT_IMAGE -> addRule(RelativeLayout.LEFT_OF,btnRight?.id?:0)
            }
            setPadding(0,getStatusBarHeight(),0,0)
            height = 44.dp2px(context)
        }

        when(centerType){
            TYPE_CENTER_TEXTVIEW ->{
                tvCenter = TextView(context).apply {
                    text = titleText
                    isBold(true)
                    setTextColor(resources.getColor(R.color.yt_color_black_252525))
                    textSize = 18f
                    gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
                    isSingleLine = true
                }
                rlMain?.addView(tvCenter, centerInnerParams)
            }
            TYPE_CENTER_SEARCH ->{
                val centerSearchParams = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                    height = 34.dp2px(context)
                    marginStart = 20.dp2px(context)
                    marginEnd = 60.dp2px(context)
                    setPadding(0,getStatusBarHeight(),0,0)
                    addRule(RelativeLayout.LEFT_OF,tvRight?.id?:0)
                    addRule(RelativeLayout.CENTER_VERTICAL)
                }

                etCenterSearch = EditText(context).apply {
                    id = generateViewId()
                    background = null
                    setBackgroundResource(R.drawable.shape_bg_gray_f5f5f5_17)
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search,0,0,0)
                    hint = searchHintText
                    gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    setPadding(8.dp2px(context),0,0,0)
                    textSize = 14f
                    imeOptions = EditorInfo.IME_ACTION_SEARCH
                    isSingleLine = true
                    setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            listener?.onEditorActionListener(etCenterSearch?.text.toString())
                            return@OnEditorActionListener true
                        }
                        false
                    })
                }
                rlMain?.addView(etCenterSearch, centerSearchParams)
            }
            TYPE_CENTER_CUSTOM ->{
                centerCustomView =
                    LayoutInflater.from(context).inflate(centerCustomViewRes?:0, rlMain, false)
                if (centerCustomView?.id == NO_ID) {
                    centerCustomView?.id = getGenerateViewId()
                }
                rlMain?.addView(centerCustomView, centerInnerParams)
            }
        }
    }

    private fun initMainLeftViews(context: Context) {
        val leftInnerParams = RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT).apply {
            height = 44.dp2px(context)
            setPadding(0,getStatusBarHeight(),0,0)
            addRule(RelativeLayout.ALIGN_PARENT_START)
            addRule(RelativeLayout.CENTER_VERTICAL)
//            marginEnd = 12.dp2px(context)
        }

        if (leftType == TYPE_LEFT_TEXTVIEW) {
            // 初始化左边TextView
            tvLeft = TextView(context).apply {
                text = leftText
                id = generateViewId()
                setTextColor(leftTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize)
                gravity = Gravity.START or Gravity.CENTER_VERTICAL
                isSingleLine = true
                setOnClickListener(this@CommonNavBar)
                setPadding(12.dp2px(context), 0, 12.dp2px(context), 0)
            }
            rlMain?.addView(tvLeft, leftInnerParams)
        } else if (leftType == TYPE_LEFT_IMAGE) {
            // 初始化左边ImageButton
            btnLeft = ImageButton(context).apply {
                id = generateViewId()
                setBackgroundColor(Color.TRANSPARENT)
                setImageResource(leftIvSrc)
                setPadding(12.dp2px(context), 0, 12.dp2px(context), 0)
                setOnClickListener(this@CommonNavBar)
            }
            rlMain?.addView(btnLeft, leftInnerParams)
        }
    }

    private fun initMainRightViews(context: Context) {
        val rightInnerParams = RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT).apply {
            addRule(RelativeLayout.CENTER_VERTICAL)
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            setPadding(0,getStatusBarHeight(),0,0)
            height = 44.dp2px(context)
        }
        if (rightType == TYPE_RIGHT_TEXTVIEW) {
            // 初始化右边TextView
            tvRight = TextView(context).apply {
                text = rightText
                id = generateViewId()
                setTextColor(rightTextColor)
                gravity = Gravity.CENTER_VERTICAL
                setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize)
                isSingleLine = true
                setPadding(12.dp2px(context), 0, 12.dp2px(context), 0)
                setOnClickListener(this@CommonNavBar)
            }
            rlMain?.addView(tvRight, rightInnerParams)
        } else if (rightType == TYPE_RIGHT_IMAGE) {
            // 初始化右边ImageBtn
            btnRight = ImageButton(context).apply {
                id = generateViewId()
                setImageResource(rightIvSrc)
                setBackgroundColor(Color.TRANSPARENT)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                setPadding(12.dp2px(context), 0, 12.dp2px(context), 0)
                setOnClickListener(this@CommonNavBar)
            }
            rlMain?.addView(btnRight, rightInnerParams)
        }
    }

    override fun onClick(v: View?) {
//        if(!FastClickUtils.isNoFastClick()) return
        when(v){
            btnLeft -> {
                listener?.onClicked(ACTION_LEFT_BUTTON)
            }
            tvLeft -> {
                listener?.onClicked(ACTION_LEFT_TEXT)
            }
            btnRight -> {
                listener?.onClicked(ACTION_RIGHT_BUTTON)
            }
            tvRight -> {
                listener?.onClicked(ACTION_RIGHT_TEXT)
            }

        }
    }

    fun setOnNavClickListener(listener: OnNavClickListener?){
        this.listener = listener
    }

    /**
     * 获取系统状态栏高度
     */
    fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        return statusBarHeight
    }

    fun setTitle(title:String?){
        if (title.isNullOrEmpty()) return
        tvCenter?.text = title
    }

    fun setRightImg(imgResource:Int){
        if(imgResource != 0){
            btnRight?.setImageResource(imgResource)
        }
    }

/*    fun hideRight(){
        btnRight?.gone()
    }
    fun showRight(){
        btnRight?.show()
    }*/

    fun setSearchText(title:String?){
        if (title.isNullOrEmpty()) return
        etCenterSearch?.setText(title)
        etCenterSearch?.setSelection(title.length)
    }

    interface OnNavClickListener {
        fun onClicked(action:Int)
        fun onEditorActionListener(searchText:String=""){}//点击搜索回调
    }

    /**
     * 获取中间自定义布局视图
     */
    fun getCenterCustomView(): View? {
        return centerCustomView
    }

    private fun getGenerateViewId(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            View.generateViewId()
        } else {
            UUID.randomUUID().hashCode()
        }
    }
}
