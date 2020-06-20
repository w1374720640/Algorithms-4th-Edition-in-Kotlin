package extensions

import edu.princeton.cs.algs4.StdIn
import edu.princeton.cs.algs4.StdOut
import edu.princeton.cs.algs4.StdRandom
import java.util.*

/**
 * 这个文件对常用的函数进行了封装，让代码更简洁
 */

fun printf(format: String, vararg args: Any) = StdOut.printf(format, *args)
fun printf(local: Locale, format: String, vararg args: Any) = StdOut.printf(local, format, *args)

//常用格式化长度方法
fun format(format: String, vararg args: Any) = String.format(format, *args)
fun format(local: Locale, format: String, vararg args: Any) = String.format(local, format, *args)

fun formatDouble(value: Double, decimal: Int) = format("%.${decimal}f", value)
fun formatFloat(value: Float, decimal: Int) = format("%.${decimal}f", value)
fun formatInt(value: Int, length: Int) = format("%${length}d", value)
fun formatLong(value: Long, length: Int) = format("%${length}d", value)
fun formatStringLength(value: String, length: Int) = format("%${length}s", value)

/**
 * 当需要从键盘输入参数时，建议先调用这个方法打印提示信息
 */
fun inputPrompt() {
    println("Please enter parameters:")
    println("(<Enter> key to confirm, <Ctrl-d> or <Ctrl-z> ends the input)")
}

//从标准输入设备（键盘）读取数据
//kotlin中有自带的readLine函数
//fun readLine() = StdIn.readLine()
fun readChar() = StdIn.readChar()
fun readAll() = StdIn.readAll()
fun readString() = StdIn.readString()
fun readInt() = StdIn.readInt()
fun readDouble() = StdIn.readDouble()
fun readFloat() = StdIn.readFloat()
fun readLong() = StdIn.readLong()
fun readShort() = StdIn.readShort()
fun readByte() = StdIn.readByte()
fun readBoolean() = StdIn.readBoolean()

//使用下面的方法获取输入数组时，根据平台不同需要用<Ctrl-d> 或 <Ctrl-z>结束输入
fun readAllStrings() = StdIn.readAllStrings()
fun readAllLines() = StdIn.readAllLines()
fun readAllInts() = StdIn.readAllInts()
fun readAllLongs() = StdIn.readAllLongs()
fun readAllDoubles() = StdIn.readAllDoubles()


//随机数简洁写法
fun setSeed(seed: Long) = StdRandom.setSeed(seed)
fun getSeed() = StdRandom.getSeed()
fun random() = StdRandom.uniform()//[0,1)范围内的Double值
fun random(n: Int) = StdRandom.uniform(n)//[0,n)范围内的Int值
fun random(n: Long) = StdRandom.uniform(n)
fun random(a: Int, b: Int) = StdRandom.uniform(a, b)//[a,b)范围内的Int值
fun random(a: Double, b: Double) = StdRandom.uniform(a, b)
fun randomBoolean() = StdRandom.bernoulli()//以相等概率返回true或false
fun randomBoolean(p: Double) = StdRandom.bernoulli(p)//以指定概率（true的概率）返回true或false

/**
 * 对可能抛出异常的代码用try{}catch(){}包裹
 */
inline fun safeCall(catchAction: (e: Exception) -> Unit = { println(it.message) },
                    finallyAction: () -> Unit = {},
                    action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        catchAction(e)
    } finally {
        finallyAction()
    }
}

/**
 * 统计代码运行时间
 */
inline fun spendTimeMillis(action: () -> Unit): Long {
    val startTime = System.currentTimeMillis()
    action()
    val endTime = System.currentTimeMillis()
    return endTime - startTime
}

fun main() {
}