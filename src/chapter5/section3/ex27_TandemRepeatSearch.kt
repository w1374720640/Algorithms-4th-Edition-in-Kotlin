package chapter5.section3

/**
 * 串联重复查找
 * 在字符串s中，基础字符串b的串联重复就是连续将b至少重复两遍（无重叠）的一个子字符串。
 * 开发并实现一个线性时间的子字符串查找算法，接受给定的字符b和s，返回s中b的最长串联重复的起始位置。
 * 例如，当b为“abcad”而s为“abcabcababcababcababcab”时，你的程序应该返回3。
 *
 * 解：中文版原文中有翻译错误，给出的示例b应该为“abcab”
 * 将两个b字符拼接，然后以拼接后的字符串为模板，使用RabinKarp算法匹配s
 */
fun ex27_TandemRepeatSearch(b: String, s: String): Int {
    val pat = b + b
    val rabinKarp = RabinKarp(pat)
    return rabinKarp.search(s)
}

fun main() {
    val s = "abcabcababcababcababcab"
    check(ex27_TandemRepeatSearch("abcab", s) == 3)
    check(ex27_TandemRepeatSearch("ab", s) == 6)
    check(ex27_TandemRepeatSearch("bca", s) == 1)
    check(ex27_TandemRepeatSearch("abc", s) == 0)
    check(ex27_TandemRepeatSearch("abca", s) == 23)
    check(ex27_TandemRepeatSearch("babc", s) == 23)
    check(ex27_TandemRepeatSearch("abab", s) == 23)
    check(ex27_TandemRepeatSearch("abcad", s) == 23)
    println("check succeed.")
}