package com.example.mystoryapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.mystoryapp.R
import com.example.mystoryapp.tools.Matcher
import com.google.android.material.textfield.TextInputLayout

class Email: AppCompatEditText, View.OnTouchListener {
    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }

    private fun init(){
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text.isNullOrEmpty()){
                    (this@Email.parent.parent as TextInputLayout).error = context.getString(R.string.cannot_be_empty)
                }else{
                    if (text?.length!! > 0 && !Matcher.emailValid(text.toString())){
                        (this@Email.parent.parent as TextInputLayout).error = context.getString(R.string.email_is_not_valid)
                    }else {
                        (this@Email.parent.parent as TextInputLayout).error = null
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (text.isNullOrEmpty()){
            (this@Email.parent.parent as TextInputLayout).error = context.getString(R.string.cannot_be_empty)
        }else{
            if (text?.length!! > 0 && !Matcher.emailValid(text.toString())){
                (this@Email.parent.parent as TextInputLayout).error = context.getString(R.string.email_is_not_valid)
            }else {
                (this@Email.parent.parent as TextInputLayout).error = null
            }
        }
        return false
    }

}