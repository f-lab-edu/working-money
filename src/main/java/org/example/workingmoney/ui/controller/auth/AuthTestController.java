package org.example.workingmoney.ui.controller.auth;

import org.example.workingmoney.ui.controller.common.Response;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthTestController {

    @GetMapping("/authTest")
    public Response<String> authTest() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return Response.ok("Hello " + name);
    }
}
