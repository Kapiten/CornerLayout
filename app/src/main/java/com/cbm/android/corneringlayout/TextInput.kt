package com.cbm.android.corneringlayout

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.setPadding
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
    var btn: TextView? = null
    private var isKeyboardShowing = false
    var ctext: Int = 0; var passwordToggle = false; var showOption = false
    var text = ""; var textSize = -1f; var spTextSize=-1
    var hint = "" ; var hintColor = Color.BLACK; var hintColors: ColorStateList? = null
    var textColor = Color.BLACK; var textColors: ColorStateList? = null
    var tintOption = Color.parseColor("#FF808080"); var tintOptions: ColorStateList? = null
    var optionsDrawable: Drawable? = null; var optionsDrawableGravity = Gravity.CENTER_VERTICAL
    var sizeOption:Int=0; var sizeOptionH:Int=0; var sizeOptionW:Int=0;
//    var passwordToggle: Boolean = false
//    var textPadding:Int=0; var textPaddingTop:Int=0; var textPaddingBottom:Int=0; var textPaddingLeft:Int=0; var textPaddingStart:Int=0; var textPaddingRight:Int=0; var textPaddingEnd:Int=0;

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

    private fun create(): TextInput {
        when (ctext) {
            1 -> { contentText = CornerLayout.ContentText.Simple }
            2 -> { contentText = CornerLayout.ContentText.Advanced }
        }
        if(text==null){text=""}
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
                                content.lstet.transformationMethod =
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

                    if(showOption) {
                        content.lstopt.visibility = VISIBLE
                        btn = content.lstopt
                        if(optionsDrawable!=null) {
                            content.lstopt.setText("")
                            content.lstopt.background = optionsDrawable
                            if (tintOptions != null) {
                                content.lstopt.backgroundTintList=(tintOptions)
                            } else {
                                content.lstopt.backgroundTintList=(
                                        getCornerLayout().makeCurrentStateColor(
                                            tintOption
                                        ))
                            }

//                            if(sizeOption>0) {
//                                var optS = sizeOption
//                                optS = if(optS>40){40}else{optS}
//                                content.lstopt.layoutParams = LinearLayout.LayoutParams(optS, optS)
//                            } else {
//                                var optSH = if(sizeOptionH>0){sizeOptionH}else{26}
//                                optSH = if(optSH>40){40}else{optSH}
//                                var optSW = if(sizeOptionW>0){sizeOptionW}else{26}
//                                optSW = if(optSW>40){40}else{optSW}
//                            }
                            content.lstopt.layoutParams = LinearLayout.LayoutParams(sizeOption, sizeOption)

                            (content.lstopt.layoutParams as LinearLayout.LayoutParams).gravity=optionsDrawableGravity
                        } else {
//                            if(sizeOption>0) {
//                                var optS = sizeOption.toFloat()*0.5F
//                                optS = if(optS>40){40F}else{optS}
//                                content.lstopt.setTextSize(TypedValue.COMPLEX_UNIT_SP, optS)
//                            } else {
//                            }
                                var sohw = if(sizeOption>0){sizeOption}else{if(sizeOption>0){sizeOption}else{0}}.toFloat()
//                                sohw = if(sohw>40){40F}else{sohw}
                                content.lstopt.setTextSize(TypedValue.COMPLEX_UNIT_SP, if(sohw>0){(sohw*0.5F)}else{26F})
                            try { (content.lstet.layoutParams as LinearLayout.LayoutParams).rightMargin = -(context.getDrawable(R.drawable.ic_visibility_24)!!.toBitmap().width + content.lstopt.textSize.toInt())
                            }catch(ex:Exception){ex.printStackTrace()}
                            if (tintOptions != null) {
                                content.lstopt.setTextColor(tintOptions)
                            } else {
                                content.lstopt.setTextColor(
                                    getCornerLayout().makeCurrentStateColor(
                                        tintOption
                                    )
                                )
                            }
//                            content.lstopt.setTextSize(
//                                TypedValue.COMPLEX_UNIT_PX,
//                                et!!.textSize*1f
//                            )
                        }
                        content.lstopt.setOnClickListener { et!!.text.clear(); et!!.requestFocus() }
                    } else {content.lstopt.visibility = GONE; content.lstopt.setOnClickListener(null); btn=null}

                    if(content.lstet!=null) {
                        if (text != null) {
                            content.lstet.text.clear();content.lstet.text.append(text)
                        } else {
                            content.lstet.text.clear()
                        }
                    }
                    if(hint!=null)content.lstet.hint = hint
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
                    if(spTextSize>=0) {content.lstet.setTextSize(TypedValue.COMPLEX_UNIT_SP, spTextSize.toFloat())}
                    else if (textSize >= 0) {
                        content.lstet.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
                    }
                    
//                    if(textPadding>0) { content.lstet.setPadding(textPadding) }
//                    else { content.lstet.setPadding(if(textPaddingLeft>0){textPaddingLeft}else{textPaddingStart}, textPaddingTop, if(textPaddingRight>0){textPaddingRight}else{textPaddingEnd}, textPaddingBottom); }

                    content.lstet.onFocusChangeListener = OnFocusChangeListener { v, hasFocus -> textHasFocus(hasFocus) }
                } else if (contentText!!.equals(CornerLayout.ContentText.Advanced)) {
                    (content as LayoutAdvancedTextBinding)
                    et = content.lit.editText
                    if(passwordToggle) {
                        content.lit.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                        et!!.setSingleLine(true)
                        et!!.transformationMethod=PasswordTransformationMethod.getInstance()
                        et!!.inputType=(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS + InputType.TYPE_TEXT_VARIATION_PASSWORD)
//                            et!!.setImportantForAutofill(IMPORTANT_FOR_AUTOFILL_NO)
                    }

                    if(showOption) {
                        content.litopt.visibility = VISIBLE
                        btn=content.litopt
                        content.litopt.setOnClickListener { et!!.text.clear(); et!!.requestFocus() }
                        if(optionsDrawable!=null) {
                            content.litopt.setText("")
                            content.litopt.background = optionsDrawable
                            content.litopt.layoutParams = LinearLayoutCompat.LayoutParams(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics)),
                                Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24f, resources.displayMetrics)))
                            if (tintOptions != null) {
                                content.litopt.backgroundTintList=(tintOptions)
                            } else {
                                content.litopt.backgroundTintList=(getCornerLayout().makeCurrentStateColor(tintOption))
                            }

                            if(sizeOption>0) {
                                var optS = if(sizeOption>0){sizeOption}else{26}
                                optS = if(optS>40){40}else{optS}
                                content.litopt.layoutParams = LinearLayout.LayoutParams(optS, optS)
                            } else {
                                var optSH = if(sizeOptionH>0){sizeOptionH}else{26}
                                optSH = if(optSH>40){40}else{optSH}
                                var optSW = if(sizeOptionW>0){sizeOptionW}else{26}
                                optSW = if(optSW>40){40}else{optSW}
                                content.litopt.layoutParams = LinearLayout.LayoutParams(optSH, optSW)
                            }
                        } else {
                            if(sizeOption>0) {
                                var optS = sizeOption.toFloat()*0.5F
                                optS = if(optS>40){40F}else{optS}
                                content.litopt.setTextSize(TypedValue.COMPLEX_UNIT_SP, optS)
                            } else {
                                var optSH = if(sizeOptionH>0){sizeOptionH}else{26}
                                optSH = if(optSH>40){40}else{optSH}
                                var optSW = if(sizeOptionW>0){sizeOptionW}else{26}
                                optSW = if(optSW>40){40}else{optSW}
                                val sohw = if(optSH>0){optSH}else{if(optSW>0){optSW}else{0F}}
                                content.litopt.setTextSize(TypedValue.COMPLEX_UNIT_SP, if(sohw.toFloat()>0){(sohw.toFloat()*0.5F)}else{26F})
                            }

                            if(tintOptions!=null) {content.litopt.setTextColor(tintOptions)}
                            else {content.litopt.setTextColor(getCornerLayout().makeCurrentStateColor(tintOption))}
//                            content.litopt.setTextSize(TypedValue.COMPLEX_UNIT_SP, spTextSize.toFloat())
                        }
                        (content .litopt.layoutParams as LinearLayout.LayoutParams).gravity = optionsDrawableGravity
                    } else {content.litopt.visibility = GONE; content.litopt.setOnClickListener(null); btn=null}

//                    if(textPadding>0) { content.litET.setPadding(textPadding) }
//                    else { content.litET.setPadding(if(textPaddingLeft>0){textPaddingLeft}else{textPaddingStart}, textPaddingTop, if(textPaddingRight>0){textPaddingRight}else{textPaddingEnd}, textPaddingBottom); }

                    if (content.lit.editText!=null){
                        if(text != null) {
                        content.lit.editText!!.text.clear()
                        content.lit.editText!!.text.append(text)
                    } else {content.lit.editText!!.text.clear()}}
                    if(hint!=null){content.lit.setHint(hint)}
                    if(hint!=null){content.lit.hint = hint}
                    if (hintColors != null) {
                        content.lit.setHintTextColor(hintColors)
                    }
                    if (textColors != null) {
                        content.lit.editText!!.setTextColor(textColors)
                    } else if (textColor >= 0) {
                        content.lit.editText!!.setTextColor(textColor)
                    }
                    if(spTextSize>=0) {content.lit.editText!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, spTextSize.toFloat())}
                    else if (textSize >= 0) {
                        content.lit.editText!!.setTextSize(textSize)
                    }

                    content.lit.editText!!.onFocusChangeListener = OnFocusChangeListener { v, hasFocus -> textHasFocus(hasFocus) }
                }
            }
        }
        return this
    }

    fun createDefault(): TextInput {
        ctext = CornerLayout.ContentText.Simple.ordinal+1
        hint = "Sample"
        textSize = resources.getDimension(R.dimen.s4dp)
        return create()
    }

    fun create(attrs: AttributeSet?): TextInput {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CornerLayout)
        if(typedArray!=null) {
            try {
                ctext = typedArray!!.getInt(R.styleable.CornerLayout_contentText, 0)
                try{text = typedArray!!.getString(R.styleable.CornerLayout_text)!!}catch (ex:Exception){ex.printStackTrace();text=""}
                try{hint = typedArray!!.getString(R.styleable.CornerLayout_hint)!!} catch(ex:Exception) {ex.printStackTrace();hint=""}
                hintColor = if(typedArray!!.getString(R.styleable.CornerLayout_hintColor)!!.startsWith("#"))
                {Color.parseColor(typedArray!!.getString(R.styleable.CornerLayout_hintColor))}
                else {typedArray!!.getColor(R.styleable.CornerLayout_hintColor, Color.BLACK)}
                hintColors = typedArray!!.getColorStateList(R.styleable.CornerLayout_hintColor)
                textColor = typedArray!!.getColor(R.styleable.CornerLayout_textColor, Color.BLACK)
                textColors = typedArray!!.getColorStateList(R.styleable.CornerLayout_textColor)
                spTextSize = typedArray!!.getInteger(R.styleable.CornerLayout_spTextSize, -1)
                textSize = typedArray!!.getDimension(R.styleable.CornerLayout_textSize, -1f)
                passwordToggle =
                    typedArray!!.getBoolean(
                        R.styleable.CornerLayout_passwordToggleEnabled,
                        false
                    )
                optionsDrawable =
                    typedArray!!.getDrawable(R.styleable.CornerLayout_optionsDrawable)
                showOption = typedArray!!.getBoolean(R.styleable.CornerLayout_showOption, false)
                tintOption = typedArray!!.getColor(
                    R.styleable.CornerLayout_tintOption,
                    Color.parseColor("#FF808080")
                )
                tintOptions = typedArray!!.getColorStateList(R.styleable.CornerLayout_tintOption)
                sizeOption = typedArray!!.getDimensionPixelSize(R.styleable.CornerLayout_sizeOption, 26)
                sizeOptionH = typedArray!!.getDimensionPixelSize(R.styleable.CornerLayout_sizeOptionH, 26)
                sizeOptionW = typedArray!!.getDimensionPixelSize(R.styleable.CornerLayout_sizeOptionW, 26)
                optionsDrawableGravity = typedArray!!.getInt(R.styleable.CornerLayout_optionsDrawableGravity, optionsDrawableGravity)
//                textPadding=typedArray!!.getDimension(R.styleable.CornerLayout_textPadding, 0f).toInt()
//                textPaddingTop=typedArray!!.getDimension(R.styleable.CornerLayout_textPaddingTop, 0f).toInt()
//                textPaddingLeft=typedArray!!.getDimension(R.styleable.CornerLayout_textPaddingLeft, 0f).toInt()
//                textPaddingRight=typedArray!!.getDimension(R.styleable.CornerLayout_textPaddingRight, 0f).toInt()
//                textPaddingStart=typedArray!!.getDimensionPixelSize(R.styleable.CornerLayout_textPaddingStart, 0).toInt()
//                textPaddingEnd=typedArray!!.getDimensionPixelOffset(R.styleable.CornerLayout_textPaddingEnd, 0).toInt()
//                textPaddingBottom=typedArray!!.getDimension(R.styleable.CornerLayout_textPaddingBottom, 0f).toInt()
                return create()
            } catch (ex: Exception) { ex.printStackTrace(); return createDefault()
            } /*finally { typedArray.recycle() }*/
        } else {return createDefault()}
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

    fun setTextType(type:CornerLayout.ContentText) { ctext = if(type.equals(CornerLayout.ContentText.Simple)){1}else{2} }

    fun getTextType(): CornerLayout.ContentText {return if(ctext==1){CornerLayout.ContentText.Simple}else{CornerLayout.ContentText.Advanced}}

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

    fun getView(): View {
        return if(view==null) this else view!!
    }

    fun setViewBinding(viewBinding: ViewBinding?) {
        this.viewBinding = viewBinding
    }

    fun getViewBinding(): ViewBinding? {
        return this.viewBinding
    }

    fun setOnOptionsClick(listener: OnClickListener) {
        if(btn!=null)btn!!.setOnClickListener(listener)
    }

    companion object {
        val TAG = TextInput.javaClass.simpleName
    }

}