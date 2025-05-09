package com.example.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var listener: (() -> Unit)? = null

    private var isPlaying = false

    private val drawRect = RectF()

    private var bitPlay: Bitmap? = null
    private var bitPause: Bitmap? = null
    private var playResId: Int = 0
    private var pauseResId: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlaybackButtonView)
        playResId = typedArray.getResourceId(R.styleable.PlaybackButtonView_iconPlay, 0)
        pauseResId = typedArray.getResourceId(R.styleable.PlaybackButtonView_iconPause, 0)

        typedArray.recycle()

    }

    fun addListener(action: () -> Unit) {
        listener = action
    }

    private fun onPlayButtonClick() {
        listener?.invoke()
    }

    fun setPlayBtnState(playing: Boolean) {
        isPlaying = playing
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                onPlayButtonClick()
                return true
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitPlay = vectorToBitmap(playResId)
        bitPause = vectorToBitmap(pauseResId)

        drawRect.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = resolveSizeAndState(100, widthMeasureSpec, 0).coerceAtMost(
            resolveSizeAndState(100, heightMeasureSpec, 0)
        )
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val bitmap = if (isPlaying) bitPause else bitPlay
        bitmap?.let {
            canvas.drawBitmap(it, null, drawRect, null)
        }
    }

    private fun vectorToBitmap(drawableResId: Int): Bitmap? {
        val drawable =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                context.getDrawable(drawableResId) as VectorDrawable
            } else {
                VectorDrawableCompat.create(resources, drawableResId, null)
            }

        drawable?.let {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            it.setBounds(0, 0, width, height)
            it.draw(canvas)
            return bitmap
        }
        return null
    }
}