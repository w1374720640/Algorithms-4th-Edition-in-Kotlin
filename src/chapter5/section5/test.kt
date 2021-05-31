package chapter5.section5

import chapter2.sleep

/**
 * 测试压缩算法，使用PictureDump来显示数据
 */
fun compressionTest(path: String, compressPath: String, expandPath: String, lineCount: Int, radius: Double, create: () -> Compression) {
    // 压缩数据
    val alphabetCompression = create()
    val compressStdIn = BinaryStdIn(path)
    val compressStdOut = BinaryStdOut(compressPath)
    alphabetCompression.compress(compressStdIn, compressStdOut)

    // 解压数据
    val expandStdIn = BinaryStdIn(compressPath)
    val expandStdOut = BinaryStdOut(expandPath)
    alphabetCompression.expand(expandStdIn, expandStdOut)

    // 分别打印原始数据、压缩后的数据、解压后的数据
    PictureDump(lineCount, radius).dump(BinaryStdIn(path))
    sleep(3000)
    PictureDump(lineCount, radius).dump(BinaryStdIn(compressPath))
    sleep(5000)
    PictureDump(lineCount, radius).dump(BinaryStdIn(expandPath))
}