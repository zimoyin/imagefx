package io.github.zimoyin.imagefx.node

import io.github.zimoyin.imagefx.RendererNode
import io.github.zimoyin.io.github.zimoyin.imagefx.Margin
import io.github.zimoyin.io.github.zimoyin.imagefx.layout.Padding


/**
 *
 * @author : zimo
 * @date : 2025/03/05
 */
abstract class AbsNode : RendererNode {
    override var x: Int = 0
    override var y: Int = 0
    override var width: Int = 0
    override var height: Int = 0
    override var margin: Margin = Margin()
    override var parent: RendererNode? = null
        set(value) {
            if (field != null) throw IllegalStateException("Parent can only be set once")
            if (value == null) throw IllegalStateException("Parent can not be null")
            field = value
            initializeWithParent(value)
        }

    open fun initializeWithParent(parent: RendererNode) {}
}