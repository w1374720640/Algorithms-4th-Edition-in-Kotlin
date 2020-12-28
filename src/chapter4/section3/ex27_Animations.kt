package chapter4.section3

import chapter2.sleep

/**
 * 动画
 * 编写一段程序将最小生成树算法用动画表现出来。
 * 用程序处理mediumEWG.txt来产生类似于图4.3.12和图4.3.14的示意图
 *
 * 解：使用mediumEWG.txt文件中的数据无法让边和权重相等，不够直观，
 * 使用getRandomEWG()函数生成一个权重和欧几里得距离（直线距离）相等的图
 */
fun main() {
    val randomGraph = getRandomEWG(V = 250, d = 0.15)
    drawEWGGraph(randomGraph.first, points = randomGraph.second, showIndex = false, delay = 100) {
        PrimMST(it)
    }

    sleep(2000)
    drawEWGGraph(randomGraph.first, points = randomGraph.second, showIndex = false, delay = 100) {
        KruskalMST(it)
    }
}