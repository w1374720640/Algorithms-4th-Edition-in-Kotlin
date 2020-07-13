package chapter1.exercise1_5

import edu.princeton.cs.algs4.In
import extensions.formatDouble
import extensions.spendTimeMillis

/**
 * 将整数对全部添加到UF类中，返回最终的联结数和消耗的时间
 */
fun unionFind(uf: UF, pairArray: Array<Pair<Int, Int>>): Pair<Int, Long> {
    val time = spendTimeMillis {
        pairArray.forEach {
            uf.union(it.first, it.second)
        }
    }
    return uf.count() to time
}

/**
 * 从文件中读取最大值N和整数对
 * 参数只接受三个值，1 2 3，分别对应小规模、中等规模、超大规模
 */
fun getNAndPairArrayFromFile(scale: Int): Pair<Int, Array<Pair<Int, Int>>> {
    val path = when (scale) {
        2 -> "./data/mediumUF.txt"
        3 -> "./data/largeUF.txt"
        else -> "./data/tinyUF.txt"
    }
    val input = In(path)
    //N表示整数对列表中的最大值
    val N = input.readInt()
    val array = input.readAllInts()
    val pairArray = Array(array.size / 2) { array[it * 2] to array[it * 2 + 1] }
    return N to pairArray
}

inline fun unionFindTest(create: (Int) -> UF) {
    var size = 1000
    var preTime = 0L
    val pair = getNAndPairArrayFromFile(3)
    while (true) {
        val uf = create(pair.first)
        val result = unionFind(uf, pair.second.copyOfRange(0, minOf(size, pair.second.size)))
        val ratio = if (preTime == 0L) "/" else formatDouble(result.second.toDouble() / preTime, 2)
        println("unionNum=${minOf(size, pair.second.size)} remainingAmount=${result.first} spendTime=${result.second}ms ratio=${ratio}")
        preTime = result.second
        if (size >= pair.second.size) {
            break
        } else {
            size *= 2
        }
    }
}