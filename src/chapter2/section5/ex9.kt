package chapter2.section5

import edu.princeton.cs.algs4.In
import extensions.formatLong
import extensions.formatStringLength
import kotlin.math.max
import kotlin.math.min

/**
 * 为将右侧所示的数据排序编写一个新的数据类型
 *
 * 解：道琼斯指数，按成交量排序
 */
class DJIA(val date: String, val volume: Long) : Comparable<DJIA> {
    override fun compareTo(other: DJIA): Int {
        return volume.compareTo(other.volume)
    }

    override fun toString(): String {
        return "${formatStringLength(date, 9)} ${formatLong(volume, 10)}"
    }
}

fun main() {
    val list = mutableListOf<DJIA>()
    // csv文件是纯文本文件，默认用英文逗号分隔，用excel打开会格式化成表格样式
    // 但是用Notepad++或Sublime Text打开会显示原始文本，可以直接用字符流读取
    val input = In("./data/DJIA.csv")
    // csv文件第一行为标题，其它每行对应一条数据
    val titles = input.readLine().split(',')
    val dateIndex = 0
    val volumeIndex = 5
    while (!input.isEmpty) {
        val data = input.readLine().split(",")
        list.add(DJIA(data[dateIndex], data[volumeIndex].toLong()))
    }
    list.sort()
    for (i in 0..min(10, list.size - 1)) {
        println(list[i])
    }
    println("...")
    for (i in max(0, list.size - 10) until list.size) {
        println(list[i])
    }
}