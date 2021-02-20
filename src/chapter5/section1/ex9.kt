package chapter5.section1

/**
 * 实现能够处理变长字符串的低位优先的字符串排序算法
 *
 * 解：低位优先字符串排序会从右向左遍历所有的字符，
 * 先遍历所有字符串，找到字符串的最大长度，
 * 假设所有字符串的长度都为最大长度，长度不够的右侧填充空白，从右向左遍历，
 * 修改charAt()方法，使得右侧空白部分的索引为-1，非空白部分返回实际索引
 *
 * 若字符串的数量为N，字符串的最大长度为M，则低位优先字符串排序的时间复杂度为O(NM)
 */
object ex9 {
    private const val R = 256

    fun sort(array: Array<String>) {
        var maxLength = 0
        array.forEach {
            if (it.length > maxLength) maxLength = it.length
        }
        val aux = Array(array.size) { "" }
        for (d in maxLength - 1 downTo 0) {
            val count = IntArray(R + 2)
            array.forEach {
                count[charAt(it, d) + 2]++
            }
            for (i in 1 until count.size) {
                count[i] += count[i - 1]
            }
            array.forEach {
                val index = charAt(it, d) + 1
                aux[count[index]] = it
                count[index]++
            }
            for (i in array.indices) {
                array[i] = aux[i]
            }
        }
    }

    private fun charAt(string: String, d: Int): Int {
        return if (d >= string.length) {
            -1 // 右侧空白字符
        } else {
            string[d].toInt() // 非空白字符
        }
    }
}

fun main() {
    val msdData = getMSDData()
    ex9.sort(msdData)
    println(msdData.joinToString(separator = "\n"))
}