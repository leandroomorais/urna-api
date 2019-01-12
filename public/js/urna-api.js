function servicoTerminal(){
	var result = {status: $("#status").val(), ipUrna: $("#id_ipUrna").val()};
	var result1 = {};
	
	if(($("#status").val() == "liberada" || $("#status").val() == "bloqueada") && $("#id_ipUrna").val() != ""){
		$('#resultado').html(JSON.stringify(result));	
	}else{
		$('#resultado').html(JSON.stringify(result1));	
	}
}

function servicoFinalizar(){
	var result2 = {status: $("#finalizar").val(), ipUrna: $("#id_ipUrna").val()};
	var result1 = {};
	if(($("#finalizar").val() == "true" || $("#finalizar").val() == "false") && $("#id_ipUrna").val() != "" ){
		$('#resultado2').html(JSON.stringify(result2));		
	}else{
		$('#resultado2').html(JSON.stringify(result1));	
	}
}

function servicoCancelar(){
	var result3 = {status: $("#cancelar").val(), ipUrna: $("#id_ipUrna").val()};
	var result1 = {};
	if(($("#cancelar").val() == "true" || $("#cancelar").val() == "false") && $("#id_ipUrna").val() != ""){
		$('#resultado3').html(JSON.stringify(result3));
	}else{
		$('#resultado3').html(JSON.stringify(result1));	
	}
}

function servicoSecao(){
	var result4 = {secao: $("#secao").val(), ip: $("#ipTerminal").val(), ipUrna: $("#id_ipUrna").val()};
	if($("#secao").val() != "" && $("#ipTerminal").val() != "" && ipUrna: $("#id_ipUrna").val() != ""){
		$('#resultado4').html(JSON.stringify(result4));	
	}
}

function servicoBoletim(){
	$.getJSON("https://urna-api.herokuapp.com/api/boletim/"+$("#ipUrna").val()).done(function(data){
		$("#resultadoIPUrna").text(JSON.stringify(data));
	});
}

