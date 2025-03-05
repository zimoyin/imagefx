## 图片布局渲染器

这是一个可以对图片进行布局的库。通过几个布局类来对图片进行简单的布局，以此来生成特定布局的图片，他类似与 javafx 的布局类一样

## 快速开始
* 在 maven 中导入 io.github.zimoyin:imagefx:1.0 即可
```java
// 创建 VBox 容器并设置背景颜色
VBox root = new VBox();
root.setBackground(new Background(Color.YELLOW));

// 添加 10 个 Text 节点
for (int i = 0; i < 10; i++) {
    Text textNode = new Text("这是测试文本 " + (i + 1) + "，这是一个较长的自动换行示例文本");
    textNode.setFill(Color.RED);
    textNode.setAutoWrap(true); // 设置自动换行
    root.add(textNode);
}

// 将节点转换为 BufferedImage
BufferedImage bufferedImage = ImageFxUtil.nodeToBufferedImage(root);

// 保存图像到文件
ImageFxUtil.saveImage(bufferedImage, "./image.png");

////// 如果想要那带 g2d 可以通过以下方式
val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
val g = image.createGraphics()
root.render(g) // 渲染
g.dispose()

```
其余的布局类有
* [BorderLayout.kt](src/main/kotlin/io/github/zimoyin/imagefx/layout/BorderLayout.kt)
* [FlowLayout.kt](src/main/kotlin/io/github/zimoyin/imagefx/layout/FlowLayout.kt)
* [HBox.kt](src/main/kotlin/io/github/zimoyin/imagefx/layout/HBox.kt)
* [Layout.kt](src/main/kotlin/io/github/zimoyin/imagefx/layout/Layout.kt)
* [Padding.kt](src/main/kotlin/io/github/zimoyin/imagefx/layout/Padding.kt)
* [VBox.kt](src/main/kotlin/io/github/zimoyin/imagefx/layout/VBox.kt)
* 如果你想自己实现一个布局类可以继承 [Layout.kt](src/main/kotlin/io/github/zimoyin/imagefx/layout/Layout.kt)

可以渲染的节点有
* [ImageNode.kt](src/main/kotlin/io/github/zimoyin/imagefx/node/ImageNode.kt)
* [TextNode.kt](src/main/kotlin/io/github/zimoyin/imagefx/node/TextNode.kt)
* 如果你想实现一个节点类可以继承 [AbsNode.kt](src/main/kotlin/io/github/zimoyin/imagefx/node/AbsNode.kt)

其他工具类
* ImageFxUtil.frameDrawToImage(frame) : 将 swing 的组件转为图片
* ImageFxUtil.nodeToJPanel(root) : 将 layout 转为 swing 的 JPanel