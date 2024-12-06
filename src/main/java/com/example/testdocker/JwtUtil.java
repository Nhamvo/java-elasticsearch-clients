package com.example.testdocker;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // Thay vì khai báo key thủ công, sử dụng phương thức secretKeyFor để tạo key an toàn
    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + 1000 * 60 * 60); // 1 giờ sau


    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expiryDate)  // Đặt thời gian hết hạn
                .signWith(secretKey)  // Sử dụng secretKey đã tạo
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration != null && expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }



    public Date getExpirationDateFromToken(String token) {
        return extractAllClaims(token).getExpiration(); // Trả về expiration nếu có
    }

}
