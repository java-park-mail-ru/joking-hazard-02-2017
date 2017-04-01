package sample.Controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.Models.LogInModel;
import sample.Views.ResponseCode;
import sample.Services.AccountService;
import sample.Views.LogInData;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@SuppressWarnings("Duplicates")
@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class LogInController {
    @NotNull
    private final MessageSource messageSource;

    @NotNull
    private final AccountService accountService;

    public LogInController(@NotNull AccountService accountService, @NotNull MessageSource messageSource) {
        this.accountService = accountService;
        this.messageSource = messageSource;
    }

    @RequestMapping(path = "/api/user/login", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseCode> getMsg(@RequestBody LogInData body, HttpSession httpSession) {
        final LogInModel body_model = new LogInModel(body.getUserLogin(), body.getPassHash());
        final AccountService.ErrorCodes resp = accountService.login(body_model);

        switch (resp) {

            case INVALID_AUTH_DATA: {
                return new ResponseEntity<>(new ResponseCode(false,
                        messageSource.getMessage("msgs.bad_request", null, Locale.ENGLISH)),
                        HttpStatus.BAD_REQUEST);
            }

            case INVALID_LOGIN: case INVALID_PASSWORD: {
                return new ResponseEntity<>(new ResponseCode(false,
                        messageSource.getMessage("msgs.forbidden", null, Locale.ENGLISH)),
                        HttpStatus.FORBIDDEN);
            }

            case OK: {
                httpSession.setAttribute("userLogin", body.getUserLogin());
                return new ResponseEntity<>(new ResponseCode(true,
                        messageSource.getMessage("msgs.ok", null, Locale.ENGLISH)),
                        HttpStatus.OK);
            }

            default: {
                return new ResponseEntity<>(new ResponseCode(false,
                        messageSource.getMessage("msgs.internal_server_error", null, Locale.ENGLISH)),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
