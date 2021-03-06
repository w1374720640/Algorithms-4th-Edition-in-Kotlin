package chapter5.section2

import chapter5.section1.Alphabet
import edu.princeton.cs.algs4.Queue

/**
 * R²向分支的根结点的三向单词查找树
 * 如正文所述，为TST添加代码，在前两层结点中实现多向分支
 */
class R2BranchingTST<V : Any>(private val alphabet: Alphabet) : StringST<V> {

    /**
     * 长度为1的键对应的值保存在这个结点中
     */
    inner class OneCharNode(val char: Char) {
        var value: V? = null
    }

    /**
     * 长度大于等于2的键对应的值保存在这个结点中
     */
    inner class TwoCharNode(val firstChar: Char, val secondChar: Char) {
        val tst = TST<V>()
    }

    // 保存空键对应的值
    private var emptyKeyValue: V? = null

    private val oneCharRootNodes = Array(alphabet.R()) {
        OneCharNode(alphabet.toChar(it))
    }

    private val twoCharRootNodes = Array(alphabet.R() * alphabet.R()) {
        val i = it / alphabet.R()
        val j = it % alphabet.R()
        TwoCharNode(alphabet.toChar(i), alphabet.toChar(j))
    }

    private fun getOneCharRootNode(key: String): OneCharNode {
        require(key.isNotEmpty())
        return oneCharRootNodes[alphabet.toIndex(key[0])]
    }

    private fun getTwoCharRootNode(key: String): TwoCharNode {
        require(key.length >= 2)
        val firstChar = key[0]
        val secondChar = key[1]
        val index = alphabet.toIndex(firstChar) * alphabet.R() + alphabet.toIndex(secondChar)
        return twoCharRootNodes[index]
    }

    override fun put(key: String, value: V) {
        when (key.length) {
            0 -> emptyKeyValue = value
            1 -> getOneCharRootNode(key).value = value
            else -> getTwoCharRootNode(key).tst.put(key.substring(2), value)
        }
    }

    override fun get(key: String): V? {
        return when (key.length) {
            0 -> emptyKeyValue
            1 -> getOneCharRootNode(key).value
            else -> getTwoCharRootNode(key).tst.get(key.substring(2))
        }
    }

    override fun delete(key: String) {
        when (key.length) {
            0 -> if (emptyKeyValue != null) emptyKeyValue == null else throw NoSuchElementException()
            1 -> {
                val node = getOneCharRootNode(key)
                if (node.value != null) node.value = null else throw NoSuchElementException()
            }
            else -> {
                val node = getTwoCharRootNode(key)
                node.tst.delete(key.substring(2))
            }
        }
    }

    override fun contains(key: String): Boolean {
        return when (key.length) {
            0 -> emptyKeyValue != null
            1 -> getOneCharRootNode(key).value != null
            else -> getTwoCharRootNode(key).tst.contains(key.substring(2))
        }
    }

    override fun isEmpty(): Boolean {
        return size() == 0
    }

    override fun size(): Int {
        var size = if (emptyKeyValue == null) 0 else 1
        oneCharRootNodes.forEach {
            if (it.value != null) size++
        }
        twoCharRootNodes.forEach {
            size += it.tst.size()
        }
        return size
    }

    override fun keys(): Iterable<String> {
        val queue = Queue<String>()
        if (emptyKeyValue != null) queue.enqueue("")
        for (i in 0 until alphabet.R()) {
            val oneCharNode = oneCharRootNodes[i]
            if (oneCharNode.value != null) {
                queue.enqueue(oneCharNode.char.toString())
            }
            for (j in 0 until alphabet.R()) {
                val index = i * alphabet.R() + j
                val twoCharNode = twoCharRootNodes[index]
                val prefix = String(charArrayOf(twoCharNode.firstChar, twoCharNode.secondChar))
                twoCharNode.tst.keys().forEach {
                    queue.enqueue(prefix + it)
                }
            }
        }
        return queue
    }

    override fun longestPrefixOf(s: String): String? {
        var result = if (emptyKeyValue == null) null else ""
        if (s.isEmpty()) return result
        val oneCharNode = getOneCharRootNode(s)
        if (oneCharNode.value != null) {
            result = oneCharNode.char.toString()
        }
        if (s.length == 1) return result
        val twoCharNode = getTwoCharRootNode(s)
        val prefix = twoCharNode.tst.longestPrefixOf(s.substring(2))
        if (prefix != null) {
            result = s.substring(0, 2) + prefix
        }
        return result
    }

    override fun keysWithPrefix(s: String): Iterable<String> {
        if (s.isEmpty()) return keys()
        val queue = Queue<String>()
        if (s.length == 1) {
            val oneCharNode = getOneCharRootNode(s)
            if (oneCharNode.value != null) {
                queue.enqueue(oneCharNode.char.toString())
            }
            val firstIndex = alphabet.toIndex(oneCharNode.char)
            for (i in firstIndex * alphabet.R() until (firstIndex + 1) * alphabet.R()) {
                val twoCharNode = twoCharRootNodes[i]
                val prefix = String(charArrayOf(twoCharNode.firstChar, twoCharNode.secondChar))
                twoCharNode.tst.keys().forEach {
                    queue.enqueue(prefix + it)
                }
            }
        } else {
            val twoCharNode = getTwoCharRootNode(s)
            val prefix = String(charArrayOf(twoCharNode.firstChar, twoCharNode.secondChar))
            twoCharNode.tst.keys().forEach {
                queue.enqueue(prefix + it)
            }
        }
        return queue
    }

    override fun keysThatMatch(s: String): Iterable<String> {
        val queue = Queue<String>()
        if (s.isEmpty()) {
            if (emptyKeyValue != null) queue.enqueue("")
            return queue
        }
        if (s.length == 1) {
            val char = s[0]
            if (char == '.') {
                oneCharRootNodes.forEach {
                    if (it.value != null) {
                        queue.enqueue(it.char.toString())
                    }
                }
            } else {
                val oneCharNode = getOneCharRootNode(s)
                if (oneCharNode.value != null) {
                    queue.enqueue(oneCharNode.char.toString())
                }
            }
        } else {
            val firstChar = s[0]
            val secondChar = s[1]
            when {
                firstChar == '.' && secondChar == '.' -> {
                    twoCharRootNodes.forEach { twoCharNode ->
                        val prefix = String(charArrayOf(twoCharNode.firstChar, twoCharNode.secondChar))
                        twoCharNode.tst.keysThatMatch(s.substring(2)).forEach {
                            queue.enqueue(prefix + it)
                        }
                    }
                }
                firstChar == '.' -> {
                    val secondIndex = alphabet.toIndex(secondChar)
                    for (i in secondIndex until twoCharRootNodes.size step alphabet.R()) {
                        val twoCharNode = twoCharRootNodes[i]
                        val prefix = String(charArrayOf(twoCharNode.firstChar, twoCharNode.secondChar))
                        twoCharNode.tst.keysThatMatch(s.substring(2)).forEach {
                            queue.enqueue(prefix + it)
                        }
                    }
                }
                secondChar == '.' -> {
                    val firstIndex = alphabet.toIndex(firstChar)
                    for (i in firstIndex * alphabet.R() until (firstIndex + 1) * alphabet.R()) {
                        val twoCharNode = twoCharRootNodes[i]
                        val prefix = String(charArrayOf(twoCharNode.firstChar, twoCharNode.secondChar))
                        twoCharNode.tst.keysThatMatch(s.substring(2)).forEach {
                            queue.enqueue(prefix + it)
                        }
                    }
                }
                else -> {
                    val twoCharNode = getTwoCharRootNode(s)
                    val prefix = String(charArrayOf(twoCharNode.firstChar, twoCharNode.secondChar))
                    twoCharNode.tst.keysThatMatch(s.substring(2)).forEach {
                        queue.enqueue(prefix + it)
                    }
                }
            }
        }
        return queue
    }
}

fun main() {
    testStringST { R2BranchingTST(Alphabet.EXTENDED_ASCII) }
}