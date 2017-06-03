package sample.Game.Messages.SystemMessages;

/**
 * Created by ksg on 02.06.17.
 */
public class ErrorMessage extends BaseSystemMessage {
    String msg;
    public ErrorMessage(String msg){
        this.msg = msg;
    }
    public String getType() {
        return "Error";
    }
    public String getMsg(){
        return msg;
    }
}
