package com.android.xamoom.htmltextview.Spans

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.TextPaint
import android.text.style.LeadingMarginSpan

class NumberSpan(first: Int, var numberText: String,
                 textPaint: TextPaint) : LeadingMarginSpan.Standard(first) {
  var textWidth: Int = 0
  var leadingMargin: Int = 0
  var innerTextMargin: Int = 10

  init {
    textWidth = textPaint.measureText(numberText).toInt()
    numberText = String.format("%s.", numberText)
    leadingMargin = first
  }

  override fun drawLeadingMargin(c: Canvas?, p: Paint?, x: Int, dir: Int, top: Int, baseline: Int,
                                 bottom: Int, text: CharSequence?, start: Int, end: Int, first: Boolean,
                                 l: Layout?) {
    c?.drawText(numberText, (x + leadingMargin).toFloat(), baseline.toFloat(), p)
  }

  override fun getLeadingMargin(p0: Boolean): Int {
    return textWidth + leadingMargin + innerTextMargin
  }
}