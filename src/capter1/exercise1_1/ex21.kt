package capter1.exercise1_1

import java.math.BigDecimal

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

fun main() {
    ex21()
}