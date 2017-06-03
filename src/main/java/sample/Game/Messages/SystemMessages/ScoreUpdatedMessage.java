package sample.Game.Messages.SystemMessages;

/**
 * Created by ksg on 02.06.17.
 */
public class ScoreUpdatedMessage extends BaseSystemMessage{
    public String getType() {
        return "ScoreUpdated";
    }
}
