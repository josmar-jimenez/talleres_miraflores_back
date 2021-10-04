package com.inventory_system.backend.service;

import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.inventory_system.backend.util.InventorySystemConstant.INSUFFICIENT_PRIVILEGES;
import static com.inventory_system.backend.util.InventorySystemConstant.INSUFFICIENT_PRIVILEGES_CODE;

@Service
public class TokenService {

    @Value("${jwt.secretKey}")
    private  String SECRET;

    @Value("${jwt.tokenTimeout}")
    private  String TOKEN_TIMEOUT;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    public String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts.builder().setId("backendID").setSubject(username)
                .claim("authorities",grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Integer.valueOf(TOKEN_TIMEOUT)*1000))
                .signWith(SignatureAlgorithm.HS512,	SECRET.getBytes()).compact();
        return "Bearer " + token;
    }

    public String getUserNick() {
        String token = request.getHeader("Authorization").split("Bearer ")[1];
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void checkRoleCreateAndUpdate() throws UnauthorizedException, BusinessException {
        String nick = this.getUserNick();
        User user = userService.findByNick(nick);
        if(user.getRole().getId()!=1&&user.getRole().getId()!=2){
            throw new BusinessException(INSUFFICIENT_PRIVILEGES_CODE, INSUFFICIENT_PRIVILEGES);
        }
    }
}
