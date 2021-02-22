package chapter5.section1

import chapter1.section3.*
import chapter2.section2.shuffleLinkedList

/**
 * 链表排序
 * 编写一个排序算法，接受一条以String为键值参数的结点链表并重新按顺序排列所有结点
 * （返回一个指向键值最小的结点的指针）。
 * 使用三向字符串快速排序。
 *
 * 解：标准三向字符串快速排序使用索引将数组分为三部分，
 * 对双向链表的排序使用结点将链表分割为三部分，
 * 只需要额外实现比较、交换链表两个结点的方法，其余代码大致相同
 */
fun ex16_LinkedListSort(list: DoublyLinkedList<String>) {
    if (list.isEmpty()) return
    // 随机打乱链表，时间复杂度O(NlgN)，空间复杂度O(lgN)，参考练习2.2.18
    list.shuffleLinkedList()
    ex16_LinkedListSort(list, list.first!!, list.last!!, 0)
}

private fun ex16_LinkedListSort(list: DoublyLinkedList<String>,
                                startNode: DoubleNode<String>,
                                endNode: DoubleNode<String>,
                                d: Int) {
    if (startNode === endNode) return
    var i = startNode.next
    var j = startNode
    var k = endNode
    // 经过交换结点，startNode结点可能已经不是最左侧的结点，需要手动记录最左侧结点
    var newStartNode: DoubleNode<String>? = null
    var newEndNode: DoubleNode<String>? = null
    while (i != null) {
        val result = compare(i, j, d)
        when {
            result > 0 -> {
                // 双向链表交换结点后，虽然i、j、k的引用不变，但在链表中的实际位置已经改变了，和数组索引不同
                list.swap(i, k)
                if (newEndNode == null) newEndNode = i
                val temp = i.previous!!
                i = k // j不可能和i或k是相同结点，所以j不需要重新赋值
                k = temp
            }
            result < 0 -> {
                list.swap(i, j)
                if (newStartNode == null) newStartNode = i
                val temp = i.next!!
                if (i === k) { // 如果i和k是同一个结点，则在给i重新赋值时也需要给k重新赋值
                    k = j
                }
                i = j.next
                j = temp
            }
            else -> {
                i = i.next
            }
        }
        if (i == null || k.next === i) break
    }
    if (newStartNode != null) ex16_LinkedListSort(list, newStartNode, j.previous!!, d)
    if (j.item.length > d) ex16_LinkedListSort(list, j, k, d + 1)
    if (newEndNode != null) ex16_LinkedListSort(list, k.next!!, newEndNode, d)
}

private fun compare(node1: DoubleNode<String>, node2: DoubleNode<String>, d: Int): Int {
    return when {
        d >= node1.item.length && d >= node2.item.length -> 0
        d >= node1.item.length -> -1
        d >= node2.item.length -> 1
        else -> node1.item[d].compareTo(node2.item[d])
    }
}

/**
 * 交换双向链表的两个结点
 */
fun <T> DoublyLinkedList<T>.swap(node1: DoubleNode<T>, node2: DoubleNode<T>) {
    if (node1 === node2) return
    val pre1 = node1.previous
    val next1 = node1.next
    val pre2 = node2.previous
    val next2 = node2.next

    if (next1 === node2) { // 结点1和结点2相邻，结点2在结点1后面
        if (pre1 == null) {
            first = node2
        } else {
            pre1.next = node2
        }
        if (next2 == null) {
            last = node1
        } else {
            next2.previous = node1
        }
        node1.previous = node2
        node1.next = next2
        node2.previous = pre1
        node2.next = node1
    } else if (next2 === node1) { // 结点1和结点2相邻，结点1在结点2后面
        if (pre2 == null) {
            first = node1
        } else {
            pre2.next = node1
        }
        if (next1 == null) {
            last = node2
        } else {
            next1.previous = node2
        }
        node1.previous = pre2
        node1.next = node2
        node2.previous = node1
        node2.next = next1
    } else { // 结点1和结点2不相邻，前后关系不确定
        if (pre1 == null) {
            first = node2
        } else {
            pre1.next = node2
        }
        if (next1 == null) {
            last = node2
        } else {
            next1.previous = node2
        }
        if (pre2 == null) {
            first = node1
        } else {
            pre2.next = node1
        }
        if (next2 == null) {
            last = node1
        } else {
            next2.previous = node1
        }
        node1.previous = pre2
        node1.next = next2
        node2.previous = pre1
        node2.next = next1
    }
}

fun main() {
    val array = getMSDData()
    val list = DoublyLinkedList<String>()
    array.forEach { list.addTail(it) }
    ex16_LinkedListSort(list)
    val stringBuilder = StringBuilder()
    list.forwardIterator().forEach {
        stringBuilder.append(it)
                .append("\n")
    }
    println(stringBuilder)
}