package chapter5.section1

/**
 * 键索引计数排序
 */
fun keyIndexCountSort(array: Array<Key>, R: Int) {
    val count = IntArray(R + 1)
    array.forEach {
        //  键的取值范围为[1,R-1]
        val key = it.key()
        require(key in 1 until R)
        // 统计频率
        count[key + 1]++
    }
    for (i in 1 until R) {
        // 计算每个key的起始索引
        count[i] += count[i - 1]
    }
    // 额外辅助数组
    val aux = arrayOfNulls<Key>(array.size)
    array.forEach {
        val key = it.key()
        // 根据索引将对象放到指定位置
        aux[count[key]] = it
        // 索引自增
        count[key]++
    }
    for (i in array.indices) {
        val key = aux[i]
        // aux数组中的每个位置应该都不为空
        check(key != null)
        array[i] = key
    }
}

fun main() {
    val group = getStudentGroup()
    val array = group.first
    val R = group.second
    println(array.joinToString(separator = "\n"))

    keyIndexCountSort(array, R)

    println()
    println(array.joinToString(separator = "\n"))
}