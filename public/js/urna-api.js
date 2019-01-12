function servicoTerminal(){
	var result = {status: $("#status").val(), ipUrna: $("#ipUrna1").val()};
	var result1 = {};
	
		if(($("#status").val() == "liberada" || $("#status").val() == "bloqueada") && ($("#ipUrna1").val() != "")){
			$('#resultado').html(JSON.stringify(result));
		}else{
			$('#resultado').html(JSON.stringify(result1));	
		}
	
}

function servicoFinalizar(){
	var result2 = {status: $("#finalizar").val(), ipUrna: $("#ipUrna2").val()};
	var result1 = {};
	if(($("#finalizar").val() == "true" || $("#finalizar").val() == "false") && $("#ipUrna2").val() != "" ){
		$('#resultado2').html(JSON.stringify(result2));		
	}else{
		$('#resultado2').html(JSON.stringify(result1));	
	}
}

function servicoCancelar(){
	var result3 = {status: $("#cancelar").val(), ipUrna: $("#ipUrna3").val()};
	var result1 = {};
	if(($("#cancelar").val() == "true" || $("#cancelar").val() == "false") && $("#ipUrna3").val() != ""){
		$('#resultado3').html(JSON.stringify(result3));
	}else{
		$('#resultado3').html(JSON.stringify(result1));	
	}
}

function servicoSecao(){
	var result4 = {secao: $("#secao").val(), ip: $("#ipTerminal").val(), ipUrna: $("#ipUrna5").val()};
	if($("#secao").val() != "" && $("#ipTerminal").val() != "" && $("#ipUrna5").val() != ""){
		$('#resultado4').html(JSON.stringify(result4));	
	}
}

function servicoBoletim(){
	$.getJSON("https://urna-api.herokuapp.com/api/boletim/"+$("#ipUrna4").val()).done(function(data){
		$("#resultadoIPUrna").text(JSON.stringify(data));
	});
}

