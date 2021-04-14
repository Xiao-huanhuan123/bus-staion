<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>留言</title>

    <!-- Bootstrap -->


    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>

    <![endif]-->

    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

</head>
<body>
<div class="container">

    <div class="jumbotron text-center">
        <h1>留言板</h1>
        <p>所有留言</p>
    </div>
    <div id="wordArea"></div>
    <div>
        <h2>留言</h2>
        <input type="text" class="form-control s-margin-1" placeholder="标题" id="title">
        <hr>
        <textarea class="form-control s-margin-1" placeholder="内容" rows="3" id="content"></textarea>
        <hr>
        <button class="btn btn-primary s-margin-1" type="submit" onclick="leaveWord()">提交</button>
    </div>
</div>

<#--<!-- jQuery (necessary for Bootstrap's JavaScript plugins) &ndash;&gt;-->
<script src="js/jquery.min.js"></script>

<!-- sweetAlert -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script type="application/javascript">
   /* if(getCookie("user") == null)
        window.location.href= address+"index.html";*/

    initWords();
    function initWords() {
       /* var users = JSON.parse(getCookie("user"));
        var user = {};
        user.userId = users.userId;*/
        var response;
        $.ajax({
            async : false,
            type : 'POST',
            url : "/getWords",
            data : {},
            dataType : 'json',
            success : function(result) {
                response = result;
            },
            error : function(result) {
                alert('服务器异常');
            }
        });
        if(response.status == "0"){
            var words = response.content;
            words = eval("("+words+")");
            var html = "";
            for(var i=0;i<words.length;i++){
                html+='<h2 class="s-margin-2">'+words[i].title+'</h2>'+
                    '        <hr>'+
                    '        <h5>'+words[i].content+'</h5>';
            }
            document.getElementById("wordArea").innerHTML = html;
        }
        else{
            alert(response.content);
        }
    }

    function leaveWord() {
        swal("留言成功!", "You clicked the button!", "success");
        // var users = JSON.parse(getCookie("user"));
        var word = {};
        word.title = document.getElementById("title").value;
        word.content = document.getElementById("content").value;
        // word.userId = users.userId;
        var response;
        $.ajax({
            async : false,
            type : 'POST',
            url : "/leaveWord",
            data : word,
            dataType : 'json',
            success : function(result) {
                response = result;
            },
            error : function(result) {
                alert('服务器异常');
            }
        });
        if(response.status == "0"){
            document.getElementById("wordArea").innerHTML += '<h2 class="s-margin-2">'+word.title+'</h2>'+
                '        <hr>'+
                '        <h5>'+word.content+'</h5>';
        }
        else{
            alert(response.content);
        }
    }
</script>
</body>
</html>