package com.tiendaintegral.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
import javax.crypto.SecretKey; import java.nio.charset.StandardCharsets; import java.util.Date;
@Service
public class JwtService {
 private final SecretKey key; private final long expiration;
 public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-ms}") long expiration){
  byte[] bytes=java.util.Base64.getDecoder().decode(secret); if(bytes.length<32) throw new IllegalArgumentException("JWT_SECRET debe tener al menos 256 bits"); key=Keys.hmacShaKeyFor(bytes); this.expiration=expiration;
 }
 public String generate(String username,String role){Date now=new Date();return Jwts.builder().subject(username).claim("role",role).issuedAt(now).expiration(new Date(now.getTime()+expiration)).signWith(key).compact();}
 public String username(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();}
 public boolean valid(String token){try{return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration().after(new Date());}catch(Exception e){return false;}}
}
