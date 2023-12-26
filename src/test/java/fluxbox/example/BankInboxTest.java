package fluxbox.example;

import org.fluxbox.example.msg.TransactionMsg;
import org.fluxbox.fluxbox.Command;
import org.fluxbox.fluxbox.MsgTypeNotAccepted;
import org.fluxbox.fluxbox.defaultImpl.DefaultInTray;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class BankInboxTest {
    class BadMsg implements Command{};
    @Test
    public void testMsgTypes(){
        DefaultInTray defaultInbox = new DefaultInTray("Bank",List.of(TransactionMsg.class));
        assertThat(defaultInbox.supportedMessageTypes(),equalTo(List.of(TransactionMsg.class)));
    }
    @Test()
    public void testRejectsBadMessageType(){

        DefaultInTray defaultInbox = new DefaultInTray("Bank",List.of(TransactionMsg.class));
          Exception x = assertThrows(
                  MsgTypeNotAccepted.class, ()->{
                      defaultInbox.despatch(new BadMsg());
                  },"Expected Exception"
          );
    }


}
