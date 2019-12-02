package com.tensquare.qa.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "tensquare-base", fallback = BaseClientImp.class)
public interface BaseClient {

    /**
     * 根据ID查询
     * @param labelId
     * @return
     */
    @GetMapping("/label/{labelId}")
    public Result findById(@PathVariable("labelId") String labelId);
}
