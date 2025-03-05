package io.github.zimoyin.io.github.zimoyin.imagefx.layout

/**
 *
 * @author : zimo
 * @date : 2025/03/05
 */
data class Padding(
    var left: Int = 0,
    var right: Int = 0,
    var top: Int = 0,
    var bottom: Int = 0
) {
    constructor(all: Int = 0) : this(all, all, all, all)
    constructor(leftAndRight: Int = 0, topAndBottom: Int = 0) : this(leftAndRight, leftAndRight, topAndBottom, topAndBottom)
}
