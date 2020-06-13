package chapter1.exercise1_3

import extensions.inputPrompt
import extensions.random
import extensions.readAllStrings

/**
 * 随机背包
 * 迭代数据时，返回的是随机顺序，每次迭代顺序都不相同，且所有N!种可能出现的概率相同
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

    //将最后一位随机和前面任意一位交换（也可能是自己），然后返回最后一位，剩余数量减一
    //随机打乱数组顺序的方法参考练习1.1.36b
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var last = size

        override fun hasNext(): Boolean {
            return last > 0
        }

        override fun next(): T {
            if (last <= 0) throw NoSuchElementException()
            val index = random(last--)
            val temp = array[last]
            array[last] = array[index]
            array[index] = temp
            return array[last] as T
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