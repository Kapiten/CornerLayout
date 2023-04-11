package com.cbm.android.corneringlayout

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import androidx.core.view.ViewCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

//#set { tebogo.sibiya = "thabiso_ramolobeng" }

/*
* Created by tebogo.sibiya on 14/02/2023.
*/class CornerLayout : LinearLayout {
    var ivLeft: ImageView?=null
    var ivRight: ImageView?=null
    var conditionView: ImageView? = null
    var error: String? = ""
    private var llcContainer: LinearLayout? = null
    private var content: Any? = null
    private var contentType: ContentType? = null
    private var contentText: ContentText = ContentText.Simple
    var leftDrawable: Drawable? = null
    var rightDrawable: Drawable? = null
    private var popupWindow: View? = null
    private var popup: PopupWindow? = null
    private var isKeyboardShowing = false

    //corners
    private var shapeAppearanceModel: ShapeAppearanceModel? = null
    private var shapeDrawable: MaterialShapeDrawable? = null
    private var cornerType: Int = 1
    private var radius = -1f
    private var topLeftRadius = -1f
    private var topRightRadius = -1f
    private var bottomLeftRadius = -1f
    private var bottomRightRadius = -1f

    var fillColor: Int = Color.TRANSPARENT
    var fillColors: ColorStateList? = null
    var strokeColor: Int = Color.GRAY
    var strokeColors: ColorStateList? = null
    private var strokeWidth: Float = -1f
    private var colorStates: Array<IntArray>? = null
    private var colorColors: IntArray? = null

    constructor(context: Context?) : super(context)
    {initView();}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    {initView();setCustoms(attrs)}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){initView();setCustoms(attrs)}

    fun initView() {
        View.inflate(context, R.layout.layout_corner, this)
        orientation=VERTICAL
    }

    fun setCustoms(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CornerLayout)
        val ctype = ta!!.getInt(R.styleable.CornerLayout_contentType, 0)
        if(ta.getInt(R.styleable.CornerLayout_contentType, 0)>0) {
            val ll = View.inflate(context, R.layout.layout_content_typed, this)

            ivLeft = ll.findViewById(R.id.llcIVLeft)
            ivRight = ll.findViewById(R.id.llcIVRight)
            llcContainer = ll.findViewById(R.id.llcHolder)

            when(ctype) {
                1-> {
                    content = TextInput(context, llcContainer!!)
                        //.setLeftImage(ivLeft!!)
                        //.setRightImage(ivRight!!)
                        .create(attrs)
//                    (content as TextInput).passwordToggle = ta.getBoolean(R.styleable.CornerLayout_passwordToggleEnabled, false)

                    llcContainer!!.addView(
                        if((content as TextInput).getViewBinding()!=null)
                        {(content as TextInput).getViewBinding()!!.root}
                        else{((content as TextInput).getView())},
                        LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                        )
                    )

//                    content.et!!.isEnabled =
                }
                2->{}
            }
        }
        if(content is TextInput) {
            (content as TextInput).et!!.gravity = gravity
            (content as TextInput).et!!.isEnabled = ta.getBoolean(R.styleable.CornerLayout_enabled, true)
            val texting = !ta.getBoolean(R.styleable.CornerLayout_noText, false);
            (content as TextInput).et!!.isFocusable = texting
            if(!ta.getBoolean(R.styleable.CornerLayout_passwordToggleEnabled, false))
            {(content as TextInput).et!!.inputType = ta.getInt(R.styleable.CornerLayout_inputType, InputType.TYPE_CLASS_TEXT);
            (content as TextInput).et!!.isSingleLine = ta.getBoolean(R.styleable.CornerLayout_singleLine, false);}
        }
        leftDrawable = ta!!.getDrawable(R.styleable.CornerLayout_leftDrawable)
        rightDrawable = ta!!.getDrawable(R.styleable.CornerLayout_rightDrawable)
        val conditional = ta!!.getInt(R.styleable.CornerLayout_conditionView, 0)
        setRadius(ta.getDimension(R.styleable.CornerLayout_radius, -1f))
        if(ta.getDimension(R.styleable.CornerLayout_topLeftRadius, -1f)>0) {
            setTopLeftRadius(ta.getDimension(R.styleable.CornerLayout_topLeftRadius, -1f))
        }
        if(ta.getDimension(R.styleable.CornerLayout_topRightRadius, -1f)>0) {
            setTopRightRadius(ta.getDimension(R.styleable.CornerLayout_topRightRadius, -1f))
        }
        if(ta.getDimension(R.styleable.CornerLayout_bottomLeftRadius, -1f)>0) {
            setBottomLeftRadius(ta.getDimension(R.styleable.CornerLayout_bottomLeftRadius, -1f))
        }
        if(ta.getDimension(R.styleable.CornerLayout_bottomRightRadius, -1f)>0) {
            setBottomRightRadius(ta.getDimension(R.styleable.CornerLayout_bottomRightRadius, -1f))
        }

        popupWindow = LayoutInflater.from(context).inflate(R.layout.popup_window, this, false)
        popup = PopupWindow(
            popupWindow,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
            false
        )
        if(popupWindow!=null){popupWindow!!.setOnClickListener { if(popup!=null&&popup!!.isShowing)popup!!.dismiss() }}

        if(leftDrawable!=null&&ivLeft!=null) {
            ivLeft!!.visibility = View.VISIBLE
            ivLeft!!.setImageDrawable(leftDrawable)
        }

        if(rightDrawable!=null&&ivRight!=null) {
            ivRight!!.visibility = View.VISIBLE
            ivRight!!.setImageDrawable(rightDrawable)
        }
//        if(paddingTop>0) {
//            ivRight!!.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
//            ivLeft!!.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
//            if(content is TextInput) {
//                (content as TextInput).et!!.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
//            }
//            setPadding(0)
//        }
        if(conditional>0) {
            when(conditional) {
                1->{conditionView=ivLeft}
                2->{conditionView=ivRight}
            }
            conditionView!!.visibility=View.GONE
        }

        cornerType = ta.getInt(R.styleable.CornerLayout_cornerType, 1)
        if(radius>0) {
            shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
                .setAllCorners(if(cornerType==0)CornerFamily.ROUNDED else CornerFamily.CUT, radius)
                .build()
        } else {
            shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
                .setTopLeftCorner(if(cornerType==0)CornerFamily.ROUNDED else CornerFamily.CUT, topLeftRadius)
                .setTopRightCorner(if(cornerType==0)CornerFamily.ROUNDED else CornerFamily.CUT, topRightRadius)
                .setBottomLeftCorner(if(cornerType==0)CornerFamily.ROUNDED else CornerFamily.CUT, bottomLeftRadius)
                .setBottomRightCorner(if(cornerType==0)CornerFamily.ROUNDED else CornerFamily.CUT, bottomRightRadius)
                .build()
        }

        shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel!!)
        if(ta.getDrawable(R.styleable.CornerLayout_backgroundDrawable)!=null) {
            this.background = ta.getDrawable(R.styleable.CornerLayout_backgroundDrawable)

        } else {
            if(ta.getColorStateList(R.styleable.CornerLayout_fillColor)!=null) {
                shapeDrawable!!.fillColor = ta.getColorStateList(R.styleable.CornerLayout_fillColor)
                fillColors = shapeDrawable!!.fillColor
            } else if(ta.getColor(R.styleable.CornerLayout_fillColor, Color.TRANSPARENT)<Color.TRANSPARENT) {
                shapeDrawable!!.fillColor = makeCurrentStateColor(ta.getColor(R.styleable.CornerLayout_fillColor, Color.TRANSPARENT))
                fillColor = ta.getColor(R.styleable.CornerLayout_fillColor, Color.TRANSPARENT)
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

            if(ta.getColorStateList(R.styleable.CornerLayout_strokeColor)!=null) {
                shapeDrawable!!.strokeColor = ta.getColorStateList(R.styleable.CornerLayout_strokeColor)
                strokeColors = shapeDrawable!!.strokeColor
            } else if(ta.getColor(R.styleable.CornerLayout_strokeColor, Color.TRANSPARENT)<Color.TRANSPARENT) {
                shapeDrawable!!.strokeColor = makeCurrentStateColor(ta.getColor(R.styleable.CornerLayout_strokeColor, Color.TRANSPARENT))
                strokeColor = ta.getColor(R.styleable.CornerLayout_strokeColor, Color.TRANSPARENT)
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

            shapeDrawable!!.strokeWidth = ta.getDimension(R.styleable.CornerLayout_strokingWidth, -1f);
        }

        isEnabled = ta.getBoolean(R.styleable.CornerLayout_enabled, true)
        if(!isEnabled) {
            if(ta.getColorStateList(R.styleable.CornerLayout_fillColor)==null) {
                shapeDrawable!!.fillColor = makeCurrentStateColor(Color.parseColor("#FFB0B0B0"))

            }
        }
        ta.recycle()

        viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            var r = Rect();
            getWindowVisibleDisplayFrame(r);
            var screenHeight = getRootView().getHeight();

            var keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                if (!isKeyboardShowing) {
                    isKeyboardShowing = true
                }
            } else {
                // keyboard is closed
                if (isKeyboardShowing) {
                    isKeyboardShowing = false
                }
            }

            try {
                if(popup!=null&&popup!!.isShowing) {
                    var yoff = 0
                    if ((this.y + conditionView!!.y) > resources.displayMetrics.heightPixels - 400 || isKeyboardShowing) {
                        popup!!.contentView.measure(
                            View.MeasureSpec.UNSPECIFIED,
                            View.MeasureSpec.UNSPECIFIED
                        )
                        yoff = -1 * ((popup!!.contentView.measuredHeight/2) + this.height)
                    }
                    popup!!.update(conditionView!!, 0, yoff, popup!!.width, popup!!.height)
                }
            } catch(ex:java.lang.Exception) {}
        })

        bgShape()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        bgShape()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return !isEnabled
    }
    private fun bgShape() {
        if(radius>0||(topLeftRadius>0||topRightRadius>0||bottomLeftRadius>0||bottomRightRadius>0)) {
            ViewCompat.setBackground(this, shapeDrawable!!)
        }
    }

    fun getContent(): Any? {
        return content
    }

    fun getContentText(): TextInput? {
        return (content as TextInput)
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

    fun setLeftImage(imageView: ImageView) {
        ivLeft = imageView
    }

    fun getLeftImage(): ImageView? {
        return this.ivLeft
    }

    fun setRightImage(imageView: ImageView) {
        ivRight = imageView
    }

    fun getRightImage(): ImageView? {
        return this.ivRight
    }

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

    fun setErrorMessage(value: String?) {
        error = value
        if(conditionView==null) {conditionView=ivRight}
        if(error!=null&&!error!!.isEmpty()) {
            conditionView!!.visibility = VISIBLE
        } else {
            conditionView!!.visibility = GONE
        }
        if(error != null) {
            conditionView!!.visibility = View.VISIBLE
            if (!popup!!.isShowing) {
                var yoff = 0;
                if((this.y+conditionView!!.y)>resources.displayMetrics.heightPixels-400||isKeyboardShowing) {
                    popup!!.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    yoff = -1*(popup!!.contentView.measuredHeight+this.height)
                }
                if(this.height<1)this.height
                popupWindow!!.findViewById<TextView>(R.id.tvPopupWindow).text =
                    error
                popup!!.showAsDropDown(conditionView!!, 0, yoff)

                Log.d(TAG, "displayMetrics.heightPixels="+resources.displayMetrics.heightPixels
                        +"\nconditionView!!.y="+conditionView!!.y
                        +"\n(this.y+conditionView!!.y)="+(this.y+conditionView!!.y))
            }

        } else {
            conditionView!!.visibility = View.GONE
            if (popup!!.isShowing) {
                popup!!.dismiss()
            }
        }
    }

    fun getErrorMessage(): String? {
        return error
    }

    fun setErrorCondition(value: Boolean, message: String?) {
        if(value){setErrorMessage(message!!)}
        else{setErrorMessage(null)}
    }

    enum class ContentType {Text, Image}
    enum class ContentText {Simple, Advanced}

    companion object {
        val TAG =CornerLayout.javaClass.simpleName
    }
}