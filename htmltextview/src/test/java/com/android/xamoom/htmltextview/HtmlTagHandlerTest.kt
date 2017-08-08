package com.android.xamoom.htmltextview

import android.text.Editable
import android.text.TextPaint
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import android.text.style.RelativeSizeSpan
import com.android.xamoom.htmltextview.Spans.CustomBulletSpan
import com.android.xamoom.htmltextview.Spans.NumberSpan
import com.android.xamoom.htmltextview.Spans.TableCellSpan
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.inOrder
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(22))
class HtmlTagHandlerTest {
  var tagHandler: HtmlTagHandler? = null
  var spannableMock: Editable? = null

  @Before
  fun setup() {
    tagHandler = HtmlTagHandler(10.0f, TextPaint(), ArrayList<HtmlTable>(), 100)
    spannableMock = Mockito.spy(Editable.Factory().newEditable(""))
  }

  @Test
  fun testInit() {
    val tables = ArrayList<HtmlTable>()
    tables.add(HtmlTable("", TextPaint()))

    val tagHandler = HtmlTagHandler(0.0f, TextPaint(), tables, 0)

    Assert.assertEquals(1, tagHandler.tableStack.size)
  }

  @Test
  fun testHandleTagNull() {
    tagHandler?.handleTag(true, null, spannableMock, null)

    Mockito.verifyZeroInteractions(spannableMock)
  }

  @Test
  fun testGetLastWithoutStart() {
    tagHandler?.handleTag(false, HtmlTagHandler.TAG_FONTSIZE, spannableMock, null)

    Mockito.verify(spannableMock!!, Mockito.never()).setSpan(Mockito.any(), Mockito.anyInt(),
        Mockito.anyInt(),Mockito.anyInt())
  }

  @Test
  fun testHandleTagFontsize() {
    tagHandler?.handleTag(true, "fontsize18px", spannableMock, null)
    spannableMock?.append("Something")
    tagHandler?.handleTag(false, "fontsize18px", spannableMock, null)

    val fontSizeCapture = ArgumentCaptor.forClass(RelativeSizeSpan::class.java)
    Mockito.verify(spannableMock!!, Mockito.times(2)).setSpan(fontSizeCapture.capture(),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
    Assert.assertEquals(1.8f, fontSizeCapture.allValues[1].sizeChange)
  }

  @Test
  fun testHandlerTagUnorderedList() {
    tagHandler?.handleTag(true, HtmlTagHandler.TAG_UNORDEREDLIST, spannableMock, null)

    Assert.assertEquals(1, tagHandler?.lists?.size)

    tagHandler?.handleTag(true, HtmlTagHandler.TAG_LISTITEM, spannableMock, null)
    spannableMock?.append("Something")
    tagHandler?.handleTag(false, HtmlTagHandler.TAG_LISTITEM, spannableMock, null)
    tagHandler?.handleTag(false, HtmlTagHandler.TAG_UNORDEREDLIST, spannableMock, null)

    Assert.assertEquals(0, tagHandler?.lists?.size)
    val inOrder = inOrder(spannableMock, spannableMock)
    inOrder.verify(spannableMock!!).setSpan(Mockito.any(LeadingMarginSpan::class.java),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
    inOrder.verify(spannableMock!!).setSpan(Mockito.any(BulletSpan::class.java),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
  }

  @Test
  fun testHandlerTagUnorderedListMultiple() {
    tagHandler?.lists?.add(HtmlTagHandler.TAG_UNORDEREDLIST)
    tagHandler?.lists?.add(HtmlTagHandler.TAG_UNORDEREDLIST)

    tagHandler?.handleTag(true, HtmlTagHandler.TAG_LISTITEM, spannableMock, null)
    spannableMock?.append("Something")
    tagHandler?.handleTag(false, HtmlTagHandler.TAG_LISTITEM, spannableMock, null)

    val bulletCapture = ArgumentCaptor.forClass(CustomBulletSpan::class.java)
    val inOrder = inOrder(spannableMock, spannableMock)

    inOrder.verify(spannableMock!!).setSpan(Mockito.any(LeadingMarginSpan::class.java),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
    inOrder.verify(spannableMock!!).setSpan(bulletCapture.capture(),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
    Assert.assertEquals(54, bulletCapture.value.leading)
  }

  @Test
  fun testHandlerTagOrderedList() {
    tagHandler?.handleTag(true, HtmlTagHandler.TAG_ORDEREDLIST, spannableMock, null)

    Assert.assertEquals(1, tagHandler?.lists?.size)

    tagHandler?.handleTag(true, HtmlTagHandler.TAG_LISTITEM, spannableMock, null)
    spannableMock?.append("Something")
    tagHandler?.handleTag(false, HtmlTagHandler.TAG_LISTITEM, spannableMock, null)
    tagHandler?.handleTag(false, HtmlTagHandler.TAG_ORDEREDLIST, spannableMock, null)

    Assert.assertEquals(0, tagHandler?.lists?.size)
    Mockito.verify(spannableMock!!).setSpan(Mockito.any(NumberSpan::class.java),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
  }

  @Test
  fun testHandlerTable() {
    val table = HtmlTable("<table>\n" +
        "    <tr bgcolor=\"dadada\">\n" +
        "        <td>1.</td>\n" +
        "        <td>Some Name</td>\n" +
        "        <td>31</td>\n" +
        "    </tr>\n" +
        "</table>", TextPaint())
    tagHandler?.tableStack?.add(table)

    tagHandler?.handleTag(true, HtmlTagHandler.TAG_TABLE, spannableMock, null)
    Assert.assertEquals(1, tagHandler?.tableRowBackgroundColors?.size)
    tagHandler?.handleTag(true, HtmlTagHandler.TAG_TABLE_ROW, spannableMock, null)

    tagHandler?.handleTag(true, HtmlTagHandler.TAG_TABLE_CELL, spannableMock, null)
    spannableMock?.append("Something")
    tagHandler?.handleTag(false, HtmlTagHandler.TAG_TABLE_CELL, spannableMock, null)

    tagHandler?.handleTag(true, HtmlTagHandler.TAG_TABLE_CELL, spannableMock, null)
    spannableMock?.append("Something")
    tagHandler?.handleTag(false, HtmlTagHandler.TAG_TABLE_CELL, spannableMock, null)

    tagHandler?.handleTag(true, HtmlTagHandler.TAG_TABLE_CELL, spannableMock, null)
    spannableMock?.append("Something")
    tagHandler?.handleTag(false, HtmlTagHandler.TAG_TABLE_CELL, spannableMock, null)

    spannableMock?.append("Something")

    tagHandler?.handleTag(false, HtmlTagHandler.TAG_TABLE_ROW, spannableMock, null)
    Assert.assertEquals(0, tagHandler?.tableCells?.size)
    tagHandler?.handleTag(false, HtmlTagHandler.TAG_TABLE, spannableMock, null)
    Assert.assertEquals(0, tagHandler?.tableStack?.size)

    val cellCapture = ArgumentCaptor.forClass(TableCellSpan::class.java)
    val inOrder = inOrder(spannableMock, spannableMock)
    inOrder.verify(spannableMock!!).setSpan(Mockito.any(HtmlTagHandler.TableRow::class.java),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
    inOrder.verify(spannableMock!!).setSpan(Mockito.any(HtmlTagHandler.TableCell::class.java),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
    inOrder.verify(spannableMock!!).setSpan(cellCapture.capture(),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
    inOrder.verify(spannableMock!!).setSpan(Mockito.any(HtmlTagHandler.TableCell::class.java),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
    inOrder.verify(spannableMock!!).setSpan(cellCapture.capture(),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
    inOrder.verify(spannableMock!!).setSpan(Mockito.any(HtmlTagHandler.TableCell::class.java),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
    inOrder.verify(spannableMock!!).setSpan(cellCapture.capture(),
        Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())

    Assert.assertEquals(0.0f, cellCapture.allValues[0].startOffset)
    Assert.assertEquals(2.0f, cellCapture.allValues[1].startOffset)
    Assert.assertEquals(98.0f, cellCapture.allValues[2].startOffset)
  }
}