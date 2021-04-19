package chapter5.section4

fun testNFA(create: (String) -> NFA) {
    val nfa1 = create("((A*B|AC)D)")
    check(nfa1.recognizes("AAABD"))
    check(nfa1.recognizes("BD"))
    check(nfa1.recognizes("ACD"))
    check(!nfa1.recognizes("AACD"))
    check(!nfa1.recognizes("ABBD"))
    check(!nfa1.recognizes("AAAB"))
    check(!nfa1.recognizes("AAABDAB"))

    val nfa2 = create("(.*AB((C|D*E)F)*G)")
    check(nfa2.recognizes("ABG"))
    check(nfa2.recognizes("CCABG"))
    check(nfa2.recognizes("ABABCFG"))
    check(nfa2.recognizes("ACDDABDDDEFCFEFG"))
    check(nfa2.recognizes("AAABCFCFDEFCFDDDEFG"))
    check(!nfa2.recognizes("AAABCG"))
    check(!nfa2.recognizes("ABDEFCEFCG"))
    check(!nfa2.recognizes("ABBCFG"))
    check(!nfa2.recognizes("ABABAB"))
    check(!nfa2.recognizes("ABABFG"))
    check(!nfa2.recognizes("ABGFC"))
    println("NFA check succeed.")
}