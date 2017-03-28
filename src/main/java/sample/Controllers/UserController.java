package sample.Controllers;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import sample.Models.UserInfoModel;
import sample.Services.AccountService;
import sample.Views.ResponseCode;
import sample.Views.PassForm;
import sample.Views.MailForm;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import java.util.Locale;

import sample.Views.UserInfo;

@SuppressWarnings("Duplicates")
//@CrossOrigin(origins = "https://jokinghazard.herokuapp.com")
@RestController
public class UserController {
    @SuppressWarnings("unused")
    private final MessageSource messageSource;

    @SuppressWarnings("unused")
    @NotNull
    final AccountService accServ;

    @SuppressWarnings("unused")
    public UserController(@NotNull AccountService accountService, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.accServ = accountService;
    }

    @RequestMapping(path = "/api/user/data", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ResponseCode<UserInfo>> getWho(HttpSession httpSession) {
        final UserInfoModel data[] = {null,};
        String msg = messageSource.getMessage("msgs.ok", null, Locale.ENGLISH);
        Boolean result = true;

        final String id = (String) httpSession.getAttribute("userLogin");

        if (id == null) {
            msg = messageSource.getMessage("msgs.not_found", null, Locale.ENGLISH);
            result = false;

            return new ResponseEntity<ResponseCode<UserInfo>>(new ResponseCode<UserInfo>(result, msg), HttpStatus.NOT_FOUND);
        }

        final AccountService.ErrorCodes retCode = accServ.getUserData(id, data);

        if (retCode != AccountService.ErrorCodes.OK) {
            msg = messageSource.getMessage("msgs.forbidden", null, Locale.ENGLISH);
            result = false;

            return new ResponseEntity<ResponseCode<UserInfo>>(new ResponseCode<UserInfo>(result, msg), HttpStatus.FORBIDDEN);
        }

        final UserInfoModel dataMod = data[0];
        final UserInfo dataView = new UserInfo(dataMod.getUserMail(), dataMod.getUserLogin());

        return new ResponseEntity<ResponseCode<UserInfo>>(new ResponseCode<UserInfo>(result, msg, dataView), HttpStatus.OK);
    }

    @RequestMapping(path = "/api/logout", method = RequestMethod.GET)
    public void logOut(HttpSession httpSession) {
        httpSession.invalidate();
    }

    @RequestMapping(path = "/api/user/changeMail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseCode> changeMail(@RequestBody MailForm str, HttpSession httpSession) {
        Boolean resCode = false;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = messageSource.getMessage("msgs.bad_request", null, Locale.ENGLISH);

        if (str.getStrCont() != null) {
            final String login = (String) httpSession.getAttribute("userLogin");
            final AccountService.ErrorCodes result = accServ.changeMail(str.getStrCont(), login);

            switch (result) {

                case INVALID_SESSION: {
                    resCode = false;
                    msg = messageSource.getMessage("msgs.not_found", null, Locale.ENGLISH);
                    status = HttpStatus.NOT_FOUND;
                    break;
                }

                case OK: {
                    resCode = true;
                    msg = messageSource.getMessage("msgs.ok", null, Locale.ENGLISH);
                    status = HttpStatus.OK;
                    break;
                }
            }
        }

        return new ResponseEntity<ResponseCode>(new ResponseCode(resCode, msg), status);

    }

    @RequestMapping(path = "/api/user/changePass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<ResponseCode> changePass(@RequestBody PassForm form, HttpSession httpSession) {
        Boolean resCode = false;
        HttpStatus status = HttpStatus.NOT_FOUND;
        String msg = messageSource.getMessage("msgs.not_found", null, Locale.ENGLISH);

        final String login = (String) httpSession.getAttribute("userLogin");

        if (login == null) {
            return new ResponseEntity<ResponseCode>(new ResponseCode(resCode, msg), status);
        }


        if (form.getOldPassHash() != null && form.getNewPassHash() != null) {

            if (!accServ.checkPass(form.getOldPassHash(), login)) {
                msg = messageSource.getMessage("msgs.forbidden", null, Locale.ENGLISH);
                status = HttpStatus.FORBIDDEN;

            } else {
                final AccountService.ErrorCodes error = accServ.changePassHash(form.getNewPassHash(), login);

                switch (error) {

                    case OK: {
                        resCode = true;
                        msg = messageSource.getMessage("msgs.ok", null, Locale.ENGLISH);
                        status = HttpStatus.OK;
                        break;
                    }
                }
            }

        } else {
            msg = messageSource.getMessage("msgs.bad_request", null, Locale.ENGLISH);
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<ResponseCode>(new ResponseCode(resCode, msg), status);
    }

}