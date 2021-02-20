package chapter5.section1

/**
 * 低位优先的字符串排序（定长字符串）
 *
 * 根据字符串的前W个元素排序，要求字符串长度至少大于等于W
 * 书中第454页倒数第二行中规定，在本章中使用常数R=256作为String类的参数
 */
object LSD {
    private const val R = 256

    fun sort(array: Array<String>, W: Int) {
        val aux = Array(array.size) { "" }

        for (i in W - 1 downTo 0) {
            val count = IntArray(R + 1)
            array.forEach {
                val index = it[i].toInt()
                count[index + 1]++
            }
            for (j in 1 until R) {
                count[j] += count[j - 1]
            }
            array.forEach {
                val index = it[i].toInt()
                aux[count[index]] = it
                count[index]++
            }
            for (j in aux.indices) {
                array[j] = aux[j]
            }
        }
    }
}

fun getNumberPlates() = arrayOf(
        "4PGC938",
        "2IYE230",
        "3CIO720",
        "1ICK750",
        "1OHV845",
        "4JZY524",
        "1ICK750",
        "3CIO720",
        "1OHV845",
        "1OHV845",
        "2RLA629",
        "2RLA629",
        "3ATW723"
)

fun main() {
    val array = getNumberPlates()
    LSD.sort(array, 7)
    println(array.joinToString(separator = "\n"))
}