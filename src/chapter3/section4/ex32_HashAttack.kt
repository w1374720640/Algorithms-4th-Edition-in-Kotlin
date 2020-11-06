package chapter3.section4

import extensions.random
import extensions.randomBoolean
import kotlin.math.pow

/**
 * 散列攻击
 * 找出2^n个hashCode()方法返回值均相同且长度均为2^n的字符串
 * 假设String类型的hashCode()方法的实现如下：
 * public int hashCode() {
 *     int hash = 0;
 *     for (int i = 0; i < length(); i++)
 *         hash = (hash * 31) + charAt(i);
 *     return hash;
 * }
 * 重要提示：Aa和BB的散列值相同
 *
 * 解：这里假设题目要求字符串必须以26个字母的大小写组成
 * A~Z的ASCII码为65~90 a~z的ASCII码为97~122
 * a-31=B b-31=C ... y-31=Z
 * B+31=a C+31=b ... Z+31=y
 * 将字符两两一组，判断每组字符是否可以转化为另一组字符
 * 若两个字符都在[B,Z]内，则可以将第一个字符减一，第二个字符加31（转化为小写字母）
 * 若两个字符都在[a,y]内，则可以将第一个字符加一，第二个字符减31（转化为大写字母）
 * 若第一个字符在[A,Y]内，第二个字符在[a,y]内，则可以将第一个字符加一，第二个字符减31（转化为大写字母）
 * 若第一个字符在[b,z]内，第二个字符在[B,Z]内，则可以将第一个字符减一，第二个字符加31（转化为小写字母）
 * 上述四条中，第一条和第三条互为逆操作，第二条和第四条互为逆操作
 * 通过递归，获取所有能够能够转化成的字符串，参考练习3.2.9中的递归实现全排列代码
 */
private typealias Transform = (CharArray, Int, Int) -> Unit

fun ex32_HashAttack(origin: String): List<String> {
    val list = ArrayList<String>()
    val array = origin.toCharArray()
    attack(array, 0, list)
    return list
}

private fun attack(array: CharArray, start: Int, list: ArrayList<String>) {
    for (i in start..array.size - 2) {
        val transform = getTransform(array[i], array[i + 1])
        val inverseTransform = getInverseTransform(array[i], array[i + 1])
        if (transform != null && inverseTransform != null) {
            // 将相邻的两个字符替换成hash值相同的另两个字符
            transform(array, i, i + 1)
            list.add(String(array))
            // i + 1 而不是 i + 2 因为第一个和第二个转换后，第二个可能和第三个转换
            attack(array, i + 1, list)
            // 恢复原来的字符
            inverseTransform(array, i, i + 1)
        }

    }
}

private val BZBZ: Transform = { array, first, second ->
    array[first] = array[first] - 1
    array[second] = array[second] + 31
}
private val ayay: Transform = { array, first, second ->
    array[first] = array[first] + 1
    array[second] = array[second] - 31
}

// 这里需要注意，由于kotlin的bug，lambda类型的变量如果名称忽略大小写后相等，编译时会抛出异常
// 这里如果用ayay和AYay两个变量，会抛出异常，加下划线区分
private val AYay_: Transform = { array, first, second ->
    array[first] = array[first] + 1
    array[second] = array[second] - 31
}
private val bzBZ_: Transform = { array, first, second ->
    array[first] = array[first] - 1
    array[second] = array[second] + 31
}

private fun getTransform(first: Char, second: Char): Transform? {
    return when {
        first in 'B'..'Z' && second in 'B'..'Z' -> BZBZ
        first in 'a'..'y' && second in 'a'..'y' -> ayay
        first in 'A'..'Y' && second in 'a'..'y' -> AYay_
        first in 'b'..'z' && second in 'B'..'Z' -> bzBZ_
        else -> null
    }
}

private fun getInverseTransform(first: Char, second: Char): Transform? {
    return when {
        first in 'B'..'Z' && second in 'B'..'Z' -> AYay_
        first in 'a'..'y' && second in 'a'..'y' -> bzBZ_
        first in 'A'..'Y' && second in 'a'..'y' -> BZBZ
        first in 'b'..'z' && second in 'B'..'Z' -> ayay
        else -> null
    }
}

fun main() {
    val n = 2
    val size = 2.0.pow(n).toInt()
    val charArray = CharArray(size) {
        if (randomBoolean()) {
            random('a'.toInt(), 'z'.toInt()).toChar()
        } else {
            random('A'.toInt(), 'Z'.toInt()).toChar()
        }
    }
    val origin = String(charArray)
    val list = ex32_HashAttack(origin)
    var hashEqual = true
    val originHash = origin.hashCode()
    for (result in list) {
        if (result.hashCode() != originHash) {
            hashEqual = false
            break
        }
    }
    println("origin=$origin   hash=${origin.hashCode()}")
    println("list.size=${list.size} list=${list.joinToString(limit = 20)}")
    println("hashEqual=$hashEqual")
}