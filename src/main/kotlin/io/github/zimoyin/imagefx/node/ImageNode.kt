package io.github.zimoyin.imagefx.node

import io.github.zimoyin.imagefx.RendererNode
import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


/**
 *
 * @author : zimo
 * @date : 2025/03/05
 */
class ImageNode(private var bufferedImage: BufferedImage) : AbsNode() {
    var autoWrap = false

    init {
        width = bufferedImage.width
        height = bufferedImage.height
    }

    override fun initializeWithParent(parent: RendererNode) {
        if (autoWrap) {
            width = parent.width
            height = parent.height
        }
    }

    override fun update() {
        parent?.let { initializeWithParent(it) }
        bufferedImage = resizeImage(width, height)
        if (autoWrap) bufferedImage = resizeImage(width, height)
    }

    override fun render(g: Graphics2D) {
        g.drawImage(bufferedImage, x, y, width, height, null)
    }

    /**
     * 重设图片大小, 如果参数为0, 则计算等比例缩放
     * @param targetWidth 目标宽度
     * @param targetHeight 目标高度
     */
    fun resizeImage(targetWidth: Int, targetHeight: Int): BufferedImage {
        require(targetWidth >= 0 && targetHeight >= 0) { "Width and height must be non-negative" }
        require(targetWidth != 0 || targetHeight != 0) { "Both width and height cannot be zero" }

        val originalWidth = bufferedImage.width
        val originalHeight = bufferedImage.height

        val (newWidth, newHeight) = when {
            targetWidth == 0 -> {
                val ratio = targetHeight.toDouble() / originalHeight
                (originalWidth * ratio).toInt() to targetHeight
            }

            targetHeight == 0 -> {
                val ratio = targetWidth.toDouble() / originalWidth
                targetWidth to (originalHeight * ratio).toInt()
            }

            else -> targetWidth to targetHeight
        }

        val scaledImage = BufferedImage(newWidth, newHeight, bufferedImage.type)
        scaledImage.createGraphics().apply {
            setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            drawImage(bufferedImage, 0, 0, newWidth, newHeight, null)
            dispose()
        }
        return scaledImage
    }

    /**
     * 获取图片的亮度
     */
    fun getImageLight(image: BufferedImage): Int {
        var totalBrightness = 0.0
        val totalPixels = image.width * image.height
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val rgb = image.getRGB(x, y)
                val red = (rgb shr 16) and 0xFF
                val green = (rgb shr 8) and 0xFF
                val blue = rgb and 0xFF
                // 使用简单的平均值法计算亮度
                val brightness = (red + green + blue) / 3.0
                totalBrightness += brightness
            }
        }

        // 计算平均亮度
        return (totalBrightness / totalPixels).toInt()
    }

    /**
     * 修改图片的透明度
     */
    fun setImageAlpha(alpha: Float = 0.5f): BufferedImage {
        val transparentImage = BufferedImage(
            bufferedImage.width, bufferedImage.height, BufferedImage.TYPE_INT_ARGB
        )

        val g2d = transparentImage.createGraphics()
        g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)

        // 绘制原始图像到带有透明度的图像上
        g2d.drawImage(bufferedImage, 0, 0, null)
        g2d.dispose()

        return transparentImage
    }

    companion object {
        fun create(path: String): ImageNode {
            return ImageNode(ImageIO.read(File(path)))
        }

        fun create(path: File): ImageNode {
            return ImageNode(ImageIO.read(path))
        }

        fun create(path: ByteArray): ImageNode {
            return ImageNode(ImageIO.read(path.inputStream()))
        }
    }
}