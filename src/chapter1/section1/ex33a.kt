package chapter1.section1

//向量点乘
//向量的点乘,也叫向量的内积、数量积，对两个向量执行点乘运算，就是对这两个向量对应位一一相乘之后求和的操作，点乘的结果是一个标量。
//对于向量a和向量b  a=[a1,a2,...an]   b=[b1,b2,...bn]  a和b的点积公式为 a·b=a1*b1+a2*b2+...+an*bn
fun ex33a(x: Array<Int>, y: Array<Int>): Int {
    require(x.size == y.size) { "向量点乘要求两个向量的行列数相同，x.size=${x.size} y.size=${y.size}" }
    require(x.isNotEmpty())
    var result = 0
    for (i in x.indices) {
        result += x[i] * y[i]
    }
    return result
}

fun main() {
    val x = arrayOf(1, 2, 3)
    val y = arrayOf(4, 5, 6)
    println("x=${x.joinToString()}")
    println("y=${y.joinToString()}")
    val result = ex33a(x, y)
    println("result=${result}")
}