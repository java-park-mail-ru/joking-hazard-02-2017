package sample.Controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
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
//@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class LogInController {
    private final MessageSource messageSource;

    @SuppressWarnings("unused")
    @NotNull
    final AccountService accServ;

    @SuppressWarnings("unused")
    public LogInController(@NotNull AccountService accountService, @NotNull MessageSource messageSource) {
        this.accServ = accountService;
        this.messageSource = messageSource;
    }

    @RequestMapping(path = "/api/user/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseCode> getMsg(@RequestBody LogInData body, HttpSession httpSession) {
        Boolean resCode = true;
        String msg = messageSource.getMessage("msgs.ok", null, Locale.ENGLISH);
        HttpStatus status = HttpStatus.OK;

        final LogInModel body_model = new LogInModel(body.getUserLogin(), body.getPassHash());
        final AccountService.ErrorCodes resp = accServ.login(body_model);

        switch (resp) {

            case INVALID_AUTH_DATA: {
                msg = messageSource.getMessage("msgs.bad_request", null, Locale.ENGLISH);
                resCode = false;
                status = HttpStatus.BAD_REQUEST;
                break;
            }

            case INVALID_LOGIN: case INVALID_PASSWORD: {
                resCode = false;
                msg = messageSource.getMessage("msgs.forbidden", null, Locale.ENGLISH);
                status = HttpStatus.FORBIDDEN;
                break;
            }

            case OK: {
                httpSession.setAttribute("userLogin", body.getUserLogin());
                break;
            }
        }

        return new ResponseEntity<ResponseCode>(new ResponseCode(resCode, msg), status);
    }
}
