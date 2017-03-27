package sample.Controllers;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import sample.Models.UserInfoModel;
import sample.Services.AccountService;
import sample.Views.ResponseCode;
import sample.Views.PassForm;
import sample.Views.StringContainer;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import java.util.Locale;
import sample.Views.UserInfo;

//@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class UserController {
    @SuppressWarnings("unused")
    @NotNull
    final AccountService accServ;
    @SuppressWarnings("unused")
    private final MessageSource messageSource;

    @SuppressWarnings("unused")
    public UserController(@NotNull AccountService accountService, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.accServ = accountService;
    }

    @RequestMapping(path = "/api/user/data", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ResponseCode<UserInfo>> getWho(HttpSession httpSession) {
        final UserInfoModel data[] = {null,};
        String msg = "Ok";
        boolean result = false;
        final String id = (String) httpSession.getAttribute("userLogin");
        if (id == null) {
            msg = messageSource.getMessage("msgs.invalid_session", null, Locale.ENGLISH);
            return new ResponseEntity<ResponseCode<UserInfo>>(new ResponseCode<UserInfo>(false,msg),HttpStatus.FORBIDDEN);
        }
        AccountService.ErrorCodes retCode = accServ.getUserData(id,data);
        if (retCode != AccountService.ErrorCodes.OK){
            msg = messageSource.getMessage("msgs.invalid_session", null, Locale.ENGLISH);
            return new ResponseEntity<ResponseCode<UserInfo>>(new ResponseCode<UserInfo>(false,msg),HttpStatus.FORBIDDEN);
        }
        UserInfoModel dataMod = data[0];
        UserInfo dataView = new UserInfo(dataMod.getUserMail(),dataMod.getUserLogin());
        return new ResponseEntity<ResponseCode<UserInfo>>(new ResponseCode<UserInfo>(result,msg, dataView),
                HttpStatus.OK);
    }

    @RequestMapping(path = "/api/logout", method = RequestMethod.GET)
    public void logOut(HttpSession httpSession) {
        httpSession.invalidate();
    }

    @RequestMapping(path = "/api/user/changeMail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseCode> changeMail(@RequestBody StringContainer str, HttpSession httpSession) {
        final ResponseCode resp;
        String msg;
        final String login = (String) httpSession.getAttribute("userLogin");
        if (login == null) {
            msg = messageSource.getMessage("msgs.invalid_session", null, Locale.ENGLISH);
            return new ResponseEntity<ResponseCode>(new ResponseCode(false,msg),HttpStatus.FORBIDDEN);
        }
        final boolean result = false;
        msg = messageSource.getMessage("msgs.error", null, Locale.ENGLISH);
        return new ResponseEntity<ResponseCode>( new ResponseCode(false, msg), HttpStatus.FORBIDDEN);

    }

    @RequestMapping(path = "/api/user/changePass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public  ResponseEntity<ResponseCode> changePass(@RequestBody PassForm form, HttpSession httpSession) {
        final String msg;
        final String login = (String) httpSession.getAttribute("userLogin");
        if (login == null) {
            msg = messageSource.getMessage("msgs.invalid_session", null, Locale.ENGLISH);
            return new  ResponseEntity<ResponseCode>(new ResponseCode(false, msg), HttpStatus.FORBIDDEN);
        }
        if (!accServ.checkPass(form.getOldPassHash(), login)) {
            msg = messageSource.getMessage("msgs.invalid_password", null, Locale.ENGLISH);
            return new  ResponseEntity<ResponseCode>(new ResponseCode(false, msg), HttpStatus.OK);
        }
        AccountService.ErrorCodes err =  accServ.changePassHash(form.getNewPassHash(), login);
        switch (err){
            case INVALID_SESSION:
                msg = messageSource.getMessage("msgs.invalid_session", null, Locale.ENGLISH);
                return new  ResponseEntity<ResponseCode>(new ResponseCode(false, msg), HttpStatus.OK);

        }
        msg = messageSource.getMessage("msgs.ok", null, Locale.ENGLISH);
        return new  ResponseEntity<ResponseCode>(new ResponseCode(true, msg), HttpStatus.OK);
    }

}