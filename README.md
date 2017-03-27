# JTest
## API DOCS
#### Адрес
<code>/api/user/signup</code>
#### Запрос
<code>{
"pass": "111", 
"userLogin":"211", 
"userMail":"1@1.ru"
}</code>
#### Ответ
<code>{"result": true,"errorMsg": "ok"}</code>
#### Адрес 
<code>/api/user/login</code>
#### Запрос
<code> {"pass": "111", "userLogin":"211"} </code>
#### Ответ
<code> {
"result": false,
"errorMsg": "Invalid login"
} </code>
#### Адрес 
<code>/api/user/changeMail</code>
#### Запрос
<code> {"strCont":"1@2.ru"} </code>
#### Ответ
<code>{
"result": true,
"errorMsg": "ok"
} </code>
#### Адрес 
<code>/api/user/changePass</code>
#### Запрос
<code> {"oldPass":"111", "newPass":"122"} </code>
#### Ответ
<code> {
"result": false,
"errorMsg": "Invalid password"
}</code>
#### Адрес 
<code>/api/who_i_am</code>
#### Ответ
<code> {"userMail":"1@1.ru","userLogin":"111"}</code>
#### Адрес 
<code>/api/logout</code>
