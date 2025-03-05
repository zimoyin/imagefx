package io.github.zimoyin.imagefx.layout

import io.github.zimoyin.io.github.zimoyin.imagefx.Margin
import io.github.zimoyin.io.github.zimoyin.imagefx.layout.Padding

class FlowLayout : Layout() {
    override var padding: Padding = Padding()
    override var margin: Margin = Margin()
    var spacing: Int = 5 // Horizontal and vertical spacing between elements

    override fun layout() {
        updateTree()

        var currentX = x + padding.left
        var currentY = y + padding.top
        var rowHeight = 0
        val lineWidths = mutableListOf<Int>() // Track the widths of each line for container width calculation

        children.forEach { child ->
            // Check if the current child fits in the current row
            if (currentX + child.width + child.margin.left + child.margin.right > x + width - padding.right) {
                // Move to the next line
                currentX = x + padding.left
                currentY += rowHeight + spacing
                rowHeight = 0
            }

            // Position the child
            child.x = currentX + child.margin.left
            child.y = currentY + child.margin.top

            // Update row height with the tallest child in the current row
            val childTotalHeight = child.height + child.margin.top + child.margin.bottom
            if (childTotalHeight > rowHeight) {
                rowHeight = childTotalHeight
            }

            // Move currentX to the next position
            currentX += child.width + child.margin.left + child.margin.right + spacing

            // Add the current line's width to lineWidths
            val lineWidth = currentX - x - padding.left - spacing
            lineWidths.add(lineWidth)
        }

        // Calculate container dimensions
        val contentWidth = if (lineWidths.isNotEmpty()) {
            lineWidths.maxOrNull() ?: (0 + padding.left + padding.right)
        } else {
            padding.left + padding.right
        }
        val contentHeight = if (children.isNotEmpty()) {
            currentY - y - spacing + rowHeight + padding.bottom
        } else {
            padding.top + padding.bottom
        }

        width = maxOf(width, contentWidth)
        height = maxOf(height, contentHeight)
        if (height <=0 || width <= 0) throw IllegalArgumentException("height and width must be greater than 0; height=$height, width=$width")
    }
}