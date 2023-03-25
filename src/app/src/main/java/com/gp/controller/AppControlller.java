package com.gp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppControlller {

    @GetMapping
    String main() {
        return "main_page";
    }

    @GetMapping("create_restapi")
    String createRestApi() {
        return "create_restapi";
    }

    @GetMapping("create_msgqueue")
    String createMsQueue() {
        return "create_msgqueue";
    }

    @GetMapping("requests_history")
    String requestHistory() {
        return "requests_history";
    }

    @GetMapping("usersapimodels")
    String usersapimodels() {
        return "usersapimodels";
    }

    @GetMapping("usersmsgqueues")
    String usersmsgqueues() {
        return "usersmsgqueues";
    }

    @GetMapping("msg_history")
    String msg_history() {
        return "msg_history";
    }

    @GetMapping("restapi_history")
    String restapi_history() {
        return "requests_history";
    }

}
