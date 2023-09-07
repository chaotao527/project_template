<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=!messagesPerField.existsError('username','password') displayInfo=realm.password && realm.registrationAllowed && !registrationDisabled??; section>
    <#if section = "form">
      <div id="kc-form">
        <div class="tl"></div>
        <div class="tr"></div>
        <div class="bl"></div>
        <div class="br"></div>
        <div id="kc-form-wrapper">
            <#if realm.password>
              <form id="kc-form-login" onsubmit="login.disabled = true; return true;"
                    action="${url.loginAction}"
                    method="post">
                <div class="${properties.kcFormGroupClass!}">

                  <div id="username-wrapper">
                    <input tabindex="1" id="username" class="${properties.kcInputClass!}"
                           name="username" onkeypress="return event.keyCode !== 32"
                           value="${(login.username!'')}" type="text" autofocus autocomplete="off"
                           placeholder="账号"
                           onkeyup="clearErrorMessage(event)"
                           onkeydown="enterLogin(event)"
                           aria-invalid="<#if messagesPerField.existsError('username','password')>true</#if>"
                    />
                  </div>

                  <span id="username-input-error" class="${properties.kcInputErrorMessageClass!}"
                        style="color: red; display:block; line-height: 1.5rem; height: 1.5rem"
                        aria-live="polite">
                            </span>
                </div>

                <div class="${properties.kcFormGroupClass!}">

                  <div id="password-wrapper"
                       style="<#if messagesPerField.existsError('password')>border-color:red<#else>border-color:transparent</#if>">
                    <input tabindex="2" id="password" class="${properties.kcInputClass!}"
                           name="password" onkeypress="return event.keyCode !== 32"
                           type="password" autocomplete="off" placeholder="密码"
                           onkeyup="clearErrorMessage(event)"
                           onkeydown="enterLogin(event)"
                           aria-invalid="<#if messagesPerField.existsError('username','password')>true</#if>"
                    />
                    <img id="password-eye" src="${url.resourcesPath}/img/no_eye.png"
                         onclick="togglePassword()" width="14" height="14"/>
                  </div>

                  <span id="password-input-error" class="${properties.kcInputErrorMessageClass!}"
                        style="color: red; display:block; line-height: 1.5rem; height: 1.5rem"
                        aria-live="polite">
                            </span>
                </div>

                <div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
                  <div class="${properties.kcFormOptionsWrapperClass!}"
                       style="text-align: right; margin: 12px 0 32px;"><span><a tabindex="5"
                                                                                onclick="onResetClicked()"
                                                                                style="color: white; text-decoration: none;"
                                                                                href="javascript:void(0)">忘记密码，申请重置</a></span>
                  </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormGroupClass!}">
                  <input type="hidden" id="id-hidden-input" name="credentialId"
                         <#if auth.selectedCredential?has_content>value="${auth.selectedCredential}"</#if>/>
                  <input tabindex="4"
                         style="cursor: pointer"
                         class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                         name="login" id="kc-login" type="button" onclick="customLogin()"
                         value="${msg("doLogIn")}"/>
                </div>

                <div class="${properties.kcFormGroupClass!}"
                     style=" margin-top: 24px; text-align: center">
                            <span style="color: white">还没有账号，点击<a tabindex="5"
                                                                         style="color: #1354eb; text-decoration: none;"
                                                                         href="javascript:window.location.href='${client.baseUrl!""}#/register'">申请</a>
                            </span>
                </div>
              </form>
            </#if>
        </div>
      </div>
    </#if>
</@layout.registrationLayout>

<script type="text/javascript">
  function checkRetries() {
    let baseUrl = '${client.baseUrl!""}api/v1';
    if (baseUrl.startsWith('http://localhost')) {
      baseUrl = 'http://localhost:8081/api/v1';
    }
    const form = document.getElementById('kc-form-login');
    const username = form.username.value || '';
    if (username.length === 0) {
      return;
    }
    const password = form.password.value || '';
    fetch(baseUrl + '/user?username=' + username, {method: 'head'}).then(resp => {
      const retries = +resp.headers.get('x-amz-meta-retries');
      if (retries > 0 && retries < 5) {
        setErrorMessage('密码错误，还有' + (5 - retries) + '次机会，机会次数为0时账号将被锁定，需联系管理员解锁', 'password')
        document.getElementById("password").focus()
      } else if (retries === 5) {
        setErrorMessage('账号已停用，请联系管理员了解详情！', 'username');
      }
    })
  }

  checkRetries();
</script>

<script type="text/javascript">

  async function customLogin() {
    const form = document.getElementById('kc-form-login');
    const username = form.username.value || '';
    const password = form.password.value || '';
    if (username.length === 0) {
      setErrorMessage('账号不可为空！', 'username');
      setErrorMessage('请输入密码！', 'password');
      return;
    } else if (password.length === 0) {
      setErrorMessage('请输入密码！', 'password');
      return;
    }
    let baseUrl = '${client.baseUrl!""}api/v1';
    if (baseUrl.startsWith('http://localhost')) {
      baseUrl = 'http://localhost:8081/api/v1';
    }
    const response = await fetch(baseUrl + '/user?username=' + username, {method: 'head'});
    if (response.status === 404) {
      setErrorMessage('账号不存在，请检查后重新输入', 'username');
      return;
    }
    const status = response.headers.get('x-amz-meta-status');
    if (status === 'normal' || status === 'pending' || status === 'rejected') {
      clearErrorMessage();
      form.submit();
    } else if (status === 'reset-password') {
      setErrorMessage('账号已锁定，请联系管理员解锁！', 'username');
    } else if (status === 'disable') {
      setErrorMessage('账号已停用，请联系管理员了解详情！', 'username');
    }
  }

  function enterLogin(event) {
    if (event.keyCode === 13) {
      customLogin()
    }
  }

  function clearErrorMessage(event) {
    if (event && event.keyCode === 13) {
      return;
    }
    const userInput = document.getElementById('username');
    const passInput = document.getElementById('password');
    const userError = document.getElementById('username-input-error');
    const passError = document.getElementById('password-input-error');
    userError.innerHTML = passError.innerHTML = '&ensp;';
    userInput.style.borderColor = passInput.style.borderColor = 'transparent';
  }

  function setErrorMessage(text, target) {
    const inputField = document.getElementById(target + '-wrapper');
    const inputError = document.getElementById(target + '-input-error');
    inputError.innerText = text;
    inputField.style.borderColor = 'red';
  }

  function togglePassword() {
    const passInput = document.getElementById('password');
    const passEye = document.getElementById('password-eye');
    if (passEye.getAttribute('src').endsWith('no_eye.png')) {
      passEye.setAttribute('src', '${url.resourcesPath}/img/eye.png');
      passInput.setAttribute('type', 'text');
    } else {
      passEye.setAttribute('src', '${url.resourcesPath}/img/no_eye.png');
      passInput.setAttribute('type', 'password');
    }
  }

  async function onResetClicked() {
    const form = document.getElementById('kc-form-login');
    const username = form.username.value || '';
    if (username.length === 0) {
      setErrorMessage('账号不可为空！', 'username');
      event.preventDefault();
      return;
    } else if (username === 'admin') {
      setErrorMessage('不能重置管理员账号的密码！', 'username');
      event.preventDefault();
      return;
    } else if (username.startsWith('reserved_')) {
      setErrorMessage('不能重置预留账号的密码！', 'username');
      event.preventDefault();
      return;
    }
    let baseUrl = '${client.baseUrl!""}api/v1';
    if (baseUrl.startsWith('http://localhost')) {
      baseUrl = 'http://localhost:8081/api/v1';
    }
    const resp = await fetch(baseUrl + '/user?username=' + username, {method: 'head'});
    if (resp.status === 404) {
      setErrorMessage('账号不存在，请检查后重新输入', 'username');
      return;
    }
    const status = resp.headers.get('x-amz-meta-status');
    if (status === 'reset-password') {
      setErrorMessage('账号已锁定，请联系管理员解锁！', 'username');
      return;
    } else if (status === 'disable') {
      setErrorMessage('账号已停用，请联系管理员了解详情！', 'username');
      return;
    } else if (status === 'pending') {
      setErrorMessage('账号审核中，无法重置密码！', 'username');
      return;
    } else if (status === 'rejected') {
      setErrorMessage('账号已驳回，无法重置密码！', 'username');
      return;
    }
    const resetUrl = baseUrl + '/reset-password/' + username;
    await fetch(resetUrl, {method: 'post'});
    if (!document.getElementById("message")) {
      const messageElement = createElementFromHTML(`<div id="message" role="alert" class="el-message animate__animated animate__fadeInDown el-message--success"
     style="top: 20px; z-index: 2025; width: 80px; height: 50px;">
  <span class="el-message__icon el-icon-success">&check;</span>
  <p class="el-message__content">重置密码申请成功</p><!----></div>`);
      document.body.appendChild(messageElement);
      const t = setTimeout(() => {
        messageElement.classList.remove('animate__fadeInDown');
        messageElement.classList.add('animate__fadeOutUp');
        const t2 = setTimeout(() => {
          document.body.removeChild(messageElement);
          clearTimeout(t2)
        }, 2000);
        clearTimeout(t);
      }, 2000);
    }
  }

  function createElementFromHTML(htmlString) {
    var div = document.createElement('div');
    div.innerHTML = htmlString.trim();
    // Change this to div.childNodes to support multiple top-level nodes.
    return div.firstChild;
  }
</script>

<style>

  .el-message {
    position: fixed;
    top: 20px;
    left: calc(50% - 190px);
    min-width: 380px;
    height: 80px;
    display: flex;
    align-items: center;
    border-radius: 6px;
    padding: 20px;
  }

  .el-message .el-icon-success {
    color: #e1f3d8;
    background-color: rgb(103, 194, 58);
    border-radius: 50%;
    display: block;
    width: 20px;
    height: 20px;
    line-height: 20px;
    text-align: center;
    font-size: 12px;
  }

  .el-message--success {
    background-color: #f0f9eb;
    border-color: #e1f3d8
  }

  .el-message--success .el-message__content {
    color: #67c23a
  }

  .el-message__icon {
    margin-right: 10px
  }
</style>

