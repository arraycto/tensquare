package com.tensquare.search.controlle;

import com.tensquare.search.dao.ArticleDao;
import com.tensquare.search.pojo.Article;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleDao articleDao;


    @RequestMapping(method= RequestMethod.POST)
    public Result save(@RequestBody Article article){
        articleDao.save(article);
        return new Result(true, StatusCode.OK, "操作成功");
    }
}
