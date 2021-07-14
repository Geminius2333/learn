# Redis

## 1.1 Redis基础数据类型

### 1.1.1 String字符串

#### 1.1.1.1 常用命令 

`set key value` 设置key值 

`get key`  获取key的值 

`mset key1 value1 key2 value2` 同时设置多个key

`mget key1 key2` 同时获取多个

`setnx key` 当key不存在时,设置key的值

`msetnx key1 value1 key2 value2` 同时设置多个key,只有当key都不存在时才成功

`incr key` 为整数可用,value + 1

`decr key` 为整数可用,value - 1

`incrby key num` 为整数可用,value + num

`decrby key num` 为整数可用,value - num

`strlen key` 返回长度

`append key value` 在key的值后拼接value,返回拼接后的长度

`getrange key i j`  返回key的[i,j]的子串,index从0开始,前闭后闭.j为-1时,返回全部.
i为负数时,例:	set num 123456789, get num -5 -2 返回"345678".

`setrange key i value`  从索引i开始覆盖value.



#### 1.1.1.2  其他

1. String类型是Redis自己构建的一种 **简单动态字符串**（simple dynamic string，**SDS**),String类型最大支持**512M**.
2. 内部结构实现类似 Java 的 ArrayList,在内容小于1M时,每次扩容都是翻倍,在大于1M后,每次扩容1M.
3. **应用场景**:一般常用在需要计数的场景，比如用户的访问次数、热点文章的点赞转发数量等等



### 1.1.2 List 链表

####  1.1.2.1 常用命令

`lpush/rpush key1 value1 value2 value3`  从左边/右边插入值

`lpop/rpop key` 从左边/右边吐出一个值, **值在键在,值光键亡**

`rpoplpush key1 key2` 从 key1列表右边吐出一个值,插入到key2的左侧

`lrange key start stop` 获取key从左到右的数据, 0 左边第一个, -1 右边第一个

`lindex key index` 按照索引下表获得元素(从左到右)

`llen key` 获得列表的长度

`linsert key before value newvalue` 在value后插入新的value 

`lrem key n value` 从左边开始删除 n个列表的值为value的内容

例: rpush list1 1 2 2 3 4 ,执行 lrem list1 2 2 后, list1 内容为 1 3  4

`lset key index value` 将列表key下标为index的值替换成value

#### 1.1.2.2  其他

1. 底层是一个**双向链表**, 两端操作性能很高.**通过 `rpush/lpop`可以 实现队列** ,**通过`rpush/rpop`可以实现栈**
2. **应用场景**:发布与订阅或者说消息队列、慢查询

###  1.1.3 Hash 哈希

#### 1.1.3.1 常用命令 





#### 1.1.3.2 其他

1. 
2. 







