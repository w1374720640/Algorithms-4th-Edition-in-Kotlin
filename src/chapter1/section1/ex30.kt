package chapter1.section1

import extensions.inputPrompt
import extensions.readInt

//创建一个N*N的数组，当i和j互质时，a[i][j]为true，否则为false
//两个或两个以上的整数的最大公约数是1，则称它们为互质
//如果数域是正整数，那么1与所有正整数互质
//如果数域是整数，那么1和-1与所有整数互质，而且它们是唯一与0互质的整数
fun ex30(N: Int) {
    val array: Array<Array<Boolean>> = Array(N) { Array(N) { false } }
    //以[i,i]为分割线，先判断右侧值，再对称赋值左侧值
    //互质数为非0自然数，所以0和任何数都不互质
    for (i in 0 until N) {
        array[0][i] = i == 1
    }
    //1和任何非零自然数数都互质
    for (i in 1 until N) {
        array[1][i] = true
    }
    //求差判断法：如果两个数的差与较小值互质，则这两个数互质
    for (i in 2 until N) {
        for (j in i until N) {
            val diff = j - i
            val small = if (diff > i) i else diff
            val large = if (diff > i) diff else i
            array[i][j] = array[small][large]
        }
    }
    //将右半边的值复制到左半边
    for (i in 1 until N) {
        for (j in 0 until i) {
            array[i][j] = array[j][i]
        }
    }
    //打印数组
    for (i in 0 until N) {
        println(array[i].joinToString {
            if (it) "1" else "0"
        })
    }
}

fun main() {
    inputPrompt()
    ex30(readInt())
}