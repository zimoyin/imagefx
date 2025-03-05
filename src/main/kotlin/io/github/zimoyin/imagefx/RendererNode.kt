package io.github.zimoyin.imagefx

import io.github.zimoyin.io.github.zimoyin.imagefx.Margin
import io.github.zimoyin.io.github.zimoyin.imagefx.layout.Padding
import java.awt.Graphics2D

interface RendererNode {
    var x: Int
    var y: Int
    var width: Int
    var height: Int

    /**
     * 外边距
     * margin 是指元素边框（border）与其他元素之间的空间。
     * 它位于元素的外部，用于控制元素与其他元素之间的距离。
     * margin 区域不会受到元素背景颜色或背景图片的影响
     */
    var margin: Margin

    var parent: RendererNode?
    fun render(g: Graphics2D)

    /**
     * 更新数据以及布局信息，防止在绘制当前帧时使用过时数据
     */
    fun update(){

    }

    fun getRoot(): RendererNode {
        var node = this
        while (node.parent != null){
            node = node.parent!!
        }
        return node
    }
}
