<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<body>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent">
				<table class="form">
					<tr>
						<td width="50%">当前该点已注册人数</td>
						<td width="50%" id="registeredCount">${registeredCount }</td>
					</tr>
				</table>
				<table class="form">
					<tr>
						<td colspan="2" width="50%">录取库信息</td>
						<td colspan="2" width="50%">身份证信息</td>
					</tr>
					<tr>
						<td width="13%">录取库相片</td>
						<td width="37%" id="enrollPhotoPath"><img
							id="imgEnrollPhotoPath"
							src="${baseUrl}/themes/default/images/img_preview.png" width="90"
							height="126" /></td>
						<td width="12%"></td>
						<td width="38%" id="cardPhotoPath"><img id="imgCardPhotoPath"
							src="${baseUrl}/themes/default/images/img_preview.png" width="90"
							height="126" /></td>
					</tr>
					<tr>
						<td>姓名</td>
						<td id="enrollName"></td>
						<td>姓名</td>
						<td id="cardName"></td>
					</tr>
					<tr>
						<td>性别</td>
						<td id="enrollSex"></td>
						<td>性别</td>
						<td id="cardSex"></td>
					</tr>

					<tr>
						<td>民族</td>
						<td id="enrollNation"></td>
						<td>民族</td>
						<td id="cardNation"></td>
					</tr>
					<tr>
						<td>出生日期</td>
						<td id="enrollBornDay"></td>
						<td>出生日期</td>
						<td id="cardBornDay"></td>
					</tr>
					<tr>
						<td>地址</td>
						<td id="enrollAddress"></td>
						<td>地址</td>
						<td id="cardAddress"></td>
					</tr>
					<tr>
						<td>身份证号</td>
						<td id="enrollCertNo"></td>
						<td>身份证号</td>
						<td id="cardCertNo"></td>
					</tr>
					<tr>
						<td>注册状态</td>
						<td id="enrollStatus"></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td>分配教学点</td>
						<td id="enrollUnitname"></td>
						<td>报到教学点</td>
						<td id="reportUnitName"></td>
					</tr>
					<tr>
						<td>层次</td>
						<td id="enrollClassic"></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td>学习形式</td>
						<td id="enrollTeachingType"></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td>录取专业</td>
						<td id="enrollMajor"></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td>注册流水号</td>
						<td id="registorSerialNO"></td>
						<td></td>
						<td></td>
					</tr>
				</table>
			</div>
			<div class="formBar">
				<ul>
					<!-- 			广大不使用自动注册功能 -->
					<c:if test="${ schoolCode ne '11078' && schoolCode ne '10602' }">
						<li><div class="buttonActive">
								<div class="buttonContent">
									<input id="autoregisterid" type="checkbox" name="autoregister"
										value="Y" />
									<button type="submit">自动注册</button>
								</div>
							</div></li>
					</c:if>
					<li><div class="buttonActive" id="manMadeRegisterActive">
							<div id="manMadeRegisterContent" class="buttonContent">
								<button id="manMadeRegisterid" type="submit">注册</button>
							</div>
						</div></li>
					<li><div class="buttonActive" id="DoNotRegisterActive">
							<div id="DoNotRegisterContent" class="buttonContent">
								<button id="DoNotRegisterid" type="submit">暂不注册</button>
							</div>
						</div></li>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="button" class="close" onclick="closePdialog()">关闭</button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	//全局参数，上一个身份证号，初始为0，用于判断方法调用时，是否是同一个身份证
	var preCertNum="0";
	//全局参数，当前放在读卡器上的身份证号，初始化时设置为1
	var currentCertNum ="1";
	var currentName="";
	//延迟参数，多少秒间隔重新读取
	var timeout=3000;
	//全局参数，读卡器的设备类型,默认为1	
	var devicestype="${devicestype}";
	//是否正在注册的标识，如果已经在有一个线程在执行操作，则提醒用户重复操作
	var AllowRegister='N';
	var StartReadCard='Y';
// 	$(function(){
		
		
// 	})
	//事件监听，点击关闭页面右上角X时，退出循环读卡
		$("a[class='close']").bind('click',function(){
			StartReadCard='N';
		});
		//自动注册
		$("#autoregisterid").bind('click',function(){
// 			console.info("autoregisterid:onclick  checked:"+$("#autoregisterid").attr("checked"));
			if($("#autoregisterid").attr("checked")){
				$("#manMadeRegisterid").attr("disabled","disabled");
				$("#DoNotRegisterid").attr("disabled","disabled");
				$("#manMadeRegisterContent").css("background","grey");
				$("#manMadeRegisterActive").css("background","grey");
				$("#DoNotRegisterContent").css("background","grey");
				$("#DoNotRegisterActive").css("background","grey");				
				if(AllowRegister=='Y'&&StartReadCard=='N'){
					ReadCard_register(currentCertNum,currentName);
				}
				
			}else{
				$("#manMadeRegisterid").removeAttr("disabled");
				$("#DoNotRegisterid").removeAttr("disabled");
				$("#manMadeRegisterContent").css("background","");
				$("#manMadeRegisterActive").css("background","");
				$("#DoNotRegisterContent").css("background","");
				$("#DoNotRegisterActive").css("background","");	
			}
		});
		//手动点击注册
		$("#manMadeRegisterid").bind("click",function(){
// 			console.info("manMadeRegisterid:AllowRegister"+AllowRegister);
// 			console.info("manMadeRegisterid:StartReadCard"+StartReadCard);
			if($("#autoregisterid").attr("checked")){//勾选了自动注册，不允许使用手动点击暂时不注册
				alertMsg.error("自动注册时，不允许操作暂时不注册，请去掉自动注册勾选，再使用该功能");
			}else{
				if(AllowRegister=='N'&&StartReadCard=='Y'){
					alertMsg.warn("上一个注册操作未完成，请稍后再试");
					return false;
				}else{
// 					console.info("currentCertNum"+currentCertNum);
					if(currentCertNum!="1"){
						StartReadCard='N';
						AllowRegister='N';
						ReadCard_register(currentCertNum,currentName);
					}else{
						alertMsg.warn("未读取到学生信息,请稍后再试");
					}
				}
			}
			
		});
		//手动点击暂时不注册
		$("#DoNotRegisterid").bind("click",function(){
// 			console.info("manMadeRegisterid:AllowRegister"+AllowRegister);
// 			console.info("manMadeRegisterid:StartReadCard"+StartReadCard);
			if($("#autoregisterid").attr("checked")){//勾选了自动注册，不允许使用手动点击暂时不注册
				alertMsg.error("自动注册时，不允许操作暂时不注册，请去掉自动注册勾选，再使用该功能");
			}else{//停止注册，开始读卡
				if(AllowRegister=='Y'&&StartReadCard=='N'){
					StartReadCard='Y';
					AllowRegister='N';
					if(currentCertNum!='1'){
						clearInfo();
						ReadCard_onclick();
					}else{
						alertMsg.warn("未读取到学生信息,请稍后再试");
					}
					
				}else{
				}
				
			}
			
		});
	function ReadCard_onclick(){//第一种设备 SynCardOcx1 F200  第二种设备IdrOcx1 DKQ-A16D		
// 		console.info("StartReadCard:"+StartReadCard);
		if(StartReadCard=='Y'){
			var nRet;		 	
		 	if(devicestype=="F200"){
		 		nRet = SynCardOcx1.ReadCardMsg();
		 		currentCertNum=SynCardOcx1.CardNo==""?1:SynCardOcx1.CardNo;

		 		currentName=SynCardOcx1.NameA==""?"":$.trim(SynCardOcx1.NameA);

		 	}
		 	if(devicestype=="DKQ-A16D"){
		 		nRet = IdrOcx1.ReadCard();
		 		currentCertNum=IdrOcx1.CardNo==""?1:IdrOcx1.CardNo;
		 		currentName=IdrOcx1.NameL==""?"":$.trim(IdrOcx1.NameL);
		 	}		 	
		    if(nRet==0){
// 		    	console.info("preCertNum!=currentCertNum:"+preCertNum+"   "+currentCertNum);
		    	if(preCertNum!=currentCertNum){
		    		//清除表格数据
		    		clearInfo();
		    		preCertNum=currentCertNum;
		    		//加载身份证信息
		    		DisplayStuCardInfo();
			   		var certNum = currentCertNum;
// 	 		   		var certNum ='440982199210232376';
					var url = "${baseUrl}/edu3/register/studentinfo/registering_getEnrollInfo.html?certNum="+certNum+"&recruitPlanID=${recruitPlanID}";
					$.ajax({
			   			type:'POST',
			   			url:url,
			   			dataType:"json",
			   			success:function(data){	
			   				$("#registeredCount").text(data['registeredCount']);
			   				//加载录取库信息
			   				DisplayStuEnrollInfo(data);
			   				//身份证与录取库信息对比
			   				compare4cardAndenroll();			   				
			   				AllowRegister='Y';//读卡成功并加载录取信息后，才允许进行注册操作
// 			   				console.info("AllowRegister:"+AllowRegister);
			   				StartReadCard='N';//注册或注册待时，停止读卡
				   			if($("#autoregisterid").attr("checked")){//是否勾选了自动注册
	// 			   				currentCertNum=certNum;
				   				ReadCard_register(certNum,currentName);
				   			}			   				
			   			}
					});
		    	}else{
		    		if(StartReadCard=='Y'){//是同一个身份证，继续循环读卡
		    	    	setTimeout("ReadCard_onclick()", timeout);
		    	    }
		    	}
		   	}else{//nRet !=0  未读到卡的信息  进入循环读卡 
			    if(StartReadCard=='Y'){			    	
			    	setTimeout("ReadCard_onclick()", timeout);
			    }
		   	}
		}
	}
	ReadCard_onclick();	
	function ReadCard_register(certNum,cardName){
		//占用注册，因此停止注册
		var postUrl = "${baseUrl}/edu3/register/studentinfo/registering_ByPersonalIDReader.html";
   		$.ajax({
   			type:'POST',
   			url:postUrl,
   			dataType:"json",
   			data:{certNum:certNum,cardName:cardName},
   			success:function(data){
   				StartReadCard='Y';
   				if(data['registorSerialNO']!=""){
					$("#registorSerialNO").text(data['registorSerialNO']);
				}
   				if(data['statusCode']==300){
   					alertMsg.warn(data['message']);
   					setTimeout("ReadCard_onclick()", timeout);
   				}else{//注册成功
   					alertMsg.correct(data['message']);
   					$("#enrollStatus").text("已注册");
   				    setTimeout("ReadCard_onclick()", timeout);   				 		
   				}
   			},
   			error:function(){
   				alertMsg.error("网络连接出错，请稍后再试");
   			}
   		});
	}
	
	//清除表格数据信息
	function clearInfo(){
		$("#enrollName").text("");
		$("#enrollSex").text("");
		$("#enrollNation").text("");
		$("#enrollBornDay").text("");
		$("#enrollAddress").text("");
		$("#enrollCertNo").text("");
		$("#imgEnrollPhotoPath").attr("src","${baseUrl}/themes/default/images/img_preview.png");
		$("#enrollStatus").text("");
		$("#enrollUnitname").text("");
		$("#registorSerialNO").text("");
		$("#cardName").text("");
		$("#cardSex").text("");
		$("#cardNation").text("");
		$("#cardBornDay").text("");
		$("#cardAddress").text("");
		$("#cardCertNo").text("");
		$("#reportUnitName").text("");
		$("#manMadeRegisterid").removeAttr("disabled");
		$("#manMadeRegisterContent").css("background","");
		$("#manMadeRegisterActive").css("background","");
// 		$("#imgCardPhotoPath").attr("src","${baseUrl}/themes/default/images/img_preview.png");
		
	}
	//显示学生身份证信息
	function DisplayStuCardInfo(){
		if(devicestype=="F200"){
			SynCardOcx1.SetNationType(2);
			SynCardOcx1.SetBornType(1);
			SynCardOcx1.SetSexType(1);
// 			SynCardOcx1.SetPhotoType(1);
// 			SynCardOcx1.SetPhotoName(2);
			$("#cardName").text($.trim(SynCardOcx1.NameA));			
			$("#cardSex").text(SynCardOcx1.Sex);
			$("#cardNation").text(SynCardOcx1.Nation);
			$("#cardBornDay").text(SynCardOcx1.Born);
			$("#cardAddress").text(SynCardOcx1.Address);
			$("#cardCertNo").text(SynCardOcx1.CardNo);
// 			$("#cardCertNo").text('452523197902011980');
			var Base64Photo = SynCardOcx1.Base64Photo;
			$("#imgCardPhotoPath").attr("src","data:image/Jpg;base64,"+Base64Photo);
		}
		if(devicestype=="DKQ-A16D"){
			$("#cardName").text(IdrOcx1.NameL);			
			$("#cardSex").text(IdrOcx1.SexL);
			$("#cardNation").text(IdrOcx1.NationL);
			$("#cardBornDay").text(IdrOcx1.BornL);
			$("#cardAddress").text(IdrOcx1.Address);
			$("#cardCertNo").text(IdrOcx1.CardNo);
			var Base64Photo = IdrOcx1.GetPhotoBuffer();
			$("#imgCardPhotoPath").attr("src","data:image/Jpg;base64,"+Base64Photo);
		}
	}
	//显示录取库信息
	function DisplayStuEnrollInfo(data){
		$("#enrollName").text(data['name']);
		$("#enrollSex").text(data['gender']);
		$("#enrollNation").text(data['nation']);
		$("#enrollBornDay").text(data['bornday']);
		$("#enrollAddress").text(data['address']);
		$("#enrollCertNo").text(data['certNum']);
		$("#enrollStatus").text(data['register_flag']);
		$("#enrollUnitname").text(data['unitname']);
		$("#enrollClassic").text(data['classic']);
		$("#enrollTeachingType").text(data['teachingType']);
		$("#enrollMajor").text(data['major']);
		$("#registorSerialNO").text(data['registorSerialNO']);
		$("#reportUnitName").text(data['reportUnitName']);
		if(data['recruitphotopath']!=null){
			$("#imgEnrollPhotoPath").attr("src","${rootUrl}/common/students/"+data['recruitphotopath']);
		}
	}
	//身份证与录取库信息对比
	function compare4cardAndenroll(){
		if($("#enrollName").text()!=$("#cardName").text()){			
			$("#enrollName").css({"color":"red"});
			$("#cardName").css({"color":"red"});
			$("#manMadeRegisterid").attr("disabled","disabled");
			$("#manMadeRegisterContent").css("background","grey");
			$("#manMadeRegisterActive").css("background","grey");
		}else{
			$("#enrollName").css({"color":"green"});
			$("#cardName").css({"color":"green"});
		}
		if($("#enrollSex").text()!=$("#cardSex").text()){			
			$("#enrollSex").css({"color":"red"});
			$("#cardSex").css({"color":"red"});
		}else{
			$("#enrollSex").css({"color":"green"});
			$("#cardSex").css({"color":"green"});
		}
		if($("#enrollNation").text()!=$("#cardNation").text()){			
			$("#enrollNation").css({"color":"red"});
			$("#cardNation").css({"color":"red"});
		}else{
			$("#enrollNation").css({"color":"green"});
			$("#cardNation").css({"color":"green"});
		}
		if($("#enrollBornDay").text()!=$("#cardBornDay").text()){			
			$("#enrollBornDay").css({"color":"red"});
			$("#cardBornDay").css({"color":"red"});
		}else{
			$("#enrollBornDay").css({"color":"green"});
			$("#cardBornDay").css({"color":"green"});
		}
		if($("#enrollCertNo").text()!=$("#cardCertNo").text()){			
			$("#enrollCertNo").css({"color":"red"});
			$("#cardCertNo").css({"color":"red"});
		}else{
			$("#enrollCertNo").css({"color":"green"});
			$("#cardCertNo").css({"color":"green"});
		}
		// 教学点
		if($("#enrollUnitname").text()!=$("#reportUnitName").text()){
			$("#enrollUnitname").css({"color":"red"});
			$("#reportUnitName").css({"color":"red"});
		}else{
			$("#enrollUnitname").css({"color":"green"});
			$("#reportUnitName").css({"color":"green"});
		}
	}
	
	function closePdialog(){
		StartReadCard='N';
		$.pdialog.closeCurrent();
	}
</script>
</html>