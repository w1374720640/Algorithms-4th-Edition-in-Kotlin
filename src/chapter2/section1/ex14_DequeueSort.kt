package chapter2.section1

import chapter1.section3.*
import chapter2.enumValueOf
import edu.princeton.cs.algs4.StdRandom

/**
 * 出列排序
 * 说说你会如何将一副扑克牌排序，限制条件是只能查看最上面的两张牌，交换最上面的两张牌，
 * 或者将最上面的一张牌放到这摞牌的最下面。
 *
 * 解：因为没有没有要求按花色排序，所以只需要按点数排序
 * 使用冒泡排序的思想排序
 * 比较头部两张牌的大小，保留大的值，小的放入牌底，一次循环后，最大的值在牌顶
 * 下次循环开始时，将已排序的值依次放入牌底，剩余的牌中再选出最大值，不断循环
 */
fun ex14_DequeueSort(wrapper: PokerWrapper) {
    wrapper.print()
    for (i in wrapper.SIZE - 1 downTo 0) {
        repeat(wrapper.SIZE - 1 - i) {
            wrapper.reverse()
        }
        for (j in 0 until i) {
            val pair = wrapper.look()
            if (pair.first > pair.second) {
                wrapper.swap()
            }
            wrapper.reverse()
        }
    }
    wrapper.print()
}

/**
 * 对扑克可操作的行为进行封装
 */
class PokerWrapper {
    val SIZE = 52
    private val list = DoublyLinkedList<Poker>()

    //获取一副随机打乱的扑克牌
    init {
        val array = Array(SIZE) { Poker(it / 4, enumValueOf(it % 4)) }
        //使用乱序算法打乱数组
        StdRandom.shuffle(array)
        array.forEach {
            list.addTail(it)
        }
    }

    fun look(): Pair<Poker, Poker> {
        return list.get(0) to list.get(1)
    }

    fun swap() {
        val second = list.delete(1)
        list.addHeader(second)
    }

    fun reverse() {
        val first = list.deleteHeader()
        list.addTail(first)
    }

    fun print() {
        println(list.joinToString())
    }
}

fun main() {
    ex14_DequeueSort(PokerWrapper())
}