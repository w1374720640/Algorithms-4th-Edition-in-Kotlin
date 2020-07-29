package chapter2.section2

import chapter1.section3.*
import extensions.formatDouble
import extensions.formatInt
import extensions.randomBoolean

/**
 * 打乱链表
 * 实现一个分治算法，使用线性对数级别的时间和对数级别的额外空间随机打乱一条链表
 *
 * 解：使用逆向的自顶向下归并排序打乱链表
 * 先创建两个空链表，遍历原始链表，随机将值放入两个新链表中
 * 使用递归的方式，分别将每个链表拆分成两个链表
 * 当拆分后的链表长度为1时，重新合并两个链表
 * 每次递归将链表拆分成两半，所以最大递归层数lgN层
 * 每层都需要完整的遍历列表，运行时间为N，所以共需要NlgN的运行时间
 * 一次拆分需要的额外空间为2，所以同一时刻需要的最大额外空间为2lgN
 */
fun <T> DoublyLinkedList<T>.shuffleLinkedList() {
    if (this.size() <= 1) return
    //列表长度为2时，直接判断是否需要调换两个元素的顺序，提高效率
    if (this.size() == 2) {
        if (randomBoolean()) {
            this.addHeader(this.deleteTail())
        }
        return
    }
    val leftList = DoublyLinkedList<T>()
    val rightList = DoublyLinkedList<T>()
    repeat(this.size()) {
        if (randomBoolean()) {
            leftList.addTail(this.deleteHeader())
        } else {
            rightList.addTail(this.deleteHeader())
        }
    }
    leftList.shuffleLinkedList()
    rightList.shuffleLinkedList()
    this.appendList(leftList)
    this.appendList(rightList)
}

fun <T> DoublyLinkedList<T>.appendList(list: DoublyLinkedList<T>) {
    when {
        list.isEmpty() -> {
            //Do Nothing
        }
        this.isEmpty() -> {
            this.first = list.first
            this.last = list.last
        }
        else -> {
            this.last!!.next = list.first
            list.first!!.previous = this.last
            this.last = list.last
        }
    }
}

fun main() {
    val size = 100
    val list = DoublyLinkedList<Int>()
    repeat(size) {
        list.addTail(it)
    }
    println("Before shuffle: ${list.joinToString()}")
    list.shuffleLinkedList()
    println("After shuffle : ${list.joinToString()}")
    println()

    checkShuffleResult(10, 100000)
}

/**
 * 检查随机打乱打乱链表的方法是否真的随机
 * 将大小为M的链表打乱N次，每次打乱前将链表初始化为a[i]=i，
 * 打印M*M的表格，i行j列表示i在打乱后落到j位置的次数
 * 参考练习1.1.36c
 */
fun checkShuffleResult(size: Int, times: Int) {
    val array = Array(size) { Array(size) { 0 } }
    repeat(times) {
        val list = DoublyLinkedList<Int>()
        repeat(size) {
            list.addTail(it)
        }
        list.shuffleLinkedList()
        //
        var index = 0
        list.forwardIterator().forEach {
            array[it][index++]++
        }
    }
    val expect = times.toDouble() / size
    println("expect=${formatDouble(expect, 2)}")
    array.forEach { list ->
        println(list.joinToString { formatInt(it, 5) })
    }
}
