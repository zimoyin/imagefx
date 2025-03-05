package io.github.zimoyin.imagefx

import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 *
 * @author : zimo
 * @date : 2025/03/05
 */
data class BackgroundImage(
    private val image: BufferedImage,
    var x: Int = 0,
    var y: Int = 0,
    var width: Int = image.width,
    var height: Int = image.height,
    var alpha: Float = 1f
) {
    fun toBufferedImage(): BufferedImage {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g = image.createGraphics()
        g.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
        g.drawImage(this.image, x, y, width, height, null)
        g.dispose()
        return image
    }

    companion object {
        fun create(path: String): BackgroundImage {
            return BackgroundImage(ImageIO.read(File(path)))
        }

        fun create(path: File): BackgroundImage {
            return BackgroundImage(ImageIO.read(path))
        }

        fun create(path: ByteArray): BackgroundImage {
            return BackgroundImage(ImageIO.read(path.inputStream()))
        }
    }
}
