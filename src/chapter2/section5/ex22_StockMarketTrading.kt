package chapter2.section5

import chapter2.section4.HeapMaxPriorityQueue
import chapter2.sleep
import extensions.random
import extensions.randomBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * 股票交易
 * 投资者对一只股票的买卖交易都发布在电子交易市场中
 * 他们会指定最高买入价和最低卖出价，以及在该价位买卖的笔数
 * 编写一段程序，用优先队列来匹配买家和卖家并用模拟数据进行测试
 * 可以使用两个优先队列，一个用于买家一个用于卖家，当一方的报价能够和另一方的一份或多份报价匹配时就进行交易
 *
 * 解：股票的交易规则：
 * 买方的出价按由高到低的顺序依次排序，出价高的优先成交，相同价格按照提交时间排序，先提交的先成交
 * 卖方的出价按由低到高的顺序依次排序，出价低的优先成交，相同价格按照提交时间排序，先提交的先成交
 * 当买方提交一个价格时，先在卖方队列中查询，是否有小于等于出价的，如果有，则按照卖方队列的最低价成交，否则加入到买方队列中等待交易
 * 当卖方提交一个价格时，先在买方队列中查询，是否有大于等于出价的，如果有，则按照买方队列的最高价成交，否则加入到卖方队列中等待交易
 * 也就是说，当你提交一个价格时，初次匹配有可能比你的出价更优惠（买的更便宜，卖的更贵），
 * 但如果初次匹配失败，被加入到队列中，只可能按出价成交或未成交
 * 买卖可以部分成交，如果以10元的价格买10手，卖方队列里有9元1手，10元3手，11元5手，则以9元成交1手，10元成交3手，剩余6手以10元的价格加入买方队列
 * 买方队列的最大价格始终小于卖方的最低价格，否则可以撮合成功，订单被移除队列
 *
 * 创建两种订单，买单和卖单，生成两个优先队列分别存放买单和卖单，
 * 买方订单按出价由高到低的顺序依次排序，卖方订单按出价由低到高的顺序依次排序
 * 当有新订单时，先去对方的队列中比较最高优先级的订单是否匹配，匹配成功的部分弹出队列，匹配失败的部分加入队列
 */
class StockMarketTrading {
    private val buyPQ = HeapMaxPriorityQueue<BuyOrder>()
    private val sellPQ = HeapMaxPriorityQueue<SellOrder>()

    fun buy(buyOrder: BuyOrder) {
        while (buyOrder.volume != 0 && !sellPQ.isEmpty() && buyOrder.price >= sellPQ.max().price) {
            val sellOrder = sellPQ.max()
            val price = minOf(buyOrder.price, sellOrder.price)
            val volume = minOf(buyOrder.volume, sellOrder.volume)
            deal(buyOrder, sellOrder, price, volume)
            buyOrder.volume -= volume
            sellOrder.volume -= volume
            if (sellOrder.volume == 0) {
                sellPQ.delMax()
            }
        }
        if (buyOrder.volume != 0) {
            buyPQ.insert(buyOrder)
        }
    }

    fun sell(sellOrder: SellOrder) {
        while (sellOrder.volume != 0 && !buyPQ.isEmpty() && sellOrder.price <= buyPQ.max().price) {
            val buyOrder = buyPQ.max()
            val price = maxOf(sellOrder.price, buyOrder.price)
            val volume = minOf(sellOrder.volume, buyOrder.volume)
            deal(buyOrder, sellOrder, price, volume)
            buyOrder.volume -= volume
            sellOrder.volume -= volume
            if (buyOrder.volume == 0) {
                buyPQ.delMax()
            }
        }
        if (sellOrder.volume != 0) {
            sellPQ.insert(sellOrder)
        }
    }

    private fun deal(buyOrder: BuyOrder, sellOrder: SellOrder, price: Int, volume: Int) {
        //可以通过buyOrder和sellOrder的index参数通知相应的投资者交易成功，为了简洁起见这里不打印index的值
        println("deal: price=$price volume=$volume")
    }
}

//为方便起见，价格用整数表示
class BuyOrder(val price: Int, var volume: Int) : Comparable<BuyOrder> {
    init {
        require(price > 0 && volume > 0)
    }

    val index = atomicInteger.incrementAndGet()

    companion object {
        //线程安全的自增索引，全局共享，保证每个对象的索引唯一且逐渐增长
        private val atomicInteger = AtomicInteger()
    }

    override fun compareTo(other: BuyOrder): Int {
        return when {
            //价格越高的优先级越高
            price > other.price -> 1
            price < other.price -> -1
            index < other.index -> 1
            index > other.index -> -1
            else -> 0
        }
    }
}

class SellOrder(val price: Int, var volume: Int) : Comparable<SellOrder> {
    init {
        require(price > 0 && volume > 0)
    }

    val index = atomicInteger.incrementAndGet()

    companion object {
        //线程安全的自增索引，全局共享，保证每个对象的索引唯一且逐渐增长
        private val atomicInteger = AtomicInteger()
    }

    override fun compareTo(other: SellOrder): Int {
        return when {
            //价格越高的优先级越低
            price > other.price -> -1
            price < other.price -> 1
            index < other.index -> 1
            index > other.index -> -1
            else -> 0
        }
    }
}

fun main() {
    val stockMarketTrading = StockMarketTrading()
    //随机买卖10次，价格在[40,60)范围内随机，数量在[1,100)范围内随机
    repeat(10) {
        if (randomBoolean()) {
            val buyOrder = BuyOrder(random(40, 60), random(1, 100))
            println("buy: price=${buyOrder.price} volume=${buyOrder.volume}")
            stockMarketTrading.buy(buyOrder)
        } else {
            val sellOrder = SellOrder(random(40, 60), random(1, 100))
            println("sell: price=${sellOrder.price} volume=${sellOrder.volume}")
            stockMarketTrading.sell(sellOrder)
        }
        println()
        sleep(2000)
    }
}
