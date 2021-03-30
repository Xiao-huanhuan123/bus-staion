<!DOCTYPE html>
<#include "../public/head.ftl">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
    </style>
<#--    <script src="//mapopen.bj.bcebos.com/github/BMapGLLib/TrackAnimation/src/TrackAnimation.min.js"></script>-->
<#--    <script type="text/javascript" src="//api.map.baidu.com/api?type=webgl&v=1.0&ak=您的密钥"></script>-->
    <title>视角动画</title>
</head>
<body>
<#--<div id="allmap"></div>-->
输入起始地址: <input type="text" id="startaddress">
输入结束地址: <input type="text" id="endaddress">
<a id="search"  href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">搜索</a>
<div id="container"
     style="position: absolute;  width: 100%; height: 90%; overflow:hidden;">
</div>
</body>
</html>
<script type="text/javascript">
    // GL版命名空间为BMapGL
    // 按住鼠标右键，修改倾斜角和角度
    var bmap = new BMapGL.Map("container");    // 创建Map实例
    bmap.centerAndZoom(new BMapGL.Point(113.3926,22.51595), 17);  // 初始化地图,设置中心点坐标和地图级别
    bmap.enableScrollWheelZoom(true);     // 开启鼠标滚轮缩放
    var path = [{
        'lng': 113.422632,
        'lat': 22.551823
    }, {
        'lng': 113.407172,
        'lat': 22.540224
    }, {
        'lng': 113.39827,
        'lat': 22.515971
    }, {
        'lng': 113.417422,
        'lat': 22.513968
    }, {
        'lng': 113.394821,
        'lat': 22.50335
    }, {
        'lng': 113.39827,
        'lat': 22.502215
    }, {
        'lng': 113.421051,
        'lat': 22.481812
    }];
    var point = [];
    for (var i = 0; i < path.length; i++) {
        point.push(new BMapGL.Point(path[i].lng, path[i].lat));
    }
    var pl = new BMapGL.Polyline(point);
    setTimeout('start()', 3000);
    function start () {
        trackAni = new BMapGLLib.TrackAnimation(bmap, pl, {
            overallView: true,
            tilt: 30,
            duration: 20000,
            delay: 300
        });
        trackAni.start();
    }


    $(function(){
        var opts = {
            width : 300,     // 信息窗口宽度
            height: 100,     // 信息窗口高度
            title : "站台信息" , // 信息窗口标题
            enableMessage:true//设置允许信息窗发送短息
        };

        $("input[id=startaddress]").validatebox({
            required : true,
            missingMessage:"请输入一个起始地址"
        });

        $("input[id=endaddress]").validatebox({
            required : true,
            missingMessage:"请输入一个结束地址"
        });

        $("#search").click(function(){
            remove_overlay(map);
            deletePoint(map);
            var startaddress=$("#startaddress").val();
            var endaddress=$("#endaddress").val();
            // ajax("roadline_bussearch.action",{startaddress:startaddress,endaddress:endaddress},"post",onsuccess);
            //  setTimeout('start()', 3000);
        });
    });

</script>