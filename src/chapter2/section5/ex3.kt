package chapter2.section5

import java.math.BigDecimal

/**
 * 找出账户余额Balance类的实现代码的错误
 * 为什么compareTo()方法对Comparable接口的实现有缺陷？
 * 如何修复这个问题
 *
 * 解：原文中的代码破坏了相等的传递性，例如a=0.1,b=0.5,c=0.8，a==b b==c a!=c
 * 账户余额不应该使用Double类型存储，会丢失精度，应该使用BigDecimal存储
 */
class Balance(var amount: BigDecimal) : Comparable<Balance> {
    override fun compareTo(other: Balance): Int {
        return amount.compareTo(other.amount)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Balance) return false
        if (amount === other.amount) return true
        return amount == other.amount
    }

    override fun hashCode(): Int {
        return amount.hashCode()
    }
}