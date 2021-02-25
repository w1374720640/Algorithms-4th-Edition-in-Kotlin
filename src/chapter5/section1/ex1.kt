package chapter5.section1

import chapter3.section3.RedBlackBST
import edu.princeton.cs.algs4.Stack

/**
 * 实现一种排序算法，首先统计不同键的数量，然后使用一个符号表来实现键索引计数法并将数组排序。
 * （这种方法不适用于不同键的数量很大的情况）
 *
 * 解：创建一个有序符号表，这里使用[chapter3.section3.RedBlackBST]
 * 每个键对应的值表示该键出现的次数，遍历原始数组，统计每个键出现的次数
 * 逆序遍历符号表所有的键，从最后一个键开始，用总数减去最后一个键的值就是最后一个键的起始索引，
 * 最后一个键的起始索引再减去倒数第二个键的数量就是倒数第二个键的起始索引，依次类推求出所有键的起始索引，
 * 创建一个与原数组等长的辅助数组，遍历原始数组，根据符号表中的索引将每个元素放到辅助数组中的相应位置，
 * 最后再将辅助数组中的内容复制回原始数组。
 *
 * 若原始数据总数为N，键的数量为R，则
 * 创建符号表的时间复杂度为O(NlgR)
 * 计数转换为索引的时间复杂度为O(R)
 * 创建辅助数组、复制回原数组的时间复杂度为O(N)
 * 遍历原数组的时间复杂度为O(NlgR)
 * 总时间复杂度为O(NlgR)，在R不是更大的情况下，时间复杂度为线性
 */
fun ex1(array: Array<Key>) {
    val st = RedBlackBST<Key, Int>()
    array.forEach {
        val value = st.get(it)
        if (value == null) {
            st.put(it, 1)
        } else {
            st.put(it, value + 1)
        }
    }

    val keys = st.keys()
    val reverseKeys = Stack<Key>()
    keys.forEach { reverseKeys.push(it) }
    var count = array.size
    reverseKeys.forEach {
        count -= st.get(it)!!
        st.put(it, count)
    }

    val aux = arrayOfNulls<Key>(array.size)
    array.forEach {
        val index = st.get(it)!!
        aux[index] = it
        st.put(it, index + 1)
    }
    for (i in array.indices) {
        array[i] = aux[i]!!
    }
}

fun main() {
    val studentGroup = getStudentGroup().first
    ex1(studentGroup)
    println(studentGroup.joinToString(separator = "\n"))
}