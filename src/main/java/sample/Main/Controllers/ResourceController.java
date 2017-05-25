package sample.Main.Controllers;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sample.Main.Views.CardView;
import sample.Main.Views.ResponseCode;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by lieroz on 25.05.17.
 */
@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
@PropertySource("classpath:dropbox.properties")
public class ResourceController {
    @Value("${dropbox.access-token}")
    private String ACCESS_TOKEN;

    @Value("${dropbox.resources}")
    private String RESOURCES_PATH;

    @Value("${dropbox.resources-censored}")
    private String RESOURCES_CENSORED_PATH;

    @Autowired
    private MessageSource messageSource;

    private DbxClientV2 dbxClientV2;

    @PostConstruct
    public void setup() {
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("dropbox", "en_US");
        dbxClientV2 = new DbxClientV2(dbxRequestConfig, ACCESS_TOKEN);
    }

    @RequestMapping(value = "/api/resources", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCardPack() {
        Integer cardId = 0;
        List<CardView> deck = new ArrayList<>();
        try {
            ListFolderResult result = dbxClientV2.files().listFolder(RESOURCES_PATH);
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    String url = dbxClientV2.files().getTemporaryLink(metadata.getPathLower()).getLink();
                    deck.add(new CardView(++cardId, url));
                }
                if (!result.getHasMore()) {
                    break;
                }
                result = dbxClientV2.files().listFolderContinue(result.getCursor());
            }
        } catch (DbxException ex) {
            return new ResponseEntity<>(new ResponseCode(false,
                    messageSource.getMessage("dropbox.bad", null, Locale.ENGLISH)),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseCode<>(true,
                messageSource.getMessage("msgs.ok", null, Locale.ENGLISH),
                deck), HttpStatus.OK);
    }
}
