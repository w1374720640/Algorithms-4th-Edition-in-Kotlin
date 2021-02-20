package chapter5.section1

/**
 * 字母表
 * 实现5.0.2节给出的Alphabet类的API并用它实现能够处理任意字母表的低位优先的和高位优先的字符串排序算法。
 *
 * 解：基于字母表的低位优先字符串排序算法，参考练习5.1.9
 */
fun alphabetLSDSort(array: Array<String>, alphabet: Alphabet) {
    var maxLength = 0
    array.forEach {
        if (it.length > maxLength) maxLength = it.length
    }
    val aux = Array(array.size) { "" }
    for (d in maxLength - 1 downTo 0) {
        val count = IntArray(alphabet.R() + 2)
        array.forEach {
            count[lsdCharAt(it, alphabet, d) + 2]++
        }
        for (i in 1 until count.size) {
            count[i] += count[i - 1]
        }
        array.forEach {
            val index = lsdCharAt(it, alphabet, d) + 1
            aux[count[index]] = it
            count[index]++
        }
        for (i in array.indices) {
            array[i] = aux[i]
        }
    }
}

private fun lsdCharAt(string: String, alphabet: Alphabet, d: Int): Int {
    return if (d >= string.length) {
        -1 // 右侧空白字符
    } else {
        alphabet.toIndex(string[d]) // 非空白字符
    }
}

fun main() {
    val msdData = getMSDData()
    alphabetLSDSort(msdData, Alphabet.EXTENDED_ASCII)
    println(msdData.joinToString(separator = "\n"))
}
