package chapter5.section3

/**
 * 为BoyerMoore类添加一个count()方法来统计模式字符串的在文本中的出现次数，
 * 再添加一个searchAll()方法来打印出所有出现的位置。
 */
fun BoyerMoore.searchAll(txt: String): IntArray {
    val list = ArrayList<Int>()
    val N = txt.length
    val M = pat.length
    var i = 0
    var j = M - 1
    while (i <= N - M) {
        if (txt[i + j] == pat[j]) {
            j--
        } else {
            var k = j - right[alphabet.toIndex(pat[j])]
            if (k <= 0) {
                k = 1
            }
            i += k
            j = M - 1
        }
        if (j < 0) {
            list.add(i)
            i++
            j = M - 1
        }
    }
    return list.toIntArray()
}

fun BoyerMoore.count(txt: String): Int {
    return searchAll(txt).size
}

fun main() {
    val pat = "AAA"
    val txt = "AABAAAAABABAAAA"
    val boyerMoore = BoyerMoore(pat)
    check(boyerMoore.count(txt) == 5)
    check(boyerMoore.searchAll(txt).contentEquals(intArrayOf(3, 4, 5, 11, 12)))
    println("check succeed.")
}