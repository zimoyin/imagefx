package io.github.zimoyin.imagefx.layout

import io.github.zimoyin.io.github.zimoyin.imagefx.Margin
import io.github.zimoyin.io.github.zimoyin.imagefx.layout.Padding

class VBox : Layout() {
    /**
     * 内边距
     * padding 是指元素内容（content）与其边框（border）之间的空间。
     * 它位于元素的内部，影响的是元素自身的尺寸。
     * 如果给一个元素设置了背景颜色或背景图片，padding 区域也会被填充为该背景。
     */
    override var padding: Padding = Padding()

    /**
     * 外边距
     * margin 是指元素边框（border）与其他元素之间的空间。
     * 它位于元素的外部，用于控制元素与其他元素之间的距离。
     * margin 区域不会受到元素背景颜色或背景图片的影响
     */
    override var margin: Margin = Margin()

    /**
     * 元素间距
     */
    var spacing: Int = 5



    override fun layout() {

        var currentY = y + padding.top // 应用上内边距
        val lastNode = children.lastOrNull()
        updateTree()
        children.forEach { child ->
            child.x = x + padding.left + child.margin.left
            child.y = currentY + child.margin.top
            // 计算父容器的宽度（子元素宽度 + 左右 margin + 父容器左右 padding）
            val childTotalWidth = child.width + child.margin.left + child.margin.right
            width = maxOf(width, padding.left + childTotalWidth + padding.right)
            currentY = child.y + child.height + child.margin.bottom + spacing
        }

        // 计算父容器高度（总内容高度 + 父容器上下 padding）
        val contentHeight = if (children.isNotEmpty()) {
            // 扣除最后一个子元素的多余间距，并加上父容器下内边距
            currentY - spacing - (lastNode?.margin?.bottom?:0) /* 最后一个子元素的下边距 */ + padding.bottom
        } else {
            padding.top + padding.bottom // 无子元素时高度由内边距决定
        }
        height = maxOf(height, contentHeight)
        if (height <=0 || width <= 0) throw IllegalArgumentException("height and width must be greater than 0; height=$height, width=$width")

    }
}