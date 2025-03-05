import io.github.zimoyin.imagefx.layout.*
import io.github.zimoyin.imagefx.node.TextNode
import io.github.zimoyin.io.github.zimoyin.imagefx.ImageFxUtil
import io.github.zimoyin.io.github.zimoyin.imagefx.saveImage
import io.github.zimoyin.io.github.zimoyin.imagefx.toJPanel
import java.awt.*
import javax.swing.JFrame


// 示例用法
fun main() {
    val frame = JFrame("Layout Demo")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(800, 600)

    val root = VBox().apply {
        background = Color.YELLOW
        repeat(10) {
            add(TextNode("这是测试文本 ${it + 1}，这是一个较长的自动换行示例文本")).apply {
                color = Color.RED
                autoWrap = true
            }
        }
    }

    root.background = Color.BLUE
    root.color = Color.RED
    root.saveImage()
    val panel = root.toJPanel()
    frame.add(panel)
    frame.isVisible = true
}
