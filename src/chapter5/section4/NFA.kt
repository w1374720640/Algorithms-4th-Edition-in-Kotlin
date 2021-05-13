package chapter5.section4

import chapter4.section2.Digraph
import chapter4.section2.DirectedDFS
import edu.princeton.cs.algs4.Bag
import edu.princeton.cs.algs4.Stack

/**
 * 正则表达式的模式匹配，非确定有限状态自动机
 * 支持连接操作、或操作'|'、闭包操作'*'、括号'(' ')'、通配符'.'、至少重复1次'+'、重复0或1次'?'、
 * 指定的集合[]、范围集合[A-Z]、补集[^A-Z]、转义符'\'
 * 暂不支持指定重复次数如{3}{1-2}等，
 * 转义符只能转义元字符，不能转义其他特殊字符如"\t" "\n" "\d"等，
 * '*'或'+'操作符后不能接'?'操作符
 * 或操作'|'必须包含在括号内
 *
 * 各种符号对应的操作如下：
 * 连接：每个字母都有一个匹配转换指向下一个字符
 * 单字符闭包：当闭包运算符（*）出现在单个字符后时，在该字符和“*”之间添加两条相互指向的ε-转换
 * 括号、“|”运算、括号外闭包：使用一个栈保存所有的左括号和“|”运算，当遇见右括号时，从栈中弹出一个字符，
 *     判断是否是左括号，如果是“|”运算符，加入一个新列表中，重新从栈中弹出一个字符，直到找到左括号，
 *     从左括号添加到每个“|”运算符后一位的ε-转换，添加每个“|”运算符到右括号的ε-转换，
 *     判断右括号右侧是否为闭包，如果是，在左括号和“*”之间添加两条相互指向的ε-转换
 * 通配符：和普通字符相同，每个字符都有一个匹配转换指向下一个字符，匹配时能够匹配任意字符
 * 至少重复一次：和闭包的区别是只有从“+”字符指向单字符或左括号的ε-转换，没有从单字符或左括号指向“+”字符的ε-转换
 * 重复0或1次：和闭包的区别是只有从单字符或左括号指向“?”字符的ε-转换，没有从“?”字符指向单字符或左括号的ε-转换
 * 指定的集合：对于指定的集合[ABC]D，分别添加从左中括号到A、B、C的ε-转换，再分别添加从A、B、C到右中括号的匹配转换
 *     指定集合的匹配转换不是下一个字符，需要创建一个新数组用于记录匹配转换的状态，
 *     集合中除了取补集的'^'字符、连接字符'-'和转义字符'\'外不能包含其他任何元字符、括号，不能嵌套
 * 范围集合：对于范围集合[A-D]F，分别添加从左中括号到A、-、D的ε-转换，再分别添加从A、-、D到右中括号的匹配转换，
 *     匹配字符串时，如果待匹配字符为'-'，读取左右两侧字符判断是否匹配
 * 补集：对于指定集合[^AB-E]F，添加从左中括号到'^'字符的ε-转换，再添加从'^'字符到右中括号的匹配转换，
 *     匹配字符串时，如果匹配到'^'字符，判断当前字符是否在后面的集合中，如果在则不匹配，否则匹配
 *     对于集合，还需要考虑闭包运算符，因为集合不能嵌套，所以可以将左中括号同样加入栈中，遇见右中括号时弹出
 * 转义序列：对于模式"A\.B"来说，添加一个从'A'到'\'的匹配转换，从'\'到'B'的匹配转换，同时需要考虑B位置为闭包或其他重复次数的情况
 */
class NFA(val regex: String) {
    val M = regex.length
    val digraph = Digraph(M + 1)

    // 记录匹配转换的下一个字符，因为范围集合中的匹配转换可能不是相邻字符
    val match = IntArray(M + 1) { -1 }

    init {
        val stack = Stack<Int>()
        var i = 0
        while (i < M) {
            when (regex[i]) {
                '(' -> {
                    digraph.addEdge(i, i + 1)
                    stack.push(i)
                }
                '|' -> stack.push(i)
                '*' -> {
                    // 单字符的闭包
                    digraph.addEdge(i, i - 1)
                    digraph.addEdge(i - 1, i)
                    digraph.addEdge(i, i + 1)
                }
                '+' -> {
                    digraph.addEdge(i, i - 1)
                    digraph.addEdge(i, i + 1)
                }
                '?' -> {
                    digraph.addEdge(i - 1, i)
                    digraph.addEdge(i, i + 1)
                }
                ')' -> {
                    val orList = ArrayList<Int>()
                    var top = stack.pop()
                    while (regex[top] != '(') {
                        orList.add(top)
                        top = stack.pop()
                    }
                    for (j in orList.indices) {
                        digraph.addEdge(top, orList[j] + 1)
                        digraph.addEdge(orList[j], i)
                    }
                    digraph.addEdge(i, i + 1)
                    if (checkNextCharRepeat(i, top, digraph)) {
                        i++
                        digraph.addEdge(i, i + 1)
                    }
                }
                '[' -> stack.push(i)
                ']' -> {
                    val left = stack.pop()
                    for (j in left + 1 until i) {
                        // 清空集合内元素的匹配转换
                        match[j] = -1
                    }
                    if (regex[left + 1] == '^') {
                        digraph.addEdge(left, left + 1)
                        match[left + 1] = i
                    } else {
                        var j = left + 1
                        while (j < i) {
                            digraph.addEdge(left, j)
                            match[j] = i
                            if (regex[j] == '\\') {
                                j++
                            }
                            j++
                        }
                    }
                    digraph.addEdge(i, i + 1)
                    if (checkNextCharRepeat(i, left, digraph)) {
                        i++
                        digraph.addEdge(i, i + 1)
                    }
                }
                '\\' -> { // 在java编译环境中转义字符本身需要转义
                    match[i] = i + 2
                    if (checkNextCharRepeat(i + 1, i, digraph)) {
                        i += 2
                        digraph.addEdge(i, i + 1)
                    } else {
                        i++
                    }
                }
                else -> match[i] = i + 1
            }
            i++
        }
        check(stack.isEmpty) { "Regular expression format error." }
    }

    /**
     * 判断当前模块的下一个字符是否是重复操作，需要传入当前位置和左括号位置
     */
    private fun checkNextCharRepeat(index: Int, leftIndex: Int, digraph: Digraph): Boolean {
        if (index == M - 1) return false
        when (regex[index + 1]) {
            '*' -> {
                digraph.addEdge(leftIndex, index + 1)
                digraph.addEdge(index + 1, leftIndex)
            }
            '+' -> digraph.addEdge(index + 1, leftIndex)
            '?' -> digraph.addEdge(leftIndex, index + 1)
            // 暂时只支持重复任意次、重复至少一次、重复0或1次，不支持重复指定次数
            else -> return false
        }
        return true
    }

    fun recognizes(txt: String): Boolean {
        val N = txt.length
        var i = 0
        // 存放所有经过ε-转换等价的点，代码保证了所有点不会重复，所以直接用Bag不需要用Set
        var equalSet = Bag<Int>()
        var dfs = DirectedDFS(digraph, 0)
        for (j in 0..M) {
            if (dfs.marked(j)) {
                equalSet.add(j)
            }
        }
        while (i < N) {
            val char = txt[i]
            // 存放匹配转换后的点，所有点不会重复
            val matchSet = Bag<Int>()
            val iterator = equalSet.iterator()
            while (iterator.hasNext()) {
                val j = iterator.next()
                if (j == M) continue
                when (regex[j]) {
                    '.' -> addMatch(matchSet, j)
                    '^' -> {
                        var k = j + 1
                        var hasMatch = false
                        while (!hasMatch && regex[k] != ']') {
                            if (regex[k] == '-') {
                                hasMatch = char > regex[k - 1] && char < regex[k + 1]
                            } else {
                                hasMatch = char == regex[k]
                            }
                            k++
                        }
                        if (!hasMatch) { // 只有在集合中匹配失败才是在补集中匹配成功
                            addMatch(matchSet, j)
                        }
                    }
                    '-' -> {
                        if (char > regex[j - 1] && char < regex[j + 1]) {
                            addMatch(matchSet, j)
                        }
                    }
                    '\\' -> {
                        if (regex[j + 1] == char) { // 转义字符需要直接和后一个字符匹配
                            addMatch(matchSet, j)
                        }
                    }
                    // 在可以使用转义字符的情况下，所有元字符都必须特殊处理，不能放到else里
                    '*', '+', '?', '(', ')', '[', ']', '|' -> {
                    }
                    else -> {
                        if (regex[j] == char) {
                            addMatch(matchSet, j)
                        }
                    }
                }
            }
            i++
            if (matchSet.isEmpty) return false
            equalSet = Bag()
            // 找到match中所有点为起点可达的所有顶点
            dfs = DirectedDFS(digraph, matchSet)
            for (j in 0..M) {
                if (dfs.marked(j)) {
                    equalSet.add(j)
                }
            }
        }
        equalSet.forEach {
            if (it == M) return true
        }
        return false
    }

    // 将匹配转换的下一个元素添加到背包中，下一个元素不应该为-1
    private fun addMatch(bag: Bag<Int>, index: Int) {
        check(match[index] != -1) { "Match conversion should not be -1" }
        bag.add(match[index])
    }
}

fun main() {
    testNFA { NFA(it) }
}