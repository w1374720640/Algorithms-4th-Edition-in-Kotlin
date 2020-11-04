package chapter3.section4

import chapter3.section1.testST
import edu.princeton.cs.algs4.Queue

/**
 * 线性探测法中的延时删除
 * 为LinearProbingHashST添加一个delete()方法，在删除一个键值对时将其值设为null，
 * 并在调用resize()方法时将键值对从表中删除。
 * 这种方法的主要难点在于决定合适应该调用resize()方法。
 * 请注意：如果后来的put()方法为该键指定了一个新的值，你应该用新值将null覆盖掉。
 * 你的程序在决定扩张或者收缩数组时不但要考虑到数组的空元素，也要考虑到这种死掉的元素。
 */
class LazyDeleteLinearProbingHashST<K : Any, V : Any>(m: Int = 4) : LinearProbingHashST<K, V>(m) {
    //值被null覆盖但是键仍然存在的键值对数量
    private var deleteNum = 0

    override fun resize(size: Int) {
        val newST = LazyDeleteLinearProbingHashST<K, V>(size)
        for (i in keys.indices) {
            if (keys[i] != null && values[i] != null) {
                @Suppress("UNCHECKED_CAST")
                newST.put(keys[i] as K, values[i] as V)
            }
        }
        m = newST.m
        keys = newST.keys
        values = newST.values
        deleteNum = 0
    }

    override fun put(key: K, value: V) {
        //如果键的数量占满一半则扩容，包括有值的键和没有值的键
        if (n + deleteNum >= m / 2) resize(m * 2)
        var i = hash(key)
        while (true) {
            @Suppress("UNCHECKED_CAST")
            when (keys[i] as? K) {
                null -> {
                    keys[i] = key
                    values[i] = value
                    n++
                    return
                }
                key -> {
                    if (values[i] == null) {
                        deleteNum--
                        n++
                    }
                    values[i] = value
                    return
                }
                else -> {
                    i = (i + 1) % m
                }
            }
        }
    }

    override fun delete(key: K) {
        var i = hash(key)
        while (true) {
            @Suppress("UNCHECKED_CAST")
            when (keys[i] as? K) {
                null -> throw NoSuchElementException()
                key -> {
                    if (values[i] == null) throw NoSuchElementException("already delete.")
                    //删除键值对时，只需要将值置空，不需要将键置空，也不需要移动后面的键，但是需要记录被删除值的键值对数量
                    values[i] = null
                    n--
                    deleteNum++
                    //检查是否需要重新调整大小
                    if (m >= 8 && n <= m / 8) resize(m / 2)
                    return
                }
                else -> i = (i + 1) % m
            }
        }
    }

    override fun keys(): Iterable<K> {
        val queue = Queue<K>()
        for (i in keys.indices) {
            if (keys[i] != null && values[i] != null) {
                @Suppress("UNCHECKED_CAST")
                queue.enqueue(keys[i] as K)
            }
        }
        return queue
    }

    override fun joinToString(limit: Int): String {
        val stringBuilder = StringBuilder("m=${m} n=${n}")
        for (i in keys.indices) {
            if (i >= limit) {
                stringBuilder.append(" ...")
                break
            }
            if (keys[i] == null || values[i] == null) {
                stringBuilder.append(" _") //使用下划线替代null
            } else {
                stringBuilder.append(" ${keys[i].toString()}")
            }
        }
        return stringBuilder.toString()
    }
}

private fun testLazyDeleteST(st: LazyDeleteLinearProbingHashST<Int, Int>) {
    require(st.isEmpty())
    repeat(4) {
        st.put(it, 0)
    }
    check(st.size() == 4)
    check(st.joinToString().startsWith("m=8 n=4"))
    repeat(4) {
        st.delete(it)
    }
    check(st.isEmpty())
    check(st.joinToString().startsWith("m=4 n=0"))
    repeat(4) {
        st.put(it + 4, 0)
    }
    check(st.size() == 4)
    check(st.joinToString().startsWith("m=8 n=4"))
    st.put(8, 0)
    check(st.size() == 5)
    check(st.joinToString().startsWith("m=16 n=5"))
    println("LazyDeleteLinearProbingHashST check succeed.")
}

fun main() {
    testST(LazyDeleteLinearProbingHashST())
    testLazyDeleteST(LazyDeleteLinearProbingHashST())
}