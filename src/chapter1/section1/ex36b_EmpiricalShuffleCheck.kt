package chapter1.section1

import extensions.inputPrompt
import extensions.random
import extensions.readAllInts

//随机打乱数组，且在其他位置的概率相等（也可能位置不变）
fun ex36b_EmpiricalShuffleCheck(array: IntArray): IntArray {
    for (i in 0 until array.size - 1) {
        //练习1.1.37修改成以下代码
        //val j = Random.Default.nextInt(array.size)
        //从[i,array.size)中随机选择一个值作为索引，和i位置交换
        val j = i + random(array.size - i)
        val temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }
    return array
}

fun main() {
    inputPrompt()
    val array = readAllInts()
    println(array.joinToString())
    val result = ex36b_EmpiricalShuffleCheck(array)
    println(result.joinToString())
}