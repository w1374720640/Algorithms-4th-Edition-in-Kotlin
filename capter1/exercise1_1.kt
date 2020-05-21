//判断数组中的所有值是否相同
fun exercise1_1_3(args: Array<String>) {
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
fun exercise1_1_5(x: Double, y: Double) {
    fun between0And1(num: Double): Boolean = num > 0 && num < 1
    if (between0And1(x) && between0And1(y)) {
        println("true")
    } else {
        println("false")
    }
}

//求打印结果，个人感觉毫无意义
fun exercise1_1_7a() {
    var t = 9.0f
    while (java.lang.Math.abs(t - 9.0f / t) > 0.001f) {
        t = (9.0f / t + t) / 2.0f
    }
    println(t)
}

fun exercise1_1_7b() {
    var sum = 0
    for (i in 1 until 1000) {
        for (j in 0 until i) {
            sum++
        }
    }
    println(sum)
}

fun exercise1_1_7c() {
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
fun exercise1_1_9(num: Int) {
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
fun exercise1_1_13(array: Array<Array<Int>>) {
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
fun exercise1_1_14(N: Int) {
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
fun exercise1_1_15(M: Int, source: Array<Int>): Array<Int> {
    val result = Array<Int>(M) { 0 }
    for (i in source) {
        if (i >= M) {
            println("The value in the source array should not be greater than $M")
            continue
        }
        result[i] = result[i] + 1
    }
    println("source=${source.joinToString { it.toString() }}")
    println("result=${result.joinToString { it.toString() }}")
    println("source.size=${source.size},result total=${result.sum()}")
    return result
}

//求exercise1_1_16(6)运行结果
fun exercise1_1_16(n: Int): String {
    if (n <= 0) return ""
    return exercise1_1_16(n - 3) + n + exercise1_1_16(n - 2) + n
}
