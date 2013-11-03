<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%> 
<%@ taglib uri="/struts-dojo-tags" prefix="sx"%>
<html>
<sx:head />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>编辑建筑信息</title>
<link href="css/taxpayer.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/resources/js/jquery-1.9.1.js"></script>
<script type="text/javascript"> 
	function cancel() {
		var form = document.forms[0];
		form.action = "/ems/building.do";
		form.submit();
	} 
</script>
</head>
<body class="rtm_body">
	<h3>${message}</h3>
	<s:form id="bdform" action="buildingSave">
		<!------------------ 编辑栏 ------------------>
		<table width="*" cellpadding="0" cellspacing="1"  class="ow_fim_mg" >
			<tr>
				<td class="rtm_shb_i"><s:hidden name="building.id" /></td>
				<td> 
				</td> 
			</tr>
			<tr> 
				<td class="ow_fim_l" > 
					<s:text name="编码"></s:text>
				</td>
				<td class="ow_fim_r" >
					<s:textfield name="building.code" /> 
				</td>
				<td class="ow_fim_l"> 
					<s:text name="名称"></s:text>
				</td>
				<td colspan="3"  class="ow_fim_r" >
					<s:textfield name="building.name" />
				</td>
			</tr>  
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="省份"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.province" /> 
				</td>
				<td class="ow_fim_l"> 
					<s:text name="城市"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.city" /> 
				</td>
				<td class="ow_fim_l"> 
					<s:text name="地址"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.address" />
				</td>
			</tr>  
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="能耗监测工程验收日期"></s:text>
				</td>
				<td class="ow_fim_r"> 
					<sx:datetimepicker name="building.ecDate"  displayFormat="yyyy-MM-dd" ></sx:datetimepicker> 
				</td>
				<td class="ow_fim_l"> 
					<s:text name="填表日期"></s:text>
				</td>
				<td  colspan="3" class="ow_fim_r"> 
					<sx:datetimepicker name="building.fillFormDate"  displayFormat="yyyy-MM-dd" ></sx:datetimepicker>  
				</td>
			</tr> 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="建设年代"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.buildYear" /> 
				</td>
				<td class="ow_fim_l"> 
					<s:text name="建筑层数（层）"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.buildFloor"  /> 
				</td>
				<td class="ow_fim_l"> 
					<s:text name="建筑类别"></s:text>
				</td>
				<td class="ow_fim_r">   
				<!-- 
					<s:combobox name="name"  list="bfList" listValue="code" 
				listKey="name" readonly="true"  /> 
				 
				<s:select name="energyConsumption.functionCode" id="energyConsumption.functionCode" 
			     list="bfList" listValue="name" listKey="code" 
				value="energyConsumption.functionCode"></s:select>
				-->
				<s:select name="building.buildingFunction.id" id="building.buildingFunction.id" list="bfList" 
				 listValue="name" listKey="id" value="building.buildingFunction.id"></s:select>
				</td>
			</tr>    
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="建筑总面积（平方米）"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.buildArea" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="空调总面积（平方米）"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.airconditioner" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="采暖总面积（平方米）"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.heatingArea" />
				</td>
			</tr> 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="建筑空调系统编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.airconditionerSystemCode" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="建筑空调系统名称"></s:text>
				</td>
				<td colspan="3" class="ow_fim_r">
					<s:textfield name="building.airconditionerSystem" /> 
				</td> 
			</tr>
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="建筑采暖形式编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.heatingFormCode" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="建筑采暖形式名称"></s:text>
				</td>
				<td colspan="3" class="ow_fim_r">
					<s:textfield name="building.heatingForm" />
				</td> 
			</tr>	  
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="建筑结构形式编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.structureCode" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="建筑结构形式名称"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.structure" /> 
				</td> 
				<td class="ow_fim_l"> 
					<s:text name="建筑体型系数"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.shapeCoefficient" /> 
				</td> 
			</tr>	 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="建筑外墙形式编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.exteriorCode" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="建筑外墙形式名称"></s:text>
				</td>
				<td colspan="3" class="ow_fim_r">
					<s:textfield name="building.exterior" />
				</td> 
			</tr>	
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="建筑外墙保温形式编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.exteriorWallCode" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="建筑外墙保温形式名称"></s:text>
				</td>
				<td colspan="3" class="ow_fim_r">
					<s:textfield name="building.exteriorWall" /> 
				</td> 
			</tr>	 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="建筑外窗类型编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.windowTypeCode" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="建筑外窗类型名称"></s:text>
				</td>
				<td colspan="3" class="ow_fim_r">
					<s:textfield name="building.windowType" />
				</td> 
			</tr>	 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="建筑玻璃类型编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.glassTypesCode" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="建筑玻璃类型名称"></s:text>
				</td>
				<td colspan="3" class="ow_fim_r">
					<s:textfield name="building.glassTypes" /> 
				</td> 
			</tr>	 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="窗框材料类型编码"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.windowFrameMaterialCode" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="窗框材料类型名称"></s:text>
				</td>
				<td colspan="3" class="ow_fim_r">
					<s:textfield name="building.windowFrameMaterial" />
				</td> 
			</tr>	
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="电价"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.electrovalency" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="水价"></s:text>
				</td>
				<td colspan="3" class="ow_fim_r">
					<s:textfield name="building.waterPrice" />
				</td> 
			</tr>	 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="气价"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.gasPrice" />
				</td>
				<td class="ow_fim_l"> 
					<s:text name="热价"></s:text>
				</td>
				<td colspan="3" class="ow_fim_r">
					<s:textfield name="building.feverPrice" /> 
				</td> 
			</tr>	 
			<tr> 
				<td class="ow_fim_l"> 
					<s:text name="附加项"></s:text>
				</td>
				<td class="ow_fim_r">
					<s:textfield name="building.attacha" />
				</td>
				<td class="ow_fim_r"> 
					<s:textfield name="building.attachb" />
				</td>
				<td colspan="3" class="ow_fim_r">
					<s:textfield name="building.attachc" /> 
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