package com.m1ku.textprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator


/**
 * Author: m1Ku
 * Email: howx172@163.com
 * Create Time: 2018/5/28
 * Description: 带text文本的进度条
 */
class TextProgressBar : View {

    private val colorArray = IntArray(2)
    private val positionArray = floatArrayOf(
            0f,
            1f
    )
    private var mBarHeight = 8
    private var mTrackColor = Color.parseColor("#d6eafe")
    private var mStartColor = Color.parseColor("#4a6cfb")
    private var mEndColor = Color.parseColor("#819af2")
    private var mTextColor = Color.GRAY
    private var mTextSize = 13
    private var mTopTextMargin = 15 //顶部文字距进度条的距离
    private var mBottomTextMargin = 10 //底部文字距进度条的距离
    private lateinit var mTrackPaint: Paint
    private lateinit var mTextPaint: Paint
    private lateinit var mBarPaint: Paint
    private lateinit var mDotPaint: Paint
    private var bottomText = listOf<String>()
    private var topText = listOf<String>()
    private var mDivisionWidth = 0 //每一个分区的宽度
    private var mMaxProgress = 0f
    private var mCurrentProgress = 0f
    private val animateTime = 2000L
    private lateinit var mDotBitmap: Bitmap

    //使用this委托给两个参数的构造函数
    constructor(context: Context) : this(context, null)

    //使用this委托给三个参数的构造函数
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    //使用super调用父类的构造函数，初始化其基类
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TextProgressBar)
        mBarHeight = ta.getDimensionPixelOffset(R.styleable.TextProgressBar_barHeight, context.dp2px(mBarHeight))
        mTrackColor = ta.getColor(R.styleable.TextProgressBar_trackColor, mTrackColor)
        mTextColor = ta.getColor(R.styleable.TextProgressBar_barTextColor, mTextColor)
        mTextSize = ta.getDimensionPixelSize(R.styleable.TextProgressBar_barTextSize, context.sp2px(mTextSize))
        mTopTextMargin = ta.getDimensionPixelSize(R.styleable.TextProgressBar_topTextMargin, context.dp2px(mTopTextMargin))
        mBottomTextMargin = ta.getDimensionPixelSize(R.styleable.TextProgressBar_bottomTextMargin, context.dp2px(mBottomTextMargin))
        mStartColor = ta.getColor(R.styleable.TextProgressBar_barStartColor, mStartColor)
        mEndColor = ta.getColor(R.styleable.TextProgressBar_barEndColor, mEndColor)
        colorArray[0] = mStartColor
        colorArray[1] = mEndColor
        ta.recycle()

        initPaint()
    }

    private fun initPaint() {
        mDotBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_progress_dot)
        mTrackPaint = Paint().apply {
            color = mTrackColor
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
            strokeWidth = mBarHeight.toFloat()
            style = Paint.Style.FILL_AND_STROKE
        }

        mTextPaint = Paint().apply {
            color = mTextColor
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = mTextSize.toFloat()
        }

        mBarPaint = Paint().apply {
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
            strokeWidth = mBarHeight.toFloat()
            style = Paint.Style.FILL_AND_STROKE
        }

        mDotPaint = Paint().apply {
            isAntiAlias = true
            color = Color.RED
            strokeWidth = 5f
            style = Paint.Style.FILL
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode == MeasureSpec.AT_MOST) {
            //控件高度 = 进度条高度 + 上文字距离 + 下文字距离 + 上下文字大小
            height = mBarHeight + mTopTextMargin + mBottomTextMargin + 2 * mTextSize
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mDivisionWidth = (width - mDotBitmap.width) / (bottomText.size - 1)
        //画底部进度滑轨
        canvas?.drawLine(
                mDotBitmap.width / 2f,
                height / 2f,
                width - mDotBitmap.width / 2f,
                height / 2f,
                mTrackPaint)

        val currentX = (mCurrentProgress / mMaxProgress) * (width - mDotBitmap.width) - mBarHeight / 2 + mDotBitmap.width / 2
        //为滑轨设置渐变色
        val gradient = LinearGradient(
                0f,
                height / 2f,
                currentX + mBarHeight / 2,
                height / 2f,
                colorArray,
                positionArray,
                Shader.TileMode.REPEAT
        )
        mBarPaint.shader = gradient
        //画进度条滑轨
        canvas?.drawLine(
                mDotBitmap.width / 2f,
                height / 2f,
                currentX,
                height / 2f,
                mBarPaint)

        //画进度条圆点
        canvas?.drawBitmap(mDotBitmap, currentX - mDotBitmap.width / 2,
                height / 2f - (mDotBitmap.height / 2), mBarPaint)

//        canvas?.drawPoint(currentX, height / 2f, mDotPaint)
        //画顶部文字
        if (topText.isNotEmpty()) {
            topText.forEachIndexed { index, s ->
                //确定基线高度
                val rect = Rect()
                mTextPaint.getTextBounds(s, 0, s.length, rect)
                val fontMetrics = mTextPaint.fontMetricsInt
                val dy = ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom).toFloat()
                //文字宽高
                val textHeight = rect.height()
                canvas?.drawText(s,
                        mDivisionWidth / 2f + mDivisionWidth * index,
                        height / 2 - textHeight - mTopTextMargin + dy,
                        mTextPaint)
            }
        }

        //画底部文字
        if (bottomText.isNotEmpty()) {
            bottomText.forEachIndexed { index, s ->
                //测量文本获取高度
                val rect = Rect()
                mTextPaint.getTextBounds(s, 0, s.length, rect)
                val textHeight = rect.height()
                val textWidth = rect.width()
                if (index == 0) {
                    canvas?.drawText(s,
                            mBarHeight / 2f + textWidth / 2f,
                            height / 2f + mBarHeight + textHeight + mBottomTextMargin,
                            mTextPaint)
                } else {
                    canvas?.drawText(s,
                            index * mDivisionWidth + textWidth / 2f,
                            height / 2f + mBarHeight + textHeight + mBottomTextMargin,
                            mTextPaint)
                }
            }
        }
    }

    fun setBottomText(bottomText: List<String>) {
        this.bottomText = bottomText
        invalidate()
    }

    fun setTopText(topText: List<String>) {
        this.topText = topText
        invalidate()
    }

    fun setMaxProgress(maxProgress: Float) {
        this.mMaxProgress = maxProgress
    }

    fun setCurrentProgress(progress: Float) {
        val animator = ValueAnimator.ofFloat(0f, progress)
        animator.addUpdateListener {
            mCurrentProgress = it.animatedValue as Float
            invalidate()
        }
        animator.interpolator = AccelerateInterpolator()
        animator.duration = animateTime
        animator.start()
    }

    /**
     * dp转为px
     */
    private fun Context.dp2px(value: Int): Int = (value * resources.displayMetrics.density).toInt()


    /**
     * sp转px
     * @param value sp
     */
    private fun Context.sp2px(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

}