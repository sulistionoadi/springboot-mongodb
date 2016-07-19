package com.sulistionoadi.belajar.jwt.config;

import com.sulistionoadi.belajar.jwt.handler.LoginFailureHandler;
import com.sulistionoadi.belajar.jwt.handler.LoginSuccessHandler;
import com.sulistionoadi.belajar.jwt.security.JwtAuthenticationEntryPoint;
import com.sulistionoadi.belajar.jwt.security.JwtAuthenticationTokenFilter;
import com.sulistionoadi.belajar.jwt.security.SecUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
    
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public JwtAuthenticationTokenFilter 
        authenticationTokenFilterBean() throws Exception {
        JwtAuthenticationTokenFilter authenticationTokenFilter = 
                new JwtAuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(
                authenticationManagerBean());
        return authenticationTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //We dont need session management because token is stateless session
        //http
        //    .sessionManagement()
        //    .maximumSessions(1)
        //    .maxSessionsPreventsLogin(true)
        //   .sessionRegistry(sessionRegistry());

        http
            .csrf().disable() // we don't need CSRF because our token is invulnerable
            //.csrf().csrfTokenRepository(csrfTokenRepository())
            //.and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
                .antMatchers("/", "index.html").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/styles/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/scripts/**").permitAll()
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
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true);
        
        // Custom JWT based security filter
        http
            .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
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
