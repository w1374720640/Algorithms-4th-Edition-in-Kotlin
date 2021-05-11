package chapter5.section4

/**
 * 将5.4.6.4节框注“经典的一般正则表达式模式匹配（GREP）NFA的用例”中的GREP修改为GREPmatch，
 * 将模式用括号包裹起来但不在模式两端加上“.*”。
 * 这样程序就只会打出属于给定正则表达式所描述的语言的输入行字符串。
 * 给出以下命令的结果：
 * a. % java GREPmatch "(A|B)(C|D)" < tinyL.txt
 * b. % java GREPmatch "A(B|C)*D" < tinyL.txt
 * c. % java GREPmatch "(A*B|AC)D" < tinyL.txt
 */
fun ex7(pat: String, txts: Array<String>) {
    val regexp = "(${pat})"
    val nfa = NFA(regexp)
    txts.forEach {
        if (nfa.recognizes(it)) {
            println(it)
        }
    }
}

fun getTinyL() = arrayOf(
        "AC",
        "AD",
        "AAA",
        "ABD",
        "ADD",
        "BCD",
        "ABCCBD",
        "BABAAA",
        "BABBAAA"
)

fun main() {
    ex7("(A|B)(C|D)", getTinyL())
    println()
    ex7("A(B|C)*D", getTinyL())
    println()
    ex7("(A*B|AC)D", getTinyL())
}