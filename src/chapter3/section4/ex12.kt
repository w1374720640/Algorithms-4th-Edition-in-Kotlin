package chapter3.section4

import chapter3.section1.LinkedListST
import chapter3.section2.fullArray

/**
 * 设有键A到G，散列值如下所示
 * 将它们按照一定顺序插入到一张初始为空大小为7的基于线性探测的散列表中（这里数组的大小不会动态调整）
 * 下面哪个选项是不可能由插入这些键产生的？
 * 给出这些键在构造散列表时可能所需的最大和最小探测次数，并给出相应的插入顺序来证明你的答案
 * a. E F G A C B D
 * b. C E B G F D A
 * c. B D F A C E G
 * d. C G B A D E F
 * e. F G B D A C E
 * f. G E C A D B F
 *
 * 键            A   B   C   D   E   F   G
 * 散列值(M=7)   2   0   0   4   4   4   2
 *
 * 解：对7个字母全排列，分别将每个排列按顺序插入线性探测表中，记录最终结果及比较次数
 * 记录每个选项最大探测次数和最小探测次数
 *
 * 因为散列值有两个0，两个2，三个4，每组数据不可能重叠
 * 所以前两位必定是BC的排列，再后两位是AG的排列，最后三位是DEF的排列
 * 上面所有选项都不可能由插入这些键产生
 */
fun main() {
    val array = "ABCDEFG".toCharArray().toTypedArray()
    val list = ArrayList<Array<Char>>()
    //对指定数组全排列
    fullArray(array, 0, list)

    val options = LinkedListST<String, Int>()
    options.put("EFGACBD", 0)
    options.put("CEBGFDA", 1)
    options.put("BDFACEG", 2)
    options.put("CGBADEF", 3)
    options.put("FGBDACE", 4)
    options.put("GECADBF", 5)

    //额外加入一个序列，证明我的代码没有问题 (⊙﹏⊙)
    options.put("CBGAFED", 6)

    val minArray = arrayOfNulls<Result>(7)
    val maxArray = arrayOfNulls<Result>(7)
    list.forEach {
        val result = getInsertResult(it)
        val index = options.get(String(result.after.toCharArray()))
        if (index != null) {
            val minResult = minArray[index]
            if (minResult == null || result.compareTimes < minResult.compareTimes) {
                minArray[index] = result
            }
            val maxResult = maxArray[index]
            if (maxResult == null || result.compareTimes > maxResult.compareTimes) {
                maxArray[index] = result
            }
        }
    }

    for (i in 0..6) {
        val minResult = minArray[i]
        val maxResult = maxArray[i]
        if (minResult == null || maxResult == null) {
            println("i=$i impossible")
        } else {
            println("i=$i minResult=${minResult}")
            println("i=$i maxResult=${maxResult}")
        }
    }
}

private class Result(val before: Array<Char>, val after: Array<Char>, val compareTimes: Int) {
    override fun toString(): String {
        return "[before:${before.joinToString()} after:${after.joinToString()} compareTimes:${compareTimes}]"
    }
}

private val keyHashCodes = mapOf<Char, Int>('A' to 2, 'B' to 0, 'C' to 0, 'D' to 4, 'E' to 4, 'F' to 4, 'G' to 2)

private fun getInsertResult(array: Array<Char>): Result {
    val st = object : LinearProbingHashST<Char, Int>(7) {
        //插入键需要比较的总次数
        var compareTimes = 0

        override fun hash(key: Char): Int {
            return keyHashCodes[key] ?: throw IllegalArgumentException()
        }

        override fun resize(size: Int) {
        }

        override fun put(key: Char, value: Int) {
            var i = hash(key)
            while (true) {
                compareTimes++
                @Suppress("UNCHECKED_CAST")
                when (keys[i] as? Char) {
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
    array.forEach { st.put(it, 0) }
    val keys = st.keys().iterator()
    val result = Array(7) { keys.next() }
    return Result(array, result, st.compareTimes)
}

