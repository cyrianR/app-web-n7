package fr.n7.spring_boot_api.websocket;

import fr.n7.spring_boot_api.payload.response.RoleUpdateResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/updateRoles")
    @SendTo("/topic/roles")
    public RoleUpdateResponse updateRoles(RoleUpdateResponse message) {
        return new RoleUpdateResponse(
            message.getUserId(),
            message.getUsername(),
            message.getRoles()
        );
    }
}