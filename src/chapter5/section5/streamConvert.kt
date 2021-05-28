package chapter5.section5

import chapter2.sleep
import extensions.random
import extensions.spendTimeMillis
import java.io.*
import kotlin.concurrent.thread

/**
 * 将输入流转换为输出流
 *
 * 例如从网络的输入流中读取数据，再以输出流向文件系统中写入数据
 * 可以自定义不同类型的输出流来接收输入的数据，所以使用一个创建输出流的函数作为参数
 */
inline fun convertInputStreamToOutputStream(inputStream: InputStream, createOutputStream: () -> OutputStream) {
    val outputStream = createOutputStream()
    while (true) {
        val byte = inputStream.read()
        if (byte == -1) break
        outputStream.write(byte)
    }
    inputStream.close()
    outputStream.flush()
    outputStream.close()
}

/**
 * 将输出流转换为输入流
 *
 * 例如将一个程序的运行结果作为另一个程序的输入参数
 * 必须先将所有内容写入输出流中，再将输出流中的数据一次性取出构造一个输入流
 * 比较耗费内存，不适用于数据量较大的输出流，只能使用ByteArray作为节点流将数据全部写入内存中
 */
fun convertOutputStreamToInputStream(outputStream: ByteArrayOutputStream): ByteArrayInputStream {
    val buffer = outputStream.toByteArray()
    return ByteArrayInputStream(buffer)
}

/**
 * 创建一个输出流和输入流的管道
 *
 * 通过管道可以将一个程序的输出作为另一个程序的输入，并且无需将所有数据全部加载到内存中，节省内存
 * 采用生产者/消费者模型，必须在两个不同的线程中使用输出流和输入流，否则会造成死锁，
 * 向输出流中写入数据，从输入流中读取数据，当缓存空间被填满或为空时，线程处于阻塞状态，
 * 直到输入流取出数据或输出流填充新数据
 */
fun createPiped(): Pair<PipedInputStream, PipedOutputStream> {
    val inputStream = PipedInputStream()
    val outputStream = PipedOutputStream()
    outputStream.connect(inputStream)
    return inputStream to outputStream
}

fun main() {
    // 拷贝文件，一边从输入流读，一边向输出流写，不会将所有内容拷贝到内存中，在同一个线程中运行
    val time = spendTimeMillis {
        // largeEWD.txt是一个较大的测试文件，约300M大小
        val inputStream = BufferedInputStream(FileInputStream("./data/largeEWD.txt"))
        convertInputStreamToOutputStream(inputStream) {
            val file = File("./out/output/largeEWD_copy.txt")
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            BufferedOutputStream(FileOutputStream(file))
        }
    }
    println("file copy succeed, spend $time ms")
    println()


    // 一次性向输出流中写入10个值，再从输入流中读取所有数据，必须先将所有内容拷贝到内存中，不适合大量数据，在同一个线程中运行
    val outputStream2 = ByteArrayOutputStream()
    println("write all value.")
    repeat(10) {
        // 写数据时只会写入Int的后8位，随机的整数大小不应超过255
        outputStream2.write(random(256))
    }
    val convertInputStream2 = convertOutputStreamToInputStream(outputStream2)
    println("read all value.")
    while (true) {
        val byte = convertInputStream2.read()
        if (byte == -1) break
        println(byte)
    }
    // 对于ByteArrayOutputStream和ByteArrayInputStream来说，close()方法没有实际意义，close之后继续读写也不会报错
    outputStream2.close()
    convertInputStream2.close()
    println("end")
    println()


    // 通过管道将输出流转换成输入流，不会将所有内容一次性加载到内存中，但是必须在两个线程中分别使用输出流和输入流
    // 读和写的顺序不固定，但是可以保证缓存区不为空时才会读，缓存区未满时才会写，输出流先关闭，输入流后关闭
    val piped = createPiped()
    // 创建一个子线程从输入流中读取数据
    thread {
        while (true) {
            // 读方法陷入阻塞状态后，即使没有其他线程唤醒，也会每1000毫秒自动唤醒一次
            val byte = piped.first.read()
            if (byte == -1) break
            println("read: $byte")
        }
        piped.first.close()
        println("piped input stream close.")
    }
    println("start write random int:")
    // 在主线程中写入数据
    repeat(10) {
        // 写数据时只会写入Int的后8位，随机的整数大小不应超过255
        val value = random(256)
        println("write: $value")
        piped.second.write(value)
        // 添加一个延时代码，模拟线程阻塞状态
        sleep(500)
    }
    piped.second.close()
    println("piped output stream close.")
}