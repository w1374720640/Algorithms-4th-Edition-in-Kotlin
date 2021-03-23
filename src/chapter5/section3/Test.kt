package chapter5.section3

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