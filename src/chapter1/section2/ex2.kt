package chapter1.section2

import edu.princeton.cs.algs4.Interval1D
import extensions.inputPrompt
import extensions.readAllDoubles

/**
 * 编写一个Interval1D的用例，从命令行接受一个整数N。
 * 从标准输入中读取N个间隔（每个间隔由一对double值定义）并打印出所有相交的间隔队。
 */
fun main() {
    inputPrompt()
    val pointArray = readAllDoubles()
    require(pointArray.size % 2 == 0) { "Please enter an even number of Double values" }
    val intervalList = mutableListOf<Interval1D>()
    for (i in pointArray.indices step 2) {
        intervalList.add(Interval1D(pointArray[i], pointArray[i + 1]))
    }
    for (i in 0 until intervalList.size - 1) {
        for (j in i + 1 until intervalList.size) {
            if (intervalList[i].intersects(intervalList[j])) {
                println(intervalList[i].toString() + " " + intervalList[j].toString())
            }
        }
    }
}