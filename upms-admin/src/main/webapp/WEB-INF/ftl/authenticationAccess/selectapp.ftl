<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <title>Application selection</title>
    <link rel="stylesheet" href="${static}/insdep/easyui.css" type="text/css"/>
    <script src="${static}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${static}/js/jquery.cookie.js" type="text/javascript"></script>
    <script src="${static}/js/jquery.easyui.1.5.min.js" type="text/javascript"></script>
    <script src="${static}/js/insdep/jquery.insdep-extend.min.js" type="text/javascript"></script>
    <style type="text/css" media="all">
        .clearfix:before,
        .clearfix:after {
            content: '';
            display: table;
        }

        .clearfix:after {
            clear: both;
        }

        .clearfix {
            *zoom: 1;
        }

        body {
            font-family: Arial;
        }

        .login {
            padding-top: 5%;
            width: 500px;
            margin: 0 auto;
        }
        .top {
            position: relative;
            width: 100%;
            text-align: center;
        }

        .copyright {
            margin-top: 15%;
            text-align: center;
            font-size: 12px;
            color: #999;
            padding-bottom: 20px;
        }
        .login-info {
            padding: 10px 10px 10px 10px;
            font-size: 14px;
        }
        .wms-linkbutton {
            padding: 0 0 0 10px;
        }
        .wms-linkbutton:hover{
           color: red;
        }
        ul,li{
            list-style: none;
        }
        .login-cnt {
            width: 80%;
            min-width: 640px;
            margin: 5% auto 0;
        }
        .login-cnt li{
            width: 30%;
            float:left;
            margin-bottom: 20px;
            margin-right: 20px;
        }

        .wms-linkbutton2 {
            display:block;
            padding: 15px 0;
            border: 1px solid #666;
            text-align: center;
            font-size: 18px;
            text-decoration: none;
            color: #666;
        }

        .wms-linkbutton2:hover {
            border: 1px solid #000;
            color: #000;
        }
        .wms-linkbutton2 span{
            text-transform: uppercase;
        }
    </style>
</head>
<body>
<body>
    <div class="login-info">
        <span style="float: right">
            Welcome, ${authUser.userName} <a class="wms-linkbutton" href="${ctx}/logout?appKey=upms&token=${token}">Sign out</a>
        </span>
        Current location: Application selection
    </div>
    <div class="login">
        <div class="top">
            <img src="${static}/style/images/logo.png" alt="">
        </div>
    </div>
    <div class="login-cnt">
        <ul class="clearfix">
            <#if applicationList?exists>
            <#list applicationList as child>
            <li>
                <a class="wms-linkbutton2" href="${child.domainName}" >${child.appName}</a>
            </li>
            </#list>
            </#if>
        </ul>
    </div>
    <div class="copyright">&copy; 2005-${.now?string("yyyy")} Jollychic Copyright, All Rights Reserved.</div>
<body>
</html>