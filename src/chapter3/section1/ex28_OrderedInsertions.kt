package chapter3.section1

import extensions.spendTimeMillis

/**
 * 有序的插入
 * 修改ArrayOrderedST，使得插入一个比当前所有键都大的键只需要常数时间
 * 这样在构造符号表时有序的使用put()插入键值对就只需要线性时间了
 */
class OrderedInsertionsST<K : Comparable<K>, V : Any> : ArrayOrderedST<K, V>() {
    override fun put(key: K, value: V) {
        when {
            !isEmpty() && key > max() -> {
                if (needExpansion()) {
                    expansion()
                }
                keys[size] = key
                values[size] = value
                size++
            }
            !isEmpty() && key == max() -> {
                values[size - 1] = value
            }
            else -> super.put(key, value)
        }
    }
}

fun main() {
    testOrderedST(OrderedInsertionsST())

    val size = 500_0000
    //递增的key
    val keys = Array(size) { it }
    val time1 = spendTimeMillis {
        val st = ArrayOrderedST<Int, String>()
        keys.forEach {
            st.put(it, "")
        }
    }
    println("time1=$time1 ms")
    val time2 = spendTimeMillis {
        val st = OrderedInsertionsST<Int, String>()
        keys.forEach {
            st.put(it, "")
        }
    }
    println("time2=$time2 ms")
}