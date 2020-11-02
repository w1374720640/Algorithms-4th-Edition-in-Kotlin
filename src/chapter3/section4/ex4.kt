package chapter3.section4

/**
 * 使用散列函数 (a * k) % M 将 S E A R C H X M P L 中的第k个键散列为一个数组索引
 * 编写一段程序找出a和最小的M，使得该散列函数得到的每个索引都不相同（没有碰撞）
 * 这样的函数也被成为完美散列函数
 *
 * 解：(a * k) % M 这个公式中的k应该是第k个元素的哈希值而不是索引k，否则a=1，M等于总长度即可满足题意
 * m从输入长度开始递增，a从1不断递增，直到公式计算出的所有值都不相同
 */
fun main() {
    val inputArray = "SEARCHXMPL".toCharArray()
    var outputArray = BooleanArray(inputArray.size)
    val resetOutputArray = {
        for (i in outputArray.indices) {
            outputArray[i] = false
        }
    }
    var a = 0
    var m = inputArray.size
    var allDifferent = false
    loop@ while (!allDifferent) {
        a++
        //限制a<=1000
        if (a > 1000) {
            m++
            a = 0
            outputArray = BooleanArray(m)
            continue
        }

        resetOutputArray()
        for (key in inputArray) {
            val hash = (a * key.hashCode()) % m
            //这里hash值原本可能为负数，但是限制a<=1000后hash不可能为负数
            if (outputArray[hash]) {
                continue@loop
            } else {
                outputArray[hash] = true
            }
        }
        allDifferent = true
    }

    println("a=$a m=$m")
    val stringBuilder = StringBuilder()
    inputArray.forEach {
        stringBuilder.append("key:$it hash=${(a * it.hashCode()) % m}\n")
    }
    println(stringBuilder.toString())
}