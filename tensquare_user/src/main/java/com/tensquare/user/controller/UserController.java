package com.tensquare.user.controller;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tensquare.user.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 登录
	 * @return
	 */
	@RequestMapping(value = "/login",method= RequestMethod.POST)
	public Result login(@RequestBody User user){
		user = userService.login(user.getMobile(), user.getPassword());
		if (user == null){
			return new Result(false,StatusCode.LOGINERROR,"登录失败");
		}
		//使得前后端可以通话的操作，采用JWT来实现
		//生成令牌
		String token = jwtUtil.createJWT(user.getId(), user.getMobile(), "user");
		Map<String,Object> map = new HashMap<>();
		map.put("token", token);
		map.put("roles", "user");
		return new Result(true,StatusCode.OK,"登录成功",map);
	}

	/**
	 * 发送短信验证码
	 * @return
	 */
	@RequestMapping(value = "/sendsms/{mobile}",method= RequestMethod.POST)
	public Result sendsms(@PathVariable String mobile){
		userService.sendsms(mobile);
		return new Result(true,StatusCode.OK,"查询成功");
	}

	/**
	 * 注册
	 * @return
	 */
	@RequestMapping(value = "/register/{code}",method= RequestMethod.POST)
	public Result register(@PathVariable String code, @RequestBody User user){
		String syscode = (String)redisTemplate.opsForValue().get("checkcode_" + user.getMobile());
		//提取系统正确的验证码
		if(syscode==null){
			throw new RuntimeException("请点击获取短信验证码");
		}
		if(!syscode.equals(code)){
			throw new RuntimeException("验证码输入不正确");
		}

		user.setFollowcount(0);//关注数
		user.setFanscount(0);//粉丝数
		user.setOnline(0L);//在线时长
		user.setRegdate(new Date());//注册日期
		user.setUpdatedate(new Date());//更新日期
		user.setLastdate(new Date());//最后登陆日期
		userService.add(user);
		return new Result(true,StatusCode.OK,"注册成功");
	}

	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除 必须有admin角色权限
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
