package extensions

import edu.princeton.cs.algs4.In
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

/**
 * 从标准输入中读取数据时，可以添加提示文案
 */
private inline fun <T> read(message: String, readAction: () -> T): T {
    if (message.isNotEmpty()) {
        print(message)
    }
    return readAction()
}

//从标准输入设备（键盘）读取数据
//kotlin中有自带的readLine函数
fun readLine(message: String = "") = read(message) { StdIn.readLine() }
fun readChar(message: String = "") = read(message) { StdIn.readChar() }
fun readString(message: String = "") = read(message) { StdIn.readString() }
fun readInt(message: String = "") = read(message) { StdIn.readInt() }
fun readDouble(message: String = "") = read(message) { StdIn.readDouble() }
fun readFloat(message: String = "") = read(message) { StdIn.readFloat() }
fun readLong(message: String = "") = read(message) { StdIn.readLong() }
fun readShort(message: String = "") = read(message) { StdIn.readShort() }
fun readByte(message: String = "") = read(message) { StdIn.readByte() }
fun readBoolean(message: String = "") = read(message) { StdIn.readBoolean() }

//使用下面的方法获取输入数组时，根据平台不同需要用<Ctrl-d> 或 <Ctrl-z>结束输入
fun readAll(message: String = "") = read(message) { StdIn.readAll() }
fun readAllStrings(message: String = "") = read(message) { StdIn.readAllStrings() }
fun readAllLines(message: String = "") = read(message) { StdIn.readAllLines() }
fun readAllInts(message: String = "") = read(message) { StdIn.readAllInts() }
fun readAllLongs(message: String = "") = read(message) { StdIn.readAllLongs() }
fun readAllDoubles(message: String = "") = read(message) { StdIn.readAllDoubles() }


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
inline fun safeCall(catchAction: (e: Exception) -> Unit = { println("Throw an exception, message = ${it.message}") },
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

/**
 * 快速创建指定大小的整数数组
 */
fun getIntArrayFromFile(size: Int, path: String = "./data/1Mints.txt"): IntArray {
    val originArray = In(path).readAllInts()
    return if (size < originArray.size) originArray.copyOf(size) else originArray
}

fun main() {
}