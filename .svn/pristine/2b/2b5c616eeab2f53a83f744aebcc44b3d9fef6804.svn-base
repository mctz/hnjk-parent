$(function() {
	$('#myflow')
			.myflow(
					$
							.extend(
									true,
									{
										basePath : "",
										restore : eval("(" +
												"{" +
													"states:{" +
														"rect4:{type:'start',text:{text:'开始'}, attr:{ x:409, y:10, width:50, height:50}, props:{text:{value:'开始'},temp1:{value:''},temp2:{value:''}}}," +
														"rect5:{type:'task',text:{text:'任务1'}, attr:{ x:386, y:116, width:100, height:50}, props:{text:{value:'任务1'},assignee:{value:''},form:{value:''},desc:{value:''}}}," +
														"rect6:{type:'fork',text:{text:'分支'}, attr:{ x:410, y:209, width:50, height:50}, props:{text:{value:'分支'},temp1:{value:''},temp2:{value:''}}}," +
														"rect7:{type:'task',text:{text:'任务2'}, attr:{ x:192, y:317, width:100, height:50}, props:{text:{value:'任务2'},assignee:{value:''},form:{value:''},desc:{value:''}}}," +
														"rect8:{type:'task',text:{text:'任务3'}, attr:{ x:385, y:317, width:100, height:50}, props:{text:{value:'任务3'},assignee:{value:''},form:{value:''},desc:{value:''}}}," +
														"rect9:{type:'task',text:{text:'任务4'}, attr:{ x:556, y:320, width:100, height:50}, props:{text:{value:'任务4'},assignee:{value:''},form:{value:''},desc:{value:''}}}," +
														"rect10:{type:'join',text:{text:'合并'}, attr:{ x:410, y:416, width:50, height:50}, props:{text:{value:'合并'},temp1:{value:''},temp2:{value:''}}}," +
														"rect11:{type:'end',text:{text:'结束'}, attr:{ x:409, y:633, width:50, height:50}, props:{text:{value:'结束'},temp1:{value:''},temp2:{value:''}}}," +
														"rect12:{type:'task',text:{text:'任务5'}, attr:{ x:384, y:528, width:100, height:50}, props:{text:{value:'任务5'},assignee:{value:''},form:{value:''},desc:{value:''}}}" +
														"}," +
													"paths:{" +
														"path13:{from:'rect4',to:'rect5', dots:[],text:{text:'TO 任务1'},textPos:{x:37,y:-4}, props:{text:{value:''}}}," +
														"path14:{from:'rect5',to:'rect6', dots:[],text:{text:'TO 分支'},textPos:{x:56,y:-1}, props:{text:{value:''}}}," +
														"path15:{from:'rect6',to:'rect8', dots:[],text:{text:'TO 任务3'},textPos:{x:24,y:-5}, props:{text:{value:''}}}," +
														"path16:{from:'rect8',to:'rect10', dots:[],text:{text:'TO 合并'},textPos:{x:41,y:8}, props:{text:{value:''}}}," +
														"path17:{from:'rect10',to:'rect12', dots:[],text:{text:'TO 任务5'},textPos:{x:36,y:-5}, props:{text:{value:''}}}," +
														"path18:{from:'rect12',to:'rect11', dots:[],text:{text:'TO 结束'},textPos:{x:32,y:0}, props:{text:{value:''}}}," +
														"path19:{from:'rect6',to:'rect7', dots:[{x:244,y:232}],text:{text:'TO 任务2'},textPos:{x:0,y:-10}, props:{text:{value:'TO 任务2'}}}," +
														"path20:{from:'rect7',to:'rect10', dots:[{x:242,y:435}],text:{text:'TO 合并'},textPos:{x:-3,y:17}, props:{text:{value:'TO 合并'}}}," +
														"path21:{from:'rect6',to:'rect9', dots:[{x:607,y:234}],text:{text:'TO 任务4'},textPos:{x:0,y:-10}, props:{text:{value:'TO 任务4'}}}," +
														"path22:{from:'rect9',to:'rect10', dots:[{x:607,y:439}],text:{text:'TO 合并'},textPos:{x:-8,y:16}, props:{text:{value:'TO 合并'}}}" +
														"}," +
													"props:{props:{name:{value:'新建流程'},key:{value:''},desc:{value:''}}}" +
												"})"),
										editable : false

									}, {
										"activeRects" : {
											"rects" : [ {
												"paths" : [],
												"name" : "任务3"
											}, {
												"paths" : [],
												"name" : "任务4"
											}, {
												"paths" : [],
												"name" : "任务2"
											} ]
										},
										"historyRects" : {
											"rects" : [
													{
														"paths" : [ "TO 任务1" ],
														"name" : "开始"
													},
													{
														"paths" : [ "TO 分支" ],
														"name" : "任务1"
													},
													{
														"paths" : [ "TO 任务3",
																"TO 任务4",
																"TO 任务2" ],
														"name" : "分支"
													} ]
										}
									}));

});