package chapter5.section3

import edu.princeton.cs.algs4.In

/**
 * 测试给定的[StringSearch]实现是否正确
 */
fun testStringSearch(create: (String) -> StringSearch) {
    val txt1 = "ABACADABRAC"
    check(create("ABAC").search(txt1) == 0)
    check(create("ABACADABRAC").search(txt1) == 0)
    check(create("BACA").search(txt1) == 1)
    check(create("ABRA").search(txt1) == 6)
    check(create("A").search(txt1) == 0)
    check(create("R").search(txt1) == 8)
    check(create("ABAA").search(txt1) == 11)
    check(create("ABACADABRACC").search(txt1) == 11)

    val txt2 = "AAAAAAAAAB"
    check(create("AAAA").search(txt2) == 0)
    check(create("AAAAB").search(txt2) == 5)
    check(create("AAAABB").search(txt2) == 10)

    val txt3 = "ABAAAABAAAAAAAAA"
    check(create("BAAAAAAAAA").search(txt3) == 6)

    val txt4 = "BCBAABACAABABACAA"
    check(create("ABABAC").search(txt4) == 9)

    val txt5 = "FINDINAHAYSTACKNEEDLEINA"
    check(create("NEEDLE").search(txt5) == 15)

    check(create("A").search("") == 0)

    println("StringSearch check succeed.")
}

/**
 * 练习5.3.7~5.3.10中，用于测试searchAll()方法是否正确
 */
fun <T> testSearchAll(searchFun: T.(String) -> IntArray, create: (String) -> T) {
    val pat1 = "AAA"
    val txt1 = "AABAAAAABABAAAA"
    val result1 = intArrayOf(3, 4, 5, 11, 12)
    check(create(pat1).searchFun(txt1).contentEquals(result1))

    val pat2 = "B"
    val result2 = intArrayOf(2, 8, 10)
    check(create(pat2).searchFun(txt1).contentEquals(result2))

    val pat3 = "BB"
    val result3 = intArrayOf()
    check(create(pat3).searchFun(txt1).contentEquals(result3))

    println("searchAll test succeed.")
}

fun getTinyTaleStream() = In("./data/tinyTale.txt")

fun <T> testStreaming(searchFun: T.(In) -> Int, create: (String) -> T) {
    check(create("it was the best").searchFun(getTinyTaleStream()) == 0)
    check(create("t was").searchFun(getTinyTaleStream()) == 1)
    check(create(" ").searchFun(getTinyTaleStream()) == 2)
    check(create("worst").searchFun(getTinyTaleStream()) == 36)
    check(create("times\nit").searchFun(getTinyTaleStream()) == 45)
    check(create("it was the age").searchFun(getTinyTaleStream()) == 51)
    check(create("despair").searchFun(getTinyTaleStream()) == 269)
    check(create("it has").searchFun(getTinyTaleStream()) == 277)
}