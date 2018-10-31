<script>
		$(document).ready(function(){
			$("#${treeId}").treeview();
			
			//点击树节点
			$("#${treeId}").find("li").find(">span").filter(".${pickType}").find(">span").click(function(event) {
				<#if isSingle>
				if($("select[name=${id}SelectArea] option").length>0){
					$("#${id}MessageOut").text("只能选一条记录");
					return ;
				}
				</#if>
				if(!$("select[name=${id}SelectArea] option").is("[value="+$(this).attr("idvalue")+"]")){
					$("<option value="+$(this).attr("idvalue")+">"+$(this).text()+"</option>").appendTo("#${id}SelectArea");
					$("#${id}MessageOut").text("无");
				}else{
					$("#${id}MessageOut").text("该项已经选择");
				}
			}).add( $("a", this) ).hoverClass();
			
			//点击删除
			$("#${id}Delselected").click(function(event){
				var selecteds = $("select[name=${id}SelectArea] option:selected");
				if(selecteds.length>0){
					selecteds.remove();
					$("#${id}MessageOut").text("无");
				}else{
					$("#${id}MessageOut").text("请选择一条记录进行删除");
				}
			});
			
			//点击全部删除
			$("#${id}DelAll").click(function(event){
				$("select[name=${id}SelectArea] option").remove();
				$("#${id}MessageOut").text("无");
			});
			
			//点击全选
			$("#${id}PickAll").click(function(event){
				<#if isSingle>
					$("#${id}MessageOut").text("只能选一条记录,不能全选");
					return ;
				</#if>
				$("#${treeId}").find("li").find(">span").filter(".${pickType}").find(">span").each(function(){					
					if(!$("select[name=${id}SelectArea] option").is("[value="+$(this).attr("idvalue")+"]")){
						$("<option value="+$(this).attr("idvalue")+">"+$(this).text()+"</option>").appendTo("#${id}SelectArea");
					}
				});
			});
			
			//点击确定
			$("#${id}DoPick").click(function(event){
				${callBackFun}($("select[name=${id}SelectArea] option"));
			});
		});
		
</script>
<table cellpadding="0" cellspacing="0"
			style="border:solid 1px #333;border-bottom-style:outset">
			<tr>
				<td>
					<b>&nbsp;待选：</b>
					<br />
					<div
						style="width:200px;height:278px;overflow:auto;border:solid 1px #666;background-color: #fdfdfd;">