package chapter3.section4

import extensions.formatDouble
import extensions.random
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 停车问题
 * 用实验研究来验证一个猜想：
 * 向一张大小为M的基于线性探测法的散列表中插入M个随机键所需的比较次数为~cM^(3/2)，其中c=sqrt(PI/2)
 */
fun main() {
    val M = 10000

    val st = object : LinearProbingHashST<Int, Int>(M) {
        //插入键需要比较的总次数
        var compareTimes = 0

        override fun resize(size: Int) {
        }

        override fun put(key: Int, value: Int) {
            var i = hash(key)
            while (true) {
                compareTimes++
                @Suppress("UNCHECKED_CAST")
                when (keys[i] as? Int) {
                    null -> {
                        keys[i] = key
                        values[i] = value
                        n++
                        return
                    }
                    key -> {
                        values[i] = value
                        return
                    }
                    else -> {
                        i = (i + 1) % m
                    }
                }
            }
        }
    }

    while (st.size() < M) {
        val key = random(Int.MAX_VALUE)
        if (!st.contains(key)) {
            st.put(key, 0)
        }
    }

    val c = sqrt(PI / 2)
    val expect = c * (M.toDouble().pow(1.5))
    println("expect=${formatDouble(expect, 0)} real=${st.compareTimes}")
}