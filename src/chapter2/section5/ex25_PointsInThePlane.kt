package chapter2.section5

import edu.princeton.cs.algs4.Point2D
import kotlin.math.atan2

/**
 * 为Point2D类型编写三个静态的比较器，一个按照x坐标比较，一个按照y坐标比较，一个按照点到原点的距离进行比较。
 * 编写两个非静态比较器，一个按照两个点到第三个点的距离比较，一个按照两个点相对于第三个点的辐角比较
 */

/**
 * 按照x坐标比较
 */
val Point2DXComparator = Comparator<Point2D> { o1, o2 ->
    o1.x().compareTo(o2.x())
}

/**
 * 按照y坐标比较
 */
val Point2DYComparator = Comparator<Point2D> { o1, o2 ->
    o1.y().compareTo(o2.y())
}

/**
 * 按照点到原点的距离进行比较
 */
val Point2DOriginDistanceComparator = Comparator<Point2D> { o1, o2 ->
    o1.distanceSquaredTo(Point2D(0.0, 0.0)).compareTo(o2.distanceSquaredTo(Point2D(0.0, 0.0)))
}

/**
 * 创建一个新的比较器，按照两个点到第三个点的距离比较
 */
fun Point2DDistanceComparator(other: Point2D): Comparator<Point2D> {
    return Comparator { o1, o2 ->
        o1.distanceSquaredTo(other).compareTo(o2.distanceSquaredTo(other))
    }
}

/**
 * angleTo()方法在Point2D中是私有方法，通过扩展函数让它成为public方法
 */
fun Point2D.angleTo(that: Point2D): Double {
    val dx = that.x() - x()
    val dy = that.y() - y()
    return atan2(dy, dx)
}

/**
 * 创建一个新的比较器，按照两个点相对于第三个点的辐角比较
 */
fun Point2DAngleComparator(other: Point2D): Comparator<Point2D> {
    return Comparator { o1, o2 ->
        o1.angleTo(other).compareTo(o2.angleTo(other))
    }
}

fun main() {
    val point1 = Point2D(1.0, 4.0)
    val point2 = Point2D(2.0, 3.0)
    val otherPoint = Point2D(5.0, 5.0)
    println(Point2DXComparator.compare(point1, point2))
    println(Point2DYComparator.compare(point1, point2))
    println(Point2DOriginDistanceComparator.compare(point1, point2))
    println(Point2DDistanceComparator(otherPoint).compare(point1, point2))
    println(Point2DAngleComparator(otherPoint).compare(point1, point2))
}