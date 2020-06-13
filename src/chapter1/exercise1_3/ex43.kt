package chapter1.exercise1_3

import extensions.inputPrompt
import extensions.readString
import java.io.File

fun printFileList(file: File, prefix: String = "") {
    println(prefix + file.name)
    if (file.isDirectory) {
        val fileList = file.listFiles()
        fileList?.forEach {
            printFileList(it, "$prefix  ")
        }
    }
}

fun main() {
    inputPrompt()
    //当前路基为项目根目录，例如，ex43这个文件的完整路径是 ./src/chapter1/exercise1_3/ex43.kt
    val path = readString()
    val file = File(path)
    printFileList(file)
}