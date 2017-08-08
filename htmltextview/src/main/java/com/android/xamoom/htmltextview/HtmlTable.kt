package com.android.xamoom.htmltextview

import android.graphics.Color
import android.text.TextPaint
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class HtmlTable(html: String, paint: TextPaint) {
  var rows: ArrayList<String> = ArrayList()
  var cellsPerRow: ArrayList<ArrayList<String>> = ArrayList()
  var rowBackgroundColors: ArrayList<Int?> = ArrayList()

  var cellSizes: ArrayList<Float> = ArrayList()
  var largestCellSize: ArrayList<Float> = ArrayList()

  init {
    parseTable(html, paint)
  }

  /**
   * Parses table initializes properties.
   *
   * @param html String of html
   * @param paint TextPaint for measuring
   */
  private fun parseTable(html: String, paint: TextPaint) {
    val document = Jsoup.parse(html)

    val rows = document.select("tr")
    for (element in rows) {
      this.rows.add(element.outerHtml())

      // save background color
      rowBackgroundColors.add(getRowStyle(element))

      val cellList: ArrayList<String> = ArrayList()
      val cells = element.select("td")

      var count = 0
      for (cell in cells) { // parse cells & measure cells
        cellList.add(cell.toString())

        val size = paint.measureText(cell.html())
        updateLargestCellSize(count, size) // update largest cell size if necessary

        count += 1
      }

      this.cellsPerRow.add(cellList)
    }

    cellSizes.addAll(largestCellSize)
  }

  /**
   * Updates largestCellSize for index.
   *
   * @param index Index of cell per row.
   * @param size Measured size of cell content as Float
   */
  private fun updateLargestCellSize(index: Int, size: Float) {
    if (largestCellSize.size == index || size > largestCellSize[index]) {
      if (largestCellSize.getOrNull(index) == null) {
        largestCellSize.add(index, size)
      } else {
        largestCellSize[index] = size
      }
    }
  }

  /**
   * Parses bgColor for rows.
   *
   * @param element Jsoup element of tr
   * @return color as int or null
   */
  private fun getRowStyle(element: Element) : Int? {
    element.attributes()
        .filter { it.html().contains("bgcolor", true) }
        .forEach { return Color.parseColor(String.format("#%s", it.value)) }

    return null
  }

  /**
   * Sum of Cellsizes.
   *
   * @return Sum of cellSizes
   */
  fun maximalRowSize(): Float {
    val fullsize = cellSizes.sum()
    return fullsize
  }
}