package chapter5.section5

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * 向输出流中写入比特流的API
 */
class BinaryStdOut(private val outputStream: OutputStream = System.out) {

    // 写文件时使用带缓存的OutputStream
    constructor(path: String) : this(BufferedOutputStream(FileOutputStream(path)))

    private var byte = 0
    private var n = 0

    /**
     * 将缓存的数据写入输出流，如果缓存中内容不足8位，右侧用0补全
     */
    private fun write() {
        if (n == 0) return
        outputStream.write(byte shl (8 - n))
        byte = 0
        n = 0
    }

    /**
     * 写入指定的比特
     */
    fun write(b: Boolean) {
        byte = byte shl 1
        if (b) {
            byte = byte or 1
        }
        n++
        if (n == 8) {
            write()
        }
    }

    /**
     * 写入指定的8位字符
     */
    fun write(c: Char) {
        write(c.toInt(), 8)
    }

    /**
     * 写入指定字符的低r（1~16）位
     */
    fun write(c: Char, r: Int) {
        require(r in 1..16)
        write(c.toInt(), r)
    }

    /**
     * 写入指定的8位Byte
     */
    fun write(b: Byte) {
        write(b.toInt(), 8)
    }

    /**
     * 写入指定的16位Short
     */
    fun write(s: Short) {
        write(s.toInt(), 16)
    }

    /**
     * 写入指定的32位Int
     */
    fun write(i: Int) {
        write(i, 32)
    }

    /**
     * 写入指定Int的低1~32位
     */
    fun write(i: Int, r: Int) {
        require(r in 1..32)
        repeat(r) {
            val flag = 1 shl (r - it - 1)
            write((i and flag) != 0)
        }
    }

    /**
     * 写入指定的64位Long
     */
    fun write(l: Long) {
        repeat(64) {
            val flag = 1L shl (64 - it - 1)
            write((l and flag) != 0L)
        }
    }

    /**
     * 写入指定的32位Float
     */
    fun write(f: Float) {
        val i = f.toRawBits()
        write(i)
    }

    /**
     * 写入指定的64位Double
     */
    fun write(d: Double) {
        val l = d.toRawBits()
        write(l)
    }

    /**
     * 写入字符串，字符串只支持ASCII码字符
     */
    fun write(s: String) {
        for (c in s) {
            write(c)
        }
    }

    /**
     * 写入指定的字符编码长度的字符串
     */
    fun write(s: String, r: Int) {
        for (c in s) {
            write(c, r)
        }
    }

    /**
     * 关闭比特流
     */
    fun close() {
        write()
        outputStream.flush()
        outputStream.close()
    }
}

fun main() {
    // 测试向指定文件写入数据
    val path = "./out/output/test.txt"
    val file = File(path)
    // 需要先判断文件的父目录是否存在，如果不存在需要手动创建文件夹
    // 不需要手动创建文件，FileOutputStream会自动创建文件（但不会自动创建路径上不存在的文件夹）
    if (!file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }
    val stdOut = BinaryStdOut(path)
    stdOut.write("hello world!")
    stdOut.close()
}