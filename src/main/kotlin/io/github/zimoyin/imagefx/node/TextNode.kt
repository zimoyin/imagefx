package io.github.zimoyin.imagefx.node

import io.github.zimoyin.imagefx.RendererNode
import io.github.zimoyin.imagefx.layout.Layout
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class TextNode(private var content: String) : AbsNode() {
    var color: Color? = null
    var autoWrap: Boolean = false
    var fontSize: Int = 12
        set(value) {
            field = value
            font = font?.deriveFont(value.toFloat())
        }
    var font: Font? = null
    private var lineHeight: Int = 0
    private var lines: List<String> = emptyList()

    init {
        calculateSize()
    }

    override fun initializeWithParent(parent: RendererNode) {
        calculateSize()
    }

    private fun calculateSize() {
        val metrics = getFontMetrics()
        lineHeight = metrics.height

        if (autoWrap && parent != null) {
            var maxWidth: Int
            if (parent is Layout) {
                // 获取父容器的内容区域实际宽度
                val parent = parent as Layout
                maxWidth = parent.maxWidth - parent.padding.left - parent.padding.right // 减去父容器的边距
                maxWidth = maxWidth - parent.margin.left - parent.margin.right // 减去父容器的边距
            } else {
                // 如果父容器不是 Layout，则使用父容器的宽度
                maxWidth = parent?.width ?: 0
            }
            maxWidth = maxWidth - margin.left - margin.right // 减去边距
            maxWidth -= x // 减去 x 轴上的文本偏移量
            lines = wrapText(content, maxWidth, metrics)
        } else {
            lines = listOf(content)
        }

        width = lines.maxOfOrNull { metrics.stringWidth(it) } ?: 0
        height = lineHeight * lines.size
    }

    private fun wrapText(text: String, maxWidth: Int, metrics: FontMetrics): List<String> {
        val lines = mutableListOf<String>()
        val words = text.toCharArray().map { it.toString() }
        val currentLine = StringBuilder()

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val width = metrics.stringWidth(testLine)

            if (width <= maxWidth) {
                currentLine.append(" $word")
            } else {
                lines.add(currentLine.toString())
                currentLine.clear()
                currentLine.append(word)
            }
        }
        lines.add(currentLine.toString())
        return lines
    }

    private fun getFontMetrics(font: Font? = this.font): FontMetrics {
        // 创建一个1x1像素的透明缓冲图像
        val img = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
        // 获取Graphics2D对象并设置字体
        val g2d = img.createGraphics()
        if (font != null) g2d.font = font else g2d.font = g2d.font.deriveFont(fontSize)
        // 获取FontMetrics并释放资源
        val metrics = g2d.fontMetrics
        g2d.dispose()
        return metrics
    }

    override fun update() {
        calculateSize()
    }

    override fun render(g: Graphics2D) {
        calculateSize()
        if (font != null) g.font = font else g.font = g.font.deriveFont(fontSize)
        val tc = g.color
        if (color != null) g.color = color
        // TODO 绘制边框
//        g.drawRect(x, y, width + margin.left + margin.right+3, height+ margin.top + margin.bottom+1)
        lines.forEachIndexed { index, line ->
            val yPos = y + (lineHeight * (index + 1)) - 2
            g.drawString(line, x, yPos)
        }
        g.color = tc
    }
}