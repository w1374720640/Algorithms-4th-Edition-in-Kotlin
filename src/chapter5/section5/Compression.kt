package chapter5.section5

/**
 * 数据压缩算法通用接口
 */
interface Compression {

    /**
     * 压缩数据
     */
    fun compress(stdIn: BinaryStdIn, stdOut: BinaryStdOut)

    /**
     * 解压数据
     */
    fun expand(stdIn: BinaryStdIn, stdOut: BinaryStdOut)
}