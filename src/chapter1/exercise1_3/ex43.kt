package chapter1.exercise1_3

import extensions.inputPrompt
import extensions.readString
import java.io.File

//打印文件夹下所有文件并用递归的方式在所有子文件夹的名下缩进列出其下的所有文件
//为什么提示要用队列实现？
fun ex43(file: File, prefix: String = "") {
    println(prefix + file.name)
    if (file.isDirectory) {
        val fileList = file.listFiles()
        fileList?.forEach {
            ex43(it, "$prefix  ")
        }
    }
}

fun main() {
    inputPrompt()
    //当前路基为项目根目录，例如，ex43这个文件的完整路径是 ./src/chapter1/exercise1_3/ex43.kt
    val path = readString()
    val file = File(path)
    ex43(file)
}