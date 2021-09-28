package com.congtv5.smartmovie.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View


class ExpandableTextView : androidx.appcompat.widget.AppCompatTextView, View.OnClickListener {
    companion object {
        private const val MAX_LINES = 3
    }

    var isExpanded = false

    var myMaxLines = Int.MAX_VALUE

    constructor(context: Context) : super(context) {
        setOnClickListener(this)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        setOnClickListener(this)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setOnClickListener(this)
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        /* If text longer than MAX_LINES set DrawableBottom - I'm using '...' icon */
        post {
            maxLines = MAX_LINES
        }
    }

    override fun setMaxLines(maxLines: Int) {
        myMaxLines = maxLines
        super.setMaxLines(maxLines)
    }

    override fun onClick(v: View?) {
        /* Toggle between expanded collapsed states */
        maxLines = if (myMaxLines == Int.MAX_VALUE) MAX_LINES else Int.MAX_VALUE
    }

}