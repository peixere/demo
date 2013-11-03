<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>建筑信息</title>
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
		form.action = "/ems/buildingAdd.do";
		form.submit();
	}
	function del(id) {
		if (confirm("您确定要删除吗？"))
		{
			var form = document.forms[0];
			if(id==null)
			{
				form.action = "/ems/buildingRemove.do";
			}
			else
			{
				form.action = "/ems/buildingRemove.do?building.id="+id;
			}
			form.submit();
		}
	}
</script>
</head>
<body>
	<s:form id="bfform" action="building">
		<!------------------ 编辑栏 ------------------>
		<table width="98%" cellpadding="0" cellspacing="0"  class="dg_borderstyle">
			<tr> 
				<td style="height:40px; width:10px;" ></td>
				<td class="rtm_shb_r" style="text-align: left;">
					<input type="submit" value="新建" onclick="add();" /> 
	                <input type="submit" value="删除" onclick="del(null);" />
                </td>
			</tr>
		</table> 
		<!----------- 数据列表 ------------------>
		<table width="98%" border="0" cellpadding="0" cellspacing="1" class="dg_borderstyle">
			<tr>
				<td class="dg_headerstyle"><input type="checkbox"
					id="chooseAll" /></td>
				<td class="dg_headerstyle"><s:text name="编码" /></td>
				<td class="dg_headerstyle"><s:text name="名称" /></td>
				<td class="dg_headerstyle"><s:text name="省份" /></td>
				<td class="dg_headerstyle"><s:text name="城市" /></td>
				<td class="dg_headerstyle"><s:text name="地址" /></td>
				<td class="dg_headerstyle"><s:text name="填表日期" /></td>
				<td class="dg_headerstyle"><s:text name="验收日期" /></td>
				<td class="dg_headerstyle"><s:text name="操作" /></td>
			</tr> 
			<s:iterator value="bdList">
				<tr class="dg_alternatingitemstyle">
					<td align="center"><input type="checkbox" name="ids"
						value="${id}" /></td>
					<td align="left">${code}</td>
					<td align="left">
						<a href="buildingEdit.do?building.id=${id}">${name}</a>
					</td>
					<td align="center">${province}</td>
					<td align="center">${city}</td>
					<td align="center">${address}</td>
					<td align="center">${fillFormDate}</td>
					<td align="center">${ecDate}</td>
					<td align="center">
						<a href="buildingEdit.do?building.id=${id}">修改</a>
						<a href="javascript:del('${id}');" >删除</a>
					</td>
				</tr>
			</s:iterator>
		</table>
	</s:form>
</body>
</html>