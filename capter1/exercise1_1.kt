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

fun exercise1_1_5(x: Double, y: Double) {
    fun between0And1(num: Double): Boolean = num > 0 && num < 1
    if (between0And1(x) && between0And1(y)) {
        println("true")
    } else {
        println("false")
    }
}

fun exercise1_1_7() {
    var t = 9.0f
    while (java.lang.Math.abs(t - 9.0f / t) > 0.001f) {
        t = (9.0f / t + t) / 2.0f
    }
    println(t)
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

fun exercise1_1_14(N: Float) {
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
