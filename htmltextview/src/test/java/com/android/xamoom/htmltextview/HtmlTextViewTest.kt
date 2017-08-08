package com.android.xamoom.htmltextview

import android.content.res.Resources
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.widget.TextView
import org.robolectric.Robolectric
import org.junit.Before
import org.robolectric.Shadows
import org.robolectric.shadow.api.Shadow


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(22))
class HtmlTextViewTest {
  val context = RuntimeEnvironment.application

  @Test
  fun setDebugMode() {
    HtmlTextView.DEBUG = true

    assertTrue(HtmlTextView.DEBUG)
    assertTrue(HtmlTagHandler.DEBUG)
  }

  @Test
  fun init() {
    val textView = HtmlTextView(context)

    assertNotNull(textView.movementMethod)
  }

  @Test
  fun setHtmlTextWithoutTable() {
    val textView = HtmlTextView(context)
    val htmlText = "<span style=\"font-weight: bold;\"></span>"

    textView.setHtmlText(htmlText)

    assertNotNull(textView.htmlString)
  }

  @Test
  fun setHtmlTextWithTable() {
    val textView = HtmlTextView(context)
    val htmlText = "<table><tr><td></td></tr></table>"

    textView.setHtmlText(htmlText)

    assertEquals(1, textView.tables.size)
  }

  @Test
  fun setHtmlWithTableWithWidth() {
    val textView = HtmlTextView(context)
    val htmlText = "<span style=\"font-weight: bold;\"></span>"

    textView.setHtmlText(htmlText, 100)

    assertEquals(100, textView.htmlTagHandler?.maxTableWidth)
    assertNotNull(textView.htmlString)
  }

  @Test
  fun setHtmlWithResourceWithZeroWidth() {
    val textView = HtmlTextView(context)

    textView.setHtmlText(R.raw.test, 0)

    assertNull(textView.htmlTagHandler)
    //assertNotNull(textView.htmlString)
  }

  @Test
  fun setHtmlWithResource() {
    val textView = HtmlTextView(context)

    textView.setHtmlText(R.raw.test)

    assertNotNull(textView.htmlString)
  }

  @Test
  fun setHtmlWithResourceWithoutWidth() {
    val textView = HtmlTextView(context)

    textView.setHtmlText(R.raw.test, 0)

    assertNull(textView.htmlTagHandler)
    assertNotNull(textView.htmlString)
  }

  @Test
  fun setHtmlWithResourceNotExisting() {
    val textView = HtmlTextView(context)

    textView.setHtmlText(0, 0)

    assertNull(textView.htmlString)
  }

  @Test
  fun replaceStyleTest() {
    val textView = HtmlTextView(context)

    var htmlText = "<span style=\"font-weight: bold;\"></span>"
    htmlText += "<span style=\"text-decoration: underline;\"></span>"
    htmlText += "<span style=\"font-style: italic;\"></span>"
    htmlText += "<span style=\"font-size: 18px;\"></span>"
    htmlText += "<ul><li></li></ul>"
    htmlText += "<ol><li></li></ol>"
    htmlText += "<table><tr><td></td></tr></table>"

    textView.setHtmlText(htmlText)

    assertEquals("<html>" +
        "\n <head></head>" +
        "\n <body>" +
        "\n  <b><span style=\"font-weight: bold;\"></span></b>" +
        "\n  <u><span style=\"text-decoration: underline;\"></span></u>" +
        "\n  <i><span style=\"font-style: italic;\"></span></i>" +
        "\n  <fontsize18px>" +
        "\n   <span style=\"font-size: 18px;\"></span>" +
        "\n  </fontsize18px>" +
        "\n  <unorderedlist>" +
        "\n   <listitem></listitem>" +
        "\n  </unorderedlist>" +
        "\n  <orderedlist>" +
        "\n   <listitem></listitem>" +
        "\n  </orderedlist>" +
        "\n  <htmltable>" +
        "\n   <tbody>" +
        "\n    <tablerow>" +
        "\n     <tablecell></tablecell>" +
        "\n    </tablerow>" +
        "\n   </tbody>" +
        "\n  </htmltable>" +
        "\n </body>" +
        "\n</html>", textView.htmlString)
  }

  @Test
  fun getTablesTest() {
    val textView = HtmlTextView(context)

    var htmlText = "<table><tr><td></td></tr></table>"
    htmlText += "<table><tr><td></td></tr></table>"

    textView.setHtmlText(htmlText)

    assertEquals(2, textView.tables.size)
  }

}