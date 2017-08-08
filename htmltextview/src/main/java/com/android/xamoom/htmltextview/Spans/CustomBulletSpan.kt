package com.android.xamoom.htmltextview.Spans

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.BulletSpan

class CustomBulletSpan(gapWidth: kotlin.Int, var leading: Int?) : BulletSpan(gapWidth) {
  override fun getLeadingMargin(first: Boolean): Int {
    return super.getLeadingMargin(first)
  }

  override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int, top: Int, baseline: Int,
                                 bottom: Int, text: CharSequence, start: Int, end: Int,
                                 first: Boolean, l: Layout) {
    super.drawLeadingMargin(c, p, leading ?: x, dir, top, baseline, bottom, text, start, end, first, l)
  }
}