package chapter1.section3

import extensions.inputPrompt
import extensions.readString
import java.io.File

/**
 * 文件列表
 * 文件夹就是一列文件和文件夹的列表。
 * 编写一个程序，从命令行接受一个文件夹名作为参数，打印出该文件夹下的所有文件并用递归的方式在所有子文件夹的名下（缩进）列出其下的所有文件。
 * 提示：使用队列，并参考java.io.File。
 *
 * 解：直接打印不需要使用队列，如果需要返回文件名列表，则需要存放入队列中。
 */
fun ex43_ListingFiles(file: File, prefix: String = "") {
    println(prefix + file.name)
    if (file.isDirectory) {
        val fileList = file.listFiles()
        fileList?.forEach {
            ex43_ListingFiles(it, "$prefix  ")
        }
    }
}

fun main() {
    inputPrompt()
    //当前路基为项目根目录，例如，ex43_ListingFiles这个文件的完整路径是 ./src/chapter1/exercise1_3/ex43_ListingFiles.kt
    val path = readString("path: ")
    val file = File(path)
    ex43_ListingFiles(file)
}