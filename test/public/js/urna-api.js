function servicoTerminal(){
	var result = {status: $("#status").val()};
	var result1 = {};
	if($("#status").val() == "liberada" || $("#status").val() == "bloqueda"){
		$('#resultado').html(JSON.stringify(result));	
	}else{
		$('#resultado').html(JSON.stringify(result1));	
	}
		
}

function servicoFinalizar(){
	var result2 = {status: $("#finalizar").val()};
	var result1 = {};
	if($("#finalizar").val() == "true" || $("#finalizar").val() == "false"){
		$('#resultado2').html(JSON.stringify(result2));		
	}else{
		$('#resultado2').html(JSON.stringify(result1));	
	}
}

function servicoCancelar(){
	var result3 = {status: $("#cancelar").val()};
	var result1 = {};
	if($("#cancelar").val() == "true" || $("#cancelar").val() == "false"){
		$('#resultado3').html(JSON.stringify(result3));
	}else{
		$('#resultado3').html(JSON.stringify(result1));	
	}
}

function servicoSecao(){
	var result4 = {secao: $("#secao").val(), ip: $("#ipTerminal").val()};
	$('#resultado4').html(JSON.stringify(result4));	
}

