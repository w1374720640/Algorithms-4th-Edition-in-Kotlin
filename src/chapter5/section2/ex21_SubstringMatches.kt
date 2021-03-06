package chapter5.section2

import edu.princeton.cs.algs4.Queue
import extensions.inputPrompt
import extensions.readString

/**
 * 子字符串匹配
 * 给定一列（短）字符串，你的任务是找到所有含有用户所寻找的字符串s的字符串。
 * 为此任务设计一份API并给出一个TST用例来实现这个API。
 * 提示：将每个单词的所有后缀（例如：string, tring, ring, ing, ng, g）插入到TST中。
 *
 * 解：预处理时，为每个给定的字符串创建一个字符串符号表，将每个单词的所有后缀插入到符号表中
 * 匹配时，判断每个符号表是否含有以字符串s为前缀的键，参考练习5.2.20
 */
class SubstringMatches(private val source: Array<String>) {
    private val tstArray = Array(source.size) {
        val tst = TST<Int>()
        val string = source[it]
        for (i in string.indices) {
            tst.put(string.substring(i), 0)
        }
        tst
    }

    fun match(s: String): Iterable<String> {
        val queue = Queue<String>()
        for (i in tstArray.indices) {
            val tst = tstArray[i]
            val iterator = tst.keysWithPrefix(s).iterator()
            if (iterator.hasNext()) {
                queue.enqueue(source[i])
            }
        }
        return queue
    }
}

fun main() {
    inputPrompt()
    val source = arrayOf(
            "SelectionSort",
            "InsertionSort",
            "BubbleSort",
            "ShellSort",
            "MergeSort",
            "QuickSort",
            "HeapSort"
    )
    val substringMatches = SubstringMatches(source)
    val matchString = readString()
    println(substringMatches.match(matchString).joinToString())
}