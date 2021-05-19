package com.example.redis.paging.demo2.redisson;

import lombok.Data;

import java.io.Serializable;

@Data
public class MyItem implements Serializable {

    private Integer id;

    private String name;

    private String price;
}
