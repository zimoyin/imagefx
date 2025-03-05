package io.github.zimoyin.imagefx.layout

import io.github.zimoyin.imagefx.RendererNode
import io.github.zimoyin.io.github.zimoyin.imagefx.Margin
import io.github.zimoyin.io.github.zimoyin.imagefx.layout.Padding

@Deprecated("Use FlowLayout or VBox or HBox")
class BorderLayout : Layout() {
    enum class Position { NORTH, SOUTH, EAST, WEST, CENTER }

    override var padding: Padding = Padding()
    override var margin: Margin = Margin()
    var spacing: Int = 5 // Spacing between regions


    // Group children by their position
    private val north = mutableListOf<RendererNode>()
    private val south = mutableListOf<RendererNode>()
    private val east = mutableListOf<RendererNode>()
    private val west = mutableListOf<RendererNode>()
    private val center = mutableListOf<RendererNode>()

    fun <T : RendererNode> add(node: T, position: Position): T {
        children.add(node)
        when (position) {
            Position.NORTH -> north.add(node)
            Position.SOUTH -> south.add(node)
            Position.EAST -> east.add(node)
            Position.WEST -> west.add(node)
            Position.CENTER -> center.add(node)
        }
        return node
    }

    override fun layout() {
        updateTree()

        // Calculate dimensions for each region
        var currentY = y + padding.top
        var containerWidth = width + padding.left + padding.right
        var containerHeight = height + padding.top + padding.bottom

        // Layout NORTH region (horizontal)
        if (north.isNotEmpty()) {
            var currentX = x + padding.left
            north.forEach { child ->
                child.x = currentX + child.margin.left
                child.y = currentY + child.margin.top
                currentX += child.width + child.margin.left + child.margin.right + spacing
            }
            containerWidth = maxOf(containerWidth, currentX - spacing - x)
            containerHeight += north.maxOf { it.height + it.margin.top + it.margin.bottom } + spacing
            currentY += north.maxOf { it.height + it.margin.top + it.margin.bottom } + spacing
        }

        // Layout SOUTH region (horizontal)
        if (south.isNotEmpty()) {
            var currentX = x + padding.left
            south.forEach { child ->
                child.x = currentX + child.margin.left
                child.y = y + height - padding.bottom - child.height - child.margin.bottom
                currentX += child.width + child.margin.left + child.margin.right + spacing
            }
            containerWidth = maxOf(containerWidth, currentX - spacing - x)
            containerHeight += south.maxOf { it.height + it.margin.top + it.margin.bottom } + spacing
        }

        // Layout WEST region (vertical)
        if (west.isNotEmpty()) {
            var currentYWest = currentY
            west.forEach { child ->
                child.x = x + padding.left + child.margin.left
                child.y = currentYWest + child.margin.top
                currentYWest += child.height + child.margin.top + child.margin.bottom + spacing
            }
            containerWidth += west.maxOf { it.width + it.margin.left + it.margin.right } + spacing
        }

        // Layout EAST region (vertical)
        if (east.isNotEmpty()) {
            var currentYEast = currentY
            east.forEach { child ->
                child.x = x + width - padding.right - child.width - child.margin.right
                child.y = currentYEast + child.margin.top
                currentYEast += child.height + child.margin.top + child.margin.bottom + spacing
            }
            containerWidth += east.maxOf { it.width + it.margin.left + it.margin.right } + spacing
        }

        // Layout CENTER region
        center.forEach { child ->
            val centerX = x + padding.left + west.sumOf { it.width + it.margin.left + it.margin.right + spacing }
            val centerY = currentY
            val centerWidth = width - padding.left - padding.right -
                    west.sumOf { it.width + it.margin.left + it.margin.right } -
                    east.sumOf { it.width + it.margin.left + it.margin.right } - spacing * 2
            val centerHeight = height - padding.top - padding.bottom -
                    north.sumOf { it.height + it.margin.top + it.margin.bottom } -
                    south.sumOf { it.height + it.margin.top + it.margin.bottom } - spacing * 2

            child.x = centerX + child.margin.left
            child.y = centerY + child.margin.top
            child.width = centerWidth
            child.height = centerHeight
        }

        // Update container dimensions
        width = maxOf(width, containerWidth)
        height = maxOf(height, containerHeight)
    }
}