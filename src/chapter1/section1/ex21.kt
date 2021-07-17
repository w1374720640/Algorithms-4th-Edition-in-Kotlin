package chapter1.section1

import java.math.BigDecimal

/**
 * 编写一段程序，从标准输入按行读取数据，其中每行都包含一个名字和两个整数。
 * 然后用printf()打印一张表格，每行的若干列数据包括名字、两个整数和第一个整数除以第二个整数的结果，精确到小数点后三位。
 * 可以用这种程序将棒球球手的击球命中率或者学生的考试分数制成表格。
 */
fun main() {
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