<!-- firefox -->
	<object id="bigfileupload" classid="java:com.hnjk.applet.bigfile.FileUploadApplet.class" 
		codebase="${basePath }/applets/"
		codetype="application/x-java-applet" 
		archive="hnjk-webapp-applet-3.2.10.jar,commons-net-3.1.jar,JSObject.jar"		
		folderpath = "${folderpath}"
		maxSize = "${maxSize?c}"
		fileAccept = "${fileAccept}"						
		standby="正在载入..."		
		WIDTH = "${width}" 
		HEIGHT = "${height}"
		name="bigfileupload">
			<!-- 兼容IE -->
			<object id="bigfileupload" classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
			standby="正在载入..."			
			name="bigfileupload"			
			WIDTH = "${width}" 
			HEIGHT = "${height}"
			codebase="${basePath }/common/jinstall-6u14-windows-i586.cab#Version=6,0,0,8">
			<param name="code" value="com.hnjk.applet.bigfile.FileUploadApplet.class">
			<param name="archive" value="hnjk-webapp-applet-3.2.10.jar,commons-net-3.1.jar,JSObject.jar">
			<param name="codebase" value="${basePath }/applets/">
			<param name="type" value="application/x-java-applet;version=1.4">			
			<PARAM NAME = "folderpath" VALUE ="${folderpath}">	
			<PARAM NAME = "fileAccept" VALUE = "${fileAccept}">
			<PARAM NAME="maxSize" VALUE="${maxSize?c}">		
				<!--兼容chrome-->
				<applet id="bigfileupload" classid="java:com.hnjk.applet.bigfile.FileUploadApplet.class" 
					code="com.hnjk.applet.bigfile.FileUploadApplet.class" 
					codebase="${basePath }/applets/"
					codetype="application/x-java-applet" 
					archive="hnjk-webapp-applet-3.2.10.jar,commons-net-3.1.jar,JSObject.jar" 				
					standby="正在载入..." 
					WIDTH = "${width}" 
					HEIGHT = "${height}"
					name="bigfileupload">	
	
					<PARAM NAME = "folderpath" VALUE ="${folderpath}">
					<PARAM NAME="maxSize" VALUE="${maxSize?c}">
					<PARAM NAME = "fileAccept" VALUE = "${fileAccept}">
				</applet> 
				
			</object>
		</object>	
	<div id="_${tocken}hasuploadFiles">	
		<#if attachList ? exists>
			<#list attachList as attach>
				<li style='padding-bottom:6px' id='${attach.resourceid}'>
					<img src='${basePath}/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;
					<a onclick="_${tocken}downloadAttachFile('${attach.resourceid}')" href="##">${attach.attName}&nbsp;&nbsp;</a>
					<img src='${basePath}/jscript/jquery.uploadify/images/cancel.png' onClick="_${tocken}deleteAttachFile('${attach.resourceid}')" style='cursor:pointer;height:10px'>
				</li>
			</#list>
		</#if>
	</div>	
	<div id="_${tocken}attachList" style="display:none">
		<#if attachList ? exists>
			<#list attachList as attach>
				<input type='hidden' id='${hiddenInputName}' name='${hiddenInputName}' value='${attach.resourceid}'>
			</#list>
		</#if>
	</div>
	
<script type="text/javascript">
	//提供一个JS，给applet回调
	function getUploadFileInfo(attachName,serPath, serName, attType,fileSize){	
		$.ajax({
		   type: "POST",
		   url: "${basePath }/edu3/filemanage/upload.html",	
		   data:{attName:attachName,serName:serName,serPath:serPath,attType:attType,attSize:fileSize,formType:'${formType}',formId:'${formId}',fillinName:'${fillinName}',fillinNameId:'${fillinNameId}',isStoreToDatabase:'${isStoreToDatabase}',saveType:'ftp'},
		   dataType: "text",		 
		   success: function(data){	 
			 var result = data.split('|');			
			 var html = "<li style='padding-bottom:6px' id='"+result[0]+"'>";
			 html += "<img src='${basePath}/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;";
			 html += result[2]+"&nbsp;&nbsp;<img src='${basePath}/jscript/jquery.uploadify/images/cancel.png'  onClick=_${tocken}deleteAttachFile('"+result[0]+"');  style='cursor:pointer;height:10px'></li>";
			
			 $("#_${tocken}hasuploadFiles").append(html);
			 $("#_${tocken}attachList").append("<input type='hidden' id='${hiddenInputName}' name='${hiddenInputName}' value='"+result[0]+"'/>");		 
		   }
		});			
		
	}
	
	//删除附件
	function _${tocken}deleteAttachFile(attid){
		var pageType = '${pageType}';
		if(pageType == 'sys'){
			alertMsg.confirm("确定要删除这个附件？", {
				okCall: function(){
						var url = '${basePath }/edu3/framework/filemanage/delete.html';
						$.get(url,{fileid:attid},function(data){
							$("#_${tocken}hasuploadFiles > li[id='"+attid+"']").remove();
							$("#${hiddenInputName}[value='"+attid+"']").remove();
						});	
					}
				});
		}else{
			 if(confirm("确定要删除这个附件？")){
				$.get('${basePath }/edu3/framework/filemanage/delete.html',{fileid:attid},function(data){
					$("#_${tocken}hasuploadFiles > li[id='"+attid+"']").remove();
					$("#${hiddenInputName}[value='"+attid+"']").remove();
				});
			 }	
		}
	}
	
	//下载附件
	function _${tocken}downloadAttachFile(attid){
		var url = '${basePath }/edu3/framework/filemanage/download.html?id='+attid;
		var elemIF = document.createElement("iframe");
		elemIF.src = url; 
		elemIF.style.display = "none"; 
		document.body.appendChild(elemIF);		
	}
</script>