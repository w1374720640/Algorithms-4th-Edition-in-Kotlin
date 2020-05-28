package capter1.exercise1_1

import java.lang.Exception
import java.math.BigDecimal

//判断数组中的所有值是否相同
fun ex3(args: Array<String>) {
    fun compare(): Boolean {
        if (args.size == 0) return false
        var start = args[0]
        for (i in 1 until args.size) {
            if (start != args[i]) return false
        }
        return true
    }
    if (compare()) {
        println("equal")
    } else {
        println("not equal")
    }
}

//判断两个数是否都在0和1之间
fun ex5(x: Double, y: Double) {
    fun between0And1(num: Double): Boolean = num > 0 && num < 1
    if (between0And1(x) && between0And1(y)) {
        println("true")
    } else {
        println("false")
    }
}

//求打印结果，个人感觉毫无意义
fun ex7a() {
    var t = 9.0f
    while (java.lang.Math.abs(t - 9.0f / t) > 0.001f) {
        t = (9.0f / t + t) / 2.0f
    }
    println(t)
}

fun ex7b() {
    var sum = 0
    for (i in 1 until 1000) {
        for (j in 0 until i) {
            sum++
        }
    }
    println(sum)
}

fun ex7c() {
    var sum = 0
    var i = 1
    while (i < 1000) {
        for (j in 0 until 1000) {
            sum++
        }
        i *= 2
    }
    println(sum)
}

//将整数（所有值，无论正负）的二进制用String表示
fun ex9(num: Int) {
    //将正整数的二进制用String表示
    fun getPositiveIntegerBinaryString(positiveNum: Int): String {
        require(positiveNum >= 0)
        var s = ""
        var temp = positiveNum
        do {
            s = (temp % 2).toString() + s
            temp = temp / 2
        } while (temp > 0)
        return s
    }

    var result = ""
    if (num >= 0) {
        result = getPositiveIntegerBinaryString(num)
    } else {
        //先记录最后一位，再无符号右移一位，获取正整数二进制，拼接成最终值
        var last = if (num % 2 == 0) "0" else "1"
        result = getPositiveIntegerBinaryString(num ushr 1) + last
    }
    println(result)
}

//交换二维数组的行列
fun ex13(array: Array<Array<Int>>) {
    array.forEach {
        it.forEach {
            print(it)
        }
        println()
    }
    println("--------------------------")
    val row = array.size
    require(row > 0)
    val column = array[0].size
    array.forEach {
        require(column == it.size)
    }
    val newArray = Array<Array<Int>>(column) {
        Array<Int>(row) { 0 }
    }
    newArray.forEachIndexed { outerIndex, outerArray ->
        for (i in outerArray.indices) {
            outerArray[i] = array[i][outerIndex]
        }
    }
    newArray.forEach {
        it.forEach {
            print(it)
        }
        println()
    }
}

//接收整形参数N，返回不大于log2 N 的最大整数
fun ex14(N: Int) {
    require(N > 0)
    fun power(count: Int): Int {
        require(count >= 0)
        if (count == 0) return 1
        var initValue = 1
        for (i in 1..count) {
            initValue *= 2
        }
        return initValue
    }

    var result = 0//N等于1时值为0
    if (N > 1) {
        while (power(result + 1) <= N) {
            result++
        }
    }
    /* 如果N为float类型，取值在0~1范围内，使用下面代码可以获取结果
    if (N > 0 && N < 1) {
        while (1.0f / power(result + 1) > N) {
            result++
        }
        result = -result - 1
    }
    */
    println("result=$result")
}

//返回一个大小为M的数组，第i个元素的值为参数数组source中i出现的次数，原理类似于Java中的BitSet
fun ex15(M: Int, source: Array<Int>): Array<Int> {
    val result = Array<Int>(M) { 0 }
    for (i in source) {
        if (i >= M) {
            println("The value in the source array should not be greater than $M")
            continue
        }
        result[i]++
    }
    println("source=${source.joinToString { it.toString() }}")
    println("result=${result.joinToString { it.toString() }}")
    println("source.size=${source.size},result total=${result.sum()}")
    return result
}

//求ex16(6)运行结果
fun ex16(n: Int): String {
    if (n <= 0) return ""
    return ex16(n - 3) + n + ex16(n - 2) + n
}

//求给定正整数a和b，ex18(a, b)的计算结果
//将 + 替换为 * ,return 0 改为 return 1 然后回答问题
fun ex18(a: Int, b: Int): Int {
    if (b == 0) return 1
    if (b % 2 == 0) return ex18(a * a, b / 2)
    return ex18(a * a, b / 2) * a
}

//使用循环改进低效率的递归，消除重复的回溯
fun ex19(N: Int) {
    require(N >= 0)
    //低效版本
    fun f(N: Int): Long {
        if (N == 0) return 0
        if (N == 1) return 1
        return f(N - 1) + f(N - 2)
    }
    //高效版本
//    fun f(N: Int): Long {
//        if (N == 0) return 0
//        if (N == 1) return 1
//        val array = arrayOf<Long>(0, 1, 0)
//        for (i in 2..N) {
//            array[i % 3] = array[(i - 1) % 3] + array[(i - 2) % 3]
//        }
//        return array[N % 3]
//    }

    val startTime = System.currentTimeMillis()
    println("f(${N})=${f(N)}")
    val endTime = System.currentTimeMillis()
    println("spend time: ${endTime - startTime} ms")
}

//计算N的阶乘
fun ex20(N: Int) {
    //递归版本
    fun ln(N: Int): Int {
        if (N == 0) return 1
        return ln(N - 1) * N
    }
    //循环版本
//    fun ln(N: Int): Int {
//        if (N == 0) return 1
//        var result = 1
//        for (i in 1..N) {
//            result *= i
//        }
//        return result
//    }
    println("${N}!=${ln(N)}")
}

//从键盘接收输入，每行包含姓名、和两个整数，整数相除得到比率，保留小数点后三位
fun ex21() {
    println("Type ':end' to end the input")
    data class Player(var name: String = "", var score: Int = 0, var total: Int = 0, var radio: BigDecimal = BigDecimal.ZERO)

    val list = mutableListOf<Player>()
    var input: String? = ""
    do {
        input = readLine()
        if (input.isNullOrEmpty()) continue
        if (":end" == input.trim()) break
        val player = Player()
        val array = input.split(" ")
        try {
            var i = 0
            array.forEach {
                if (it.isNotBlank()) {
                    when (i) {
                        0 -> player.name = it
                        1 -> player.score = it.toInt()
                        2 -> player.total = it.toInt()
                    }
                    i++
                }
            }
            if (player.total != 0) {
                player.radio = BigDecimal(player.score.toDouble() / player.total.toDouble()).setScale(3, BigDecimal.ROUND_HALF_UP)
            }
            list.add(player)
        } catch (e: Exception) {
            println("input error, message=${e.message}")
        }
    } while (true)
    println("End of input")
    list.forEach {
        println("${it.name} ${it.score} ${it.total} ${it.radio}")
    }
}

//使用递归实现二分查找，并按照递归深度缩进
fun ex22(key: Int, array: Array<Int>) {
    fun rank(key: Int, array: Array<Int>, low: Int, high: Int, depth: Int): Int {
        println("    ".repeat(depth) + "low=${low} high=${high}")
        if (low > high) return -1
        val mid = (high + low) / 2
        if (key < array[mid]) return rank(key, array, low, mid - 1, depth + 1)
        if (key > array[mid]) return rank(key, array, mid + 1, high, depth + 1)
        return mid
    }
    array.sort()
    println("array=${array.joinToString()}")
    if (array.isEmpty()) {
        println("array is empty")
        return
    }
    val index = rank(key, array, 0, array.size - 1, 0)
    if (index == -1) {
        println("${key} not found")
    } else {
        println("${key} has found, index=${index}")
    }
}

//使用欧几里得算法计算最大公约数
fun ex24(a: Int, b: Int) {
    //两个整数的最大公约数等于其中较小的那个数和两数相除余数的最大公约数
    fun euclid(a: Int, b: Int): Int {
        val large = if (a > b) a else b
        val small = if (a > b) b else a
        //商数
        val quotient = large / small
        //余数
        val remainder = large % small
        println("large=${large}, small=${small} quotient=${quotient} remainder=${remainder}")
        if (remainder == 0) return small
        return euclid(small, remainder)
    }
    require(a > 0 && b > 0)
    println("The greatest common divisor of ${a} and ${b} is ${euclid(a, b)}")
}

//估算binomial(100, 50, 0.25)产生的递归调用次数
//TODO 暂时不想优化了，递归真烦
fun ex27(N: Int, k: Int, p: Double) {
    var count = 0
    fun binomial(N: Int, k: Int, p: Double): Double {
        count++
        if (N == 0 && k == 0) return 1.0
        if (N < 0 || k < 0) return 0.0
        return (1.0 - p) * binomial(N - 1, k, p) + p * binomial(N - 1, k - 1, p)
    }
    binomial(N, k, p)
    println("count=${count}")
}

//删除排序后的重复元素，因为数组是有序的，所以删除重复元素比较简单
fun ex28(array: Array<Int>) {
    if (array.size <= 1) return
    array.sort()
    println("sort array=${array.joinToString()}")
    val result = mutableListOf<Int>()
    var startIndex = 0
    var endIndex = 1
    do {
        /* 这里的删除重复元素是指元素重复三次，删除两个保留一个
        如果需要把重复元素全部删除，将下面代码中的result.add(array[startIndex])替换为
        if (endIndex - startIndex == 1) {
            result.add(array[startIndex])
        }
        */
        if (endIndex >= array.size) {
            result.add(array[startIndex])
            break
        }
        if (array[startIndex] == array[endIndex]) {
            endIndex++
        } else {
            result.add(array[startIndex])
            startIndex = endIndex
            endIndex++
        }
    } while (true)
    println("result=${result.joinToString()}")
}

//在一个有序数组中分别查找大于、小于、等于key值的数量
//因为是有序数组，所以二分法是最快的（在数据量比较大的情况下）
//查找小于key的值时，即使已经找到等于key的值，继续在左侧查找，直到找到左侧第一个不等于key的值
//也可以在找到等于key的值时，向左右两侧遍历数组，但是可能会很慢
fun ex29(key: Int, array: Array<Int>) {
    array.sort()
    fun numberLessThanKey(key: Int, array: Array<Int>): Int {
        var startIndex = 0
        var endIndex = array.size - 1
        do {
            //如果数组中不存在要查找的键，则左半边是小于键的值
            if (startIndex > endIndex) return endIndex + 1
            val midIndex = (startIndex + endIndex) / 2
            when {
                //和正常的二分法区别是，当key=array[midIndex]时，执行key<array[midIndex]相同的逻辑
                key <= array[midIndex] -> endIndex = midIndex - 1
                key > array[midIndex] -> startIndex = midIndex + 1
            }
        } while (true)
    }

    fun numberGreaterThanKey(key: Int, array: Array<Int>): Int {
        var startIndex = 0
        var endIndex = array.size - 1
        do {
            //如果数组中不存在要查找的键，则右半边是大于键的值
            if (startIndex > endIndex) return array.size - (endIndex + 1)
            val midIndex = (startIndex + endIndex) / 2
            when {
                //和正常的二分法区别是，当key=array[midIndex]时，执行key>array[midIndex]相同的逻辑
                key < array[midIndex] -> endIndex = midIndex - 1
                key >= array[midIndex] -> startIndex = midIndex + 1
            }
        } while (true)
    }
    println("key=${key} array=${array.joinToString()}")
    val lessNumber = numberLessThanKey(key, array)
    val greaterNumber = numberGreaterThanKey(key, array)
    println("array.size=${array.size} lessNumber=${lessNumber} equalNumber=${array.size - lessNumber - greaterNumber} greaterNumber=${greaterNumber}")
}

//创建一个N*N的数组，当i和j互质时，a[i][j]为true，否则为false
//两个或多个整数的公因数只有1的非零自然数叫做互质数
fun ex30(N: Int) {
    val array: Array<Array<Boolean>> = Array(N) { Array(N) { false } }
    //以[i,i]为分割线，先判断右侧值，再对称赋值左侧值
    //互质数为非0自然数，所以0和任何数都不互质
    for (i in 0 until N) {
        array[0][i] = false
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




