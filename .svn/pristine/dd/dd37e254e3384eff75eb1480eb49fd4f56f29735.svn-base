<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量设置入学资格审核状态</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<!-- Hidden District -->
			<input type="hidden" id="branchSchool" value="${branchSchool}" /> <input
				type="hidden" id="major" value="${major}" /> <input type="hidden"
				id="classic" value="${classic}" /> <input type="hidden"
				id="stuStatus" value="${stuStatus}" /> <input type="hidden"
				id="name" value="${name}" /> <input type="hidden"
				id="matriculateNoticeNo" value="${matriculateNoticeNo}" /> <input
				type="hidden" id="grade" value="${grade}" /> <input type="hidden"
				id="certNum" value="${certNum}" /> <input type="hidden" id="pageNum"
				value="${pageNum}" /> <input type="hidden" id="stus_recruitStatus"
				value="${stus }" /> <input type="hidden"
				id="batchSetStudentInfoStatusSetFlag" value="${flag }">
			<div class="searchBar">
				<ul class="searchContent">
					<c:choose>
						<c:when test="${ flag eq '0' }">
							<li><label>批量设置入学资格审核状态:</label> <gh:select
									id="recruitStatus" name="recruitStatus"
									dictionaryCode="CodeAuditStatus" /></li>
						</c:when>
						<c:when test="${ flag eq '1' }">
							<li><label>批量设置学籍状态:</label> <gh:select
									id="batchSetStudentInfoStatus" name="studentStatus"
									dictionaryCode="CodeStudentStatus"
									filtrationStr="15,19,21,23,18" /></li>
						</c:when>
					</c:choose>
				</ul>
				<ul class="searchContent" id="extensionDiv">
				</ul>
				<div class="searchContent" id="extensionDiv2"></div>
				<div class="buttonActive" style="float: right;">
					<div class="buttonContent">
						<button type="button" onclick="doBatchSetRecruitStatus();">确定
						</button>
					</div>
				</div>
			</div>

		</div>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		$("#batchSetStudentInfoStatus").change(function(){
			if("18"!=$(this).val()){
				$("#extensionDiv").html("");
				$("#extensionDiv2").html("");
				var html2 = "";
				var html1 = "";
				if("21"==$(this).val()){
					html2 = "<li><label>延至时间:</label><input type='text' id='batchSetStatus_extensionTime' name='extensionTime'  class='Wdate'  onFocus='WdatePicker({dateFmt:\"yyyy-MM-dd\"})' style='width:50%;height:18px;'/></li>";
					html1 = "<label>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label><textarea id='batchSetStatus_memo' name='batchSetStatus_memo'   style='width:50%'/></textarea>";
					$("#extensionDiv").append(html2);
					$("#extensionDiv2").append(html1);
				}else{
					if("23"==$(this).val()){
						html1 = "<label>备注(转到):</label><input type='text' id='batchSetStatus_memo' name='batchSetStatus_memo'   style='width:50%'/>学校";
						$("#extensionDiv2").append(html1);
					}else{
						html1 = "<label>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label><textarea id='batchSetStatus_memo' name='batchSetStatus_memo'   style='width:50%'/></textarea>";
						$("#extensionDiv2").append(html1);
					}
				}
			}else{
				$("#extensionDiv").html("");
				$("#extensionDiv2").html("");
			}
			
		});
	});
	function doBatchSetRecruitStatus(){
		var flag                = $("#batchSetStudentInfoStatusSetFlag").val();
		var stus		        = $('#stus_recruitStatus').val();
		var recruitStatus       = $("#recruitStatus option:selected").val();
		var studentInfoStatus   = $("#batchSetStudentInfoStatus option:selected").val();
		var batchSetStatus_extensionTime = $("#batchSetStatus_extensionTime").val();
		var batchSetStatus_memo = $("#batchSetStatus_memo").val();
		if(undefined==batchSetStatus_memo){
			batchSetStatus_memo="";
		}
		
		var branchSchool        = $('#branchSchool').val()==undefined?"":$('#branchSchool').val();
		var major		        = $('#major').val()==undefined?"":$('#major').val();
		var classic		        = $('#classic').val()==undefined?"":$('#classic').val();
		var stuStatus	        = $('#stuStatus').val()==undefined?"":$('#stuStatus').val();
		var name		        = $('#name').val()==undefined?"":$('#name').val();
		var certNum             = $('#certNum').val()==undefined?"":$('#certNum').val();
		var matriculateNoticeNo	= $('#matriculateNoticeNo').val()==undefined?"":$('#matriculateNoticeNo').val();
		var grade 		        = $('#grade').val()==undefined?"":$('#grade').val();
		var pageNum             = $('#pageNum').val()==undefined?"":$('#pageNum').val();
		var url			        = "${baseUrl}/edu3/register/studentinfo/batchSetRecruitStatus.html?stus="+stus;
		if("0"==flag){
			url                +="&flag=0&recruitStatus="+recruitStatus;
		}else if("1"==flag){
			url                +="&flag=1&studentInfoStatus="+studentInfoStatus+"&batchSetStatus_extensionTime="+batchSetStatus_extensionTime+"&batchSetStatus_memo="+encodeURIComponent(batchSetStatus_memo);
		}
		if(''==studentInfoStatus){
			alertMsg.warn("您尚未设置状态,无法保存。");
			return false; 
		}
		$.ajax({
			type:"post",
			url:url,
			dataType:"json",
			success:function(data){
			//alertMsg.info("批量设置成功!");		
			alertMsg.confirm(data["message"]);		
			//alert(data["message"]);		
			/*var url2 = "${baseUrl}/edu3/register/studentinfo/schoolroll-list.html"
					 +"?branchSchool="+branchSchool+"&major="+major
					 +"&classic="+classic+"&stuStatus="+stuStatus
					 +"&name="+name+"&matriculateNoticeNo"+matriculateNoticeNo
					 +"&grade="+grade+"&certNum="+certNum+"&pageNum="+pageNum;
				navTab.openTab('RES_SCHOOL_SCHOOLROLL_MANAGER', url2, '学籍信息管理');*/
			}
		});
		$.pdialog.closeCurrent();
		
	}
</script>
</html>