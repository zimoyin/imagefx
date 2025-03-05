package io.github.zimoyin.imagefx.layout

import io.github.zimoyin.io.github.zimoyin.imagefx.Margin
import io.github.zimoyin.io.github.zimoyin.imagefx.layout.Padding

class HBox : Layout() {
    override var padding: Padding = Padding()
    override var margin: Margin = Margin()
    var spacing: Int = 5

    override fun layout() {
        updateTree()
        var currentX = x + padding.left
        var maxChildHeight = 0

        children.forEachIndexed { index, child ->

            child.x = currentX + child.margin.left
            child.y = y + padding.top + child.margin.top

            val childTotalHeight = child.height + child.margin.top + child.margin.bottom
            if (childTotalHeight > maxChildHeight) {
                maxChildHeight = childTotalHeight
            }

            currentX = child.x + child.width + child.margin.right
            if (index < children.size - 1) {
                currentX += spacing
            }
        }

        val contentHeight = if (children.isNotEmpty()) {
            maxChildHeight + padding.top + padding.bottom
        } else {
            padding.top + padding.bottom
        }
        height = maxOf(height, contentHeight)
        width = currentX - x + padding.right
        if (height <=0 || width <= 0) throw IllegalArgumentException("height and width must be greater than 0; height=$height, width=$width")

    }
}