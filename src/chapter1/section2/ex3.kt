package chapter1.section2

import edu.princeton.cs.algs4.Interval1D
import edu.princeton.cs.algs4.Interval2D
import edu.princeton.cs.algs4.Point2D
import extensions.inputPrompt
import extensions.random
import extensions.readDouble
import extensions.readInt

/**
 * 编写一个Interval2D的用例，从命令行接受参数N、min和max。
 * 生成N个随机的2D间隔，其宽和高均匀地分布在单位正方形中的min和max之间。
 * 用StdDraw画出它们并打印出相交的间隔对的数量以及有包含关系的间隔对数量。
 *
 * 解：这题要求宽和高均匀地分布在单位正方形中的min和max之间，
 * 意思是间隔的宽度在min和max之间，具体位置任意，只要不超出正方形范围就行，高度同理，
 * 需要先用随机函数分别获取间隔的宽高，然后在保证不超出正方形范围的情况下，随机生成间隔的位置，
 * 不能先获取位置再获取宽高。
 */
fun main() {
    inputPrompt()
    val N = readInt("N: ")
    val min = readDouble("min: ")
    val max = readDouble("max: ")
    require(min < max)
    //题目中说min和max在单位正方形中
    require(min in 0.0..1.0 && max in 0.0..1.0) { "The values of min and max should be between [0,1]" }

    val intervalList = mutableListOf<Interval2D>()

    //Interval2D接口设计的太垃圾，应该像Interval1D和Point2D这两个类一样能获取x和y的值
    //题目要求判断间隔的包含关系，但是Interval2D只能判断是否包含Point2D
    //所以只能用额外列表保存左上角和右下角点的坐标
    val leftTopPointList = mutableListOf<Point2D>()
    val rightBottomPointList = mutableListOf<Point2D>()
    repeat(N) {
        val width = random(min, max)
        val height = random(min, max)
        val left = random(0.0, 1.0 - width)
        val right = left + width
        val bottom = random(0.0, 1.0 - height)
        val top = bottom + height
        leftTopPointList.add(Point2D(left, top))
        rightBottomPointList.add(Point2D(right, bottom))
        intervalList.add(Interval2D(Interval1D(left, right), Interval1D(bottom, top)))
    }
    intervalList.forEach { it.draw() }

    //有相交关系的两个间隔对数量，最大值为n选2的组合数
    var intersectsCount = 0
    //有包含关系的两个间隔对数量，最大值同样为n选2的组合数
    var containsCount = 0

    //判断位置i和位置j的间隔是否有包含关系，可能i包含j，可能j包含i
    fun interval2DContains(i: Int, j: Int) =
            (intervalList[i].contains(leftTopPointList[j])
                    && intervalList[i].contains(rightBottomPointList[j]))
                    || (intervalList[j].contains(leftTopPointList[i])
                    && intervalList[j].contains(rightBottomPointList[i]))

    for (i in 0 until intervalList.size - 1) {
        for (j in i + 1 until intervalList.size) {
            if (intervalList[i].intersects(intervalList[j])) {
                intersectsCount++
            }
            if (interval2DContains(i, j)) {
                containsCount++
            }
        }
    }
    println("intersectsCount=$intersectsCount")
    println("containsCount=$containsCount")
}