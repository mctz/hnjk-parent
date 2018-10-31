<div id="menu_tit">系统菜单</div>
<div id="PARENT">
	<ul id="nav">	
	<#list menus as menu>
		<li>
			<a class="jiahao" href="#Menu=${menu.resourceCode}" onclick="DoMenu('${menu.resourceCode}')" >${menu.resourceName}</a>
			<#assign parentCode=menu.resourceCode/>
			<ul id="${menu.resourceCode}" class="collapsed">
				<#list menu.children as child>
					<#if child?exists>
						<li><a href="${basePath}${child.resourcePath}#Menu=${parentCode}">${child.resourceName }</a></li>
					</#if>
				</#list> 
			</ul>
		</li>
	</#list>
	</ul>
</div>
<script type=text/javascript>
<!--
var LastLeftID = "";
function menuFix() {
  	var obj = document.getElementById("nav").getElementsByTagName("li"); 
 	for (var i=0; i<obj.length; i++) {
  		obj[i].onmouseover=function() {
   			this.className+=(this.className.length>0? " ": "") + "sfhover";
  		}
  		obj[i].onMouseDown=function() {
   		this.className+=(this.className.length>0? " ": "") + "sfhover";
  	}
 	 obj[i].onMouseUp=function() {
   	this.className+=(this.className.length>0? " ": "") + "sfhover";
 	 }
  	obj[i].onmouseout=function() {
 	  this.className=this.className.replace(new RegExp("( ?|^)sfhover\\b"), "");
 	 }
 }
}
function DoMenu(emid){
 var obj = document.getElementById(emid); 
 var lastObj = document.getElementById(LastLeftID);
 obj.className = (obj.className.toLowerCase() == "expanded"?"collapsed":"expanded");
 obj.parentNode.childNodes[0].className = (obj.parentNode.childNodes[0].className.toLowerCase() == "jiahao"?"jianhao":"jiahao");
 if((LastLeftID!="")&&(emid!=LastLeftID)) //关闭上一个Menu
 {
   lastObj.className = "collapsed";
   lastObj.parentNode.childNodes[0].className = (lastObj.parentNode.childNodes[0].className.toLowerCase() == "jiahao"?"jianhao":"jiahao");
 }
 LastLeftID = emid;
}
function GetMenuID(){
 var MenuID="";
 var _paramStr = new String(window.location.href);
 var _sharpPos = _paramStr.indexOf("#");
 
 if (_sharpPos >= 0 && _sharpPos < _paramStr.length - 1) {
  _paramStr = _paramStr.substring(_sharpPos + 1, _paramStr.length);
 } else {
  _paramStr = "";
 }
 
 if (_paramStr.length > 0) {
  var _paramArr = _paramStr.split("&");
  if (_paramArr.length>0)  {
   var _paramKeyVal = _paramArr[0].split("=");
   if (_paramKeyVal.length>0)   {
    MenuID = _paramKeyVal[1];
   }
  }
 }
 
 if(MenuID!="") {
  DoMenu(MenuID)
 }
}
GetMenuID(); //*这两个function的顺序要注意一下，不然在Firefox里GetMenuID()不起效果
menuFix();
-->
</script>