<!doctype html>
<html>
<head>
	<title>Change password</title>
	<meta name="decorator" content="default"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <link rel="stylesheet" href="${static}/insdep/easyui.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/style/css/main-form.css" type="text/css"/>
    <script src="${static}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${static}/js/jquery.md5.js" type="text/javascript"></script>
    <script src="${static}/js/jquery.cookie.js" type="text/javascript"></script>
    <script src="${static}/js/jquery.validate.min.js" type="text/javascript"></script>
    <style type="text/css" media="all">
        .copyright {
            margin-top: 10px;
            text-align: center;
            font-size: 12px;
            color: #999;
            padding-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="login-nav">
Current location: Change password
</div>
<div class="login-cnt">
    <div class="login-panel">
        <div class="login-tab">
            <form id="inputForm" action="${ctx}/authenticationAccess/password" method="post">
                <table>
                    <tbody>
                    <tr>
                        <td class="input-title">Username:</td>
                    </tr>
                    <tr>
                        <td class="login-box">
                            <input autocomplete="off" id="username" name="username" type="text" value="" class="login-input required"/>
                            <font color="red">*</font>
                            <label id="username-error" for="username" class="error-msg">${username_wrong!}</label>
                        </td>
                    </tr>
                    <tr>
                    <tr>
                        <td class="input-title">Old password:</td>
                    </tr>
                    <tr>
                        <td class="login-box">
                            <input autocomplete="off" id="oldPassword" name="password" type="password" value="" maxlength="20" minlength="6" class="login-input required"/>
							<font color="red">*</font>
                            <label id="oldPassword-error" for="oldPassword" class="error-msg">${password_wrong!}</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="input-title">New password:</td>
                    </tr>
                    <tr>
                        <td class="login-box">
                            <input autocomplete="off" id="newPassword" name="newPassword" type="password" value="" maxlength="20" minlength="6" class="login-input required"/>
                            <font color="red">*</font>
                            <label id="newPassword-error" for="newPassword" class="error-msg"></label>
                        </td>
                    </tr>
                    <tr>
                        <td class="input-title">Confirm password:</td>
                    </tr>
                    <tr>
                        <td class="login-box">
                            <input autocomplete="off" id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="20" minlength="6" class="login-input required" equalTo="#newPassword"/>
                            <font color="red">*</font>
                            <label id="confirmNewPassword-error" for="confirmNewPassword" class="error-msg"></label>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                    </tr>
                    <tr>
                        <td>
                            <input id="btnSubmit" class="login-button" type="submit" value="Submit"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <div class="copyright">&copy; 2005-${.now?string("yyyy")} Jollychic Copyright, All Rights Reserved.</div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $("#oldPassword").focus();
        $("#inputForm").validate({
            rules: {
            },
            messages: {
                confirmNewPassword: {
                    equalTo: "The password confirmed isn't equal to your new password!",
					required: "Please confirm your new password.",
					minlength:"Your password must between 6-20 characters.",
                    maxlength:"Your password must between 6-20 characters."
				},
                newPassword: {
                    required: "Please type your new password.",
                    minlength:"Your password must between 6-20 characters.",
                    maxlength:"Your password must between 6-20 characters."
                },
                password: {
                    required: "Please type the password.",
                    minlength:"Your password must between 6-20 characters.",
                    maxlength:"Your password must between 6-20 characters."
                },
                username: {
                    required: "Please type your username."
                }
            },
            submitHandler: function(form){
                $("#oldPassword").val($.md5($("#oldPassword").val()));
                $("#newPassword").val($.md5($("#newPassword").val()));
                $("#confirmNewPassword").val($.md5($("#confirmNewPassword").val()));
                loading('loading...');
                form.submit();
            },
            errorClass:"error-msg"
        });
    });
</script>
</body>
</html>