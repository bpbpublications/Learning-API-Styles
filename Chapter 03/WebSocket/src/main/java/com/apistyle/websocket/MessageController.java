package com.apistyle.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Message greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Message("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

}
