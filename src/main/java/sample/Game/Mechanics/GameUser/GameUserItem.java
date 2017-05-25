package sample.Game.Mechanics.GameUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import sample.Game.Mechanics.Cards.CardDeck;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Messages.ServerMessages.BaseServerMessage;
import sample.Game.Messages.ServerMessages.HandInfo;

import java.util.Vector;

/**
 * Created by ksg on 26.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class GameUserItem {
    private final Vector<GameCard> hand;
    @SuppressWarnings({"unused"})
    private Integer score;
    private GameUserInterface user;
    private BaseServerMessage lastCmd;
    private final ObjectMapper mapper;
    private final int cardNum;

    public GameUserItem(Vector<GameCard> hand, ObjectMapper mapper) {
        this.mapper = mapper;
        this.hand = new Vector<>(hand);
        cardNum = this.hand.size();
        score = 0;
    }

    public boolean reget(CardDeck deck) {
        while (this.hand.size() < cardNum) {
            final GameCard card = deck.popCard();
            if (card == null) {
                return false;
            }
            this.hand.add(card);
        }
        return true;
    }

    public void setStrategy(GameUserInterface inter) {
        inter.init(new HandInfo(mapper, hand));
        user = inter;
    }

    public void sendMessage(BaseServerMessage msg) {
        user.send(msg);
    }

    @Nullable
    public GameCard getCardFromHandByIndex(int index) {
        if ((index >= hand.size()) || (index < 0)) {
            return null;
        }
        return hand.remove(index);
    }

    public void getCardFromHand() {
        lastCmd = user.chooseCardFromHand(mapper);
    }

    public void getCardFromTable() {
        lastCmd = user.chooseCardFromTable(mapper);
    }

    public void resendLast(){
        if(lastCmd != null)
            sendMessage(lastCmd);
    }

    public void resetMessage(){
        lastCmd = null;
    }

    public boolean isUser() {
        return user.isUser();
    }

    public void close() {
        user.close();
    }

    public int getScore() {
        return score;
    }

    @SuppressWarnings("unused")
    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore() {
        this.score++;
    }

    public void sendHand() {
        user.sendHandInfo(new HandInfo(mapper, hand));
    }
}
