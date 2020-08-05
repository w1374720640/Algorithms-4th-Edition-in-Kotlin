package chapter2.section2

import chapter1.section3.*
import chapter2.getDoubleArray
import extensions.inputPrompt
import extensions.random
import extensions.readInt
import extensions.spendTimeMillis

/**
 * 链表排序
 * 实现对链表的自然排序（这是将链表排序的最佳方法，因为它不需要额外的空间，且运行时间是线性对数级别的）
 *
 * 解：将完整的链表分为四部分，已经归并过的为第一部分，正在归并的为第二和第三部分，等待归并的为第四部分
 * 第二部分和第三部分归并过程中依次拼接到第一部分后面，归并完成后再从第四部分中拆出两块用于归并
 * （很奇怪的是这里如果用扩展函数效率会比将List作为参数传递效率要高，难道是因为原有的链表操作都是通过扩展函数实现的？）
 */
fun <T : Comparable<T>> DoublyLinkedList<T>.linkedListBottomUpMergeSort() {
    val size = this.size()
    var step = 1
    while (step < size) {
        //每个归并过程开始时，第一部分置空，第二部分起始点为列表起点，循环判断第三部分和第四部分的起始位置
        var second: DoubleNode<T>? = this.first
        var third: DoubleNode<T>? = null
        var fourth: DoubleNode<T>? = null
        this.clear()
        for (i in 0 until size step 2 * step) {
            third = second
            for (j in 1..step) {
                third = third?.next
                if (third == null) break
            }
            //切断第二部分和第三部分的联系
            third?.previous?.next = null
            third?.previous = null

            fourth = third
            for (j in 1..step) {
                fourth = fourth?.next
                if (fourth == null) break
            }
            //切断第三部分和第四部分的联系
            fourth?.previous?.next = null
            fourth?.previous = null

            linkedListBottomUpMerge(second, third)
            if (fourth == null) {
                break
            } else {
                second = fourth
            }
        }
        step *= 2
    }
}

//将第二部分和第三部分拼接到第一部分后面
fun <T : Comparable<T>> DoublyLinkedList<T>.linkedListBottomUpMerge(second: DoubleNode<T>?, third: DoubleNode<T>?) {
    var secondNode = second
    var thirdNode = third
    while (secondNode != null || thirdNode != null) {
        when {
            secondNode == null -> {
                val tempNode = thirdNode!!.next
                this.addTailNode(thirdNode)
                thirdNode = tempNode
            }
            thirdNode == null -> {
                val tempNode = secondNode.next
                this.addTailNode(secondNode)
                secondNode = tempNode
            }
            secondNode.item <= thirdNode.item -> {
                val tempNode = secondNode.next
                this.addTailNode(secondNode)
                secondNode = tempNode
            }
            else -> {
                val tempNode = thirdNode.next
                this.addTailNode(thirdNode)
                thirdNode = tempNode
            }
        }
    }
}

/**
 * 在双向链表头部添加结点
 */
fun <T> DoublyLinkedList<T>.addHeaderNode(node: DoubleNode<T>) {
    node.previous = null
    node.next = first
    if (first == null) {
        first = node
        last = node
    } else {
        first!!.previous = node
        first = node
    }
}

/**
 * 在双向链表尾部添加结点
 */
fun <T> DoublyLinkedList<T>.addTailNode(node: DoubleNode<T>) {
    node.previous = last
    node.next = null
    if (last == null) {
        first = node
        last = node
    } else {
        last!!.next = node
        last = node
    }
}

/**
 * 检查迭代器是否按升序排序
 */
fun <T : Comparable<T>> checkAscOrder(iterator: Iterator<T>): Boolean {
    if (!iterator.hasNext()) return true
    var pre = iterator.next()
    while (iterator.hasNext()) {
        val value = iterator.next()
        if (pre > value) return false
        pre = value
    }
    return true
}

/**
 * 比较基于链表的归并排序和基于数组的排序效率差距
 */
fun compareLinkedListAndArray(
        linkedListSortMethod: () -> Unit,
        arraySortMethod: (Array<Double>) -> Unit,
        times: Int,
        size: Int
) {
    require(times > 0)
    var linkedListTime = 0L
    repeat(times) {
        val list = DoublyLinkedList<Double>()
        repeat(size) {
            list.addTail(random())
        }
        linkedListTime += spendTimeMillis { linkedListSortMethod() }
    }
    linkedListTime /= times
    println("Linked list average spend $linkedListTime ms")

    var arrayTime = 0L
    repeat(times) {
        val array = getDoubleArray(size)
        arrayTime += spendTimeMillis { arraySortMethod(array) }
    }
    arrayTime /= times
    println("Array average spend $arrayTime ms")
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    val list = DoublyLinkedList<Double>()
    repeat(size) {
        list.addTail(random())
    }
    list.linkedListBottomUpMergeSort()
    val isAscOrder = checkAscOrder(list.forwardIterator())
    println("isAscOrder=$isAscOrder size=${list.size()}")

    compareLinkedListAndArray(list::linkedListBottomUpMergeSort, ::bottomUpMergeSort, 10, size)
}
