<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<#--<!DOCTYPE html>-->
<#include "../public/head.ftl">
<html>
<head>
    <title>公交车管理系统</title>
    <!-- sweetAlert -->
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

    <script type="text/javascript">
        $(function(){
            var map = new BMap.Map("container");
            addControlMap(map,"中山市",12);

            var pointLabel = new BMap.Point(113.276681,22.65264); //用来显示信息

            $("input[id=content]").validatebox({
                required : true,
                missingMessage:"请输入一个站点或线路"
            });


            $("#clear").click(function(){


            });

            $("#search").click(function(){

                remove_overlay(map);
                deletePoint(map);
                var content=$("#content").val();
                var type=$('#state').combobox('getValue');
                ajax("roadlineSearch.action",{content:content,type:type},"post",onsuccess);
            });




            function onsuccess(data){

                // markers=new Array(); //用来存储标注信息
                // points=new Array(); //用来存储点集信息

                if(data.status=="success"){
                    switch(data.type){
                    case 0:
                        getRoadLineListByIdMap(map,data.roadline.id);
                        var roadline = data.roadline;
                        var str = roadline.name + ": ";
                        for(var a=0;a<roadline.linestations.length;a++){
                            if(a!=0&&a!=roadline.linestations.length-1){
                                str += "->";
                            }
                            str += roadline.linestations[a].roadstation.name;
                        }
                        document.getElementById('info').innerHTML = str;
                        break;
                    case 1:
                        getRoadLineListMapForRoadstation(map,data);		//将放的可行的直达路径显示到地图上
                        addRoadStationOnMap(map,data.roadstation);
                        var roadlinelist = data.rows;
                        var str = "经过" + data.roadstation.name + "站点的线路共有：" + roadlinelist.length + "条,分别是：" + '<hr>';
                        for(var a=0;a<roadlinelist.length;a++){
                            var roadline = roadlinelist[a];
                            str += roadline.name + ": ";
                            for(var b=0;b<roadline.linestations.length;b++){
                                if(b!=0&&b!=roadline.linestations.length-1)
                                    str += "->";
                                str += roadline.linestations[b].roadstation.name;
                                if(b==roadline.linestations.length-1)
                                    str += '<hr>';
                            }
                        }
                        document.getElementById('info').innerHTML = str;
                        break;
                    }
                }else{
                    alert(data.status);
                }
            }
        });
    </script>
</head>
<body>
<div style="left:20px;top:20px;">
    输入搜索内容: <input type="text" id="content" >
    <select class="easyui-combobox" id="state" style="width:120px;">
        <option value=0 selected="selected">线路搜索</option>
        <option value=1>站点搜索</option>
    </select>
    <a id="search"  href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">搜索</a>
    <a id="clear"  href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">清空</a>
</div>
<div id="container"
     style=" width: 100%; height: 100%;">
</div>
<div id="info"></div>
</body>
</html>