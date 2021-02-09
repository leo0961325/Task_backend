package com.link8.tw.service.impl;

import com.link8.tw.jet.ContactResponse;
import com.link8.tw.jet.JetUser;
import com.link8.tw.jet.SendTextMessageRequest;
import com.link8.tw.service.JetService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Service
public class JetServiceImpl implements JetService {

    private Logger logger = LoggerFactory.getLogger(JetServiceImpl.class);

    @Override
    public void sendMessage(String userId, String message) {
        String userIdForJet = userId.replace(".", "");

        String token = "78caca4ab9dc98a95dbc0c559ea8dfaa297bcba4cf749a0d965f825658221d3e20d3c3ca92ace7e132ac171d5e954087";
        Client client = ClientBuilder.newClient();
        ContactResponse response = client.target("https://robot.jet666.com/robot/v1/info/getContacts")
                .request(MediaType.APPLICATION_JSON)
                .header("token", token)
                .get(ContactResponse.class);


        Optional<JetUser> userOp = response.getData().stream().filter(x -> StringUtils.equalsAnyIgnoreCase(x.getNickname().replace(".", ""), userIdForJet)).findAny();


        if (userOp.isPresent()) {

            JetUser jetUser = userOp.get();
            SendTextMessageRequest request = new SendTextMessageRequest();
            request.setChatType(1);
            request.setChatId(jetUser.getUserId());
            request.setText(message);
            client.target("https://robot.jet666.com/robot/v1/messageSender/sendTextMessage")
                    .request(MediaType.APPLICATION_JSON)
                    .header("token", token)
                    .post(Entity.entity(request, MediaType.APPLICATION_JSON));
        } else {
            logger.warn(String.format("User %s not found", userId));
        }
        //https://robot.jet666.com/robot/v1/messageSender/sendTextMessage
    }
}
