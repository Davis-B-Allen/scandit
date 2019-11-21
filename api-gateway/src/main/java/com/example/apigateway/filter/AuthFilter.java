package com.example.apigateway.filter;

import com.example.apigateway.model.User;
import com.example.apigateway.model.UserRole;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AuthFilter extends ZuulFilter {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
//        String body = null;
//        try {
//            body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//            System.out.println(body);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String token = request.getHeader("Authorization");
//        String contentType = request.getContentType();
        String method = request.getMethod();
        String path = request.getRequestURI();
        final HttpHeaders headers = new HttpHeaders();
        if (token != null) headers.set("Authorization", token);
//        headers.set("Content-Type", contentType);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
//        final HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<User> response = restTemplate.exchange("http://auth" + path, HttpMethod.valueOf(method), entity, User.class);
            System.out.println("!!!!!!!!!!!!!!!!");
            System.out.println(response);
            System.out.println("!!!!!!!!!!!!!!!!");
            User user = response.getBody();

            if (user != null) {
                List<String> rolesList = user.getUserRoles().stream().map(UserRole::getName).collect(Collectors.toList());
                String rolesString = String.join(",", rolesList);
                ctx.addZuulRequestHeader("userId", user.getId().toString());
                ctx.addZuulRequestHeader("username", user.getUsername());
                ctx.addZuulRequestHeader("userRoles", rolesString);
            }
        } catch (HttpClientErrorException e) {
            System.out.println(e);
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(e.getStatusCode().value());
            ctx.getResponse().setHeader("Content-Type", "text/plain;charset=UTF-8");
            ctx.setResponseBody(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            ctx.getResponse().setHeader("Content-Type", "text/plain;charset=UTF-8");
            ctx.setResponseBody("Couldn't successfully authenticate request");
        }

//        ResponseEntity<User> response = restTemplate.exchange("http://auth:8082/auth", HttpMethod.valueOf(method), entity, User.class);
        return null;
    }


}
