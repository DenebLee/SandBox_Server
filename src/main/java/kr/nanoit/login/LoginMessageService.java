package kr.nanoit.login;

import kr.nanoit.config.MessageService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginMessageService implements MessageService {

    private String messageServiceType;
    private String protocol;
    private String id;
    private String password;
    private String version;

    @Override
    public String getMessageServiceType() {
        return messageServiceType;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }
}
