<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Single Sign On</title>
    <script src="${static}/js/jquery.min.js" type="text/javascript"></script>
    <style type="text/css" media="all">
        body {
            font-family: Arial;
        }

        .login {
            padding-top: 5%;
            width: 500px;
            margin: 0 auto;
        }

        .login h1 {
            margin: 10px 0 0;
        }

        .top {
            position: relative;
            width: 100%;
            text-align: center;
        }

        .login-box {
            position: relative;
        }

        .login-user-icon {
            position: absolute;
            z-index: 10;
            display: inline-block;
            width: 16px;
            height: 16px;
            margin: 12px 12px 0;
            background-image: url("${static}/style/images/icons.png");
            background-repeat: no-repeat;
            background-position: 0 0;
            top: 12px;
        }

        .login-pwd-icon {
            height: 7px;
            background-position: 0 -16px;
            top: 15px;
        }

        .login-cnt {
            margin: 0 auto;
            width: 500px;
        }

        .login-panel {
            width: 500px;
            margin-top: 50px;
            background: #FFFFFF;
        }

        .input-title {
            font-size: 14px;
        }

        .login-input {
            width: 340px;
            border: 1px solid #c5c8cc;
            height: 48px;
            font-size: 15px;
            padding-left: 40px;
            background-color: transparent;
        }

        .login-input:focus {
            outline: none;
        }

        .login-tab {
            width: 390px;
            margin: 0 auto;
            padding: 20px 0;
        }

        .login-tab td {
            padding: 5px 0;
        }

        .error-msg {
            display: block;
            color: #ff0000;
            font-size: 12px;
            margin-top: 4px;
        }

        .error-focus {
            border: 1px solid #ff0000;
        }

        .fn-hide {
            display: none;
        }

        .wms-linkbutton {
            display: block;
            width: 380px;
            height: 48px;
            line-height: 48px;
            background: #c41230;
            border-bottom: 2px solid #97011a;
            text-align: center;
            font-size: 18px;
            text-decoration: none;
            color: #FFF;
        }

        .wms-linkbutton:hover {
            background: #dd2443;
        }

        .copyright {
            margin-top: 10px;
            text-align: center;
            font-size: 12px;
            color: #999;
            padding-bottom: 20px;
        }
        .forgot-pwd{
            text-align: right;
            font-size: 12px;
            padding-bottom: 10px;
        }
        .forgot-pwd a{
            color: #666;
        }

        .login-info {
            padding: 10px 10px 10px 10px;
            font-size: 14px;
        }
        .modifyPassword-linkbutton {
            padding: 0 0 0 10px;
        }
        .modifyPassword-linkbutton:hover{
            color: red;
        }
    </style>

</head>
<body>
<div class="login-info">
        <span style="float: right">
            <a href="${ctx}/authenticationAccess/userModifyPwd" class="modifyPassword-linkbutton">Change password</a>
        </span>
</div>
<div class="login">
    <div class="top">
        <img src="${static}/style/images/logo.png" alt="">
        <h1>Single Sign On</h1>
    </div>
</div>
<div class="login-cnt">
    <div class="login-panel">
        <div class="login-tab">
                <form name="login_form" id="login_form" action="${ctx}/auth/ssoLogin" method="post">
                    <input type="hidden" name="appKey" value="${appKey}"/>
                <table>
                    <tbody>
                    <tr>
                        <td class="input-title">Username</td>
                    </tr>
                    <tr>
                        <td class="login-box">
                            <span class="login-user-icon"></span>
                            <input type="text" class="login-input J-login-user" name="userName"  placeholder="Please type your username"  value="<#if userName??>${userName!}</#if>"/>
                            <span class="error-msg fn-hide">Please type your username.</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="input-title">Password</td>
                    </tr>
                    <tr>
                        <td class="login-box">
                            <span class="login-user-icon login-pwd-icon"></span>
                            <input type="password" id="password" class="login-input J-login-pwd" autocomplete="off" name="password" value="" placeholder="Please type the password"/>
                            <span class="error-msg fn-hide">Your password must between 6-20 characters.</span>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <#if flash_message??>
                                <span class="error-msg J-error-msg">${flash_message}</span>
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <td>
                           <!-- <p class="forgot-pwd"><a href="#">Forgot your password?</a></p> -->
                            <a href="javascript:void(0)" class="wms-linkbutton" onclick="submitForm()">Sign in</a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <div class="copyright">&copy; 2005-${.now?string("yyyy")} Jollychic Copyright, All Rights Reserved.</div>
</div>


<script>
    function submitForm() {
        var $user = $(".J-login-user");
        var $pwd = $(".J-login-pwd");
        var user = $.trim($user.val());
        var pwd = $.trim($pwd.val());
        var $msg = $('.J-error-msg');
        if (!user) {
            $user.addClass('error-focus').siblings('.error-msg').removeClass('fn-hide');
            $msg.addClass('fn-hide');
            return false;
        }
        if (pwd.length < 6 || pwd.length > 20) {
            $pwd.addClass('error-focus').siblings('.error-msg').removeClass('fn-hide');
            $msg.addClass('fn-hide');
            return false;
        }
        $user.removeClass('error-focus').siblings('.error-msg').addClass('fn-hide');
        $pwd.removeClass('error-focus').siblings('.error-msg').addClass('fn-hide');
        $msg.removeClass('fn-hide');
        $('#login_form').submit();
    }

    document.onkeydown = function (event) {
        e = event ? event : (window.event ? window.event : null);
        if (e.keyCode == 13) {
            submitForm();
        }
    }
</script>
</body>
</html>