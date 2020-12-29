package chapter3.section5

/**
 * 数学集合
 * 你的目标是实现表3.5.6中的MathSET的API来处理（可变的）数学集合
 * 请使用符号表来实现它
 * 附加题：使用boolean类型的数组来表示集合
 */
class HashMathSET<K : Any> : MathSET<K> {
    private val hashSET = LinearProbingHashSET<K>()

    override fun complement(set: MathSET<K>): MathSET<K> {
        val newSet = HashMathSET<K>()
        set.forEach {
            if (!this.contains(it)) {
                newSet.add(it)
            }
        }
        return newSet
    }

    override fun union(set: MathSET<K>): MathSET<K> {
        val newSet = HashMathSET<K>()
        this.forEach {
            newSet.add(it)
        }
        set.forEach {
            newSet.add(it)
        }
        return newSet
    }

    override fun intersection(set: MathSET<K>): MathSET<K> {
        val newSet = HashMathSET<K>()
        if (this.size() < set.size()) {
            this.forEach {
                if (set.contains(it)) {
                    newSet.add(it)
                }
            }
        } else {
            set.forEach {
                if (this.contains(it)) {
                    newSet.add(it)
                }
            }
        }
        return newSet
    }

    override fun add(key: K) {
        hashSET.add(key)
    }

    override fun delete(key: K) {
        hashSET.delete(key)
    }

    override fun contains(key: K): Boolean {
        return hashSET.contains(key)
    }

    override fun isEmpty(): Boolean {
        return hashSET.isEmpty()
    }

    override fun size(): Int {
        return hashSET.size()
    }

    override fun iterator(): Iterator<K> {
        return hashSET.iterator()
    }
}

fun main() {
    testMathSET { HashMathSET() }
}