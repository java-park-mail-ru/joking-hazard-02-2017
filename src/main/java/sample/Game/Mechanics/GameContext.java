package sample.Game.Mechanics;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Game.Mechanics.Cards.CardDeck;
import sample.Game.Mechanics.Cards.GameCard;
import sample.Game.Mechanics.GameUser.GameUserItem;
import sample.Game.Mechanics.States.GameState;
import sample.Game.Mechanics.States.InitState;

import java.util.*;

/**
 * Created by ksg on 21.05.17.
 */
@SuppressWarnings("PublicField")
public class GameContext {
    public final Map<String, GameUserItem> mp;
    public final Map<String, GameCard> table;
    public final GameCard[] cards;
    public final Queue<GameUserItem> masterQeue;
    public CardDeck deck;
    public GameState state;
    public int numberOfPlayers;
    public ObjectMapper mapper;
    public int numberCardsInHand;
    public int currentRound;
    public GameUserItem master;

    public void reset() {
        table.clear();
        Arrays.fill(cards, null);
    }

    public GameContext() {
        state = new InitState(this);
        mp = new HashMap<>(); // TODO: Do it as user controller
        table = new LinkedHashMap<>();
        masterQeue = new ArrayDeque<>();
        cards = new GameCard[3];
        currentRound = 0;
    }
}