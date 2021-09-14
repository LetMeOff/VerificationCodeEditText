package com.zjh.verification

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

/**
 *  desc :
 *  @author zjh
 *  on 2021/9/14
 */
class VerificationCodeEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var text: String = ""
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 画笔
     */
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
    }

    /**
     * 回调
     */
    private var mOnEditCompleteListener: OnEditCompleteListener? = null

    /**
     * 文字大小
     */
    var textSize: Float = 50f
        set(value) {
            field = value
            mPaint.textSize = field
            invalidate()
        }

    /**
     * 文字颜色
     */
    var textColor: Int = Color.BLACK
        set(value) {
            field = value
            mPaint.color = field
            invalidate()
        }

    /**
     * 是密码时显示的文字
     */
    var passwordText: String = "●"
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 输入长度
     */
    var inputCount = 4
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 文字之间的间距
     */
    var space = 20
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 是否正方形
     */
    var isSquare = true
        set(value) {
            field = value
            requestLayout()
        }

    /**
     * 文字背景
     */
    var textBackgroundDrawable: Drawable? = null
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 是否密码样式
     */
    var isPassword = false
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 光标背景
     */
    var cursorDrawable: Drawable? = null
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 光标宽度
     */
    var cursorDrawableWidth = 0
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 光标高度
     */
    var cursorDrawableHeight = 0
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 文字是否加粗
     */
    var isBoldText: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 绘制cursor闪烁
     * */
    private var needDrawCursor = false

    init {
        isClickable = true
        isFocusable = true
        isFocusableInTouchMode = true

        //获取属性
        context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeEditText).apply {
            textSize = getDimension(R.styleable.VerificationCodeEditText_android_textSize, 50f)
            textColor =
                getColor(R.styleable.VerificationCodeEditText_android_textColor, Color.BLACK)
            inputCount = getInt(R.styleable.VerificationCodeEditText_inputCount, 4)
            space = getDimensionPixelSize(R.styleable.VerificationCodeEditText_space, 10)
            isSquare = getBoolean(R.styleable.VerificationCodeEditText_isSquare, true)
            textBackgroundDrawable =
                getDrawable(R.styleable.VerificationCodeEditText_textBackground)
            isPassword = getBoolean(R.styleable.VerificationCodeEditText_isPassword, false)
            passwordText = getString(R.styleable.VerificationCodeEditText_passwordText) ?: "●"
            isBoldText = getBoolean(R.styleable.VerificationCodeEditText_isBoldText, false)
            cursorDrawable = getDrawable(R.styleable.VerificationCodeEditText_cursorDrawable)
            cursorDrawableWidth =
                getDimensionPixelSize(
                    R.styleable.VerificationCodeEditText_cursorDrawableWidth,
                    dip2px(1f)
                )
            cursorDrawableHeight =
                getDimensionPixelSize(
                    R.styleable.VerificationCodeEditText_cursorDrawableHeight,
                    textSize.toInt()
                )
            needDrawCursor = cursorDrawable != null

            recycle()
        }

        mPaint.textSize = textSize
        mPaint.color = textColor
        mPaint.isFakeBoldText = isBoldText
    }

    /**
     * 设置输入完成监听
     */
    fun setOnEditCompleteListener(listener: OnEditCompleteListener) {
        this.mOnEditCompleteListener = listener
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection? {
        //设置软键盘属性为数字
        outAttrs!!.inputType = EditorInfo.TYPE_CLASS_NUMBER
        return null
    }

    override fun onCheckIsTextEditor(): Boolean {
        //如果是一个文本编辑器，需要返回true
        return true
    }

    override fun isInEditMode(): Boolean {
        //是否在编辑模式中
        return isFocused
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isSquare) {
            //绘制正方形
            val perWidth = (measuredWidth - space * (inputCount - 1)) / inputCount
            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(perWidth, MeasureSpec.EXACTLY)
            )
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyPreIme(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_0,
            KeyEvent.KEYCODE_1,
            KeyEvent.KEYCODE_2,
            KeyEvent.KEYCODE_3,
            KeyEvent.KEYCODE_4,
            KeyEvent.KEYCODE_5,
            KeyEvent.KEYCODE_6,
            KeyEvent.KEYCODE_7,
            KeyEvent.KEYCODE_8,
            KeyEvent.KEYCODE_9 -> {
                if (text.length < inputCount) {
                    //累加文字
                    text += event?.displayLabel
                    invalidate()

                    if (text.length == inputCount) {
                        //输入到最大长度后 收起键盘
                        hideSoft()
                        mOnEditCompleteListener?.onEditComplete(text)
                    }
                }

                return true
            }
            KeyEvent.KEYCODE_DEL -> {
                if (!TextUtils.isEmpty(text)) {
                    //删除文字
                    text = text.substring(0, text.length - 1)
                    invalidate()
                }
                return true
            }
            KeyEvent.KEYCODE_ENTER -> {
                //按键盘的回车键 收起键盘
                hideSoft()
                mOnEditCompleteListener?.onEditComplete(text)
                return true
            }
        }

        return super.onKeyUp(keyCode, event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            getInputMethodManager()?.let {
                if (isFocusable && !isFocused) {
                    requestFocus()
                }
                it.viewClicked(this)
                it.showSoftInput(this, 0)
//                it.restartInput(this)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getInputMethodManager(): InputMethodManager? {
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    /**
     * 收起键盘
     */
    private fun hideSoft() {
        getInputMethodManager()?.let {
            if (it.isActive(this)) {
                it.hideSoftInputFromWindow(windowToken, 0)
            }
        }
//        this.clearFocus()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        val fontMetrics: Paint.FontMetrics = mPaint.fontMetrics
        val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom

        // 找到每一个字的位置
        val centerY = height / 2f
        val perWidth = (width - space * (inputCount - 1)) / inputCount
        for (index in 0 until inputCount) {
            // 绘制边框背景
            drawTextBackground(canvas, index, perWidth)
            // 绘制数字，垂直居中
            if (index < text.length) {
                val s = if (isPassword) {
                    passwordText
                } else {
                    text[index].toString()
                }
                val textWidth = mPaint.measureText(s)
                canvas.drawText(
                    s,
                    (perWidth * index + perWidth / 2 - textWidth / 2) + space * index,
                    centerY + distance,
                    mPaint
                )
            }
            // 在文字的末尾绘制光标
            if (index == text.length) {
                drawCursor(canvas, index, perWidth)
            }
        }
    }

    /**
     * 绘制文字背景
     */
    private fun drawTextBackground(
        canvas: Canvas,
        index: Int,
        perWidth: Int
    ) {
        if (textBackgroundDrawable == null) return
        // 设置绘制图片的边界
        val left = perWidth * index + space * index
        textBackgroundDrawable?.setBounds(
            left,
            0,
            left + perWidth,
            height
        )
        // 如果是selector，还可以单独设置获取焦点时的背景
        if (textBackgroundDrawable is StateListDrawable) {
            if (index == text.length && isFocused) {
                textBackgroundDrawable?.state = intArrayOf(android.R.attr.state_focused)
            } else {
                textBackgroundDrawable?.state = intArrayOf(android.R.attr.state_empty)
            }
            textBackgroundDrawable?.draw(canvas)
        } else {
            // 设置其他背景：shape或者图片
            textBackgroundDrawable?.draw(canvas)
        }
    }

    /**
     * 绘制光标
     */
    private fun drawCursor(canvas: Canvas, index: Int, perWidth: Int) {
        // 如果没有焦点，不需要绘制cursor
        if (!isInEditMode) {
            needDrawCursor = true
            return
        }
        cursorDrawable?.let {
            // 判断这一次是否需要绘制cursor
            if (needDrawCursor) {
                val left = perWidth * index + space * index + perWidth / 2 - cursorDrawableWidth / 2
                it.setBounds(
                    left,
                    ((height - cursorDrawableHeight) / 2),
                    left + cursorDrawableWidth,
                    ((height + cursorDrawableHeight) / 2)
                )
                it.draw(canvas)
            }
            // 重绘延迟1000毫秒
            // 为了防止意外启动了多个延迟任务，所以先做一个移除操作
            // 开始cursor动画
            Choreographer.getInstance().removeFrameCallback(drawCursorCallback)
            Choreographer.getInstance().postFrameCallbackDelayed(drawCursorCallback, 1000)
        }
    }

    /**
     * 重绘任务，仅仅是改变下一次是否要绘制cursor的标记，然后重绘
     */
    private val drawCursorCallback = Choreographer.FrameCallback {
        needDrawCursor = !needDrawCursor
        invalidate()
    }

    private fun dip2px(dipValue: Float): Int {
        val displayMetricsDensity = context.resources.displayMetrics.density
        return (dipValue * displayMetricsDensity + 0.5f).toInt()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //为了防止占用无用的资源，在移除的时候记得停止重绘任务
        Choreographer.getInstance().removeFrameCallback(drawCursorCallback)
    }

    override fun setEnabled(enabled: Boolean) {
        if (enabled == isEnabled) {
            return
        }
        if (!enabled) {
            hideSoft()
        }
        super.setEnabled(enabled)
        if (enabled) {
            getInputMethodManager()?.restartInput(this)
        }
    }

    fun clear() {
        text = ""
    }

    fun setContent(text: String) {
        this.text = text
    }

    interface OnEditCompleteListener {
        fun onEditComplete(text: String)
    }
}