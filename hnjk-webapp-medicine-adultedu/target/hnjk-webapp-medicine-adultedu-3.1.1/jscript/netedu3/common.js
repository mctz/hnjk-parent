/**
* 网院3.0平台通用js
* by hzg
*/

/**
 * checkbox全选/全不选
 * @param checkallId 全选checkbox ID，需要加#前置
 * @param checkboxitemName 子checkbox name
 */
function checkboxAll(checkallId,checkboxItemName){
	$("INPUT[name='"+checkboxItemName+"']").attr('checked', $(checkallId).is(':checked'));
}
/**
 * 当子项中有一个没有被选中时,全选取消,否则为选中
 * @param {} checkallId 全选checkbox ID，需要加#前置
 * @param {} checkboxItemName 子checkbox name
 */
function uncheckboxAll(checkallId,checkboxItemName){
	var obj = $("input[name='"+checkboxItemName+"']");
	var chkFlag = true;
	for(var i=0;i<obj.length;i++){
		if(!obj[i].checked ){
			chkFlag = false;
			break;
		}
	}
	$(checkallId).attr('checked', chkFlag);
}

/**
 * 判断checkbox是否选中
 */
function isChecked(chkname){	
	var obj = $("input[name='"+chkname+"']");
	for(var i=0;i<obj.length;i++){
		if(obj[i].checked ){
			return true;
		}
	}
	return false;
}
