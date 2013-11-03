<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>建筑功能</title>
<link href="css/taxpayer.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/resources/js/jquery-1.9.1.js"></script>
<script type="text/javascript">
	$(function() {
		$('#chooseAll').click(function() {
			var list = $('[name=ids]').length;
			if ($('#chooseAll').attr("checked") == 'checked') {
				for ( var i = 0; i < list; i++) {
					$('[name=ids]').attr("checked", 'true');
				}
			} else {
				$('[name=ids]').click();
				chkcount--;
			}
		});
	});
	function add() {
		var form = document.forms[0];
		form.action = "/ems/buildingFuncAdd.do";
		form.submit();
	}
	function del() {
		if (confirm("您确定要删除吗？")) {
			var form = document.forms[0];
			form.action = "/ems/buildingFuncRemove.do";
			form.submit();
		}
	}
</script>
</head>
<body>
	<s:form id="bfform" action="buildingFuncSave">
		<!------------------ 编辑栏 ------------------>
		<table width="98%" cellpadding="0" cellspacing="0" class="dg_borderstyle">
			<tr class="rtm_shb_m"> 
				<td width="10px"></td>
				<td width="420px" style="text-align: left;" class="rtm_shb_t"> 
					<table>
						<tr>
							<td class="rtm_shb_i"><s:hidden name="buildingFunction.id" /></td>
							<td> 
								<s:text name="编码"></s:text>
							</td>
							<td>
								<s:textfield name="buildingFunction.code" />
							</td>
							<td> 
								<s:text name="名称"></s:text>
							</td>
							<td>
								<s:textfield name="buildingFunction.name" />
							</td>
						</tr>  
					</table> 
				</td> 
				<td class="rtm_shb_r">
					<input type="submit" value="新建" onclick="add();" /> 
					<input type="submit" value="保存" /> 
					<input type="submit" value="删除" onclick="del();" />
				</td>
			</tr>
		</table> 
		<!----------- 数据列表 ------------------>
		<table width="98%" border="0"  cellpadding="0" cellspacing="1" class="dg_borderstyle">
			<tr>
				<td class="dg_headerstyle"><input type="checkbox"
					id="chooseAll" /></td>
				<td class="dg_headerstyle"><s:text name="编码" /></td>
				<td class="dg_headerstyle"><s:text name="名称" /></td>
				<td class="dg_headerstyle"><s:text name="创建时间" /></td>
				<td class="dg_headerstyle"><s:text name="修改时间" /></td>
			</tr>
			<s:iterator value="bfList">
				<tr class="dg_alternatingitemstyle">
					<td align="center"><input type="checkbox" name="ids"
						value="${id}" /></td>
					<td align="left">${code}</td>
					<td align="left"><a
						href="buildfunc.do?buildingFunction.id=${id}">${name}</a></td>
					<td align="center">${dateCreate}</td>
					<td align="center">${dateUpdate}</td>
				</tr>
			</s:iterator>
		</table>
	</s:form>
</body>
</html>