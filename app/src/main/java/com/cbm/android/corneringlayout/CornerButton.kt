package com.cbm.android.corneringlayout

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import java.math.BigDecimal

//#set { tebogo.sibiya = "thabiso_ramolobeng" }

/*
* Created by tebogo.sibiya on 30/08/2023.
*/class CornerButton: androidx.appcompat.widget.AppCompatTextView {
    private var isKeyboardShowing = false

    //corners
    private var shapeAppearanceModel: ShapeAppearanceModel? = null
    private var shapeDrawable: MaterialShapeDrawable? = null
    private var cornerType: Int = 1
    private var radius = 0f
    private var topLeftRadius = 0f
    private var topRightRadius = 0f
    private var bottomLeftRadius = 0f
    private var bottomRightRadius = 0f

    var fillColor: Int = Color.TRANSPARENT
    var fillColors: ColorStateList? = null
    var strokeColor: Int = Color.GRAY
    var strokeColors: ColorStateList? = null
    private var strokeWidth: Float = -1f
    private var colorStates: Array<IntArray>? = null
    private var colorColors: IntArray? = null

    private lateinit var v: View;
    private lateinit var tv: TextView;

    constructor(context: Context) : super(context){initView()}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){initView();setCustoms(attrs)}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr) {initView(); setCustoms(attrs)}

    fun initView() { /*View.inflate(context, R.layout.layout_corner_button, null);*/ }

    fun setCustoms(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CornerButton)
//        val conditional = ta!!.getInt(R.styleable.CornerButton_cbConditionView, 0)
        gravity=Gravity.CENTER
        setText(ta.getString(R.styleable.CornerButton_cbText))
        Log.d(TAG, "CornerButton_cbText: " + ta.getString(R.styleable.CornerButton_cbText)+"\ntvBtn.getText="+text)
        setTextColor(ta.getColor(R.styleable.CornerButton_cbTextColor, Color.BLACK))
//        setTextColor(ta.getColorStateList(R.styleable.CornerLayout_textColor))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, ta.getInteger(R.styleable.CornerButton_cbTextSize, -1).toFloat())
        setRadius(ta.getDimension(R.styleable.CornerButton_cbRadius, 0f))
        if(ta.getDimension(R.styleable.CornerButton_cbTopLeftRadius, 0f)>0) {
            setTopLeftRadius(ta.getDimension(R.styleable.CornerButton_cbTopLeftRadius, 0f)) }

        if(ta.getDimension(R.styleable.CornerButton_cbTopRightRadius, 0f)>0) {
            setTopRightRadius(ta.getDimension(R.styleable.CornerButton_cbTopRightRadius, 0f))
        }
        if(ta.getDimension(R.styleable.CornerButton_cbBottomLeftRadius, 0f)>0) {
            setBottomLeftRadius(ta.getDimension(R.styleable.CornerButton_cbBottomLeftRadius, 0f))
        }
        if(ta.getDimension(R.styleable.CornerButton_cbBottomRightRadius, 0f)>0) {
            setBottomRightRadius(ta.getDimension(R.styleable.CornerButton_cbBottomRightRadius, 0f)) }

        cornerType = ta.getInt(R.styleable.CornerButton_cbCornerType, 1)
//        if(cornerRadius>0){radius = cornerRadius.toFloat()}
        if(radius>0) {
            shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
                .setAllCorners(if(cornerType==0) CornerFamily.ROUNDED else CornerFamily.CUT, radius)
                .build()
        } else {
            shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
                .setTopLeftCorner(if(cornerType==0) CornerFamily.ROUNDED else CornerFamily.CUT, topLeftRadius)
                .setTopRightCorner(if(cornerType==0) CornerFamily.ROUNDED else CornerFamily.CUT, topRightRadius)
                .setBottomLeftCorner(if(cornerType==0) CornerFamily.ROUNDED else CornerFamily.CUT, bottomLeftRadius)
                .setBottomRightCorner(if(cornerType==0) CornerFamily.ROUNDED else CornerFamily.CUT, bottomRightRadius)
                .build()
        }

        shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel!!)

        if(ta.getDrawable(R.styleable.CornerButton_cbBackgroundDrawable)!=null) {
            this.background = ta.getDrawable(R.styleable.CornerButton_cbBackgroundDrawable)
        } else {
            if(ta.getColorStateList(R.styleable.CornerButton_cbFillColor)!=null) {
                shapeDrawable!!.fillColor = ta.getColorStateList(R.styleable.CornerButton_cbFillColor)
                fillColors = shapeDrawable!!.fillColor
            } else if(ta.getColor(R.styleable.CornerButton_cbFillColor, Color.TRANSPARENT)< Color.TRANSPARENT) {
                shapeDrawable!!.fillColor = makeCurrentStateColor(ta.getColor(R.styleable.CornerButton_cbFillColor, Color.TRANSPARENT))
                fillColor = ta.getColor(R.styleable.CornerButton_cbFillColor, Color.TRANSPARENT)
            } else {
                colorStates = arrayOf<IntArray>(
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_pressed, android.R.attr.state_focused),
                    intArrayOf(-android.R.attr.state_enabled)
                )
                colorColors =
                    intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT)
                shapeDrawable!!.fillColor = (ColorStateList(colorStates, colorColors))
            }

            if(ta.getColorStateList(R.styleable.CornerButton_cbStrokeColor)!=null) {
                shapeDrawable!!.strokeColor = ta.getColorStateList(R.styleable.CornerButton_cbStrokeColor)
                strokeColors = shapeDrawable!!.strokeColor
            } else if(ta.getColor(R.styleable.CornerButton_cbStrokeColor, Color.TRANSPARENT)< Color.TRANSPARENT) {
                shapeDrawable!!.strokeColor = makeCurrentStateColor(ta.getColor(R.styleable.CornerButton_cbStrokeColor, Color.TRANSPARENT))
                strokeColor = ta.getColor(R.styleable.CornerButton_cbStrokeColor, Color.TRANSPARENT)
            } else {
                colorStates = arrayOf<IntArray>(
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_pressed, android.R.attr.state_focused),
                    intArrayOf(-android.R.attr.state_enabled)
                )
                colorColors =
                    intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT)
                shapeDrawable!!.strokeColor = (ColorStateList(colorStates, colorColors))
            }

            shapeDrawable!!.strokeWidth = ta.getDimension(R.styleable.CornerButton_cbStrokingWidth, -1f);
        }
        ta.recycle()

        viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            var r = Rect();
            getWindowVisibleDisplayFrame(r);
            var screenHeight = getRootView().getHeight();

            var keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                if (!isKeyboardShowing) { isKeyboardShowing = true }
            } else {
                // keyboard is closed
                if (isKeyboardShowing) { isKeyboardShowing = false }
            }

        })

        bgShape()
    }

    private fun bgShape() {
        if(radius>=0||(topLeftRadius>=0||topRightRadius>=0||bottomLeftRadius>=0||bottomRightRadius>=0)) { ViewCompat.setBackground(this, shapeDrawable!!) }
    }

    fun setRadius(value: Float) {
        radius = value
    }

    fun getRadius(): Float {
        return radius
    }

    fun setTopLeftRadius(value: Float) {
        topLeftRadius = value
    }

    fun setTopRightRadius(value: Float) {
        topRightRadius = value
    }

    fun setBottomLeftRadius(value: Float) {
        bottomLeftRadius = value
    }

    fun setBottomRightRadius(value: Float) {
        bottomRightRadius = value
    }

    fun getTopLeftRadius(): Float {
        return topLeftRadius
    }

    fun getTopRightRadius(): Float {
        return topRightRadius
    }

    fun getBottomLeftRadius(): Float {
        return bottomLeftRadius
    }

    fun getBottomRightRadius(): Float {
        return bottomRightRadius
    }

    fun setStrokeColoring(color: Int) {
        strokeColor = color
        getShapeDrawable()!!.strokeColor = makeCurrentStateColor(strokeColor)
        bgShape()
    }

    fun setStrokeColoring(color: ColorStateList) {
        strokeColors = color
        getShapeDrawable()!!.strokeColor = strokeColors
        bgShape()
    }

    fun setFilling(color: Int) {
        fillColor = color
        getShapeDrawable()!!.fillColor = makeCurrentStateColor(strokeColor)
        bgShape()
    }

    fun setFilling(color: ColorStateList) {
        fillColors = color
        getShapeDrawable()!!.fillColor = fillColors
        bgShape()
    }

    fun setStrokeWidth(width:Float) {strokeWidth=width}

    fun getStrokeWidth(): Int {return BigDecimal(strokeWidth.toString()).toInt()}

    fun getShapeDrawable(): MaterialShapeDrawable? {
        return shapeDrawable
    }

    fun makeCurrentStateColor(value: Int): ColorStateList {
        colorStates = arrayOf<IntArray>(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_pressed, android.R.attr.state_focused),
            intArrayOf(-android.R.attr.state_enabled)
        )
        colorColors =
            intArrayOf(value, Color.TRANSPARENT, Color.parseColor("#FFB0B0B0"))

        return ColorStateList(colorStates, colorColors)
    }

    companion object {
        val TAG = CornerButton.Companion.javaClass.simpleName
    }
}