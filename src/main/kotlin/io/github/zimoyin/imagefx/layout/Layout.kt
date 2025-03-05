package io.github.zimoyin.imagefx.layout

import io.github.zimoyin.imagefx.BackgroundImage
import io.github.zimoyin.imagefx.RendererNode
import io.github.zimoyin.io.github.zimoyin.imagefx.Margin
import io.github.zimoyin.io.github.zimoyin.imagefx.layout.Padding
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage

abstract class Layout : RendererNode {
    val children = mutableListOf<RendererNode>()
    var color: Color? = null
    var background: Color? = null
    var backgroundImage: BackgroundImage? = null

    override var x: Int = 0

    override var y: Int = 0

    override var width: Int = 0
        set(value) {
            if (value > maxWidth) {
                field = maxWidth
                return
            }
            field = value
        }
    override var height: Int = 0
        set(value) {
            if (value > maxHeight) {
                field = maxHeight
                return
            }
            field = value
        }
    override var parent: RendererNode? = null
        set(value) {
            if (field != null) throw IllegalStateException("Parent can only be set once")
            field = value
        }

    var maxWidth: Int = Int.MAX_VALUE
    var maxHeight: Int = Int.MAX_VALUE


    /**
     * 内边距
     * padding 是指元素内容（content）与其边框（border）之间的空间。
     * 它位于元素的内部，影响的是元素自身的尺寸。
     * 如果给一个元素设置了背景颜色或背景图片，padding 区域也会被填充为该背景。
     */
    open var padding: Padding = Padding()

    override var margin: Margin = Margin()

    /**
     * 是否启用抗锯齿
     */
    var enabledAntialiasing = false

    abstract fun layout()

    fun <T : RendererNode> add(node: T): T {
        children.add(node)
        node.parent = this
        updateWithAddNode(node)
        return node
    }

    fun <T : Layout> add(node: T): T {
        children.add(node)
        updateNodeSize(node)
        node.parent = this
        updateWithAddNode(node)
        return node
    }

    private fun updateWithAddNode(node:RendererNode){
        node.update()
        layout()
    }

    fun updateNodeSize(node:Layout){
        node.maxWidth = if (node.maxWidth > maxWidth) maxWidth else node.maxWidth
        node.maxHeight = if (node.maxHeight > maxHeight) maxHeight else node.maxHeight
        node.width = if (node.width > width) width else node.width
        node.height = if (node.height > height) height else node.height
        node.update()
        val layouts = node.children.filterIsInstance<Layout>()
        layouts.forEach {
            node.updateNodeSize(it)
        }
    }

    override fun update() {
        layout()
    }

    fun updateTree(children0: MutableList<RendererNode> = children){
        children0.forEach {
            it.update()
            if (it is Layout){
                updateTree(it.children)
            }
        }
    }


    override fun render(g: Graphics2D) {
        if (enabledAntialiasing) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        }
        val tc = g.color
        val tbc = g.background
        if (color == null) color = tc
        if (background == null) background = tbc
        g.color = color
        g.background = background
        renderBackground(g)
        // 设置背景图
        if (backgroundImage != null) {
            g.drawImage(resizeImage(backgroundImage!!.toBufferedImage(), width, height), x, y, null)
        }
        children.forEach {
            it.render(g)
            layout()
        }
        g.color = tc
        g.background = tbc
    }

    private fun resizeImage(originalImage: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage {
        val resizedImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB)
        val g2d = resizedImage.createGraphics()
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null)
        g2d.dispose()
        return resizedImage
    }


    private fun renderBackground(g: Graphics2D) {
        val tc = g.color
        g.color = g.background
        g.fillRect(x, y, width, height)
        g.color = tc
    }
}
