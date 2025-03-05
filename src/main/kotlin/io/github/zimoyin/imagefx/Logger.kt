package io.github.zimoyin.imagefx

/**
 *
 * @author : zimo
 * @date : 2025/03/05
 */


fun Info(string: String, vararg args: Any?): String {
    // 1. 参数校验与占位符替换（复用之前的逻辑）
    val placeholderRegex = Regex("\\{}")
    val requiredArgs = placeholderRegex.findAll(string).count()
    require(args.size == requiredArgs) {
        "参数数量不匹配：需要 $requiredArgs 个，实际 ${args.size} 个"
    }
    var index = 0
    val formattedMessage = placeholderRegex.replace(string) {
        args[index++].toString()
    }

    // 2. 生成元数据
    val timestamp = System.currentTimeMillis().let {
        java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(it)
    }
    val logLevel = "INFO"
    val threadName = Thread.currentThread().name
    val callerClass = Throwable().stackTrace[1].className // 获取调用者的类名

    println(callerClass)
    // 3. 组合最终日志格式
    val logEntry = "$timestamp $logLevel [$threadName] [$callerClass] : $formattedMessage"
    return logEntry
}

