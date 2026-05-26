<!DOCTYPE/>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<% String version = "01.00.26"; %>
 <html lang="es">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
        <link rel="stylesheet" media="screen" type="text/css" href="css/font-awesome.min.css">
        <link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
        <link rel=stylesheet media=screen type=text/css href=webbooking.css />
		<LINK href="Styles.css" type="text/css" rel="StyleSheet">
		<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css?v=<%=version%>">
		<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css?v=<%=version%>">
		<script type="text/javascript" src="js/jquery/1.12.0.js?v=<%=version%>"></script>
		<script type="text/javascript" src="js/v2/global.js?v=<%=version%>"></script>
		<script type="text/javascript" src="DatosFiscalesForm.js?v=<%=version%>"></script>
		<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon">
        <title>Constancia de Situación Fiscal</title>
    </head>
    
    <body id="body" onload="initScript();" class="backgroundStandard widthOverflow" style="margin: 0px !important; cursor: default;">
        
        <% String selectedDescription=request.getParameter("selecteddescription"); %>
            <script type="text/javascript">
                oldSelectedDescription = '<%= selectedDescription %>';
            </script>
            <form name="frm">
                <fieldset class="SubHead">
                    <legend class="SubHead">Constancia de Situación Fiscal</legend>
                    <table >
                    	<caption hidden="hidden">Constancia de Situación Fiscal</caption>
                        <tr>
                        <th hidden="hidden" scope="row">
		                    		
                    	</th>
                    	</tr>
                    	<tr>
                        <tbody>
                            <tr>
                            <td>
                            	<table>
                            	<caption hidden="hidden">Constancia de Situación Fiscal</caption>
                            	<tr>
		                    	<th hidden="hidden" scope="row">
		                    		
		                    	</th>
		                    	</tr>
		                    	<tr>
                                <td width="50" height="5">
                                    <p align="right" class="LabelForm">RFC:</p>
                                </td>
                                <td width="200">
                                	<input onchange="rfcChange();" id="rfc" type="text" name="rfc" onblur="return changeCase(this);" class="textinput">
                                </td>
                                <td>
                                	<p align="left" class="LabelForm">Tipo de Cliente:</p>
                                </td>
                                <td>
                                	<select id="clntType" onchange="valClntType(this.value)" property="tipoCliente" disabled="disabled">
					                    <option value="C">COMPAÑIA</option>
					                    <option value="I">INDIVIDUO</option>
					                </select>
                                </td>
                                </tr>
                                </table>
                            </td>
                            </tr>
                            <tr>
                            <td>
                            	<fieldset id="fsPM" style="display: block;" class="SubHead">
				                    <legend class="SubHead">Personas Morales</legend>
				                    <table >
				                    	<caption hidden="hidden">Constancia de Datos Persona Moral</caption>
				                    	<tr>
				                    	<th hidden="hidden" scope="row">
				                    		
				                    	</th>
				                    	</tr>
				                        <tbody>
				                            <tr>
				                                <td width="150" height="5">
				                                    <p align="right" class="LabelForm">Razón Social:</p>
				                                </td>
				                                <td width="150" height="5">
				                                	<input id="personaMoral" type="text" name="razonSocial" onblur="return changeCase(this);" class="textboxbig">
				                                </td>
				                            </tr>
				                            <tr>
				                            	<td width="150" height="5">
				                                    <p align="right" class="LabelForm">Regimen de Capital:</p>
				                                </td>
				                                <td width="150" height="5">
				                                    <input type="text" name="regmCapital" onblur="return changeCase(this);" class="textboxbig">
				                                </td>
				                            </tr>
				                        </tbody>
				                    </table>
				                </fieldset>
				                </td>
                            </tr>
                            <tr>
                            <td>
                            	<fieldset id="fsPF" style="display: none;" class="SubHead">
				                    <legend class="SubHead">Personas Fisicas</legend>
				                    <table>
				                    	<caption hidden="hidden">Captura de Datos Persona Física</caption>
				                        <tr>
				                        <th hidden="hidden" scope="row">
		                    		
				                    	</th>
				                    	</tr>
				                    	<tr>
				                        <tbody>
				                            <tr >
				                            <td height="5">
				                            	<p align="right" class="LabelForm">Nombre(s):</p>
					                        </td>
					                        <td width="150" height="5">
					                        	<input onblur="return changeCase(this);"  id="personaFisica" type="text" name="nombre" onblur="return changeCase(this);" class="textboxbig">
					                        </td>
				                            </tr>
				                            <tr>
				                            	<td width="150" height="5">
				                                    <p align="right" class="LabelForm">Primer Apellido:</p>
				                                </td>
				                                <td width="50" height="5">
				                                    <input onblur="return changeCase(this);"  id="personaFisica" type="text" name="primerApellido" onblur="return changeCase(this);" class="textboxbig">
				                                </td>
				                            </tr>
				                            <tr>
				                            	<td width="150" height="5">
				                                    <p align="right" class="LabelForm">Segundo Apellido:</p>
				                                </td>
				                                <td width="50" height="5">
				                                    <input onblur="return changeCase(this);" type="text" name="segundoApellido" onblur="return changeCase(this);" class="textboxbig">
				                                </td>
				                            </tr>
				                        </tbody>
				                    </table>
				                </fieldset>
				                </td>
                            </tr>	
                            <tr>
                            <td>
                            	<fieldset class="SubHead">
				                    <legend class="SubHead">Domicilio Fiscal</legend>
				                    <table >
				                    	<caption hidden="hidden">Captura de Domicilio Fiscal</caption>
				                        <tr>
				                        <th hidden="hidden" scope="row">
		                    		
				                    	</th>
				                    	</tr>
				                    	<tr>
				                        <tbody>
				                            <tr>
				                                <td width="150" height="5">
				                                    <p align="right" class="LabelForm">Calle:</p>
				                                </td>
				                                <td width="50" height="5">
				                                	<input onblur="return changeCase(this);" type="text" name="calle" class="textinput">
				                                </td>
				                                <td width="150" height="5">
				                                    <p align="right" class="LabelForm">N Ext:</p>
				                                </td>
				                                <td width="50" height="5">
				                                	<input type="text" name="nExt" class="textinput">
				                                </td>
				                                <td width="150" height="5">
				                                    <p align="right" class="LabelForm">N Int:</p>
				                                </td>
				                                <td width="50" height="5">
				                                	<input type="text" name="nInt" class="textinput">
				                                </td>
				                            </tr>
				                            <tr>
				                            	<td width="150" height="5">
				                                    <p align="right" class="LabelForm">Código Postal:</p>
				                                </td>
				                                <td width="50" height="5">
				                                    <input id="domicilioFiscal" type="text" name="codigoPostal" class="textinput">
				                                    <html:button property="codigoPostalBut" styleClass="button1 buttonMore"  value="..."  disabled = "false" style = "width:20" onclick="openLov('postal')" />
				                                </td>
				                                <td width="50" height="5">
				                                    <p align="right" class="LabelForm">Colonia:</p>
				                                </td>
				                                <td width="50" height="5">
				                                    <input onblur="return changeCase(this);" type="text" name="colonia" class="textinput">
				                                </td>
				                            </tr>
				                            <tr>
				                            	<td width="150" height="5">
				                                    <p align="right" class="LabelForm">Ciudad:</p>
				                                </td>
				                                <td width="50" height="5">
				                                    <input onblur="return changeCase(this);" type="text" name="ciudad" class="textinput">
				                                </td>
				                                <td width="50" height="5">
				                                    <p align="right" class="LabelForm">Municipio:</p>
				                                </td>
				                                <td width="50" height="5">
				                                    <input onblur="return changeCase(this);" type="text" name="municipio" class="textinput">
				                                </td>
				                                <td width="50" height="5">
				                                    <p align="right" class="LabelForm">Estado:</p>
				                                </td>
				                                <td width="50" height="5">
				                                    <input onblur="return changeCase(this);" type="text" name="estado" class="textinput">
				                                </td>
				                            </tr>
				                            <tr>
				                        </tbody>
				                    </table>
				                </fieldset>
				                </td>
                            </tr>
                            <tr>
	                            <td>
	                            	<fieldset class="SubHead">
					                    <legend class="SubHead">Actividades y Obligaciones</legend>
					                    <table>
					                    	<caption hidden="hidden">Captura de Regimen Fiscal y USO CFDI</caption>
					                        <tr>
					                        <th hidden="hidden" scope="row">
		                    		
					                    	</th>
					                    	</tr>
					                    	<tr>
					                        <tbody>
					                            <tr>
					                                <td width="150" height="5">
					                                    <p align="right" class="LabelForm">Regimen Fiscal:</p>
					                                </td>
					                                <td width="50" height="5">
					                                	<input type="text" name="regimenFiscalId" hidden="hidden">
					                                	<input id="regfiUCfdi" type="text" name="regimenFiscalDes" class="textboxbig">
					                                </td>
					                                <td align="right" style="margin-right: 4px;">
													 	<input name="regimFiscBtn" type="button" style="width: 50" class="button1 buttonMore" value="..."  onclick="openLov('regimFiscal')" />
											 		</td>
					                            </tr>
					                            <tr>
					                            	<td width="50" height="5">
					                                    <p align="right" class="LabelForm">Uso de CFDI:</p>
					                                </td>
					                                <td width="50" height="5">
					                                	<input type="text" name="usoCfdiId" hidden="hidden">
					                                    <input id="regfiUCfdi" type="text" name="usoCfdiDes" class="textboxbig">
					                                </td>
					                                <td align="left" style="margin-right: 4px;">
					                                	<input name="usoCfdiBtn" type="button" style="width: 50" class="button1 buttonMore"  value="..."  onclick="openLov('usoCfdi')" />
											 		</td>
					                            </tr>
					                        </tbody>
					                    </table>
					                </fieldset>
					                </td>
	                            </tr>
                        </tbody>
                    </table>
                    <input id="validData" type="checkbox" value="validData" disabled="disabled">Datos Fiscales Validados
                </fieldset>
                <table width=20% align=center>
                	<caption hidden="hidden"></caption>
                	<tr>
                	<th hidden="hidden" scope="row">
		                    		
                   	</th>
                   	</tr>
                   	<tr>
                    <tr>
                        <td>
                        	<input type="button" name="savebtn" value="Guardar" onclick="getRow();" style="FONT-WEIGHT:bolder;COLOR:lightyellow;FONT-FAMILY:Arial;FONT-SIZE:12px;BACKGROUND-COLOR:#003366 ;width:80">
                        </td>
                        <td>
                            <input name="cnclBtn" type=button class="button1 buttonMedium" value="Cancelar"
                                onclick="closeWindow();">
                        </td>
                        <td>
                        	<input type="button" value="Limpiar" style="FONT-WEIGHT:bolder;COLOR:lightyellow;FONT-FAMILY:Arial;FONT-SIZE:12px;BACKGROUND-COLOR:#003366 ;width:80" onclick="resetForm();">
                        </td>
                    </tr>
                </table>
                
            </form>
    </body>
    </html>