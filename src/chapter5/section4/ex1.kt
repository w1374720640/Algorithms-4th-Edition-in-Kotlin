package chapter5.section4

/**
 * 给出能够描述含有以下字符的所有字符串的正则表达式
 * 4个连续的A
 * 最多4个连续的A
 * 1到4个连续的A
 */
fun main() {
    // 4个连续的A
    val nfa1 = NFA(".*AAAA.*")
    check(nfa1.recognizes("AAAA"))
    check(nfa1.recognizes("ACCCAAAAAAA"))
    check(nfa1.recognizes("CCCCAAAACCAAAAAACCC"))
    check(nfa1.recognizes("AAACAAAACCCA"))
    check(!nfa1.recognizes("AAACAA"))
    check(!nfa1.recognizes("CCCCAACAAACCC"))

    // 最多4个连续的A
    val nfa2 = NFA("(A|AA|AAA|AAAA)?([^A](A|AA|AAA|AAAA)?)*")
    check(nfa2.recognizes("CCCCCC"))
    check(nfa2.recognizes("ACCAAA"))
    check(nfa2.recognizes("CCCCAACCA"))
    check(nfa2.recognizes("ACCAAAACCC"))
    check(nfa2.recognizes("ACACACA"))
    check(!nfa2.recognizes("AAAAA"))
    check(!nfa2.recognizes("ACCAAAAAAAA"))
    check(!nfa2.recognizes("CACACAAAAAACCCAA"))

    // 1到4个连续的A
    val nfa3 = NFA("((A|AA|AAA|AAAA)([^A](A|AA|AAA|AAAA)?)*|([^A]+(A|AA|AAA|AAAA))+[^A]*)")
    check(nfa3.recognizes("A"))
    check(nfa3.recognizes("ACCACCAAAC"))
    check(nfa3.recognizes("AACAAAACCCCA"))
    check(nfa3.recognizes("CCCACCAACCCAAC"))
    check(!nfa3.recognizes("CCCCC"))
    check(!nfa3.recognizes("AAAAAAAA"))
    check(!nfa3.recognizes("AACCCAAAAA"))
    check(!nfa3.recognizes("CCAAAAAACAACC"))

    println("check succeed.")
}