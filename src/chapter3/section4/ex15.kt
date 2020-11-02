package chapter3.section4

/**
 * 在最坏情况下，向一张初始为空、基于线性探测法并能够动态调整数组大小的散列表中插入N个键需要多少次比较
 *
 * 解：N²/2次
 */
fun main() {
    val st = object : LinearProbingHashST<Int, Int>() {
        var compareTimes = 0

        override fun hash(key: Int): Int {
            return 1
        }

        override fun put(key: Int, value: Int) {
            if (n >= m / 2) resize(m * 2)
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
    val N = 10000
    repeat(N) {
        st.put(it, 0)
    }
    println("N=$N compareTimes=${st.compareTimes} compareTimes/N^2=${st.compareTimes.toDouble() / (N * N)}")
}