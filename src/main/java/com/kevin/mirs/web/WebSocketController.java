package com.kevin.mirs.web;


import com.kevin.mirs.dto.MIRSResult;
import com.kevin.mirs.service.SockJSHandler;
import com.kevin.mirs.vo.SimpleUserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class WebSocketController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    SockJSHandler sockJSHandler;

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 发送给对多个客户端
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/hello")
    @SendTo("/topic/system-messages")
    public SimpleUserMessage broadcast(SimpleUserMessage message) throws Exception {
        return message;
    }

    /**
     * 这里用的是@SendToUser，这就是发送给单一客户端的标志。本例中，
     * 客户端接收一对一消息的主题应该是“/user/” + 用户Id + “/message” ,这里的用户id可以是一个普通的字符串，只要每个用户端都使用自己的id并且服务端知道每个用户的id就行。
     *
     * @return
     */
    @MessageMapping("/message")
    @SendToUser("/message")
    public SimpleUserMessage endToEndMessage(SimpleUserMessage message) throws Exception {
        return message;
    }

    @ResponseBody
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public MIRSResult<Boolean> send(@RequestBody SimpleUserMessage message) {

        if(message.getType() == 0) {
            template.convertAndSend("/topic/system-messages", message);
        } else {
            template.convertAndSendToUser(message.getTo(), "/message", message);
        }
        return new MIRSResult<Boolean>(true, true);
    }

}
