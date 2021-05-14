package chapter5.section4

/**
 * 5.4.4 画出模式(((A|B)*|CD*|EFG)*)*所对应的NFA
 * 5.4.5 画出练习5.4.4的NFA的ε-转换有向图
 */
fun main() {
    val nfa = NFA("(((A|B)*|CD*|EFG)*)*")
    check(nfa.recognizes(""))
    check(nfa.recognizes("AAABBB"))
    check(nfa.recognizes("CCCDDDCD"))
    check(nfa.recognizes("EFGEFG"))
    check(nfa.recognizes("ABABAACCDDEFG"))
    check(nfa.recognizes("EFGCDDBABA"))
    println(nfa.match.joinToString())
    println(nfa.digraph)
}