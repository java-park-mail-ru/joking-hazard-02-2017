package sample.Game.Messages.UserMessages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import sample.Game.Messages.BaseGameMessage;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings({"unused", "DefaultFileTemplate"})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = ChooseCardFromHand.class, name = "ChooseCardFromHand"),
        @Type(value = ChooseCardFromTable.class, name = "ChooseCardFromTable"),
        @Type(value = ChooseCardFromTable.class, name = "UserExit")
})
public class BaseUserMessage extends BaseGameMessage {
    @Override
    public String getType() {
        return "BaseUserMessage";
    }
}
