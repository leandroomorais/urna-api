package controllers;

 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.CancelarVotacao;
import models.Candidato;
import models.Cargo;
import models.FinalizarVotacao;
import models.Partido;
import models.Secao;
import models.Status;
import models.Votacao;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;

public class UrnaEletronica extends Controller{
	private static boolean votoValido = false;
	private static boolean votoNulo = false;
	private static boolean votoBranco = false;
	private static final Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
	
	public static void main() {
		render();
	}

	public static void enviarVoto(String partido, String cargo, int numero, String nome){
		Votacao votacao = new Votacao();
		if(partido.equals("Voto Branco") && cargo.equals("Voto Branco") && numero == -1 && nome.equals("Voto Branco")) {
			votacao.votoBranco = 1;
			votoBranco = true;
			votacao.save();
		}else if(partido.equals("Voto Nulo") && cargo.equals("Voto Nulo") && numero == -2 && nome.equals("Voto Nulo")) {
			votacao.votoNulo = 1;
			votoNulo = true;
			votacao.save();
		}else {
			Partido partido2 = new Partido();
			partido2.sigla = partido;
			partido2.save();
			
			Cargo cargo2 = new Cargo();
			cargo2.cargo = cargo;
			cargo2.save();
	 
			votoValido = true;
			votacao.votoValido = 1;
			
			votacao.save();
			
			Candidato candidato = new Candidato();
			candidato.nome = nome;
			candidato.cargo = cargo2;
			candidato.numero = numero;
			candidato.partido = partido2;
			List<Votacao> votosValidos = new ArrayList<>();
			votosValidos.add(votacao);
			candidato.votoValidos = votosValidos;
			candidato.save();
		}
		if(votoValido) {
			Map paramentros = new HashMap<>();
			paramentros.put("partido", partido);
			paramentros.put("cargo", cargo);
			paramentros.put("numero", numero);
			paramentros.put("nome", nome);
			paramentros.put("voto", votacao.votoValido);
			HttpResponse response = WS.url("http://localhost:9090/receberVotos").setParameters(paramentros).post();
			votoValido = false;
		}else if(votoBranco) {
			Map paramentros = new HashMap<>();
			paramentros.put("partido", partido);
			paramentros.put("cargo", cargo);
			paramentros.put("numero", numero);
			paramentros.put("nome", nome);
			paramentros.put("voto", -1);
			HttpResponse response = WS.url("http://localhost:9090/receberVotos").setParameters(paramentros).post();
			votoBranco = false;
		}else if(votoNulo) {
			Map paramentros = new HashMap<>();
			paramentros.put("partido", partido);
			paramentros.put("cargo", cargo);
			paramentros.put("numero", numero);
			paramentros.put("nome", nome);
			paramentros.put("voto", -2);
			HttpResponse response = WS.url("http://localhost:9090/receberVotos").setParameters(paramentros).post();
			votoNulo = false;
		}
	}
	
	public static void emitirBoletim() {
		
	}
	
	public static void setTerminal(String status) {
		if(isEmptyStatus()) {
			Status status3 = new Status();
			status3.status = status;
			status3.save();
			ok();
		}else {
			long id = 1;
			Status status2 = Status.findById(id);
			status2.status = status;
			status2.save();
			ok();
		}
	}
	
	public static void finalizarVotacao(boolean finalizar) {
		if(finalizar) {
			if(isEmptyFinalizadaVotacao()) {
				FinalizarVotacao finalizarVotacao = new FinalizarVotacao();
				finalizarVotacao.status = finalizar;
				finalizarVotacao.save();
				ok();
			}else {
				long id = 1;
				FinalizarVotacao finalizarVotacao = FinalizarVotacao.findById(id);
				finalizarVotacao.status = finalizar;
				finalizarVotacao.save();
				ok();
			}
		}else {
			if(isEmptyFinalizadaVotacao()) {
				FinalizarVotacao finalizarVotacao = new FinalizarVotacao();
				finalizarVotacao.status = finalizar;
				finalizarVotacao.save();
				ok();
			}else {
				long id = 1;
				FinalizarVotacao finalizarVotacao = FinalizarVotacao.findById(id);
				finalizarVotacao.status = finalizar;
				finalizarVotacao.save();
				ok();
			}
		}
	}
	
	public static void cancelharVotacao(boolean cancelharVotacao) {
		if(cancelharVotacao) {
			if(isEmptyCancelarVotacao()) {
				CancelarVotacao cancelarVotacao = new CancelarVotacao();
				cancelarVotacao.status = cancelharVotacao;
				cancelarVotacao.save();
				ok();
			}else {
				long id = 1;
				CancelarVotacao cancelarVotacao = CancelarVotacao.findById(id);
				cancelarVotacao.status = cancelharVotacao;
				cancelarVotacao.save();
				ok();
			}
		}else {
			if(isEmptyCancelarVotacao()) {
				CancelarVotacao cancelarVotacao = new CancelarVotacao();
				cancelarVotacao.status = cancelharVotacao;
				cancelarVotacao.save();
				ok();
			}else {
				long id = 1;
				CancelarVotacao cancelarVotacao = CancelarVotacao.findById(id);
				cancelarVotacao.status = cancelharVotacao;
				cancelarVotacao.save();
				ok();
			}
		}
	}
	
	public static void enviarSecao(String secao) {
		Secao secao2 = new Secao();
		secao2.secao = secao;
		secao2.save();
		ok();
	}
	
	private static boolean isEmptyStatus() {
		List<Status> status = Status.findAll();
		if(status.isEmpty()) {
			return true;
		}
		return false;
	}
	
	private static boolean isEmptyFinalizadaVotacao() {
		List<FinalizarVotacao> list = FinalizarVotacao.findAll();
		if(list.isEmpty()) {
			return true;
		}
		return false;
	}
	
	private static boolean isEmptyCancelarVotacao() {
		List<CancelarVotacao> list = CancelarVotacao.findAll();
		if(list.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static void getCancelarVotacao() {
		long id = 1;
		CancelarVotacao cancelarVotacao = CancelarVotacao.findById(id);
		String json = g.toJson(cancelarVotacao);
		renderJSON(json);
	}
	
	public static void getFinalizadaVotacao() {
		long id = 1;
		FinalizarVotacao finalizarVotacao = FinalizarVotacao.findById(id);
		String json = g.toJson(finalizarVotacao);
		renderJSON(json);
	}
	
	public static void getTerminal() {
		long id = 1;
		Status status = Status.findById(id);
		String json = g.toJson(status);
		renderJSON(json);
	}
	
}
