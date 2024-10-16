package com.arasthel.spannedgridlayoutmanager.sample

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import om.arasthel.spannedgridlayoutmanager.sample.R
import java.math.BigDecimal

class SeekBarVertical : View {
    // 背景色
    private var bgColor = 0

    // 进度条色
    private var progressColor = 0

    // 进度条启始颜色
    private var progressStartColor = 0

    // 进度条结束颜色
    private var progressEndColor = 0

    // 背景启始颜色
    private var backgroundStartColor = 0

    // 背景结束颜色
    private var backgroundEndColor = 0

    // 进度
    private var progress = 0f

    // 最大进度值
    var maxProgress = 100f
        private set

    // 最小进度值
    var minProgress = 0f
        private set

    // false顶部  true底部拖动
    private var isProgressReverse = true
    var onProgressChangedListnter: OnProgressChangedListener? = null
        private set
    private var mPaint: Paint? = null
    private var mProgressPaint: Paint? = null
    private var mThumbPaint: Paint? = null
    private var mTouchDownX = 0f
    private var mTouchDownY = 0f
    private var mMoveDistanceX = 0f
    private var mMoveDistanceY = 0f
    private var mTouchDownMoveProgress = 0f

    // 默认拖动矩形圆角半径
    private var thumbRadius = dip2px(30f).toFloat()

    // 默认拖动矩形宽度
    private var thumbWidth = dip2px(45f).toFloat()

    // 默认拖动矩形高度
    private var thumbHeight = dip2px(10f).toFloat()

    // 默认拖动矩形与进度条上边距的距离
    private var thumbMarginProgress = dip2px(5f).toFloat()

    // 拖动图形，定义的图形则不显示默认圆角矩形
    private var thumbImage: Bitmap? = null

    // 默认拖动thumb颜色
    private var thumbColor = 0

    // 进度条图案
    private var progressImage: Bitmap? = null

    // 背景框
    private var progressBgRectF: RectF? = null

    // 进度框
    private var progressRectF: RectF? = null

    // 拖动小按钮框
    private var thumbRectF: RectF? = null
    private var progressImageSrc: Rect? = null
    private var thumbImageSrc: Rect? = null
    private val progressImageDes: RectF? = null
    private var viewParent: ViewParent? = null
    private var drawFilter: PaintFlagsDrawFilter? = null
    private var clipPath: Path? = null

    // 矩形圆角半径
    private var roundRadius = dip2px(24f).toFloat()

    // 不可拖动区域尺寸
    private var canNotDragMargin = dip2px(32f).toFloat()
    private var isPointFiveStep = false

    //  是否能拖动，主要类似普通灯类型不需要拖动，可以直接设置个onclick点击事件
    private var isDragEnable = true
    private var listener: OnClickListener? = null
    private var progressShader: Shader? = null
    private var backShader: Shader? = null
    private var progressColors:IntArray ?= null
    private var progressColorsPercents:FloatArray ?= null
    private var backColors:IntArray ?= null
    private var backColorsPercents:FloatArray ?= null
    private var isInit = false


    // 垂直呈现还是水平呈现
    private var isVertical = true

    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.SeekBarVertical)
        bgColor = typeArray.getColor(R.styleable.SeekBarVertical_progressBackgroundColor, -16776961)
        progressColor = typeArray.getColor(R.styleable.SeekBarVertical_progressColor, -256)
        progressStartColor = typeArray.getColor(R.styleable.SeekBarVertical_progressStartColor, -256)
        progressEndColor = typeArray.getColor(R.styleable.SeekBarVertical_progressEndColor, -256)
        backgroundStartColor = typeArray.getColor(R.styleable.SeekBarVertical_backgroundStartColor, -256)
        backgroundEndColor = typeArray.getColor(R.styleable.SeekBarVertical_backgroundEndColor, -256)
        thumbColor= typeArray.getColor(R.styleable.SeekBarVertical_thumbColor, -256)
        maxProgress = typeArray.getFloat(R.styleable.SeekBarVertical_maxProgressValue, 100f)
        minProgress = typeArray.getFloat(R.styleable.SeekBarVertical_minProgressValue, 0f)
        progress = typeArray.getFloat(R.styleable.SeekBarVertical_progressValue, minProgress)
        thumbRadius = typeArray.getDimension(R.styleable.SeekBarVertical_thumbRadius, thumbRadius)
        thumbMarginProgress = typeArray.getDimension(R.styleable.SeekBarVertical_thumbMarginProgress, thumbMarginProgress)
        thumbWidth = typeArray.getDimension(R.styleable.SeekBarVertical_thumbWidth, thumbWidth)
        thumbHeight = typeArray.getDimension(R.styleable.SeekBarVertical_thumbHeight, thumbHeight)
        roundRadius = typeArray.getDimension(R.styleable.SeekBarVertical_roundRadius, roundRadius)
        canNotDragMargin = typeArray.getDimension(R.styleable.SeekBarVertical_canNotDragMargin, canNotDragMargin)
        isProgressReverse = typeArray.getBoolean(R.styleable.SeekBarVertical_isProgressReverse, isProgressReverse)
        isDragEnable = typeArray.getBoolean(R.styleable.SeekBarVertical_isDragEnable, isDragEnable)
        isPointFiveStep = typeArray.getBoolean(R.styleable.SeekBarVertical_isPointFiveStep, isPointFiveStep)
        isVertical = typeArray.getBoolean(R.styleable.SeekBarVertical_isVertical, isVertical)

        var thumb = typeArray.getDrawable(R.styleable.SeekBarVertical_thumbBarDrawable)
        if (thumb != null) {
            thumbImage = (thumb as BitmapDrawable).bitmap
            thumbImage?.let {
                thumbImageSrc = Rect(0, 0, it.width, it.height)
            }
        }
        thumb = typeArray.getDrawable(R.styleable.SeekBarVertical_progressBarDrawable)
        if (thumb != null) {
            progressImage = (thumb as BitmapDrawable).bitmap
            progressImage?.let {
                progressImageSrc = Rect(0, 0, it.width, it.height)
            }
            //progressImageDes = new RectF()
        }
        if (progressStartColor != progressEndColor) {
            progressColors = intArrayOf(progressStartColor, progressEndColor)
            progressColorsPercents = floatArrayOf(0f, 1.0f)
            setProgressLinearGradient(progressColors!!, progressColorsPercents!!)
        }

        if (backgroundStartColor != backgroundEndColor){
            backColors = intArrayOf(backgroundStartColor, backgroundEndColor)
            backColorsPercents = floatArrayOf(0f, 1.0f)
            setBackLinearGradient(backColors!!, backColorsPercents!!)
        }
        typeArray.recycle()
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewParent = this.parent
    }


    override fun onMeasure( widthMeasureSpec:Int,  heightMeasureSpec:Int)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //父控件传进来的宽度和高度以及对应的测量模式
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)


    }


    private fun initPaint() {
        setLayerType(LAYER_TYPE_SOFTWARE,null)
        //this.scaleFactor = this.getResources().getDisplayMetrics().density;
        mPaint = Paint()
        mPaint?.isDither = true
        mPaint?.isAntiAlias = true
        mPaint?.style = Paint.Style.FILL
        mPaint?.color = bgColor
        drawFilter = PaintFlagsDrawFilter(0, 7)
        mProgressPaint = Paint()
        mProgressPaint?.isDither = true
        mProgressPaint?.isAntiAlias = true
        mProgressPaint?.style = Paint.Style.FILL

        mThumbPaint = Paint()
        mThumbPaint?.isDither = true
        mThumbPaint?.isAntiAlias = true
        mThumbPaint?.style = Paint.Style.FILL
        mThumbPaint?.color = thumbColor
        run { this.mProgressPaint?.color = this.progressColor }
    }

    private fun initProgressBgRect() {
        if (progressBgRectF == null) {
            progressBgRectF = RectF()
        }
        progressBgRectF?.left = 0f
        progressBgRectF?.right = this.width.toFloat()
        progressBgRectF?.top = 0f
        progressBgRectF?.bottom = this.height.toFloat()

        if (progressShader == null) {
            if (progressColors != null && progressColorsPercents != null && width != 0 && height != 0){
                setProgressLinearGradient(progressColors!!, progressColorsPercents!!)
            }
        }
        if (backShader == null){
            if (backColors != null && backColorsPercents != null && width != 0 && height != 0){
                setBackLinearGradient(backColors!!, backColorsPercents!!)
            }
        }
    }

    fun setBackLinearGradient(colors:IntArray, floats:FloatArray){
        backColors = colors
        backColorsPercents = floats
        if (progressBgRectF?.width() != null && progressBgRectF?.height() != null){
            backShader = if (isVertical) LinearGradient((progressBgRectF?.width()?:0f) / 2, 0f, (progressBgRectF?.width()?:0f) / 2, progressBgRectF?.height()?:0f,colors, floats,
                    Shader.TileMode.CLAMP) else LinearGradient(0f, (progressBgRectF?.height()?:0f) / 2, progressBgRectF?.width()?:0f, (progressBgRectF?.height()?:0f) / 2, colors, floats,
                    Shader.TileMode.CLAMP)
            mPaint?.shader = backShader
        }
    }

    fun setProgressLinearGradient(colors:IntArray, floats:FloatArray){
        progressColors = colors
        progressColorsPercents = floats
        if (progressBgRectF?.width() != null && progressBgRectF?.height() != null){
            progressShader = if (isVertical) LinearGradient((progressBgRectF?.width()?:0f) / 2, 0f, (progressBgRectF?.width()?:0f) / 2, progressBgRectF?.height()?:0f,colors, floats,
                    Shader.TileMode.CLAMP) else LinearGradient(0f, (progressBgRectF?.height()?:0f) / 2, progressBgRectF?.width()?:0f, (progressBgRectF?.height()?:0f) / 2, colors, floats,
                    Shader.TileMode.CLAMP)
            mProgressPaint?.shader = progressShader
        }
    }

    private fun calculateThumbBtn() {
        if (thumbRectF == null) {
            thumbRectF = RectF()
        }
        // 已设置高和宽
        if (isVertical){
            thumbRectF?.left = (progressRectF?.width()?:0f) / 2 - thumbWidth / 2
            thumbRectF?.right = (progressRectF?.width()?:0f) / 2 + thumbWidth / 2
            if (isProgressReverse) {
                thumbRectF?.top = (progressRectF?.top?:0f) + thumbMarginProgress
                thumbRectF?.bottom = (thumbRectF?.top?:0f) + thumbHeight
            } else {
                thumbRectF?.bottom = (progressRectF?.bottom?:0f) - thumbMarginProgress
                thumbRectF?.top = (thumbRectF?.bottom?:0f) - thumbHeight
            }
        }else{
            thumbRectF?.top = (progressRectF?.height()?:0f) / 2 - thumbWidth / 2
            thumbRectF?.bottom = (progressRectF?.height()?:0f) / 2 + thumbWidth / 2
            if (isProgressReverse) {
                thumbRectF?.left = (progressRectF?.left?:0f) + thumbMarginProgress
                thumbRectF?.right = (thumbRectF?.left?:0f) + thumbHeight
            } else {
                thumbRectF?.right = (progressRectF?.right?:0f) - thumbMarginProgress
                thumbRectF?.left = (thumbRectF?.right?:0f) - thumbHeight
            }
        }
    }

    fun setProgress(progress: Float) {
        //Log.v("pad测试", "progress:$progress  this.progress:${this.progress}")
        if (progress != this.progress) {
            if (progress >= maxProgress) {
                this.progress = maxProgress
            } else if (progress <= minProgress) {
                this.progress = minProgress
            } else {
                this.progress = progress
            }
            this.invalidate()
            onProgressChangedListnter?.onProgressChanged(this, this.progress, false)
        }
    }

    fun getProgress(): Float {
        return getValue(progress)
    }

    fun setProgressColor(progressColor: Int) {
        this.progressColor = progressColor
        this.mProgressPaint?.color = this.progressColor
        this.invalidate()
    }

    private fun calculateProgressBar() {
        if (progressRectF == null) {
            progressRectF = RectF()
        }
        val progressHeight: Float
        if (isVertical){
            progressRectF?.left = progressBgRectF?.left
            progressRectF?.right = progressBgRectF?.right
            if (isProgressReverse) {
                progressRectF?.bottom = progressBgRectF?.bottom
                progressHeight = canNotDragMargin + ((progressBgRectF?.height()?:0f) - canNotDragMargin) / (maxProgress - minProgress) * (progress - minProgress)
                progressRectF?.top = (progressRectF?.bottom?:0f) - progressHeight
                if ((progressRectF?.top?:0f) > (progressRectF?.bottom?:0f)) {
                    progressRectF?.top = progressRectF?.bottom
                }
            } else {
                progressRectF?.top = progressBgRectF?.top
                progressHeight = canNotDragMargin + ((progressBgRectF?.height() ?:0f)- canNotDragMargin) / (maxProgress - minProgress) * (progress - minProgress)
                progressRectF?.bottom = (progressRectF?.top?:0f) + progressHeight
                if ((progressRectF?.bottom?:0f) > (progressBgRectF?.bottom?:0f)) {
                    progressRectF?.bottom = progressBgRectF?.bottom
                }
            }
        }else{
            progressRectF?.top = progressBgRectF?.top
            progressRectF?.bottom = progressBgRectF?.bottom
            if (isProgressReverse) {
                // 右
                progressRectF?.right = progressBgRectF?.right
                progressHeight = canNotDragMargin + ((progressBgRectF?.width()?:0f) - canNotDragMargin) / (maxProgress - minProgress) * (progress - minProgress)
                progressRectF?.left = (progressRectF?.right?:0f) - progressHeight
                if ((progressRectF?.left?:0f) > (progressRectF?.right?:0f)) {
                    progressRectF?.left = progressRectF?.right
                }
            } else {
                // 左
                progressRectF?.left = progressBgRectF?.left
                progressHeight = canNotDragMargin + ((progressBgRectF?.width() ?:0f)- canNotDragMargin) / (maxProgress - minProgress) * (progress - minProgress)
                progressRectF?.right = (progressRectF?.left?:0f) + progressHeight
                if ((progressRectF?.right?:0f) > (progressBgRectF?.right?:0f)) {
                    progressRectF?.right = progressBgRectF?.right
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!this.isEnabled) {
                    return false
                }
                if (isDragEnable) {
                    viewParent?.requestDisallowInterceptTouchEvent(true)
                    mTouchDownX = event.x
                    mTouchDownY = event.y
                    mTouchDownMoveProgress = progress
                    onProgressChangedListnter?.onStartTrackingTouch(this)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (listener != null) performClick()
                if (isDragEnable) {
                    onProgressChangedListnter?.onStopTrackingTouch(this)
                    viewParent?.requestDisallowInterceptTouchEvent(false)
                    this.invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> if (isDragEnable) {
                mMoveDistanceX = if (isProgressReverse) mTouchDownX - event.x else event.x - mTouchDownX
                mMoveDistanceY = if (isProgressReverse) mTouchDownY - event.y else event.y - mTouchDownY
                var tempProgress = -1.0f
                tempProgress = if (isVertical)  ((maxProgress - minProgress) / (progressBgRectF?.height()?:1f) * mMoveDistanceY) else
                    ((maxProgress - minProgress) / (progressBgRectF?.width()?:1f) * mMoveDistanceX)
                progress = mTouchDownMoveProgress + tempProgress
                if (progress < minProgress) {
                    progress = minProgress
                }
                if (progress > maxProgress) {
                    progress = maxProgress
                }
                this.invalidate()
                this.progress = getValue(progress)
                onProgressChangedListnter?.onProgressChanged(this, this.progress, false)
            }
        }
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            val w = this.width
            val h = this.height
            clipPath = clipPath ?: Path()
            clipPath!!.addRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), floatArrayOf(
                    roundRadius, roundRadius,
                    roundRadius, roundRadius,
                    roundRadius, roundRadius,
                    roundRadius, roundRadius),
                    Path.Direction.CW)

            initProgressBgRect()
            isInit = true
        }

        canvas.save()
        clipPath?.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                canvas.clipPath(it)
            }else {
                canvas.clipPath(it, Region.Op.REPLACE)
            }
        }
        // 背景
        canvas.drawFilter = drawFilter
        if (progressBgRectF != null && mPaint != null)
            canvas.drawRect(progressBgRectF!!, mPaint!!)
        // 进度
        calculateProgressBar()
        if (progressImage != null) {
            progressImageSrc?.left = 0
            progressImageSrc?.top = 0
            progressImageSrc?.right = progressRectF?.width()?.toInt()
            progressImageSrc?.bottom = progressRectF?.height()?.toInt()
            if (progressRectF != null)
                canvas.drawBitmap(progressImage!!, progressImageSrc, progressRectF!!, mPaint)
        } else {
            if (progressRectF != null && mProgressPaint != null)
               canvas.drawRect(progressRectF!!, mProgressPaint!!)
        }

        // 拉手按钮
        calculateThumbBtn()
        // 拉手按钮 xml中设置了图片则用默认的图片，没设置图片用默认的圆弧矩形
        if (thumbImage != null) {
            //canvas.drawBitmap(thumbImage!!, (progressBgRectF?.width()?:0f) / 2 - thumbImage!!.width / 2, (progressRectF?.top?:0f), mPaint)
            thumbRectF?.let {
                canvas.drawBitmap(thumbImage!!, thumbImageSrc, it, mThumbPaint)
            }
        } else {
            if (thumbRectF != null && mPaint != null)
               canvas.drawRoundRect(thumbRectF!!, thumbRadius, thumbRadius, mThumbPaint!!)
        }

        canvas.restore()
    }

    fun setOnProgressChangedListener(amSeekBarListnter: OnProgressChangedListener?) {
        onProgressChangedListnter = amSeekBarListnter
    }

    interface OnProgressChangedListener {
        fun onProgressChanged(var1: SeekBarVertical?, var2: Float, var3: Boolean)
        fun onStartTrackingTouch(var1: SeekBarVertical?)
        fun onStopTrackingTouch(var1: SeekBarVertical?)
    }

    fun dip2px(dpValue: Float): Int {
        val scale = this.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun isPointFiveStep(isPointFiveStep: Boolean) {
        this.isPointFiveStep = isPointFiveStep
    }

    fun initRange(min: Float, max: Float) {
        minProgress = min
        maxProgress = max
    }

    fun setDragEnable(enable: Boolean) {
        isDragEnable = enable
    }

    fun setOpenState(isOpen: Boolean) {
        progress = if (isOpen) maxProgress else minProgress
        this.invalidate()
    }

    fun setRange(min: Float, max: Float) {
        minProgress = min
        maxProgress = max
        if (progress >= max) {
            progress = max
        }
        if (progress <= min) {
            progress = min
        }
        this.invalidate()
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        this.listener = listener
        super.setOnClickListener(listener)
    }

    private fun getValue(value: Float): Float {
        return if (isPointFiveStep) {
            var itemper = Math.round(value * 100)
            val lasti = itemper % 100
            if (lasti < 25) itemper -= lasti else if (lasti in 25..75) itemper = itemper - lasti + 50 else if (lasti > 75) itemper = itemper - lasti + 100
            val b = BigDecimal((itemper / 100.0f).toDouble())
            b.setScale(1, BigDecimal.ROUND_HALF_UP).toFloat()
        } else {
            Math.round(value).toFloat()
        }
    }
}