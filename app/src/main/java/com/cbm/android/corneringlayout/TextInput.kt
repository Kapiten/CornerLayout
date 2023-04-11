package com.cbm.android.corneringlayout

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.cbm.android.corneringlayout.databinding.LayoutAdvancedTextBinding
import com.cbm.android.corneringlayout.databinding.LayoutSimpleTextBinding
import com.google.android.material.textfield.TextInputLayout

//#set { tebogo.sibiya = "thabiso_ramolobeng" }

/*
* Created by tebogo.sibiya on 14/02/2023.
*/class TextInput: View {
    private var view: View? = null
    private var viewBinding: ViewBinding? = null
    private var contentText: CornerLayout.ContentText? = null
    private var viewGroup: ViewGroup? = null
    private var typedArray: TypedArray? = null
    var et: EditText? = null
    private var isKeyboardShowing = false
    var ctext: Int = 0
    var text = ""
    var hint = ""
    var hintColor = Color.BLACK
    var hintColors: ColorStateList? = null
    var textColor = Color.BLACK
    var textColors: ColorStateList? = null
    var textSize = -1f
//    var passwordToggle: Boolean = false

    constructor(context: Context?, viewGroup: ViewGroup?) : super(context) {
        this.viewGroup = viewGroup
    }

    constructor(context: Context?, attrs: AttributeSet?, viewGroup: ViewGroup?) : super(
        context,
        attrs
    ) {
        this.viewGroup = viewGroup
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        viewGroup: ViewGroup?
    ) : super(context, attrs, defStyleAttr) {
        this.viewGroup = viewGroup
    }

    fun create(): TextInput {
        return this
    }

    fun create(attrs: AttributeSet?): TextInput {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CornerLayout)
        if (typedArray != null) {
            val ctext = typedArray!!.getInt(R.styleable.CornerLayout_contentText, 0)
            val text = typedArray!!.getString(R.styleable.CornerLayout_text)
            val hint = typedArray!!.getString(R.styleable.CornerLayout_hint)
            val hintColor = typedArray!!.getColor(R.styleable.CornerLayout_hintColor, Color.BLACK)
            val hintColors = typedArray!!.getColorStateList(R.styleable.CornerLayout_hintColor)
            val textColor = typedArray!!.getColor(R.styleable.CornerLayout_textColor, Color.BLACK)
            val textColors = typedArray!!.getColorStateList(R.styleable.CornerLayout_textColor)
            val textSize = typedArray!!.getDimension(R.styleable.CornerLayout_textSize, -1f)
            val passwordToggle = typedArray!!.getBoolean(R.styleable.CornerLayout_passwordToggleEnabled, false)

            when (ctext) {
                1 -> {
                    contentText = CornerLayout.ContentText.Simple
                }
                2 -> {
                    contentText =
                        CornerLayout.ContentText.Advanced
                }
            }
            if (contentText != null) {
                if (contentText!!.equals(CornerLayout.ContentText.Simple)) {
                    setViewBinding(LayoutSimpleTextBinding.inflate(
                        LayoutInflater.from(context)))

                } else if (contentText!!.equals(CornerLayout.ContentText.Advanced)) {
                    try {
                        if (this.viewGroup != null) {
                            setViewBinding(LayoutAdvancedTextBinding.inflate(
                                LayoutInflater.from(context)))
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                val content = getViewBinding()?:getView()
                if (content != null) {
                    if (contentText!!.equals(CornerLayout.ContentText.Simple)) {
                        (content as LayoutSimpleTextBinding)
                        et = content.lstet
                        if(passwordToggle){
                            content.lstiv.visibility=View.VISIBLE
                            et!!.setSingleLine(true)
                            et!!.transformationMethod=
                                PasswordTransformationMethod.getInstance()
                            et!!.inputType=(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS + InputType.TYPE_TEXT_VARIATION_PASSWORD)

                            content.lstiv.setOnClickListener {
                                if(content.lstet.transformationMethod==null) {
                                    content.lstiv.setImageResource(R.drawable.ic_visibility_24)
                                    content.lstet.transformationMethod=
                                        PasswordTransformationMethod.getInstance()
                                    et!!.inputType=(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS + InputType.TYPE_TEXT_VARIATION_PASSWORD)
                                } else {
                                    content.lstiv.setImageResource(R.drawable.ic_visibility_off_24)
                                    content.lstet.transformationMethod=null
                                    et!!.inputType=InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS + InputType.TYPE_CLASS_TEXT
                                }
                            }
                        } else {
                            et!!.transformationMethod=null
                        }

                        if (text != null && !text.isEmpty()) {
                            content.lstet.text.clear()
                            content.lstet.text.append(text)
                        } else {
                            content.lstet.text.append("text")
                        }
                        content.lstet.hint = hint
                        if (hintColors != null) {
                            content.lstet.setHintTextColor(hintColors)
                        } else if (hintColor < 0) {
                            content.lstet.setHintTextColor(hintColor)
                        }
                        if (textColors != null) {
                            content.lstet.setTextColor(textColors)
                        } else if (textColor < 0) {
                            content.lstet.setTextColor(textColor)
                        }
                        if (textSize >= 0) {
                            content.lstet.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                        }

                        content.lstet.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                            textHasFocus(hasFocus)
                        }
                    } else if (contentText!!.equals(CornerLayout.ContentText.Advanced)) {
                        (content as LayoutAdvancedTextBinding)
                        et = content.lit.editText
                        if(passwordToggle) {
                            content.lit.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                            et!!.setSingleLine(true)
                            et!!.transformationMethod=
                                PasswordTransformationMethod.getInstance()
                            et!!.inputType=(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS + InputType.TYPE_TEXT_VARIATION_PASSWORD)
//                            et!!.setImportantForAutofill(IMPORTANT_FOR_AUTOFILL_NO)
                        }
                        if (text != null && !text.isEmpty()) {
                            content.lit.editText!!.text.clear()
                            content.lit.editText!!.text.append(text)
                        }
                        content.lit.hint = hint
                        if (hintColors != null) {
                            content.lit.setHintTextColor(hintColors)
                        }
                        if (textColors != null) {
                            content.lit.editText!!.setTextColor(textColors)
                        } else if (textColor >= 0) {
                            content.lit.editText!!.setTextColor(textColor)
                        }
                        if (textSize >= 0) {
                            content.lit.editText!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                        }

                        content.lit.editText!!.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                            textHasFocus(hasFocus)
                        }
                    }
                }
            }
        }

        typedArray.recycle()

        return this
    }

    private fun textHasFocus(hasFocus: Boolean) {
        if (hasFocus) {
            if (getCornerLayout().fillColors != null) {
                getCornerLayout().getShapeDrawable()!!.fillColor = getCornerLayout()
                    .makeCurrentStateColor(
                        getCornerLayout().fillColors!!.getColorForState(
                            intArrayOf(android.R.attr.state_pressed, android.R.attr.state_focused),
                            Color.TRANSPARENT
                        )
                    )
            } else {
                if (getCornerLayout().fillColor >= 0) {
                    getCornerLayout().getShapeDrawable()!!.fillColor =
                        getCornerLayout().makeCurrentStateColor(
                            getCornerLayout().fillColor
                        )
                }
            }

            if (getCornerLayout().strokeColors != null) {
                getCornerLayout().getShapeDrawable()!!.strokeColor = getCornerLayout()
                    .makeCurrentStateColor(
                        getCornerLayout().strokeColors!!.getColorForState(
                            intArrayOf(android.R.attr.state_pressed, android.R.attr.state_focused),
                            Color.TRANSPARENT
                        )
                    )
            } else {
                if (getCornerLayout().strokeColor >= 0) {
                    getCornerLayout().getShapeDrawable()!!.strokeColor =
                        getCornerLayout().makeCurrentStateColor(
                            getCornerLayout().strokeColor
                        )
                }
            }

        } else {
            if (getCornerLayout().fillColors != null) {
                getCornerLayout().getShapeDrawable()!!.fillColor =
                    getCornerLayout().fillColors!!
            } else {
                getCornerLayout().getShapeDrawable()!!.fillColor = getCornerLayout()
                    .makeCurrentStateColor(getCornerLayout().fillColor)
            }

            if (getCornerLayout().strokeColors != null) {
                getCornerLayout().getShapeDrawable()!!.strokeColor =
                    getCornerLayout().strokeColors!!
            } else {
                getCornerLayout().getShapeDrawable()!!.strokeColor = getCornerLayout()
                    .makeCurrentStateColor(getCornerLayout().strokeColor)
            }
        }
    }

    private fun getCornerLayout() = (viewGroup!!.parent.parent as CornerLayout)

    fun setContentText(contentText: CornerLayout.ContentText): TextInput {
        this.contentText = contentText
        return this
    }

    fun getContentText(): CornerLayout.ContentText? {
        return this.contentText
    }

    fun setViewGroup(viewGroup: ViewGroup): TextInput {
        this.viewGroup = viewGroup
        return this
    }

    fun getViewGroup(): ViewGroup? {
        return this.viewGroup
    }

    fun setTypedArray(typedArray: TypedArray?): TextInput {
        this.typedArray = typedArray
        return this
    }

    fun getTypedArray(): TypedArray? {
        return this.typedArray
    }

    fun setView(view: View?) {
        this.view = view
    }

    fun getView(): View? {
        return this.view
    }

    fun setViewBinding(viewBinding: ViewBinding?) {
        this.viewBinding = viewBinding
    }

    fun getViewBinding(): ViewBinding? {
        return this.viewBinding
    }

    companion object {
        val TAG = TextInput.javaClass.simpleName
    }

}