package chapter3.section1

import edu.princeton.cs.algs4.In
import extensions.spendTimeMillis

/**
 * 自组织查找
 * 自组织查找指的是一种能够将数组元素重新排序使得被访问频率较高的元素更容易被找到的查找算法
 * 请修改练习3.1.2中的ArrayST，在每次查找命中时：将被找到的键值对移动到数组的开头，将所有中间的键值对向右移动一格
 * 这个启发式的过程被成为前移编码
 *
 * 解：LRU算法，最近使用的被移动到最前面，最不常用自动挪至末尾，使用链表实现效率更高
 */
class SelfOrganizingArrayST<K : Any, V : Any> : ArrayST<K, V>() {
    override fun get(key: K): V? {
        for (i in 0 until size) {
            if (keys[i] == key) {
                val value = values[i]
                for (j in i downTo 1) {
                    keys[j] = keys[j - 1]
                    values[j] = values[j - 1]
                }
                keys[0] = key
                values[0] = value
                @Suppress("UNCHECKED_CAST")
                return value as V
            }
        }
        return null
    }
}

fun main() {
    testST(SelfOrganizingArrayST())

    val path = "./data/tale.txt"
    val minLength = 5
    val spendTime1 = spendTimeMillis {
        frequencyCounter(In(path), minLength, ArrayST())
    }
    val spendTime2 = spendTimeMillis {
        frequencyCounter(In(path), minLength, SelfOrganizingArrayST())
    }
    println("spendTime1=$spendTime1 spendTime2=$spendTime2")
}