package chapter5.section5

import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * 从输入流中读取比特流的API
 *
 * 有两个构造函数，一个从标准输入（键盘）或其他输入流中读取数据，另一个从文件中读取数据
 * 从标准输入中读取数据时，需要使用回车键将字符存入输入流（回车键也会被存入），<Ctrl-d>或<Ctrl-z>结束输入
 */
class BinaryStdIn(private val inputStream: InputStream = System.`in`) {

    // 读取文件时使用带缓存的InputStream
    constructor(path: String) : this(BufferedInputStream(FileInputStream(path)))

    // 缓存的一个字节大小的数据
    private var byte = 0

    // 可用的缓存字节数
    private var n = 0

    init {
        read()
    }

    /**
     * 所有系统的输入输出流都是基于8位一字节对齐的，所以每次至少读取8位
     */
    private fun read() {
        try {
            byte = inputStream.read()
            n = 8
        } catch (e: IOException) {
            byte = -1
            n = -1
        }
    }

    private fun requireNotEmpty() {
        if (isEmpty()) throw NoSuchElementException("Reading from empty input stream")
    }

    /**
     * 读取1位数据并返回一个Boolean值
     */
    fun readBoolean(): Boolean {
        requireNotEmpty()
        n--
        val b = (byte and (1 shl n)) != 0
        if (n == 0) {
            read()
        }
        return b
    }

    /**
     * 读取8位数据并返回一个Char值
     */
    fun readChar(): Char {
        requireNotEmpty()
        var c = byte
        if (n == 8) {
            read()
        } else {
            val oldN = n
            c = c shl (8 - oldN)
            read()
            requireNotEmpty()
            c += byte ushr oldN
            n = oldN
        }
        return c.toChar()
    }

    /**
     * 读取r（1~16）位数据并返回一个Char值
     */
    fun readChar(r: Int): Char {
        require(r in 1..16)
        if (r == 8) return readChar()
        var c = 0
        repeat(r) {
            c = c shl 1
            if (readBoolean()) {
                c = c or 1
            }
        }
        return c.toChar()
    }

    /**
     * 读取8位数据并返回一个Byte值
     */
    fun readByte(): Byte {
        return readChar().toByte()
    }

    /**
     * 读取16位数据并返回一个Short值
     */
    fun readShort(): Short {
        var s = readChar().toInt()
        s = s shl 8
        s = s or readChar().toInt()
        return s.toShort()
    }

    /**
     * 读取32位数据并返回一个Int值
     */
    fun readInt(): Int {
        var i = 0
        repeat(4) {
            i = i shl 8
            i = i or readChar().toInt()
        }
        return i
    }

    /**
     * 读取r（1~32）位数据并返回一个Int值
     */
    fun readInt(r: Int): Int {
        require(r in 1..32)
        if (r == 32) return readInt()
        var i = 0
        repeat(r) {
            i = i shl 1
            if (readBoolean()) {
                i = i or 1
            }
        }
        return i
    }

    /**
     * 读取64位数据并返回一个Long值
     */
    fun readLong(): Long {
        var l = 0L
        repeat(8) {
            l = l shl 8
            l = l or readChar().toLong()
        }
        return l
    }

    /**
     * 读取32位数据并返回一个Float值
     */
    fun readFloat(): Float {
        return Float.fromBits(readInt())
    }

    /**
     * 读取64位数据并返回一个Double值
     */
    fun readDouble(): Double {
        return Double.fromBits(readLong())
    }

    /**
     * 读取所有的字节，将每8位转换成Char，再拼接成字符串
     * 只支持ASCII码对应的字符集，不支持其他字符集
     */
    fun readString(): String {
        val builder = StringBuilder()
        while (!isEmpty()) {
            builder.append(readChar())
        }
        return builder.toString()
    }

    /**
     * 判断比特流是否为空
     */
    fun isEmpty(): Boolean {
        return byte == -1
    }

    /**
     * 关闭比特流
     */
    fun close() {
        inputStream.close()
    }
}