//package com.user_service.utils;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.user_service.service.JwtUserDetailService;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//	@Autowired
//	private JwtUserDetailService jwtUserDetailService;
//
//	@Autowired
//	private JwtUtil jwtTokenUtil;
//
//	private static final List<String> EXCLUDED_PATHS = List.of(
//			"/api/user/login",
//			"/api/user/register",
//			"/login" // Cả API lẫn trang FE
//	);
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//
//		final String requestTokenHeader = request.getHeader("Authorization");
//		String username = null;
//		String jwtToken = null;
//
//		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//			jwtToken = requestTokenHeader.substring(7);
//			try {
//				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//			} catch (IllegalArgumentException e) {
//				System.out.println("Unable to get JWT Token");
//			} catch (ExpiredJwtException e) {
//				System.out.println("JWT Token has expired");
//			}
//		}
//
//		// Only attempt to authenticate if username is extracted and not already authenticated
//		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//			UserDetails userDetails = this.jwtUserDetailService.loadUserByUsername(username);
//			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
//				Claims claims = Jwts.parserBuilder()
//						.setSigningKey(jwtTokenUtil.getSigningKey())
//						.build()
//						.parseClaimsJws(jwtToken)
//						.getBody();
//				String role = claims.get("role", String.class);
//				List<SimpleGrantedAuthority> authorities = Collections.singletonList(
//						new SimpleGrantedAuthority("ROLE_" + role));
//
//				UsernamePasswordAuthenticationToken authenticationToken =
//						new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//			}
//		}
//
//		filterChain.doFilter(request, response);
//	}
//
//	@Override
//	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//		String path = request.getServletPath();
//		return EXCLUDED_PATHS.contains(path);
//	}
//}
