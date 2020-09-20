package chapter3.section1

import edu.princeton.cs.algs4.In
import extensions.formatStringLength

/**
 * 基于字典的频率统计
 * 修改FrequencyCounter，接受一个字典文件作为参数，统计标准输入中出现在字典中的单词的频率
 * 并将单词和频率打印为两张表格，一张按照频率高低排序，一张按照字典顺序排序
 *
 * 解：函数接受两个参数，分别为字符串数组和一个字典，字典是一个以不同字符串为key，value为递增序号的符号表
 * 按照频率高低排序打印表格：
 * 创建一个新的符号表，以字符串为key，频率为value，遍历数组，判断元素是否存在于字典中，存在则将其放入符号表中并记录出现频率
 * 遍历符号表中所有的key和value，按频率逆序排序，打印表格
 * 按照字典顺序排序：
 * 创建一个新的有序符号表，以字典中字符串的序号为key，字符串为value，
 * 遍历数组，判断元素是否存在于字典中，存在则以字典中的序号为key，字符串为value放入有序符号表中
 * 因为是有序符号表，所以直接按顺序遍历符号表即可按字典顺序打印表格
 */
fun ex26_sortByFrequency(array: Array<String>, dictionary: ST<String, Int>) {
    val st = ArrayOrderedST<String, Int>()
    array.forEach {
        if (dictionary.contains(it)) {
            val count = st.get(it)
            if (count == null) {
                st.put(it, 1)
            } else {
                st.put(it, count + 1)
            }
        }
    }
    class Node(val key: String, val value: Int) : Comparable<Node> {
        override fun compareTo(other: Node): Int {
            val compare = value.compareTo(other.value)
            return if (compare != 0) {
                compare
            } else {
                key.compareTo(other.key)
            }
        }
    }
    val iterator = st.keys().iterator()
    val result = Array(st.size()) {
        val key = iterator.next()
        Node(key, st.get(key)!!)
    }
    result.sortDescending()
    result.forEach { node ->
        println("word:${formatStringLength(node.key, 12, true)} frequency:${node.value}")
    }
}

fun ex26_sortByDictionaryOrder(array: Array<String>, dictionary: ST<String, Int>) {
    val st = ArrayOrderedST<Int, String>()
    array.forEach {
        if (dictionary.contains(it)) {
            st.put(dictionary.get(it)!!, it)
        }
    }
    st.keys().forEach {
        println(st.get(it))
    }
}

fun main() {
    val input = In("./data/tinyTale.txt")
    val allString = input.readAllStrings()
    //会按照字母顺序将键排序
    val dictionary = ArrayOrderedST<String, Int>()
    allString.forEach {
        dictionary.put(it, 0)
    }
    var index = 0
    dictionary.keys().forEach {
        dictionary.put(it, index++)
    }
    //为了方便，输入和字典来自同一数据源，也可以自定义输入流
    ex26_sortByFrequency(allString, dictionary)
    println()
    ex26_sortByDictionaryOrder(allString, dictionary)
}