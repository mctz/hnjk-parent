<table width="185" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="234" align="center" valign="top" background="${baseUrl}/style/default/portal-images-2/Rlogin.gif">
        <!--判断用户是否登录，如果是则输出用户信息-->
        <#if currentUser?exists>
        	<table border="0" cellpadding="0" cellspacing="0" class="STYLE2" style="width:156px">
	          <tr>
	            <td colspan="3">&nbsp;</td>
	          </tr>
	          <tr>
	            <td colspan="3">&nbsp;</td>
	          </tr>
	          <tr>
	            <td colspan="3" align="left">&nbsp;</td>
	          </tr>
	          <tr>
	            <td width="30%" height="25" align="right">欢迎，</td>
	            <td width="70%" colspan="2" align="left" >${currentUser.cnName}</td>
	          </tr>
	          <tr>
	            <td height="25" align="left"></td>
	            <td colspan="2" align="left"><a href="${baseUrl}/edu3/framework/index.html">我的主页</a></td>
	          </tr>
	          <tr>
	            <td height="25" align="left"></td>
	            <td colspan="2" align="left"><a href="${baseUrl}/edu3/learning/bbs/index.html">学院论坛</a></td>
	          </tr>
	            <tr>
	            <td height="25" align="left"></td>
	            <td colspan="2" align="left"><a href="###" style="color:red" onclick="goLogout();">退出</a></td>
	          </tr>
	         </table>	 
	         <script type="text/javascript">
	         	function goLogout(){
	         		if(window.confirm('确定要退出系统吗?')){
	         		window.location.href='${baseUrl}/j_spring_security_logout';
	         		}
	         	}
	         </script>      
        <#else>
        	<form id="loginform" action="${baseUrl}/j_spring_security_check" method="post">
      		<table border="0" cellpadding="0" cellspacing="0" class="STYLE2" style="width:156px">
          <tr>
            <td colspan="3">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="3">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="3" align="left">&nbsp;</td>
          </tr>
          <tr>
            <td width="30%" height="25" align="left">用户名:</td>
            <td width="70%" colspan="2" align="left" valign="bottom"><input name="j_username" type="text" class="login" size="13" id="j_username" maxlength="20"/></td>
          </tr>
          <tr>
            <td height="25" align="left">密 码:</td>
            <td colspan="2" align="left"><input name="j_password" type="password" class="login" size="13" id="j_password" maxlength="20"/></td>
          </tr>
      
          <#if (loginNum > 3)>        
          <tr>
            <td height="29" align="left">验证码:</td>
            <td colspan="2" align="left">
            <input name="j_checkcode" type="text" maxlength="4" class="login" style="width:36px" id="j_checkcode"  onfocus="this.select();" onkeydown="if(event.keyCode==0xD){login(); return false;}"/>
			<img src="${baseUrl}/imageCaptcha" id="checkCodeImg" style="margin-left:-6px;margin-bottom:-10px;padding-left:4px" onclick="this.src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()" title="看不清，点击换一张" >
            </td>
          </tr>        
          </#if>
       
          <tr>          
            <td colspan="3" align="left">
            <input type="radio" name="fromNet" value="pub" checked="checked"/>公众网 
            &nbsp;&nbsp;<input type="radio" name="fromNet" value="edu"/>教育网
            </td>
          </tr>
          <tr>
            <td rowspan="2" align="left">&nbsp;</td>
            <td align="left"><input type="submit" name="Submit" value="登录"  onclick="login(); return false;"/></td>
            <td align="left"><input type="button" name="Submit2" value="帮助" /></td>
          </tr>
          <tr>
            <td height="34" colspan="2" align="right"><a href="#">忘记密码？</a></td>
          </tr>
        </table>
        </form>
        </#if>      
        
        </td>
      </tr>
      <tr>
        <td><a href="${baseUrl}/portal/service/student/service.html?url=/portal/site/service/enrolleeinfo-web.html"><img src="${baseUrl}/style/default/portal-images-2/Rbaoming.gif" width="185" height="83" border="0" /></a></td>
      </tr>
      <tr>
        <td>       
        
        <a onclick="this.newWindow = window.open('http://chat8.live800.com/live800/chatClient/chatbox.jsp?companyID=62954&amp;configID=114128&amp;jid=6206208730&amp;enterurl=http%3A%2F%2Fwww%2Escutde%2Enet%2Findex%2Easp&amp;skillId=2210&amp;timestamp=1303955892702', 'chatbox62954','toolbar=no,status=no,resizable=yes,width=600,height=400');this.newWindow.focus();this.newWindow.opener=window;return false;" href="javascript:void(0)" target="_self" id="live800iconlink">
        <img src="${baseUrl}/style/default/portal-images-2/Rzixun.gif" width="185" height="81" border="0" /></a></td>
      </tr>
      <tr>
        <td><a href="http://www.scutde.net/jpkcsb/" target="_blank">
        <img src="${baseUrl}/style/default/portal-images-2/Rjpkc.gif" width="185" height="84" border="0" /></a></td>
      </tr>
      <tr>
        <td height="74" valign="bottom" background="${baseUrl}/style/default/portal-images-2/Rlink.gif">
        <!--左侧友情链接-->
		<table border="0" cellpadding="0" cellspacing="0" class="STYLE2" style="width:96%">
          <tr>
            <td width="5%">&nbsp;</td>
            <td width="91%">            
            <select onchange="javascript:if(this.options[this.selectedIndex].value!='')window.open(this.options[this.selectedIndex].value)" name="select">
                <option value="" selected="selected">请选择学院</option>
               <#if linkList?exists>
					<#list linkList as link>
					 <#if link.linkPosition == 'textlink-scut'>                
               			<option value="${link.linkHref}" title="${link.linkName}">
               			<#if link.linkName?length lt 9 >
						${link.linkName}
						<#else>
						 ${link.linkName[0..8]}...
						</#if>                  			
               			</option>
               			</#if>
                	</#list>
                </#if>                
            </select></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
            <select onchange="javascript:if(this.options[this.selectedIndex].value!='')window.open(this.options[this.selectedIndex].value)" name="select2">
                <option value="" selected="selected">请选择其他网院</option>
                <#if linkList?exists>
					<#list linkList as link>
					 <#if link.linkPosition == 'textlink-other'>                
               			<option value="${link.linkHref}"  title="${link.linkName}">               			
               			<#if link.linkName?length lt 9 >
						${link.linkName}
						<#else>
						 ${link.linkName[0..8]}...
						</#if>               			
               			</option>
               			</#if>
                	</#list>
                </#if>     
            </select>
            </td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td><a href="mailto:scutde_yiluo@163.com" target="_blank">
        <img src="${baseUrl}/style/default/portal-images-2/Rxx.gif" width="185" height="33" border="0" /></a>
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      
    </table>

