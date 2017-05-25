package sample.Game.Messages.UserMessages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ksg on 15.05.17.
 */
public class ChooseCardFromTable extends BaseUserMessage {
    final int chosenCard;

    @JsonCreator
    public ChooseCardFromTable(
            @JsonProperty("chosenCard") int chosenCard
    ) {
        this.chosenCard = chosenCard;
    }

    @Override
    public String getType() {
        return "ChooseCardFromTable";
    }

    public int getChosenCard() {
        return chosenCard;
    }
}
