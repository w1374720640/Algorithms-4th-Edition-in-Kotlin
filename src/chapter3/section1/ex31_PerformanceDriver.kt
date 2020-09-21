package chapter3.section1

import extensions.random
import extensions.shuffle
import extensions.spendTimeMillis
import java.lang.StringBuilder

/**
 * 性能测试
 * 编写一段性能测试程序，先用put()构造一张符号表，再用get()进行访问，
 * 使得表中的每个键平均被命中10次，且有大致相同次数的未命中访问。
 * 键为长度从2到50不等的随机字符串。
 * 重复这样的测试若干遍，记录每遍的运行时间，打印平均运行时间或将它们绘制成图。
 */
fun main() {
    val size = 1_0000
    val times = 10

    repeat(times) {
        val time = spendTimeMillis {
            //随机生成长度为2~50字符的字符串
            val keys = Array(size) {
                val length = random(2, 51)
                val builder = StringBuilder()
                repeat(length) {
                    builder.append(random(10))
                }
                builder.toString()
            }
            val allKeys = Array(size * 2) {
                if (it < size) {
                    keys[it]
                } else {
                    val length = random(2, 51)
                    val builder = StringBuilder()
                    repeat(length) {
                        builder.append(random(10))
                    }
                    builder.toString()
                }
            }
            allKeys.shuffle()

            val st = ArrayOrderedST<String, Int>()
            keys.forEach {
                st.put(it, 0)
            }
            repeat(10) {
                allKeys.forEach {
                    st.get(it)
                }
            }
        }
        println("Spend $time ms")
    }
}