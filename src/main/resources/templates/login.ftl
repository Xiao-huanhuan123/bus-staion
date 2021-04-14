<!DOCTYPE html>
<#include "public/head.ftl">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>管理员登录</title>

    <link href="asserts/css/bootstrap.min.css"  rel="stylesheet">
    <!--		<link href="asserts/css/bootstrap.min.css" th:href="@{/webjars/bootstrap/4.5.3/css/bootstrap.css}" rel="stylesheet">-->
    <!-- Custom styles for this template -->
    <link href="asserts/css/signin.css"  rel="stylesheet">

    <script type="text/javascript" href="asserts/js/bootstrap.min.js"></script>

    <!-- sweetAlert -->
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

    <!-- animate 动画效果css-->
    <script src="https://unpkg.com/animate.css@3.5.2/animate.min.css"></script>


    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
</head>
<body class="gray-bg">

<div class="middle-box text-center loginscreen  animated fadeInDown">
    <div>
        <div>

            <h1 class="logo-name">城市交通线路查询系统</h1>

        </div>
<#--        <h3>欢迎使用城市交通线路查询系统</h3>-->

        <div class="form-group">
            <input type="text" id="userAccount" class="form-control" placeholder="用户名" required="">
        </div>
        <div class="form-group">
            <input type="password" id="userPassword" class="form-control" placeholder="密码" required="">
        </div>
        <button class="btn btn-primary block full-width m-b" onclick="login()">登 录</button>

        <p class="text-muted text-center"><a href="login.ftl#">
                <small>忘记密码了？</small>
            </a> | <a href="register.html">注册一个新账号</a>
        </p>

    </div>
</div>

<script>

    // 登录
    function login() {
        var userAccount = $("#userAccount").val();
        var userPassword = $("#userPassword").val();
        if (userAccount == "") {
            return false;
        }
        if (userPassword == "") {
            return false;
        }
        ajax("/v1/login",{userAccount:userAccount,userPassword:userPassword},"post",onsuccess);
        function onsuccess(data) {
            if (data.success) {
                            // 成功
                swal({
                    title: "登录成功",
                    text: "用户名和密码输入正确",
                    timer: 2000,
                    icon: "success",
                    type: "success",
                    showConfirmButton: false
                });
            } else {
                // 失败
                swal({
                    title: "登录失败",
                    timer: 2000,
                    type: "error",
                    showConfirmButton: false
                });
            }
        }

        // 登录
        // $.ajax({
        //     url: "/v1/login",
        //     type: "post",
        //     data: {
        //         userAccount: userAccount,
        //         userPassword: userPassword
        //     },
        //     success: function (data) {
        //         if (data.status=="success") {
        //             // 成功
        //             swal({
        //                 title: "登录成功",
        //                 timer: 1000,
        //                 type: "success",
        //                 showConfirmButton: false
        //             });
        //         } else {
        //             // 失败
        //             swal({
        //                 title: data.msg,
        //                 timer: 1000,
        //                 type: "error",
        //                 showConfirmButton: false
        //             });
        //         }
        //     }
        // });
    }

</script>

</body>
</html>