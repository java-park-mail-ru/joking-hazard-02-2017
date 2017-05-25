package sample.ResourceManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sample.Game.Mechanics.Cards.CardDeck;
import sample.Game.Mechanics.Cards.GameCard;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ClassNameDiffersFromFileName", "MultipleTopLevelClassesInFile", "PublicField"})
class Props {

    public int numOfCardsInDeck;
    public int defaultMaxNumber;
    public int numberOfCardsInHand;

    @SuppressWarnings("unused")
    public void setNumOfCardsInDeck(int n) {
        numOfCardsInDeck = n;
    }

    @SuppressWarnings("unused")
    public void setDefaultMaxNumber(int n) {
        defaultMaxNumber = n;
    }

    @SuppressWarnings("unused")
    public void setNumberOfCardsInHand(int n) {
        numberOfCardsInHand = n;
    }

    @SuppressWarnings("unused")
    public int getNumOfCardsInDeck() {
        return numOfCardsInDeck;
    }

    @SuppressWarnings("unused")
    public int getDefaultMaxNumber() {
        return defaultMaxNumber;
    }

    @SuppressWarnings("unused")
    public int getNumberOfCardsInHand() {
        return numOfCardsInDeck;
    }
}

@SuppressWarnings("MultipleTopLevelClassesInFile")
@Service
public class ResourceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);

    private Props props;

    public ResourceManager() {
        final ObjectMapper mapper = new ObjectMapper();
        //noinspection OverlyBroadCatchBlock
        try {
            final URL fl = Resources.getResource("Property");

            props = mapper.readValue(fl, Props.class);
        } catch (@SuppressWarnings("OverlyBroadCatchBlock") IOException e) {
            e.printStackTrace();
            LOGGER.error("can't read props", e);
        } catch (Exception e) {
            LOGGER.error("can't read props", e);
        }

    }

    public CardDeck getCardDeck() {
        final List<GameCard> lst = new ArrayList<>();
        for (int i = 0; i < props.numOfCardsInDeck; i++) {
            final GameCard card = new GameCard(i, (i % 7) == 0);
            lst.add(card);
        }
        return new CardDeck(lst);
    }

    public int defaultMaxNumber() {
        return props.defaultMaxNumber;
    }

    public int numberOfCardsInHand() {
        return props.numberOfCardsInHand;
    }
}
