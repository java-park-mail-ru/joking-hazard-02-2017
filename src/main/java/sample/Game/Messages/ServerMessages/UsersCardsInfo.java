package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Mechanics.Cards.GameCard;

import java.util.ArrayList;

/**
 * Created by ksg on 25.05.17.
 */
public class UsersCardsInfo extends BaseServerMessage{
    private final  ArrayList<GameCard> table;

    public UsersCardsInfo(ObjectMapper mapper, ArrayList<GameCard> table) {
        super(mapper);
        this.table = table;

    }

    @Override
    public String getType() {
        return "UserCardsInfo";
    }

    @SuppressWarnings("unused")
    public  ArrayList<GameCard> getHand() {
        return table;
    }
}
