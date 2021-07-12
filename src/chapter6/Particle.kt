package chapter6

import edu.princeton.cs.algs4.StdDraw
import extensions.random
import extensions.randomBoolean

/**
 * 运动的粒子对象的API
 *
 * 解题思路参考官方给的答案：https://algs4.cs.princeton.edu/61event/ （没看懂）
 * 也可以参考：https://www.dazhuanlan.com/2020/02/01/5e3548dc8b8b5/ （计算太复杂）
 */
class Particle(
        private var s: Double = random(0.05, 0.1), // 半径
        private var rx: Double = random(s, 1.0 - s), // 中心点的x轴坐标，加上半径后不能超出[0,1)的范围
        private var ry: Double = random(s, 1.0 - s), // 中心点的y轴坐标
        private var vx: Double = random() / 3 / 1000 * if (randomBoolean()) 1 else -1, // x轴方向速度，每毫秒移动的距离
        private var vy: Double = random() / 1000 * if (randomBoolean()) 1 else -1, // y轴方向速度
        private var mass: Double = s * s // 质量，默认质量和面积成正比
) {
    private var count = 0

    /**
     * 画出粒子
     */
    fun draw() {
        StdDraw.filledCircle(rx, ry, s)
    }

    /**
     * 根据时间的流逝dt改变粒子的位置
     */
    fun move(dt: Double) {
        rx += vx * dt
        ry += vy * dt
    }

    /**
     * 该粒子所参与的碰撞总数
     */
    fun count(): Int {
        return count
    }

    /**
     * 距离该粒子和粒子b碰撞所需的时间
     */
    fun timeToHit(b: Particle): Double {
        if (this === b) return Double.POSITIVE_INFINITY
        val dx: Double = b.rx - rx
        val dy: Double = b.ry - ry
        val dvx: Double = b.vx - vx
        val dvy: Double = b.vy - vy
        val dvdr = dx * dvx + dy * dvy
        if (dvdr > 0) return Double.POSITIVE_INFINITY
        val dvdv = dvx * dvx + dvy * dvy
        if (dvdv == 0.0) return Double.POSITIVE_INFINITY
        val drdr = dx * dx + dy * dy
        val sigma: Double = this.s + b.s
        val d = dvdr * dvdr - dvdv * (drdr - sigma * sigma)
        // if (drdr < sigma*sigma) StdOut.println("overlapping particles");
        // if (drdr < sigma*sigma) StdOut.println("overlapping particles");
        return if (d < 0) Double.POSITIVE_INFINITY else -(dvdr + Math.sqrt(d)) / dvdv
    }

    /**
     * 距离该粒子和水平墙体碰撞所需的时间
     */
    fun timeToHitHorizontalWall(): Double {
        return when {
            vy > 0.0 -> (1.0 - ry - s) / vy
            vy < 0.0 -> (ry - s) / vy * -1
            else -> Double.POSITIVE_INFINITY
        }
    }

    /**
     * 距离该粒子和垂直的墙体碰撞所需的时间
     */
    fun timeToHitVerticalWall(): Double {
        return when {
            vx > 0 -> (1.0 - rx - s) / vx
            vx < 0 -> (rx - s) / vx * -1
            else -> Double.POSITIVE_INFINITY
        }
    }

    /**
     * 该粒子与另一个粒子碰撞
     */
    fun bounceOff(b: Particle) {
        if (b === this) return
        val dx = b.rx - rx
        val dy = b.ry - ry
        val dvx = b.vx - vx
        val dvy = b.vy - vy
        val dvdr = dx * dvx + dy * dvy
        val dist = s + b.s

        val magnitude = 2 * mass * b.mass * dvdr / ((mass + b.mass) * dist)
        val fx = magnitude * dx / dist
        val fy = magnitude * dy / dist

        vx += fx / mass
        vy += fy / mass
        b.vx -= fx / b.mass
        b.vy -= fy / b.mass

        count++
        b.count++
    }

    /**
     * 该粒子与水平墙体碰撞
     */
    fun bounceOffHorizontalWall() {
        vy *= -1
        count++
    }

    /**
     * 该粒子与垂直墙体碰撞
     */
    fun bounceOffVerticalWall() {
        vx *= -1
        count++
    }
}