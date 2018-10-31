<!--校外学习中心首页左侧-->
<table width="250" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="154" align="center" valign="top" background="${baseUrl}/style/default/portal-images-2/Rlogin-Br.gif">
        <#if currentUser?exists>
          <table border="0" cellpadding="0" cellspacing="0" class="STYLE2" style="width:218px">
          <tr>
            <td colspan="3">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="3">&nbsp;</td>
          </tr>          
          <tr>
            <td width="21%" height="25" align="right">欢迎，</td>
            <td align="left">${currentUser.cnName}</td>          
          </tr>
          <tr>
            <td height="25" align="left"></td>
            <td align="left"><a href="${baseUrl}/edu3/framework/index.html">我的主页</a></td>
           
          </tr>
          <tr>
            <td height="29" align="left"></td>
            <td align="left"><a href="${baseUrl}/edu3/learning/bbs/index.html">学院论坛</a></td>
          </tr>          
          <tr>
            <td align="left"></td>            
            <td align="left"><a href="###" style="color:red" onclick="goLogout();">退出</a></td>
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
        <table border="0" cellpadding="0" cellspacing="0" class="STYLE2" style="width:218px">
          <tr>
            <td colspan="3">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="3">&nbsp;</td>
          </tr>
          
          <tr>
            <td width="21%" height="25" align="left">用户名</td>
            <td align="left" valign="bottom"><input name="j_username" type="text" class="login" size="13" id="j_username" maxlength="20"/></td>
            <td align="left" valign="bottom"><input type="submit" name="Submit" onclick="login(); return false;" value="登 录" /></td>
          </tr>
          <tr>
            <td height="25" align="left">密 码</td>
            <td align="left"><input name="j_password" type="password" class="login" size="13" id="j_password" maxlength="20"/></td>
            <td align="left"><input type="button" name="Submit2" value="帮 助" /></td>
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
            <td align="left" colspan="2"><input type="radio" name="fromNet" value="pub" checked="checked"/>公众网 
            &nbsp;&nbsp;<input type="radio" name="fromNet" value="edu"/>教育网</td>            
            <td width="29%" align="left"><a href="#">忘记密码？</a></td>
          </tr>
          
        </table>
        </form>
        </#if> 
        
        </td>
      </tr>
      <tr>
        <td><a href="${baseUrl}/portal/service/student/service.html?url=/portal/site/service/enrolleeinfo-web.html&uid=${schoolId}">
        <img src="${baseUrl}/style/default/portal-images-2/Rbaoming-Br.gif" width="250" height="87" border="0" /></a></td>
      </tr>
      <tr>
        <td align="center">
        <!--左侧栏目-->        
        <table border="0" cellpadding="0" cellspacing="0" class="STYLE2" style="width:95%">
          <tr>
            <td width="30%" align="left" bgcolor="#474747">　
            <span class="STYLE3">招生专栏</span></td>
            <td width="70%" align="right" bgcolor="#D2D2D2">
             <#if schoolRecruitId?exists> 
            <a href="${baseUrl}/portal/site/channel/show.html?id=${schoolRecruitId}&uid=${schoolId}">更多...</a>
             </#if>
            </td>
          </tr>          
            <#if schoolRecruitList?exists>  
        			 <#list schoolRecruitList as news>
		                <tr>
		                  <td align="left" colspan="2" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
		                  <a href="${baseUrl}/portal/site/article/show.html?id=${news.resourceid}&uid=${news.orgUnit.resourceid}" title="${news.title}">
				      		<#if news.title?length lt 17 >
									${news_index+1}. ${news.title}
									<#else>
									${news_index+1}. ${news.title[0..16]}...
									</#if>   
								</a>			      		
		                  </td>
		                </tr>  
		               </#list>    
		         <#else>
                <tr>
                  <td align="left" colspan="2" background="${baseUrl}/style/default/portal-images-2/Rdi.gif"> 
                  - 暂时无相关新闻...
                  </td>
                </tr>
		        </#if>         

        </table>
                  
          <a href="#"></a></td>
      </tr>
      
    </table>