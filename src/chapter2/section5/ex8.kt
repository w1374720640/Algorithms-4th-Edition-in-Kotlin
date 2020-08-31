package chapter2.section5

import edu.princeton.cs.algs4.In

/**
 * 编写一段程序Frequency
 * 从标准输入读取一列字符串并按照字符串出现频率由高到低的顺序打印出每个字符串及其出现的次数
 *
 * 解：读入字符串时，依次放入数组中，
 * 打印字符串时，先将字符串排序，统计每个字符串的重复次数，将字符串和重复次数关联成一个新对象，
 * 将新对象按重复次数倒序排序，打印字符串及出现次数
 */
class Frequency {
    class Node(val item: String, var count: Int) : Comparable<Node> {
        override fun compareTo(other: Node): Int {
            val countCompare = count.compareTo(other.count)
            if (countCompare != 0) return countCompare
            return item.compareTo(other.item)
        }

        override fun toString(): String {
            return "[${item}:${count}]"
        }
    }

    var list = mutableListOf<String>()

    fun input(item: String) {
        list.add(item)
    }

    fun output(): List<Node> {
        list.sort()
        if (list.isEmpty()) return emptyList()
        val nodeList = mutableListOf<Node>()
        nodeList.add(Node(list[0], 1))
        for (i in 1 until list.size) {
            if (list[i] == list[i - 1]) {
                nodeList.last().count++
            } else {
                nodeList.add(Node(list[i], 1))
            }
        }
        nodeList.sortDescending()
        return nodeList
    }
}

fun main() {
    val input = In("./data/tale.txt")
    val frequency = Frequency()
    while (!input.isEmpty) {
        frequency.input(input.readString())
    }
    val result = frequency.output()
    println("size=${result.size}")
    println(result.joinToString(limit = 100))
}

