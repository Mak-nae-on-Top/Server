package com.maknaeontop.communication.jwt;

import com.maknaeontop.communication.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtRequestFilter is a class related to verifying a token
 * when a communication request is made in an app.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Method to verify that the user is normally logged in
     * by checking the token when connecting to http communication.
     *
     * @param request       HttpServletRequest to get token
     * @param response      HttpServletResponse
     * @param chain         FilterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // 헤더가 존재하고 Bearer로 시작하면 username 추출
        if (header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);
            username = jwtTokenUtil.getIdByToken(jwtToken);
        }

        // username이 추출되었고, context에 authentication이 없는 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userService.loadUserByUsername(username);

            // 토큰이 검증되면 수동으로 authentication 세팅
            if (jwtTokenUtil.validateToken(jwtToken)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // 필터 실행
        chain.doFilter(request, response);
    }
}