package sample.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.context.MessageSource;
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
//@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class SignUpController {
    private final MessageSource messageSource;

    @SuppressWarnings("unused")
    @NotNull
    final AccountService accServ;

    @SuppressWarnings("unused")
    public SignUpController(@NotNull AccountService accountService, @NotNull MessageSource messageSource) {
        this.messageSource = messageSource;
        this.accServ = accountService;
    }

    @RequestMapping(path = "/api/user/signup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseCode> getMsg(@RequestBody UserDataView body, HttpSession httpSession) {
        Boolean resCode = true;
        String msg = messageSource.getMessage("msgs.created", null, Locale.ENGLISH);
        HttpStatus status = HttpStatus.CREATED;

        final UserData body_model = new UserData(body.getUserMail(), body.getUserLogin(), body.getPass());
        final AccountService.ErrorCodes result = accServ.register(body_model);

        switch (result) {

            case INVALID_REG_DATA:
                resCode = false;
                msg = messageSource.getMessage("msgs.bad_request", null, Locale.ENGLISH);
                status = HttpStatus.BAD_REQUEST;
                break;

            case LOGIN_OCCUPIED:
                resCode = false;
                msg = messageSource.getMessage("msgs.conflict", null, Locale.ENGLISH);
                status = HttpStatus.CONFLICT;
                break;

            case OK:
                httpSession.setAttribute("userLogin", body.getUserLogin());
                break;
        }

        return new ResponseEntity<ResponseCode>(new ResponseCode(resCode, msg), status);
    }
}
