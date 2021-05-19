package com.example.redis.paging.demo2.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
public class TestRedisson {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testInitHash(){
        RMap<Integer, MyItem> map = redissonClient.getMap("redis:paging:demo2:hash:item");
        for(int i=1;i<=30;i++){
            MyItem item = new MyItem();
            item.setId(i);
            item.setName("商品" + i);
            item.setPrice("10.00");
            map.put(i,item);
        }
    }

    @Test
    public void testQueryOne(){
        RMap<Integer, MyItem> map = redissonClient.getMap("redis:paging:demo2:hash:item");
        System.out.println("商品名称：" + map.get(3).getName()+"，价格：" + map.get(3).getPrice());
    }

    @Test
    public void initZSet(){
        RScoredSortedSet<MyItem> sortedSet = redissonClient.getScoredSortedSet("redis:paging:demo2:zset:item");
        for(int i=1;i<=30;i++){
            MyItem item = new MyItem();
            item.setId(i);
            item.setName("商品" + i);
            item.setPrice("10.00");
            sortedSet.add(i, item);
        }
    }

    @Test
    public void deleteOne(){
        RScoredSortedSet<MyItem> sortedSet = redissonClient.getScoredSortedSet("redis:paging:demo2:zset:item");
        sortedSet.removeRangeByScore(30, true, 30, true);
    }

    @Test
    public void testGetTotalRecord(){
        RScoredSortedSet<MyItem> sortedSet = redissonClient.getScoredSortedSet("redis:paging:demo2:zset:item");
        System.out.println(sortedSet.size());
    }

    @Test
    public void testQueryPage(){
        Set<MyItem> set = this.queryPage(2, 5);
        for (MyItem item : set){
            System.out.println(String.format("id=%s，name=%s，price=%s", item.getId(), item.getName(), item.getPrice()));
        }
    }

    private Set<MyItem> queryPage(int pageNo, int pageSize) {
        Integer start = (pageNo-1) * pageSize;
        Integer stop = start + pageSize -1;
        RScoredSortedSet<MyItem> sortedSet = redissonClient.getScoredSortedSet("redis:paging:demo2:zset:item");
        return sortedSet.entryRange(start, stop).stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
    }
}
