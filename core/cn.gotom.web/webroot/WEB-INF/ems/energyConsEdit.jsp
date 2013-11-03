<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%> 
<%@ taglib uri="/struts-dojo-tags" prefix="sx"%>
<html>
<sx:head />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>编辑能源消耗量采集记录</title>
<link href="css/taxpayer.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/resources/js/jquery-1.9.1.js"></script>
<script type="text/javascript"> 
	function cancel() {
		var form = document.forms[0];
		form.action = "/ems/energyCons.do";
		form.submit();
	} 
</script>
</head>
<body class="rtm_body">
	<h3>${message}</h3>
	<s:form id="bdform" action="energyConsSave">
		<!------------------ 编辑栏 ------------------>
		<table width="*" cellpadding="0" cellspacing="1"  class="ow_fim_mg" >
			<tr>
				<td class="rtm_shb_i"><s:hidden name="energyConsumption.id" /></td>
				<td> 
				</td> 
			</tr>    
			<tr> 
				<td class="ow_fim_l" style="width:140px;"> 
					<s:text name="采集时间"></s:text>
				</td>
				<td class="ow_fim_r" > 
					<sx:datetimepicker name="energyConsumption.collectDate"  displayFormat="yyyy-MM-dd" ></sx:datetimepicker> 
				</td> 
			</tr> 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="行政区划代码编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="energyConsumption.cantonCode" /> 
				</td> 
			</tr>    
			<tr>  
				<td class="ow_fim_l"> 
					<s:text name="建筑类别编码"></s:text>
				</td>
				<td class="ow_fim_r">    
					<s:select name="energyConsumption.functionCode" id="energyConsumption.functionCode"
					 list="bfList" listValue="name" listKey="code" 
					value="energyConsumption.functionCode"></s:select>
				</td>
			</tr> 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="建筑识别编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="energyConsumption.buildingCode" />
				</td> 
			</tr>
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="分类能耗编码"></s:text>
				</td>
				<td class="ow_fim_r">  
			        <s:select name="csClass"  id="csClass"
			         list="csList" listKey="value" listValue="name" 
			         value="energyConsumption.consumptionClass.value" />   
				</td> 
			</tr>	  
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="分项能耗编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="energyConsumption.subitemCode" />
				</td> 
			</tr>	 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="分项能耗一级子项编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="energyConsumption.subitemOneCode" />
				</td> 
			</tr>	
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="分项能耗二级子项编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="energyConsumption.subitemTwoCode" />
				</td> 
			</tr>	 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="消耗量"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="energyConsumption.consumption" />
				</td> 
			</tr>	 
		</table>  
		<table width="*" cellpadding="0" cellspacing="0">
			<tr>  
				<td class="rtm_shb_r" colspan="6"> 
					<input type="submit" value="保存"  /> 
					<input type="submit" onclick="cancel();" value="取消" />
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>