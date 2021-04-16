package chapter5.section3

/**
 * 串联重复查找
 * 在字符串s中，基础字符串b的串联重复就是连续将b至少重复两遍（无重叠）的一个子字符串。
 * 开发并实现一个线性时间的子字符串查找算法，接受给定的字符b和s，返回s中b的最长串联重复的起始位置。
 * 例如，当b为“abcad”而s为“abcabcababcababcababcab”时，你的程序应该返回3。
 *
 * 解：中文版原文中有翻译错误，给出的示例b应该为“abcab”
 * 题目要求返回“最长”串联重复的起始位置，可能有多个串联重复，找到最长的那个
 * 设字符s中最多有N个不重叠的b字符串，N=s.length/b.length
 * 将N个b字符串拼接成一个新字符串，以该字符串为模板创建KMP算法的dfa数组，
 * 在s字符串中查找模板，记录模板到达的最右侧位置，就可以得到最长串联重复
 */
fun ex27_TandemRepeatSearch(b: String, s: String): Int {
    val R = 256
    val builder = StringBuilder()
    repeat(s.length / b.length) {
        builder.append(b)
    }
    val pat = builder.toString()

    val dfa = Array(R) { IntArray(pat.length) }
    var x = 0
    dfa[pat[0].toInt()][0] = 1
    for (j in 1 until pat.length) {
        for (i in 0 until R) {
            dfa[i][j] = dfa[i][x]
        }
        dfa[pat[j].toInt()][j] = j + 1
        x = dfa[pat[j].toInt()][x]
    }

    var maxMatchState = b.length //至少重复两遍，所以索引至少要大于b.length
    var maxIndex = -1
    var i = 0
    var j = 0
    while (i < s.length && j < pat.length) {
        j = dfa[s[i++].toInt()][j]
        if (j % b.length == 0 && j > maxMatchState) {
            maxMatchState = j
            maxIndex = i - j
        }
    }
    return maxIndex
}

private fun test(b: String, s: String, expect: Int) {
    check(ex27_TandemRepeatSearch(b, s) == expect)
}

fun main() {
    test("abc", "abbcabcccbac", -1)
    test("aaa", "aaaaabbbaaab", -1)
    test("aaa", "aaaaaabbbaaaaaaaaa", 9)
    test("abc", "aabcaabcabcabccc", 5)
    test("abc", "abcabcaaabcabcabcabcabcccccabcabc", 8)
    println("check succeed.")
}