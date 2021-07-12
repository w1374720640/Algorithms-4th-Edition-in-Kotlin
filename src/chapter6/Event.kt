package chapter6

/**
 * 粒子模拟的事件类
 *
 * a和b均不为空：粒子与粒子碰撞
 * a非空而b为空：粒子a和垂直墙体碰撞
 * a为空而b非空：粒子b和水平墙体碰撞
 * a和b均为空：重绘事件（画出所有粒子）
 */
class Event(
        val time: Double,
        val a: Particle?,
        val b: Particle?
) : Comparable<Event> {
    private val countA = a?.count() ?: -1
    private val countB = b?.count() ?: -1

    override fun compareTo(other: Event): Int {
        return time.compareTo(other.time)
    }

    /**
     * 当前事件是否有效
     */
    fun isValid(): Boolean {
        if (a != null && countA != a.count()) return false
        if (b != null && countB != b.count()) return false
        return true
    }
}