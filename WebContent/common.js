function isAlphaNumericOnly(str){
	return _isIn(str,"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
}									

function _isIn(str, cond){
	var inStr = new String(cond);
	for (var i=0; i < str.length; i++)
	{
		var ch = str.substring(i, i+1);
		if(cond.indexOf(ch) == -1)
		{
			return false;
		}
	}	
	return true;
}			

function  trim(val){
	for(i=0; i<val.length && val.substr(i,1)==' '; i++);
	for(j=val.length-1; j>-1 && val.substr(j,1)==' '; j--);
	j++;
	if (i<j) val=val.substring(i, j);
		else val="";		
	return val;
}

function isNumber(obj){
   fval=trim(obj.value);   
   if(isNaN(fval)){
		alert("POR FAVOR, CAPTURE SOLO NUMEROS");
		obj.select();
   }else{
		number = toNumber(fval);
		obj.value=number;
   }
}

//For removing prefix 0's out.
function toNumber(val){	
	let newNumber='';
	let bool=false;
	for(i=0;i<val.length;i++){
		subStr = val.substr(i,1);
		if(!bool){
			if(subStr=='0'){
				newNumber = 0;
				continue;
			}else{
				bool = true;
			}
		}			
		newNumber=newNumber+subStr;
	}
	return newNumber;
}

//var r={'special':/[\W]/g} // quita todos los caracteres especiales. (solo letras y numeros)
var r={'special':/['"]/g}// quita apostrofe y comilla.
function validChar(o,w){
	o.value = o.value.replace(r[w],'');
}

//valida que solo se ingresen numeros enteros
function isInteger(event,txt, tariff) {
	if (txt === undefined){
		return;
	}
	if (tariff === undefined){
		return;
	}
	if (tariff != 'S' && txt.value.includes('.')){
		alert("FAVOR DE CAPTURAR N\u00DAMEROS ENTEROS");
		txt.value = txt.value.substring(0,txt.value.indexOf('.'));
	}
}

//valida que solo se ingresen numeros y 1 punto.
function isNumber(evt,txt) {
    var charCode = (evt.which) ? evt.which : evt.keyCode
    //Teclas flecha izq, der, abajo, arriba, Enter
    if(charCode == 37 || charCode == 38 || charCode == 39 || charCode == 40 || charCode == 13){
    	return true;			
    }
    if(charCode == 46){
        if (txt.value.indexOf('.') > -1) {
            return false;
        }
    }else if (charCode > 31 && (charCode < 48 || charCode > 57))
       return false;
    return true;
 }

/*funcion para evitar problema de nombre isNumber(*/
function isNumber22(obj){
   fval=trim(obj.value);   
   if(isNaN(fval)){
		alert("POR FAVOR, CAPTURE SOLO N\u00DAMEROS");
		obj.value = '';
		obj.focus();		
   }else{
		number = toNumber(fval);
		obj.value=number;
   }
}