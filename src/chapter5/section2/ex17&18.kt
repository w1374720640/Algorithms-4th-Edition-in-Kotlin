package chapter5.section2

import edu.princeton.cs.algs4.In
import extensions.inputPrompt
import extensions.readAllStrings
import extensions.readString

/**
 * 5.2.17: 拼写检查
 * 编写一个TST的用例SpellChecker，从命令行接受一个英文字典文件作为参数，
 * 然后从标准输入读取一个字符串并打印所有不在字典中的单词。
 * 请使用字符串集合数据类型。
 *
 * 5.2.18: 白名单
 * 编写一个TST的用例，解决1.1节和3.5节中介绍并讨论过的（请见3.5.2.2节）白名单问题。
 */
fun main() {
    inputPrompt()
    // 可以使用任意文件作为字典，例如《双城记》的路径: ./data/tale.txt
    val dictionary = In(readString("dictionary file: "))
    val set = ThreeWayStringSET()
    dictionary.readAllStrings().forEach {
        set.add(it)
    }
    val words = readAllStrings()
    val notContainSet = ThreeWayStringSET()
    words.forEach {
        if (!set.contains(it)) {
            notContainSet.add(it)
        }
    }
    println(notContainSet)
}