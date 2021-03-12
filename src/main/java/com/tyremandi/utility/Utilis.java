package com.tyremandi.utility;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.tyremandi.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utilis {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public String generateUserId(int lenght) {
		return generateRandomString(lenght);

	}

	public String generateAddressId(int lenght) {
		return generateRandomString(lenght);

	}

	private String generateRandomString(int lenght) {
		StringBuilder returnValue = new StringBuilder(lenght);
		for (int i = 0; i < lenght; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));

		}

		return new String(returnValue);

	}

	public static boolean hasTokenExpired(String token) {

		boolean returnValue = false;
		try {
			Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret())
					.parseClaimsJws(token).getBody();
			Date tokenExpriedDate = claims.getExpiration();
			Date todatDate = new Date();
			returnValue = tokenExpriedDate.before(todatDate);
		}
		catch(ExpiredJwtException ex){
			returnValue = true;
		}
		return returnValue;
	}
	public String generateEmailVerificationToken(String userID) {
		String token = Jwts.builder().setSubject(userID)
				      .setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
                      .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                      .compact();
		
		
		return token;
	}

	public String generatePasswordRestToken(String userId) {
		String token = Jwts.builder().setSubject(userId)
			      .setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
                  .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                  .compact();
	
	
		return token;
	}

}
