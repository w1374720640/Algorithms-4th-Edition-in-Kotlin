package chapter2.section4

import extensions.random

/**
 * 离散概率分布的取样
 * 编写一个Sample类，其构造函数接收一个double类型的数组p[]作为参数，并支持以下操作：
 * random()————返回i的概率是p[i]/T（T是p[]中所有元素之和）（中文版翻译有误）
 * change(i, v)————将p[i]的值修改为v
 * 提示：使用完全二叉树，每个结点对应一个权重p[i]，在每个结点记录其下子树的权重之和
 * 为了产生一个随机的索引，取0到T之间的一个随机数并根据各个结点的权重之和来判断沿着哪条子树搜索下去
 * 在更新p[i]时，同时更新从根节点到i的路径上的所有结点
 * 像堆那样使用数组而非显式指针实现二叉树（这句话同样翻译有误）
 *
 * 解：StdRandom.discrete()方法也可以实现根据给定概率返回索引值，但是单次查询的时间复杂度为N，使用完全二叉树可以实现lgN的复杂度
 * 不需要像构造大顶堆/小顶堆那样移动元素位置，只需要遍历一半的元素，记录每个结点及其所有子结点的概率之和
 * 设每个结点的概率为p1,p2,p3...pi，每个结点及其所有子结点的概率之和为P1,P2,P3...Pi
 * 添加一个变量sum=0，生成一个0~P1之间的随机数Q，从根节点开始对比
 * 如果sum<Q<sum+p1，则直接返回根节点索引
 * 如果sum+p1<Q<sum+p1+P2，则值在左子树，sum+=p1，在左侧查找
 * 如果sum+p1+P2<Q，则值在右子树，sum+=p1+P2，在右侧查找
 * 不断递归查找子树，直到sum<Q<sum+pi
 */
class Sample(input: DoubleArray) {
    private class Node(var p: Double, var sumOfP: Double)

    private val array = Array(input.size + 1) {
        if (it == 0) {
            Node(0.0, 0.0)
        } else {
            Node(input[it - 1], input[it - 1])
        }
    }
    private var total = 0.0

    init {
        input.forEach {
            total += it
        }
        for (i in input.size / 2 downTo 1) {
            var childP = array[i * 2].sumOfP
            if (i * 2 < input.size) {
                childP += array[i * 2 + 1].sumOfP
            }
            array[i].sumOfP += childP
        }
    }

    fun random(): Int {
        val Q = random(0.0, total)
        var i = 1
        var sum = 0.0
        while (true) {
            val node = array[i]
            when {
                Q < sum + node.p -> return i - 1 //减一是因为二叉树的索引比原数组的索引大1
                Q < sum + node.p + array[i * 2].sumOfP -> {
                    sum += node.p
                    i *= 2
                }
                else -> {
                    sum += node.p + array[i * 2].sumOfP
                    i = i * 2 + 1
                }
            }
        }
    }

    fun change(i: Int, value: Double) {
        val diff = value - array[i + 1].p
        total += diff
        array[i + 1].p = value
        array[i + 1].sumOfP = array[i + 1].sumOfP + diff
        var j = (i + 1) / 2
        while (j >= 1) {
            array[j].sumOfP += diff
            j /= 2
        }
    }
}

fun main() {
    val times = 100_0000
    val size = 10
    val array = DoubleArray(size) { random() }
    val sample = Sample(array)
    var total = 0.0

    //测试random()方法
    array.forEach { total += it }
    println("expect=${array.joinToString { (it / total * times).toInt().toString() }}")
    val result = IntArray(size)
    repeat(times) {
        val index = sample.random()
        result[index]++
    }
    println("result=${result.joinToString()}")

    //测试change(i, v)方法
    println("Change value")
    repeat(5) {
        val index = random(10)
        val value = random()
        array[index] = value
        sample.change(index, value)
    }
    total = 0.0
    array.forEach { total += it }
    println("newExpect=${array.joinToString { (it / total * times).toInt().toString() }}")
    val result2 = IntArray(size)
    repeat(times) {
        val index = sample.random()
        result2[index]++
    }
    println("newResult=${result2.joinToString()}")
}