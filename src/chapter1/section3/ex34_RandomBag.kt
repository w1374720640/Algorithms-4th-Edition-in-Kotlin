package chapter1.section3

import edu.princeton.cs.algs4.StdRandom
import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 随机背包
 * 随机背包能够存储一组元素并支持表1.3.10中的API：
 * 编写一个RandomBag类来实现这份API。
 * 请注意，除了形容词“随机”之外，这份API和Bag的API是相同的，
 * 这意味着迭代应该随机访问背包中的所有元素（对于每次迭代，所有的N!种排列出现的可能性均相同）。
 * 提示：用数组保存所有元素并在迭代器的构造函数中随机打乱它们的顺序。
 */
class RandomBag<T> : Iterable<T> {
    private var array = arrayOfNulls<Any>(2)
    private var size = 0

    fun isEmpty() = size == 0

    fun size() = size

    fun add(value: T) {
        checkResize()
        array[size++] = value
    }

    private fun checkResize() {
        if (size == array.size) {
            val newArray = arrayOfNulls<Any>(size * 2)
            array.forEachIndexed { index, any ->
                newArray[index] = any
            }
            array = newArray
        }
    }

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var i = 0

        init {
            if (size > 1) {
                // 打乱数组的指定范围
                StdRandom.shuffle(array, 0, size)
            }
        }

        override fun hasNext(): Boolean {
            return i < size
        }

        override fun next(): T {
            if (i >= size) throw NoSuchElementException()
            return array[i++] as T
        }
    }

    fun joinToString(): String {
        var result = ""
        val iterator = iterator()
        while (iterator.hasNext()) {
            result += iterator.next().toString() + " "
        }
        return result
    }
}

fun main() {
    inputPrompt()
    val array = readAllStrings()
    val bag = RandomBag<String>()
    array.forEach {
        bag.add(it)
    }
    repeat(10) {
        println(bag.joinToString())
    }

}