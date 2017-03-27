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

//@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class LogInController {
    @SuppressWarnings("unused")
    @NotNull
    final AccountService accServ;
    private final MessageSource messageSource;

    @SuppressWarnings("unused")
    public LogInController(@NotNull AccountService accountService, @NotNull MessageSource messageSource) {
        this.accServ = accountService;
        this.messageSource = messageSource;
    }

    @RequestMapping(path = "/api/user/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseCode> getMsg(@RequestBody LogInData body, HttpSession httpSession) {
        boolean resCode = false;
        String msg =  messageSource.getMessage("msgs.error", null, Locale.ENGLISH);
        LogInData.ViewError viewRes = body.valid();
        if(viewRes !=  LogInData.ViewError.OK){
            switch (viewRes){
                case INVALID_DATA_ERROR:
                    msg = messageSource.getMessage("msgs.invalid_auth_data", null, Locale.CANADA);
                    //HA CANADA
                    resCode = false;
                    break;
                default:
                    msg = messageSource.getMessage("msgs.error", null, Locale.ENGLISH);
                    resCode = false;
            }
            return new ResponseEntity<ResponseCode>(new ResponseCode(resCode,msg), HttpStatus.OK);
        }
        LogInModel body_model = new LogInModel(body.getUserLogin(), body.getPassHash());
        final AccountService.ErrorCodes resp = accServ.login(body_model);
        switch (resp){
            case INVALID_LOGIN:
                resCode = false;
                msg =  messageSource.getMessage("msgs.invalid_auth_data", null, Locale.ENGLISH);
                break;
            case INVALID_PASSWORD:
                resCode = false;
                msg =  messageSource.getMessage("msgs.invalid_auth_data", null, Locale.ENGLISH);
                break;
            case OK:
                resCode = true;
                msg =  messageSource.getMessage("msgs.ok", null, Locale.ENGLISH);
                httpSession.setAttribute("userLogin", body.getUserLogin());
                break;
            default:

        }
        return new ResponseEntity<ResponseCode>(new ResponseCode(resCode,msg), HttpStatus.OK);
    }
}
