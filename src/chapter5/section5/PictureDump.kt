package chapter5.section5

import chapter2.sleep
import edu.princeton.cs.algs4.StdDraw

/**
 * 将比特流打印成图像，每个比特一像素，白色像素表示0，黑色像素表示1
 *
 * 为了方便显示，可以调整每个比特的显示大小，width代表一个比特的正方形边长所占的像素值
 * 内容较少时，可以适当扩大每个比特的显示大小，内容较多时，缩小每个比特的显示大小
 * 绘制区域会根据内容数量自动调整大小
 */
class PictureDump(private val lineCount: Int, width: Double) : Dump {
    private val radius = width / 2

    override fun dump(stdIn: BinaryStdIn) {
        val list = ArrayList<BooleanArray>()
        var array = BooleanArray(lineCount)
        var count = 0
        var bits = 0
        while (!stdIn.isEmpty()) {
            val b = stdIn.readBoolean()
            array[count] = b
            count++
            bits++
            if (count == lineCount) {
                count = 0
                list.add(array)
                array = BooleanArray(lineCount)
            }
        }
        if (count != 0) {
            list.add(array)
        }
        println("$bits bits")
        stdIn.close()
        // 至少显示一行
        if (list.isEmpty()) {
            list.add(BooleanArray(lineCount))
        }

        // 开始绘制图形
        val width = lineCount * radius * 2
        val height = list.size * radius * 2
        StdDraw.clear()
        StdDraw.setCanvasSize(width.toInt(), height.toInt())
        StdDraw.setXscale(0.0, width)
        StdDraw.setYscale(0.0, height)
        StdDraw.setPenColor(StdDraw.BLACK)
        for (i in list.indices) {
            val line = list[i]
            for (j in line.indices) {
                if (line[j]) {
                    val x = j * radius * 2 + radius
                    // 坐标(0,0)点在左下角，绘制时以左上角为起点开始绘制
                    val y = height - (i * radius * 2 + radius)
                    StdDraw.filledSquare(x, y, radius)
                }
            }
        }
    }
}

fun main() {
    val dump = PictureDump(16, 20.0)
    val stdIn = BinaryStdIn("./data/abra.txt")
//    val stdIn = BinaryStdIn()
    dump.dump(stdIn)
    sleep(5000)

    val dump2 = PictureDump(512, 1.0)
    val stdIn2 = BinaryStdIn("./data/genomeVirus.txt")
    dump2.dump(stdIn2)

}