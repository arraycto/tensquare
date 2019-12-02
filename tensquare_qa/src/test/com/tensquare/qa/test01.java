package com.tensquare.qa;

import org.junit.Test;
import org.springframework.util.StringUtils;

public class test01 {
    @Test
    public void test01(){
        String a = null;
        if (StringUtils.isEmpty(a)){
            System.out.println("a为空");
        }else {
            System.out.println("a不为空");
        }
    }
}
