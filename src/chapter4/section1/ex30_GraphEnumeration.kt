package chapter4.section1

import chapter1.section4.combination
import extensions.inputPrompt
import extensions.readInt

/**
 * 图的枚举
 * 含有V个顶点和E条边（不含平行边）的不同的无向图共有多少种
 *
 * 解：V个顶点最多有V*(V-1)/2条边（不含平行边和自环），取E条边，用组合公式计算即可（可能会溢出Long范围）
 */
fun ex30_GraphEnumeration(V: Int, E: Int): Long {
    return combination(V * (V - 1) / 2, E)
}

fun main() {
    inputPrompt()
    val V = readInt("V: ")
    val E = readInt("E: ")
    println(ex30_GraphEnumeration(V, E))
}