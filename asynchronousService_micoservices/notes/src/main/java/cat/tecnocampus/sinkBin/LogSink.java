package cat.tecnocampus.sinkBin;

import cat.tecnocampus.useCases.NoteUseCases;
import org.aspectj.weaver.ast.Not;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * Created by eloit on 2/3/2017.
 */

@EnableBinding(Sink.class)
public class LogSink {

    private NoteUseCases noteUseCases;

    private final static String DELETE = "delete";

    public LogSink (NoteUseCases noteUseCases) {
        this.noteUseCases = noteUseCases;
    }

    @ServiceActivator(inputChannel=Sink.INPUT)
    public void notesToDelete (Object o) throws Exception {
        String mess = o.toString();
        String[] output = mess.split("-");
        System.out.println("Action: "+ output[0]);
        System.out.println("            User: " + output[1]);

        if (output[0].equalsIgnoreCase(this.DELETE)) {
            try {
                this.noteUseCases.deleteNotesFromUser(output[1]);
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            throw new Exception("Incorrect Action");
        }

    }
}
