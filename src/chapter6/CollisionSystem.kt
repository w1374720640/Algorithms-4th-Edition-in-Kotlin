package chapter6

import chapter2.section4.HeapMinPriorityQueue
import edu.princeton.cs.algs4.StdDraw

/**
 * 基于事件模拟互相碰撞的粒子
 */
class CollisionSystem(private val particles: Array<Particle>) {
    private lateinit var pq: HeapMinPriorityQueue<Event>
    private var t: Double = 0.0

    /**
     * 互相碰撞的主循环
     */
    fun simulate(limit: Double, hz: Int) {
        pq = HeapMinPriorityQueue()
        t = 0.0
        StdDraw.enableDoubleBuffering()
        redraw(hz)
        particles.forEach {
            predictCollisions(it, limit)
        }
        pq.insert(Event(1000.0 / hz, null, null))

        while (!pq.isEmpty() && t < limit) {
            val event = pq.delMin()
            if (!event.isValid()) continue
            val diffTime = event.time - t
            particles.forEach {
                it.move(diffTime)
            }
            t = event.time
            when {
                event.a != null && event.b != null -> {
                    event.a.bounceOff(event.b)
                    predictCollisions(event.a, limit)
                    predictCollisions(event.b, limit)
                }
                event.a != null -> {
                    event.a.bounceOffVerticalWall()
                    predictCollisions(event.a, limit)
                }
                event.b != null -> {
                    event.b.bounceOffHorizontalWall()
                    predictCollisions(event.b, limit)
                }
                else -> {
                    redraw(hz)
                    pq.insert(Event(t + 1000 / hz, null, null))
                }
            }

        }
    }

    /**
     * 预测其他粒子的碰撞事件
     */
    private fun predictCollisions(a: Particle, limit: Double) {
        particles.forEach {
            val time = a.timeToHit(it)
            if (time != Double.POSITIVE_INFINITY && time < limit - t) {
                pq.insert(Event(t + time, a, it))
            }
        }
        val horizontalTime = a.timeToHitHorizontalWall()
        if (horizontalTime != Double.POSITIVE_INFINITY && horizontalTime < limit - t) {
            pq.insert(Event(t + horizontalTime, null, a))
        }
        val verticalTime = a.timeToHitVerticalWall()
        if (verticalTime != Double.POSITIVE_INFINITY && verticalTime < limit - t) {
            pq.insert(Event(t + verticalTime, a, null))
        }
    }

    /**
     * 重新画出所有粒子
     */
    private fun redraw(hz: Int) {
        StdDraw.clear()
        StdDraw.setPenColor(StdDraw.BLACK)
        particles.forEach {
            it.draw()
        }
        StdDraw.show()
        StdDraw.pause(1000 / hz)
    }
}

fun main() {
    val N = 5
    val hz = 60
    val limit = 10000.0
    val particles = Array(N) { Particle() }
    val collisionSystem = CollisionSystem(particles)
    collisionSystem.simulate(limit, hz)
}