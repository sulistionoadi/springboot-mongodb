package com.sulistionoadi.belajar.jwt.config;

import com.sulistionoadi.belajar.jwt.handler.LoginFailureHandler;
import com.sulistionoadi.belajar.jwt.handler.LoginSuccessHandler;
import com.sulistionoadi.belajar.jwt.security.JwtAuthenticationTokenFilter;
import com.sulistionoadi.belajar.jwt.security.SecUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @author adi
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private SecUserDetailService userDetailService;
    @Autowired private LoginFailureHandler loginFailureHandler;
    @Autowired private LoginSuccessHandler loginSuccessHandler;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
    
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
    
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(true)
            .sessionRegistry(sessionRegistry());

        http
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/styles/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/api/halo").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/**").hasRole("USER_VIEW")
                .antMatchers("/api/user/**").hasRole("USER_EDIT")
                .antMatchers(HttpMethod.GET, "/api/permission/**").hasRole("PERMISSION_VIEW")
                .antMatchers("/api/permission/**").hasRole("PERMISSION_EDIT")
                .antMatchers(HttpMethod.GET, "/api/role/**").hasRole("USERROLE_VIEW")
                .antMatchers("/api/role/**").hasRole("USERROLE_EDIT")
                .anyRequest().authenticated()
            .and()
                .formLogin().loginPage("/login").permitAll()
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
            .and()
                .csrf().disable();
//                .csrf().csrfTokenRepository(csrfTokenRepository())
//                .and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
        
        // Custom JWT based security filter
//        http
//            .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
//        http.headers().cacheControl();
    }
    
//    private CsrfTokenRepository csrfTokenRepository() {
//        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//        repository.setHeaderName("X-XSRF-TOKEN");
//        return repository;
//    }
//    
//    private static class CsrfHeaderFilter extends OncePerRequestFilter {
//
//        @Override
//        protected void doFilterInternal(HttpServletRequest request,
//                HttpServletResponse response, FilterChain filterChain)
//                throws ServletException, IOException {
//            
//            CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//            if (csrf != null) {
//                Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
//                String token = csrf.getToken();
//                if (cookie == null || token != null && !token.equals(cookie.getValue())) {
//                    cookie = new Cookie("XSRF-TOKEN", token);
//                    cookie.setPath("/auth-server/");
//                    response.addCookie(cookie);
//                }
//            }
//            filterChain.doFilter(request, response);
//        }
//    }
}
