package com.android.xamoom.htmltextview.Spans

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.style.ReplacementSpan

class TableCellSpan(val startOffset: Float, val width: Int, backgroundColor: Int?, val padding: Int = 0) :
    ReplacementSpan() {
  var backgroundPaint: Paint? = null

  init {
    if (backgroundColor != null) {
      backgroundPaint = Paint(0)
      backgroundPaint?.color = backgroundColor
    }
  }

  override fun getSize(paint: Paint?, text: CharSequence?, start: Int, end: Int,
                       fm: Paint.FontMetricsInt?): Int {
    return paint!!.measureText(text, start, end).toInt()
  }

  override fun draw(c: Canvas?, text: CharSequence?, start: Int, end: Int, x: Float, top: Int,
                    y: Int, bottom: Int, paint: Paint?) {
    if (backgroundPaint != null) {
      c?.drawRect(Rect(startOffset.toInt(), top, startOffset.toInt() + width, bottom), backgroundPaint)
    }
    c?.drawText(text, start, end, startOffset, y.toFloat(), paint)
  }
}