package com.hzy.diffuseview.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.hzy.diffuseview.R
import com.hzy.diffuseview.widget.listener.OnCoreImageClickListener

/**
 * 水波纹扩散组件
 */
class WaterRippleDiffuseView : View {

    /**
     * 画笔
     */
    private lateinit var mPaint: Paint

    /**
     * 透明度集合
     */
    private var mAlphas: MutableList<Int> = mutableListOf()

    /**
     * 扩散圆形半径集合
     */
    private var mWidths: MutableList<Int> = mutableListOf()

    /**
     * 扩散圆圈颜色
     */
    @ColorInt
    private var mColor = resources.getColor(R.color.teal_200)

    /**
     * 圆圈中心颜色
     */
    @ColorInt
    private var mCoreColor = resources.getColor(R.color.teal_700)

    /**
     * 中心圆半径
     */
    private var mCoreRadius = 150f

    /**
     * 中心圆图片
     */
    private var mCoreBitmap: Bitmap? = null

    /**
     * 扩散圆宽度
     */
    private var mDiffuseWidth = 3

    /**
     * 最大宽度
     */
    private var mDiffuseMaxWidth = 255

    /**
     * 扩散速度
     */
    private var mDiffuseSpeed = 5

    /**
     * 是否正在扩散中
     */
    private var mIsDiffusing = false

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()

        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.WaterRippleDiffuseView,
            defStyleAttr,
            0
        )
        mColor = ta.getColor(R.styleable.WaterRippleDiffuseView_diffuse_color, mColor)
        mCoreColor = ta.getColor(R.styleable.WaterRippleDiffuseView_diffuse_coreColor, mCoreColor)
        mDiffuseWidth = ta.getInt(R.styleable.WaterRippleDiffuseView_diffuse_width, mDiffuseWidth)
        mDiffuseMaxWidth =
            ta.getInt(R.styleable.WaterRippleDiffuseView_diffuse_maxWidth, mDiffuseMaxWidth)
        mDiffuseSpeed = ta.getInt(R.styleable.WaterRippleDiffuseView_diffuse_speed, mDiffuseSpeed)
        mCoreRadius =
            ta.getFloat(R.styleable.WaterRippleDiffuseView_diffuse_coreRadius, mCoreRadius)
        // 获取中心圆形图片
        val imageId = ta.getResourceId(R.styleable.WaterRippleDiffuseView_diffuse_coreImage, -1)
        if (-1 != imageId) mCoreBitmap = BitmapFactory.decodeResource(resources, imageId)
        ta.recycle()
    }

    /**
     * 初始化
     */
    private fun init() {
        mPaint = Paint()
        // 设置抗锯齿
        mPaint.isAntiAlias = true
        mAlphas.add(255)
        mWidths.add(0)
    }

    override fun invalidate() {
        if (hasWindowFocus()) {
            super.invalidate()
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        // 绘制扩散圆
        mPaint.color = mColor
        // 设置透明度
        for (index in mAlphas.indices) {
            val alpha = mAlphas[index]
            val w = mWidths[index]
            mPaint.alpha = alpha
            // 绘制扩散圆
            canvas?.drawCircle(
                (width / 2).toFloat(),
                (height / 2).toFloat(), mCoreRadius + w, mPaint
            )

            if (alpha > 0 && w < mDiffuseMaxWidth) {
                mAlphas[index] = if (alpha - mDiffuseSpeed > 0) alpha - mDiffuseSpeed else 1
                mWidths[index] = w + mDiffuseSpeed
            }
        }

        // 判断当扩散圆扩散到指定宽度时添加新扩散圆
        if (mWidths[mWidths.size - 1] >= mDiffuseMaxWidth / mDiffuseWidth) {
            mAlphas.add(255)
            mWidths.add(0)
        }

        // 超过10个扩散圆，删除最外层
        if (mWidths.size >= 10) {
            mAlphas.removeAt(0)
            mWidths.removeAt(0)
        }

        // 绘制中心圆及图片
        mPaint.alpha = 255
        mPaint.color = mCoreColor
        canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mCoreRadius, mPaint)

        mCoreBitmap?.let {
            canvas?.drawBitmap(
                mCoreBitmap!!,
                (width / 2 - mCoreBitmap!!.width / 2).toFloat(),
                (height / 2 - mCoreBitmap!!.height / 2).toFloat(),
                mPaint
            )
        }

        if (mIsDiffusing) {
            invalidate()
        }
    }

    private var onCoreImageClickListener: OnCoreImageClickListener? = null

    fun setOnCoreImageClickListener(onClickListener: OnCoreImageClickListener) {
        this.onCoreImageClickListener = onClickListener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> { // 0
                val currentX = event.x
                val currentY = event.y
                if (currentX in (width / 2 - mCoreBitmap!!.width / 2).toFloat()..(width / 2 + mCoreBitmap!!.width / 2).toFloat() && currentY in (height / 2 - mCoreBitmap!!.height / 2).toFloat()..(height / 2 + mCoreBitmap!!.height / 2).toFloat()) {
                    onCoreImageClickListener?.onClickCoreImage(this)
                }
            }
            MotionEvent.ACTION_UP -> { // 1

            }
            MotionEvent.ACTION_MOVE -> { // 2

            }
            MotionEvent.ACTION_CANCEL -> { // 3

            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 开始扩散
     */
    fun start() {
        mIsDiffusing = true
        invalidate()
    }

    /**
     * 停止扩散
     */
    fun stop() {
        mIsDiffusing = false
        mAlphas.clear()
        mWidths.clear()
        mAlphas.add(255)
        mWidths.add(0)
        invalidate()
    }

    /**
     * 是否扩散中
     */
    fun isDiffusing(): Boolean {
        return mIsDiffusing;
    }

    /**
     * 设置扩散圆颜色
     */
    fun setColor(colorId: Int) {
        mColor = colorId;
    }

    /**
     * 设置中心圆颜色
     */
    fun setCoreColor(colorId: Int) {
        mCoreColor = colorId;
    }

    /**
     * 设置中心圆图片
     */
    fun setCoreImage(imageId: Int) {
        mCoreBitmap = BitmapFactory.decodeResource(resources, imageId);
    }

    /**
     * 设置中心圆半径
     */
    fun setCoreRadius(radius: Float) {
        mCoreRadius = radius;
    }

    /**
     * 设置扩散圆宽度(值越小宽度越大)
     */
    fun setDiffuseWidth(width: Int) {
        mDiffuseWidth = width;
    }

    /**
     * 设置最大宽度
     */
    fun setDiffuseMaxWidth(maxWidth: Int) {
        mDiffuseMaxWidth = maxWidth;
    }

    /**
     * 设置扩散速度，值越大速度越快
     */
    fun setDiffuseSpeed(speed: Int) {
        mDiffuseSpeed = speed;
    }
}