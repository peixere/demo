<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>能源消耗量采集记录</title>
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
		form.action = "/ems/energyConsAdd.do";
		form.submit();
	}
	function del(id) {
		if (confirm("您确定要删除吗？"))
		{
			var form = document.forms[0];
			if(id==null)
			{
				form.action = "/ems/energyConsRemove.do";
			}
			else
			{
				form.action = "/ems/energyConsRemove.do?energyConsumption.id="+id;
			}
			form.submit();
		}
	}
</script>
</head>
<body>  
	<s:form id="bfform" action="energyCons">
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
				<td class="dg_headerstyle"><s:text name="采集时间" /></td>
				<td class="dg_headerstyle"><s:text name="行政区划代码编码" /></td>
				<td class="dg_headerstyle"><s:text name="建筑类别编码" /></td>
				<td class="dg_headerstyle"><s:text name="建筑识别编码" /></td>
				<td class="dg_headerstyle"><s:text name="分类能耗编码" /></td>
				<td class="dg_headerstyle"><s:text name="分项能耗编码" /></td>
				<td class="dg_headerstyle"><s:text name="消耗量" /></td>
				<td class="dg_headerstyle"><s:text name="操作" /></td>
			</tr> 
			<s:iterator value="ecList">
				<tr class="dg_alternatingitemstyle">
					<td align="center"><input type="checkbox" name="ids"
						value="${id}" /></td>
					<td align="left">${collectDate}</td>
					<td align="left">
						<a href="energyConsEdit.do?energyConsumption.id=${id}">${cantonCode}</a>
					</td>
					<td align="center">${functionCode}</td>
					<td align="center">${buildingCode}</td>
					<td align="center">${consumptionClass}</td>
					<td align="center">${subitemCode}</td> 
					<td align="center">${consumption}</td>
					<td align="center">
						<a href="energyConsEdit.do?energyConsumption.id=${id}">修改</a>
						<a href="javascript:del('${id}');" >删除</a>
					</td>
				</tr>
			</s:iterator>
		</table>
	</s:form>
</body>
</html> 