package chapter2.section1

import chapter2.enumValueOf
import edu.princeton.cs.algs4.StdRandom

/**
 * 纸牌排序
 * 说说你会如何将一副扑克牌按花色排序（花色顺序是黑桃、红桃、梅花和方片），
 * 限制条件是所有牌都是背面朝上排成一列，而你一次只能翻看其中两张牌或者交换两张牌（保持背面朝上）。
 *
 * 解：先用希尔排序对扑克按点数大小排序，再四个一组，对相同点数不同花色的牌按照花色大小插入排序
 * 题目原意可能是要按黑桃0、黑桃1、黑桃2...红桃0、红桃1、红桃2...的顺序排序
 * 按同样的思想两次希尔排序即可，省略过程
 */
fun ex13_DeckSort(pokerList: Array<Poker>) {
    //按点数对扑克排序
    shellSort(pokerList)

    //对指定范围内的扑克按花色排序，包括start不包括end
    fun sortPokerBySuit(start: Int, end: Int) {
        for (i in start + 1 until end) {
            for (j in i downTo start + 1) {
                if (pokerList[j - 1].suit <= pokerList[j].suit) break
                val temp = pokerList[j - 1]
                pokerList[j - 1] = pokerList[j]
                pokerList[j] = temp
            }
        }
    }

    var start = 0
    for (i in pokerList.indices) {
        if (pokerList[i].points != pokerList[start].points) {
            sortPokerBySuit(start, i)
            start = i
        }
    }
}

/**
 * 定义扑克类，包含点数和花色两个变量，点数为[0,12]共13种，花色为四个枚举值，不包含大小王有4*13=52种
 */
class Poker(val points: Int, val suit: Suit) : Comparable<Poker> {
    enum class Suit {
        SPADE, //黑桃
        HEART, //红桃
        CLUB, //梅花
        DIAMOND //方片
    }

    override fun compareTo(other: Poker): Int {
        return points - other.points
    }

    override fun toString(): String {
        return "${points}:${suit.name}"
    }
}

fun main() {
    //一副扑克排除大小王共52张
    val pokerList = Array(52) {
        Poker(it / 4, enumValueOf(it % 4))
    }
    //打乱扑克
    StdRandom.shuffle(pokerList)
    println(pokerList.joinToString())
    ex13_DeckSort(pokerList)
    println(pokerList.joinToString())
}
