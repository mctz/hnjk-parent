<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>审核</title>
<script type="text/javascript">
	
	function auditCancelExameeInfo_in(){
		var checked =  $('input[name="auditStatus"]:checked').val();
		var resourceid = $("#exameeinfo_select_ids").val();
		var type = $("#exameeinfo_select_type").val();
		var _cstr = "";
		if(type=="cancel"){
			_cstr="注销入学资格吗？";
		}else{
			_cstr="保留学籍吗？";
		}
		var checkedStr = "";
		if(checked==''){
			alertMsg.warn("请选择一种审核方式.");
			return false;
		}else if('N'==checked){
			checkedStr = "<font color='red'>审核不通过</font>"+_cstr;
		}else if('Y'==checked){
			checkedStr = "<font color='blue'>审核通过</font>"+_cstr;
		}
		
		alertMsg.confirm("您确定要为这些考生审核: "+checkedStr,{
			okCall: function(){
				$.ajax({
			          type:"POST",
			          url:"${baseUrl}/edu3/recruit/exameeinfo/auditcancel.html",
			          data:{resourceid:resourceid,auditStatus:checked,type:type},
			          dataType:  'json',
			          success:function(date){          	   		
			         		 if(date['statusCode'] === 300){         		 	
			         		 	 alertMsg.warn(date['message'])	;	  
			         		 }else{
			         			alertMsg.info(date['message']);	
			         			$.pdialog.closeCurrent();
			    				var form =_getPagerForm(navTab.getCurrentPanel(),"RES_RECRUIT_EXAMEEINFO");
			    				navTab.reload(form.action,$(form).serializeArray(),"RES_RECRUIT_EXAMEEINFO");
			         		 }         
			          }            
				});
			}
		});
		
	}
	
	</script>
</head>
<body>
	<div style="margin-top: 8px;">
		<input type="hidden" id="exameeinfo_select_ids" value="${ids }" /> <input
			type="hidden" id="exameeinfo_select_type" value="${type }" /> <input
			type="radio" id="modeSelect0" name="auditStatus" value="Y"
			checked="checked" />审核通过 <input type="radio" id="modeSelect1"
			name="auditStatus" value="N" />审核不通过
	</div>
	<br>
	<br>
	<div class="buttonActive">
		<div class="buttonContent">
			<button type="button" onclick="auditCancelExameeInfo_in()">确定</button>
		</div>
	</div>
</body>
</html>