
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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

        //添加没有道路站点的地图数据
        ajax("roadline_findAllRoadlineByJson.action",{},"post",function(data){
            getRoadLineListMapWithOutStations(map,data);});

        var markers=new Array(); //用来存储标注信息
        var points=new Array(); //用来存储点集信息
        var startstationLabel;
        var endstationLabel;
        var InfoLabe;
        var curve;    //弧线信息
        var markerClusterer;    //点类聚合

        var pointLabel = new BMap.Point(113.276681,22.65264); //用来显示信息

			$("input[id=startaddress]").validatebox({
				required : true,
				missingMessage:"请输入一个起始地址"
			});  
			
			$("input[id=endaddress]").validatebox({
				required : true,
				missingMessage:"请输入一个结束地址"
			});  

            $("#clear").click(function(){

                map.removeOverlay(curve);
                for(var index=0;index<markers.length;index++){
                    var marker=markers[index];
                    console.log(marker);
                    map.removeOverlay(marker);
                }
                markers=new Array();
                map.removeOverlay(startstationLabel);
                map.removeOverlay(InfoLabe);
                map.removeOverlay(endstationLabel);
                map.removeOverlay(markerClusterer);
            });

			$("#search").click(function(){

                //首先清除地图上之前查询到的覆盖物,并且重新添加覆盖物
                map.removeOverlay(curve);
                for(var index=0;index<markers.length;index++){
                    var marker=markers[index];
                    console.log(marker);
                    map.removeOverlay(marker);
                }
                markers=new Array();
                map.removeOverlay(startstationLabel);
                map.removeOverlay(InfoLabe);
                map.removeOverlay(endstationLabel);
                map.removeOverlay(markerClusterer);

				var startaddress=$("#startaddress").val();
				var endaddress=$("#endaddress").val();
                var type=$('#state').combobox('getValue');
				ajax("roadsearch.action",{startstation:startaddress,endstation:endaddress,type:type},"post",onsuccess);
			});


			function onsuccess(data){

                markers=new Array(); //用来存储标注信息
                points=new Array(); //用来存储点集信息

				if(data.status=="success"){
                    console.log(data);
					var str="起点：";
                    var contents=new Array();//标注内容数组

					for(var a=0;a<data.roadstations.length;a++){
                        var roadstation=data.roadstations[a];
                        var point=new BMap.Point(roadstation.longitude,roadstation.latitude);
                        points.push(point);
                        contents.push(roadstation.name+' 备注:'+roadstation.demo+' 等待时间:'+roadstation.staytime+'分钟');
                        if(a==0){
                            startstationLabel= addlabelonMap(map,point,"起点","red"); //将label标签显示到地图上
                            str+=roadstation.name+"经过";
                            continue;
                        }
                        if(a==data.roadstations.length-1){
                            endstationLabel= addlabelonMap(map,point,"终点","red"); //将label标签显示到地图上
                            str+="到终点："+roadstation.name;
                            continue;
                        }
                        str+=roadstation.name+"经过";
                }
					var length = data.changeStation.length;
					var html = data.html;
                    document.getElementById('message').innerHTML = html;
                    // var changestation=data.changeLine[0];
					// str += "换乘点为：" + changestation.name;
                    //调用库函数
                    addMarksMap(map,points,markers,contents);
                    markers[0].setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                    markers[data.roadstations.length-1].setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画

                    if(data.type==0){
                        str+="最短路径得出,共耗时"+data.weight+"分钟";
                    }else if(data.type==1){
                        str+="最短路径得出,共"+data.weight+"米";
                    }

                    // showInfoMessage(str);
                    swal({
                        title: "查询成功",
                        text: str,
                        icon: "success",
                        type: "success",
                        showConfirmButton: false
                    });
                   // $("#info").append("<p>"+str+"</p>");
                    InfoLabe=addlabelonMap(map,pointLabel,str,"red"); //将label标签显示到地图上

                    curve= addCurveLineMap(map,points,"green",3,0.5); //添加道路弧线

                    markerClusterer=markerClustererMap(map,markers);//让线路点聚合

				}else{
					alert(data.status);
				}
			}
		});
    </script>
  </head>
<body>
	<div style="left:20px;top:20px;">
		输入起始地址: <input type="text" id="startaddress" >
		输入结束地址: <input type="text" id="endaddress">
        <select class="easyui-combobox" id="state" style="width:120px;">
            <option value=0 selected="selected">时间最短优先</option>
            <option value=1>距离最短优先</option>
        </select>
		 <a id="search"  href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">搜索</a>
        <a id="clear"  href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">清空</a>
	</div>
 	<div id="container"
			style=" width: 100%; height: 100%;">
	</div>
    <div id="message"></div>
</body>
</html>
