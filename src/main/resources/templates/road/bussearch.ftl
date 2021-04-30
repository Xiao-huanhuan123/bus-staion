<!DOCTYPE html>
<#--<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">-->
<#include "../public/head.ftl">
<html>
  <head>
    <title>公交车管理系统</title>
    <script type="text/javascript">
		// var map = new BMapGL.Map("container");
		// var pl;
		// map.centerAndZoom(new BMapGL.Point(113.3926,22.51595), 17);
		// map.enableScrollWheelZoom(true);
    $(function(){
		var map = new BMap.Map("container");
		addControlMap(map,"中山市",12);
		//  map.centerAndZoom(new BMapGL.Point(113.3926,22.51595), 17);
		//  map.enableScrollWheelZoom(true);
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
				ajax("roadline_bussearch.action",{startaddress:startaddress,endaddress:endaddress},"post",onsuccess);
			});
			
			function onsuccess(data){
			 	alert(data.result);
				     	switch(data.type){
					     	case 1:
								getRoadLineListMapNew(map,data);		//将放的可行的直达路径显示到地图上
								var point = new BMap.Point(data.startStation.longitude,data.startStation.latitude);
								addlabelonMap(map,point,"起点","green"); //将label标签显示到地图上
								var point = new BMap.Point(data.endStation.longitude,data.endStation.latitude);
								addlabelonMap(map,point,"终点","green"); //将label标签显示到地图上
								var point = new BMap.Point(113.3926,22.51595);
								addlabelonMap(map,point,data.label,"red"); //将label标签显示到地图上

								// var map = new BMapGL.Map("allmap");
								// map.centerAndZoom(new BMapGL.Point(113.3926,22.51595), 17);
								// map.enableScrollWheelZoom(true);
								// var point = new BMapGL.Point(113.3926,22.51595);
					     		// getRoadLineListMap(map,data);		//将放的可行的直达路径显示到地图上

								// map.clearOverlays();
								// var point =[];
								// for(var i=0;i<data.rows.length;i++){
								// 	var road=data.rows[i];
								// 	if(road.linestations[0]!=null)
								// 	{
								// 		point.push(new BMapGL.Point(road.linestations[0].roadstation.longitude, road.linestations[0].roadstation.latitude));
								// 	}
								// }
								// var pl = new BMapGL.Polyline(point);
								// setTimeout('start(map,pl)',3000);
					     		break;
					     	case 2:
					     		getRoadLineListMapNew(map,data);		//将放的可行的直达路径显示到地图上
								var point = new BMap.Point(data.startStation.longitude,data.startStation.latitude);
								addlabelonMap(map,point,"起点","green"); //将label标签显示到地图上
								var point = new BMap.Point(data.endStation.longitude,data.endStation.latitude);
								addlabelonMap(map,point,"终点","green"); //将label标签显示到地图上
					     		var point = new BMap.Point(113.3926,22.51595);
					     		addlabelonMap(map,point,data.label,"red"); //将label标签显示到地图上
					     		
					     		for(var a=0;a<data.changestation.length;a++){
					     			var point = new BMap.Point(data.changestation[a].longitude,data.changestation[a].latitude);
					     			addlabelonMap(map,point,"换乘点","blue"); //将label标签显示到地图上
					     		}
					     		break;
				     	}
			};


			function start (map,pl) {
			trackAni = new BMapGLLib.TrackAnimation(map, pl, {
				overallView: true,
				tilt: 30,
				duration: 20000,
				delay: 300
			});
			trackAni.start();
		};
			
		});

    </script>
  </head>
<body>
	输入起始地址: <input type="text" id="startaddress">
	输入结束地址: <input type="text" id="endaddress">    
	 <a id="search"  href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">搜索</a>  
 	<div id="container"
			style="position: absolute;  width: 100%; height: 90%; overflow:hidden;">
	</div>
</body>
</html>
