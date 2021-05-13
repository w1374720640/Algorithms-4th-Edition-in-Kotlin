package chapter5.section4

/**
 * 二进制数的可整除性
 * 使用正则表达式描述以下二进制字符串使得其对应的整数能够满足以下条件
 * a.被2整除
 * b.被3整除
 * c.被123整除
 *
 * 解：对于能被2整除的数来说，它的二进制字符串必定以0结尾
 * 构造能被3整除数的二进制字符串的正则表达式：
 *     参考：http://blog.2baxb.me/archives/588
 *     总结：一个数除以3的余数有三种，0、1、2，输入的可能值有两种0、1，构造出DFA，再将DFA转换成正则表达式
 *     有多种正则表达式的写法，课文5.4.3.6节给出的答案是(0|1(01*0)*1)*
 * 被123整除的正则表达式：DFA太复杂，可以构造，但不会转换成正则表达式，可以自己用一个较小的数试试，比如7
 * 关于DFA、NFA、正则表达式的相互转化，属于编译原理的知识，可以自己寻找教材或视频
 */
fun main() {
    val N = 10000
    val nfa1 = NFA("[01]*0")
    for (i in 0 until N) {
        check(nfa1.recognizes(chapter1.section1.ex9(i)) == (i % 2 == 0)) { "nfa1 check failed, i=$i" }
    }
    val nfa2 = NFA("(1(01*0)*10*)*0*")
    for (i in 0 until N) {
        check(nfa2.recognizes(chapter1.section1.ex9(i)) == (i % 3 == 0)) { "nfa2 check failed, i=$i" }
    }
    println("check succeed.")
}