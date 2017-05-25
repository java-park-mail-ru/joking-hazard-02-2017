package sample.Game.Messages.ServerMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Mechanics.Cards.GameCard;

import java.util.Vector;

/**
 * Created by ksg on 27.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class HandInfo extends BaseServerMessage {
    private final Vector<GameCard> hand;

    public HandInfo(ObjectMapper mapper, Vector<GameCard> hand) {
        super(mapper);
        this.hand = hand;

    }

    @Override
    public String getType() {
        return "HandInfo";
    }

    @SuppressWarnings("unused")
    public Vector<GameCard> getHand() {
        return hand;
    }
}
