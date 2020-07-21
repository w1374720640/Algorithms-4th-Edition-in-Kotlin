package chapter1.section1

import extensions.inputPrompt
import extensions.readInt

//将整数的二进制用String表示
fun ex9(num: Int): String {
    var result = ""
    if (num == 0) {
        result = "0"
    } else {
        //不能用CharArray，1.toChar()不等于'1'，无法打印
        val array = IntArray(32)
        var index = array.size
        var value = num
        do {
            --index
            //与1按位与
            array[index] = value and 1
            //无符号右移一位
            value = value ushr 1
            //省略二进制前面的0
        } while (value != 0)
        for (i in index until array.size) {
            result += array[i]
        }
    }
    return result
}

fun main() {
    inputPrompt()
    println(ex9(readInt()))
}