package sample.Game.Services;

import sample.Game.Messages.BaseMessageContainer;
import sample.Game.Messages.ServerMessages.GameUserInfo;
import sample.Game.Messages.ServerMessages.RoundInfo;
import sample.Game.Messages.SystemMessages.BaseSystemMessage;
import sample.Game.Messages.SystemMessages.ErrorMessage;
import sample.Game.Messages.SystemMessages.ScoreUpdatedMessage;
import sample.Game.Messages.SystemMessages.UpdateScoreMessage;
import sample.Main.Services.AccountService;
import sample.Main.Views.UserInfo;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ksg on 01.06.17.
 */
public class DatabaseWorker implements Runnable{
    private final ServerManager master;
    private final ConcurrentLinkedQueue<BaseSystemMessage> systemQue;
    private final AccountService accountService;
    private final boolean run;
    public DatabaseWorker(ServerManager master,ConcurrentLinkedQueue<BaseSystemMessage> systemQue,
                          AccountService db){
        this.master = master;
        this.systemQue = systemQue;
        this.accountService = db;
        this.run = true;
    }

    private void updateScore(BaseSystemMessage msg){
        UpdateScoreMessage upd = UpdateScoreMessage.class.cast(msg);
        RoundInfo info = upd.getInfo();
        Vector<GameUserInfo> users  =  info.getUsers();
        boolean flag = false;
        String login = "";
        for(GameUserInfo i : users) {
            login = i.getUserLogin();
            int score = i.getScore();
            try{
                accountService.updateScore(login ,score);
            } catch (Exception ex){
                flag = true;
            }
        }
        if (flag) {
            ErrorMessage eMsg = new ErrorMessage("Database Update Error");
            master.addSystemMessage(login, eMsg);
        } else {
            ScoreUpdatedMessage okMsg = new ScoreUpdatedMessage();
            master.addSystemMessage(login, okMsg);
        }
    }

    private void handle(BaseSystemMessage handledMessage){
        String type = handledMessage.getType();
        if (type=="UpdateScoreMessage") {
            this.updateScore(handledMessage);
        }
    }

    public ConcurrentLinkedQueue<BaseSystemMessage> getQue() {
        return systemQue;
    }

    public void run(){
        while (this.run){
            BaseSystemMessage msg = systemQue.poll();
            if (msg != null) {
                this.handle(msg);
            }
        }
    }
}
