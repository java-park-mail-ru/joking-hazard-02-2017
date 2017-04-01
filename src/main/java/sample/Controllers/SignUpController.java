package sample.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.Services.AccountService;
import sample.Models.UserData;
import sample.Views.ResponseCode;
import sample.Views.UserDataView;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@SuppressWarnings("Duplicates")
@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class SignUpController {
    @NotNull
    private final MessageSource messageSource;

    @NotNull
    private final AccountService accountService;

    public SignUpController(@NotNull AccountService accountService, @NotNull MessageSource messageSource) {
        this.messageSource = messageSource;
        this.accountService = accountService;
    }

    @RequestMapping(path = "/api/user/signup", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCode> getMsg(@RequestBody UserDataView body, HttpSession httpSession) {
        final UserData body_model = new UserData(body.getUserMail(), body.getUserLogin(), body.getPass());
        final AccountService.ErrorCodes result = accountService.register(body_model);

        switch (result) {

            case INVALID_REG_DATA: {
                return new ResponseEntity<>(new ResponseCode(false,
                        messageSource.getMessage("msgs.bad_request", null, Locale.ENGLISH)),
                        HttpStatus.BAD_REQUEST);
            }

            case LOGIN_OCCUPIED: {
                return new ResponseEntity<>(new ResponseCode(false,
                        messageSource.getMessage("msgs.conflict", null, Locale.ENGLISH)),
                        HttpStatus.CONFLICT);
            }

            case OK: {
                httpSession.setAttribute("userLogin", body.getUserLogin());
                return new ResponseEntity<>(new ResponseCode(true,
                        messageSource.getMessage("msgs.created", null, Locale.ENGLISH)),
                        HttpStatus.CREATED);
            }

            default: {
                return new ResponseEntity<>(new ResponseCode(false,
                        messageSource.getMessage("msgs.internal_server_error", null, Locale.ENGLISH)),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
