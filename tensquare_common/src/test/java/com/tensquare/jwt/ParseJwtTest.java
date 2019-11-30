package com.tensquare.jwt;

import io.jsonwebtoken.*;

import java.text.SimpleDateFormat;

public class ParseJwtTest {
    public static void main(String[] args) {
        try {
            Claims claims = Jwts.parser().setSigningKey("itcast")
                    .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLlsI_mmI4iLCJpYXQiOjE1NzUwOTYzNDUsImV4cCI6MTU3NTA5NjQwNSwicm9sZSI6ImFkbWluIn0.HGg--9Wj0sfA2NlEllmPumj1Ra3a6dgwbiTp7CALsmI")
                    .getBody();
            System.out.println("用户名id:"+ claims.getId());
            System.out.println("用户名:"+ claims.getSubject());
            System.out.println("登陆时间:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
            System.out.println("过期时间:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));
            System.out.println("role:"+ claims.get("role"));
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            System.out.println("已过期");
        }
    }
}
