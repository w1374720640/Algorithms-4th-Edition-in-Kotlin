package chapter5.section5

import chapter2.section4.HeapMinPriorityQueue
import chapter2.sleep

/**
 * 霍夫曼压缩
 */
class Huffman : Compression {
    class Node(val i: Int, var freq: Int, var left: Node? = null, var right: Node? = null) : Comparable<Node> {
        fun isLeaf(): Boolean {
            check((left == null && right == null) || (left != null && right != null))
            return left == null && right == null
        }

        override fun compareTo(other: Node): Int {
            return this.freq.compareTo(other.freq)
        }

    }

    override fun compress(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        val R = 256
        // 霍夫曼压缩算法需要遍历所有内容两次，如果要使用BinaryStdIn来读取数据，只能一次性全部读入内存
        val s = stdIn.readString()
        if (s.isEmpty()) {
            stdIn.close()
            stdOut.close()
            return
        }
        val frequency = IntArray(R)
        s.forEach {
            frequency[it.toInt()]++
        }

        val root = buildTrie(frequency)
        val st = Array(R) { "" }
        // 当输入中只有一种字符时，根结点也是叶子节点，对应的字符串为空字符串
        // 只会写入单词查找树和数量，不会写入具体内容
        // 解压时，会直接根据数量和字符的编码重新构建整个文本
        buildCodes(st, root, "")

        writeTrie(stdOut, root)
        stdOut.write(s.length)
        s.forEach {
            val value = st[it.toInt()]
            value.forEach { char ->
                stdOut.write(char == '1')
            }
        }
        stdIn.close()
        stdOut.close()
    }

    private fun buildTrie(freq: IntArray): Node {
        val minPQ = HeapMinPriorityQueue<Node>()
        for (i in freq.indices) {
            if (freq[i] != 0) {
                minPQ.insert(Node(i, freq[i]))
            }
        }

        while (minPQ.size() > 1) {
            val leftNode = minPQ.delMin()
            val rightNode = minPQ.delMin()
            val node = Node(-1, leftNode.freq + rightNode.freq, leftNode, rightNode)
            minPQ.insert(node)
        }

        return minPQ.delMin()
    }

    private fun buildCodes(st: Array<String>, node: Node, prefix: String) {
        if (node.isLeaf()) {
            st[node.i] = prefix
        } else {
            buildCodes(st, node.left!!, prefix + '0')
            buildCodes(st, node.right!!, prefix + '1')
        }
    }

    private fun writeTrie(stdOut: BinaryStdOut, node: Node) {
        if (node.isLeaf()) {
            stdOut.write(true)
            stdOut.write(node.i.toChar())
        } else {
            stdOut.write(false)
            writeTrie(stdOut, node.left!!)
            writeTrie(stdOut, node.right!!)
        }
    }

    override fun expand(stdIn: BinaryStdIn, stdOut: BinaryStdOut) {
        val root = readTrie(stdIn)
        if (root == null) {
            stdIn.close()
            stdOut.close()
            return
        }
        val N = stdIn.readInt()
        repeat(N) {
            readValue(stdIn, stdOut, root)
        }
        stdIn.close()
        stdOut.close()
    }

    private fun readTrie(stdIn: BinaryStdIn): Node? {
        if (stdIn.isEmpty()) return null
        val isLeaf = stdIn.readBoolean()
        if (isLeaf) {
            return Node(stdIn.readInt(8), 0)
        } else {
            return Node(-1, 0, readTrie(stdIn), readTrie(stdIn))
        }
    }

    private fun readValue(stdIn: BinaryStdIn, stdOut: BinaryStdOut, root: Node) {
        var node = root
        while (!node.isLeaf()) {
            val b = stdIn.readBoolean()
            node = if (b) node.right!! else node.left!!
        }
        stdOut.write(node.i, 8)
    }
}

fun main() {
    val path = "./data/medTale.txt"
    val compressPath = "./out/output/medTale_compress.huffman"
    val expandPath = "./out/output/medTale_expand.txt"
    testCompression(path, compressPath, expandPath, 512, 1.0) { Huffman() }

    sleep(3000)
    val qpath = "./data/q64x96.bin"
    val qcompressPath = "./out/output/q64x96_compress.huffman"
    val qexpandPath = "./out/output/q64x96_expand.bin"
    testCompression(qpath, qcompressPath, qexpandPath, 64, 4.0) { Huffman() }
}