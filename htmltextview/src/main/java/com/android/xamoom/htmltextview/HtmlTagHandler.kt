package com.android.xamoom.htmltextview

import android.text.*
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import com.android.xamoom.htmltextview.Spans.CustomBulletSpan
import com.android.xamoom.htmltextview.Spans.NumberSpan
import com.android.xamoom.htmltextview.Spans.TableCellSpan
import org.xml.sax.XMLReader
import java.util.*

class HtmlTagHandler(var textSize: Float, var textPaint: TextPaint,
                     tables: ArrayList<HtmlTable>, var maxTableWidth: Int) : Html.TagHandler {

  companion object {
    var DEBUG = false
    @JvmStatic val TAG_FONTSIZE = "fontsize"
    @JvmStatic val TAG_UNORDEREDLIST = "unorderedlist"
    @JvmStatic val TAG_ORDEREDLIST = "orderedlist"
    @JvmStatic val TAG_LISTITEM = "listitem"
    @JvmStatic val TAG_TABLE = "htmltable"
    @JvmStatic val TAG_TABLE_ROW = "tablerow"
    @JvmStatic val TAG_TABLE_CELL = "tablecell"
  }

  val paragraphIndent = 20
  val bulletMargin = 10

  var lists: Stack<String> = Stack()
  var orderedListItems: Stack<Int> = Stack()

  var tableStack: Stack<HtmlTable> = Stack()
  var tableRowBackgroundColors: Stack<Int> = Stack()
  var tableCells: Stack<String> = Stack()


  class FontSize
  class ListItem
  class TableRow
  class TableCell

  init {
    tableStack.addAll(tables)
  }

  override fun handleTag(opening: Boolean, tag: String?, output: Editable?, xmlReader: XMLReader?) {
    if (tag == null || output == null) {
      return
    }

    if(opening){
      if (tag.contains(TAG_FONTSIZE)) {
        start(output as SpannableStringBuilder, FontSize())
      } else if (tag.contains(TAG_UNORDEREDLIST)) {
        lists.add(tag)
      } else if (tag.contains(TAG_ORDEREDLIST)) {
        lists.add(tag)
        orderedListItems.push(1)
      } else if (tag.contains(TAG_LISTITEM)) {
        if (lists.peek() == TAG_ORDEREDLIST) {
          orderedListItems.push(orderedListItems.pop() + 1)
        }
        start(output as SpannableStringBuilder, ListItem())
      } else if (tag.contentEquals(TAG_TABLE)) {
        tableRowBackgroundColors.addAll(tableStack.peek().rowBackgroundColors)
      } else if (tag.contentEquals(TAG_TABLE_ROW)) {
        start(output as SpannableStringBuilder, TableRow())
      } else if (tag.contentEquals(TAG_TABLE_CELL)) {
        tableCells.push(tag)
        start(output as SpannableStringBuilder, TableCell())
      }
    } else {
      // calculate relativeFontSize
      if (tag.contains(TAG_FONTSIZE)) {
        val relativeSpanSize = calculateFontsizeProportion(tag, textSize)
        end(output as SpannableStringBuilder, FontSize::class.java,
            RelativeSizeSpan(relativeSpanSize))
      } else if (tag.contains(TAG_UNORDEREDLIST)) {
        lists.pop()
      } else if (tag.contains(TAG_ORDEREDLIST)) {
        lists.pop()
      } else if (tag.contains(TAG_LISTITEM)) {
        val text = appendNewLine(output)
        if (lists.peek() == TAG_UNORDEREDLIST) { // uls
          val bullet = createBulletListItem()
          end(text as SpannableStringBuilder, ListItem::class.java,
              LeadingMarginSpan.Standard(paragraphIndent), bullet)
        } else { // ols
          val text = appendNewLine(output)
          val numberSpan = NumberSpan(calculateListItemOffset(),
              currentNumberSpanString(), textPaint)
          end(text as SpannableStringBuilder, ListItem::class.java, numberSpan)
        }
      } else if (tag.contentEquals(TAG_TABLE_ROW)) {
        val text = appendNewLine(output)
        end(text as SpannableStringBuilder, TableRow::class.java)
        tableCells.clear()
        tableRowBackgroundColors.pop()
      } else if (tag.contentEquals(TAG_TABLE_CELL)) {
        adjustTableCellSizes()
        val startOffset = cellStartOffset()
        end(output as SpannableStringBuilder, TableCell::class.java,
            TableCellSpan(startOffset, tableStack.peek().cellSizes[tableCells.size - 1].toInt(), tableRowBackgroundColors.peek()))
      } else if (tag.contentEquals(TAG_TABLE)) {
        tableStack.pop()
      }
    }
  }

  /**
   * Adds a newline "\n" at the end of the text. Needed for different spans.
   *
   * @param text Editable text.
   * @return Editable text with added newline
   */
  private fun appendNewLine(text: Editable): Editable {
    if (text.isNotEmpty() && text[text.length - 1] != '\n') {
      text.append("\n")
    }

    return text
  }

  /**
   * Calculations the proportion needed for RelativeSizeSpan to change the size
   * of the displayed text.
   *
   * @param tag Custom fontsize html tag eg: fontsize18px
   * @param size Normal used size
   */
  private fun calculateFontsizeProportion(tag: String, size: Float): Float {
    val sizeString = tag.replace("fontsize", "")
    var newFontSize = 0.0f

    if (sizeString.contains("px")) {
      val fontSize = sizeString.replace("px", "").toFloatOrNull()
      if (fontSize != null) {
        newFontSize = fontSize / size
      }
    }

    return newFontSize
  }

  /**
   * Returns the right BulletSpan needed for any level of the list.
   * If there are multiple lists encapsulated it will return a CustomBulletSpan with
   * the correct margins depending on the encapsulation depth.
   *
   * @return BulletSpan or CustomBulletSpan
   */
  private fun createBulletListItem() : Any {
    var newBullet = BulletSpan(bulletMargin)
    if (lists.size > 1) {
      newBullet = CustomBulletSpan(bulletMargin,
          calculateListItemOffset())
    }

    return newBullet
  }

  /**
   * Returns the offset for listitems.
   *
   * @return offset for listitems
   */
  private fun calculateListItemOffset(): Int {
    return (paragraphIndent - 2) * (lists.size + (lists.size - 1))
  }

  /**
   * Returns the offset for cell items.
   * first offset is 0
   * other startOffsets get calculated by adding the the largestCellSize before the
   * current index
   */
  private fun cellStartOffset(): Float {
    var startOffset = 0.0f
    if (tableCells.size > 1) {
      for (i in 0 .. tableCells.size - 2) {
        startOffset += tableStack.peek().cellSizes[i]
      }
    }

    if (DEBUG) Log.d("HtmlTagHandler", "Cell startOffset for index " + tableCells.size + " with " +
        startOffset)

    return startOffset
  }

  /**
   * This will make the table to have 100% width.
   * It will find the biggest cell in the row and expand it to the maximal available width
   * to fill the maxTableWidth.
   */
  private fun adjustTableCellSizes() {
    if (maxTableWidth > tableStack.peek().maximalRowSize()) {
      val largestSizeIndex = tableStack.peek().cellSizes.indexOf(tableStack.peek().cellSizes.max())
      val availableSpaceForWidth = maxTableWidth - tableStack.peek().maximalRowSize()

      if (DEBUG) Log.d("HtmlTagHandler", "Adjusting cellSize: " + largestSizeIndex + " from " +
          tableStack.peek().maximalRowSize() + " to " + availableSpaceForWidth)

      tableStack.peek().cellSizes[largestSizeIndex] += availableSpaceForWidth
    }
  }

  /**
   * Returns the lastElement of the orderListItems - 1 as String to display as the numberSpan
   * for ordered lists.
   */
  private fun currentNumberSpanString(): String {
    return (orderedListItems.lastElement() - 1).toString()
  }

  private fun <T> getLast(text: Spanned, kind: Class<T>): Any? {
    /*
     * This knows that the last returned object from getSpans()
     * will be the most recently added.
     */
    val objects = text.getSpans(0, text.length, kind)

    if (objects.isEmpty()) {
      return null
    } else {
      return objects[objects.size - 1]
    }
  }

  private fun start(text: SpannableStringBuilder, mark: Any) {
    val len = text.length
    text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK)
  }

  private fun <T> end(output: Editable, kind: Class<T>, vararg replaces: Any) {
    val len = output.length
    val obj = getLast(output, kind)
    val where = output.getSpanStart(obj)

    output.removeSpan(obj)

    if (where >= 0 && where != len) {
      for (replace in replaces) {
        output.setSpan(replace, where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
      }
    }
  }
}