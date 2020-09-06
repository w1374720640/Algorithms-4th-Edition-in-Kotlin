package chapter2.section5

import extensions.inputPrompt
import extensions.readString
import java.io.File

/**
 * 按文件名排序
 * 编写一个FileSorter程序，从命令行接受一个目录名并打印出按照文件名排序后的所有文件
 * 提示：使用File数据类型
 */
fun ex28_SortFilesByName(array: Array<File>) {
    array.sortBy {
        it.name
    }
}

fun main() {
    inputPrompt()
    // 测试数据目录: ./data
    val path = readString("directory: ")
    val file = File(path)
    if (file.isDirectory) {
        val fileList = file.listFiles()
        if (fileList == null || fileList.isEmpty()) {
            println("File list is empty!")
        } else {
            ex28_SortFilesByName(fileList)
            fileList.forEach {
                println(it.name)
            }
        }
    } else {
        println("Not a directory")
    }
}