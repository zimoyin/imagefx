package io.github.zimoyin.io.github.zimoyin.imagefx


import com.sun.imageio.plugins.common.ImageUtil
import io.github.zimoyin.imagefx.RendererNode
import io.github.zimoyin.imagefx.layout.Layout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*


/**
 *
 * @author : zimo
 * @date : 2025/03/05
 */
class ImageFxUtil private constructor() {
    companion object {
        /**
         * 绘制Swing组件到图片
         */

        fun frameDrawToImage(frame: JFrame): BufferedImage {
            var image: BufferedImage? = null

            // 启用 Headless 模式（如果尚未启用）
            if (!java.awt.GraphicsEnvironment.isHeadless()) {
                System.setProperty("java.awt.headless", "true")
            }

            SwingUtilities.invokeAndWait {
                // 确保组件布局正确，若尺寸无效则尝试自动调整
                if (frame.width <= 0 || frame.height <= 0) {
                    frame.pack()
                }
                // 检查尺寸有效性
                require(frame.width > 0 && frame.height > 0) {
                    "Frame size must be positive. Current size: ${frame.size}"
                }

                // 创建与组件大小匹配的透明图像
                image = BufferedImage(frame.width, frame.height, BufferedImage.TYPE_INT_ARGB)
                val g2d = image!!.createGraphics().apply {
                    // 启用抗锯齿和文本抗锯齿
                    setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                    setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
                }

                try {
                    // 强制组件绘制到图像
                    frame.paint(g2d)
                } finally {
                    g2d.dispose()
                }
            }

            return image ?: throw IllegalStateException("draw to image failed")
        }

        @JvmOverloads
        fun toBase64(image: BufferedImage, format: String = "png"): String {
            val outputStream = ByteArrayOutputStream()
            ImageIO.write(image, format, outputStream)
            val imageBytes = outputStream.toByteArray()
            return Base64.getEncoder().encodeToString(imageBytes)
        }

        @JvmOverloads
        fun toBytes(image: BufferedImage, format: String = "png"): ByteArray {
            val outputStream = ByteArrayOutputStream()
            ImageIO.write(image, format, outputStream)
            val imageBytes = outputStream.toByteArray()
            return imageBytes
        }

        fun loadImage(filePath: String): BufferedImage {
            return ImageIO.read(File(filePath))
        }


        fun loadImage(filePath: File): BufferedImage {
            return ImageIO.read(filePath)
        }

        fun loadImage(input: InputStream): BufferedImage {
            return ImageIO.read(input)
        }

        fun loadImage(bytes: ByteArray): BufferedImage {
            val bis = ByteArrayInputStream(bytes)
            return ImageIO.read(bis)
        }

        @JvmOverloads
        fun saveImage(image: BufferedImage, filePath: String = "./image.png", formatName: String = "PNG") {
            ImageIO.write(image, formatName, File(filePath))
        }

        fun createImage(width: Int, height: Int, imageType: Int): BufferedImage {
            return BufferedImage(width, height, imageType)
        }

        fun nodeToBufferedImage(node: RendererNode): BufferedImage {
            return node.toBufferedImage()
        }

        fun nodeToBase64(node: RendererNode): String {
            return node.toBase64()
        }

        fun nodeToBytes(node: RendererNode): ByteArray {
            return node.toBytes()
        }

        fun nodeToJPanel(node: Layout): JPanel {
            return node.toJPanel()
        }

        @JvmOverloads
        fun nodeSaveImage(
            node: RendererNode,
            filePath: String = "./image.png",
            formatName: String = File(filePath).extension
        ) {
            node.saveImage(filePath, formatName)
        }
    }
}

fun Layout.toJPanel(): JPanel {
    val originalMaxWidth = maxWidth
    val originalMaxHeight = maxHeight
    val layouts = this.children.filter { it is Layout }.map { it as Layout }
    return object : JPanel() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            this@toJPanel.width = width
            this@toJPanel.maxWidth = if (originalMaxWidth > width) width else originalMaxWidth
            this@toJPanel.height = height
            this@toJPanel.maxHeight = if (originalMaxHeight > height) height else originalMaxHeight
            layouts.forEach {
                this@toJPanel.updateNodeSize(it)
            }
            render(g as Graphics2D)
        }
    }
}

fun BufferedImage.toBase64(): String {
    return ImageFxUtil.toBase64(this)
}

fun BufferedImage.toBytes(): ByteArray {
    return ImageFxUtil.toBytes(this)
}

fun BufferedImage.saveImage(filePath: String = "./image.png", formatName: String = File(filePath).extension) {
    ImageFxUtil.saveImage(this, filePath, formatName)
}

fun BufferedImage.toJPanel(): JPanel {
    val panel = JPanel()
    val imageIcon = ImageIcon(this)
    val label = JLabel(imageIcon)
    label.horizontalAlignment = SwingConstants.CENTER
    panel.add(label)
    return panel
}


fun RendererNode.toBufferedImage(): BufferedImage {
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g = image.createGraphics()
    render(g)
    g.dispose()
    return image
}

fun RendererNode.toBase64(): String {
    return toBase64(toBufferedImage())
}

fun RendererNode.toBytes(): ByteArray {
    return toBytes(toBufferedImage())
}

private fun toBase64(image: BufferedImage, format: String = "png"): String {
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(image, format, outputStream)
    val imageBytes = outputStream.toByteArray()
    return Base64.getEncoder().encodeToString(imageBytes)
}

private fun toBytes(image: BufferedImage, format: String = "png"): ByteArray {
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(image, format, outputStream)
    val imageBytes = outputStream.toByteArray()
    return imageBytes
}

fun RendererNode.saveImage(filePath: String = "./image.png", formatName: String = "PNG") {
    (this as Layout).layout()
    ImageIO.write(toBufferedImage(), formatName, File(filePath))
}