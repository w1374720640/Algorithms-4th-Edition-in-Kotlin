package capter1.exercise1_1

//求ex16(6)运行结果
//返回的是String而不是Int
fun ex16(n: Int): String {
    if (n <= 0) return ""
    return ex16(n - 3) + n + ex16(n - 2) + n
}

fun main() {
    println(ex16(6))
}