package cat.tecnocampus.sourceBin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by eloit on 2/3/2017.
 */
@Component
@EnableBinding(Source.class)
public class CustomOutputEventSource {

    private Source customSource;

    public CustomOutputEventSource (Source source) {
        this.customSource = source;
    }

    public void userToDelete (String nick) {
        String mess = "delete-"+nick;

        System.out.println("Forwarding a message "+ mess +" to queue "+ Source.OUTPUT);
        customSource.output().send(MessageBuilder.withPayload(mess).build());
    }

}
