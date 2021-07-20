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



###  1.1.3 Set 集合



#### 1.1.3.1 常用命令 

`sadd <key> <value1> <value2> ...` 将一个或多个 member 元素添加到集合中,已经存在的会被忽略

`smembers <key>` 获取该集合的所有值  

`sismember <key> value` 判断集合key中是否含有 value 值,有 1, 没有 0

`scard <key>` 返回该集合的元素个数

`srem <key> value1 value2` 删除集合中的某个元素

`spop <key> ` **随机**从集合中吐出一个值

`srandmember <key> <n>` 随机从该结合中取出n个值,**不会从集合中删除** 

`smove <source> <destination> value` 把集合 source 中的 value 移动到集合 destination 中

`sinter <key1> <key2>`返回两个集合的**交集**元素

`sunion <key1> <key2>` 返回两个集合的**并集**元素

`sdiff <key1> <key2> ` 返回两个集合的**差集**元素(包含 key1 中的, 不包含 key2 中的)



#### 1.1.3.2 其他

1. set 类似于 Java 中的 `HashSet` 。Redis 中的 set 类型是一种**无序**集合，集合中的元素没有先后顺序。
2. **应用场景:** 需要存放的数据不能重复以及需要获取多个数据源交集和并集等场景



###  1.1.4 Hash 哈希



#### 1.1.4.1 常用命令 

`hset<key> <field> <value>`  给哈希表 <key> 中的 <field> 键赋值 <value>

`hget<key> <field> ...`  从哈希表 <key> 中取出 <field> 的 <value>

`hmset<key> <field1> <value1> <field2> <value2> ...` 批量设置hash的值

`hexists <key> <field>` 查看该 hash 中是否存在 <field> 

`hkeys <key>`  列出该 hash 中所有的  <field>

`hvals <key> `  列出该 hash 中所有的 <value>

`hincrby <key> <field> <increment>`  将哈希表 <key> 中的 <field> 的 值加上 <increment>

`hsetnx <key> <field> <value>` 仅当 <field> 不存在时, 将哈希表 key 中的 <field> 设置 为 <value>



#### 1.1.4.2 其他

1. Redis hash是一个键值对集合. 是一个 string 类型的 **filed** 和 **value** 的映射表. hash 特别适合用来存储对象, 类似 Java 中的 Map<Sring, Object>. 
2. Hash 类型对应的数据结构有两种: ziplist (压缩列表), hashtable (哈希表). 当 field-value 长度较短且个数较少时, 使用 ziplist, 否则使用hashtable
3. **应用场景:** 系统中对象数据的存储。



###  1.1.5  Zset(sorted set) 有序集合



#### 1.1.5.1 常用命令 

`zadd <key> <score1><value1> <score2><value2> ...` 将一个或多个 member 元素及其 <score> 添加到有序集合 <key> 中

`zrange <key> <min> <max> [BYSCORE|BYLEV] [REV] [LIMIT OFFST COUNT] [WITHSCORES]` 返回有序集合 <key> 中, 下标在<min> 和 <max> 之间的元素 , 带 **WITHSCORES** 可以让 <score> 一起和值返回到结果集

`zrangebyscore <key> <min> <mix> [WITHSCORES] [limit offset count]` 返回有序集合 <key> 中, 所有 score 值介于 min 和 max 之间的成员. 有序集合成员按 score 值递增(从小到大)排序

`zrevrangebyscore <key> <max> <min> [WITHSCORES] [limit offset count]`  同上, 改为从大到小排列

`zincrby <key> <increment> <value>` 为元素的 <score> 加上增量 <increment>

`zrem <key> <value> `  删除该集合中, 指定值的元素

`zcount <key> <min> <max>` 统计该集合,  <score> 在 <min> 和 <max> 内的元素个数 

`zrank <key> <value>` 返回该值在集合中的排名, 从 0 开始

#### 1.1.5.2 其他

1.  Redis 有序集合 zset 与普通集合 set 非常相似 , 是一个**有序的没有重复元素**的字符串集合
2. 给每个成员关联了一个**评分(score)**, 评分(score) 被用来按照从低到高排序集合中的成员. **集合中的成员是唯一的,但是评分可以重复**
3. **应用场景：** 需要对数据根据某个权重进行排序的场景。比如在直播系统中，实时排行信息包含直播间在线用户列表，各种礼物排行榜，弹幕消息（可以理解为按消息维度的消息排行榜）等信息。



## 1.2 







