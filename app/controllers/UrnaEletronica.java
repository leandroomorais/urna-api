package controllers;

 

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.CipherInputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.CancelarVotacao;
import models.Candidato;
import models.Cargo;
import models.FinalizarVotacao;
import models.IpTerminal;
import models.IpUrna;
import models.Partido;
import models.Secao;
import models.Status;
import models.UrnaTempoVotacao;
import models.Votacao;
import oauth.signpost.http.HttpRequest;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Router;
import play.mvc.Router.Route;

public class UrnaEletronica extends Controller{
	
	private static boolean votoValido = false;
	private static boolean votoNulo = false;
	private static boolean votoBranco = false;
	private static final Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
	
	public static void index() {
		render();
	}

	public static void enviarVoto(int numCandidato, int idCargo, String nome, String ipUrna, String voto){
		Votacao votacao = new Votacao();
		if(voto.equals("Branco")) {
			votacao.votoBranco = 1;
			votoBranco = true;
			votacao.save();
		}else if(voto.equals("Nulo")) {
			votacao.votoNulo = 1;
			votoNulo = true;
			votacao.save();
		}else {
			votoValido = true;
			votacao.votoValido = 1;
			votacao.save();
			Candidato candidato = new Candidato();
			candidato.nome = nome;
			candidato.numero = numCandidato;
			if(existCandidato(candidato)) {
				candidato = Candidato.findById(getCandidato(candidato).id);
				List<Votacao> votosValidos = new ArrayList<>();
				votosValidos.add(votacao);
				candidato.votoValidos = votosValidos;
				candidato.totalVotos = getTotalVotos(candidato).totalVotos + 1;
			}else {
				candidato.totalVotos = 1;
				List<Votacao> votosValidos = new ArrayList<>();
				votosValidos.add(votacao);
				candidato.votoValidos = votosValidos;
			}
			
			candidato.save();
		}
		if(votoValido) {
			Map paramentros = new HashMap<>();
			paramentros.put("numCandidato", numCandidato);
			paramentros.put("idCargo", idCargo);
			paramentros.put("ipUrna", ipUrna);
			HttpResponse response = WS.url("http://tse.vps.leandrorego.com/api/setVotoEleitor").setParameters(paramentros).post();
			votoValido = false;
		}else if(votoBranco) {
			Map paramentros = new HashMap<>();
			paramentros.put("numCandidato", numCandidato);
			paramentros.put("idCargo", idCargo);
			paramentros.put("ipUrna", ipUrna);
			HttpResponse response = WS.url("http://tse.vps.leandrorego.com/api/setVotoEleitor").setParameters(paramentros).post();
			votoBranco = false;
		}else if(votoNulo) {
			Map paramentros = new HashMap<>();
			paramentros.put("numCandidato", numCandidato);
			paramentros.put("idCargo", idCargo);
			paramentros.put("ipUrna", ipUrna);
			HttpResponse response = WS.url("http://tse.vps.leandrorego.com/api/setVotoEleitor").setParameters(paramentros).post();
			votoNulo = false;
		}
	}
	
	
	
	private static boolean existCandidato(Candidato candidato) {
		Candidato candidato2 = Candidato.find("numero=? and nome=?", candidato.numero, candidato.nome).first();
		if(candidato2 != null) {
			return true;
		}
		return false;
	}
	
	private static Candidato getCandidato(Candidato candidato) {
		Candidato candidato2 = Candidato.find("numero=? and nome=?", candidato.numero, candidato.nome).first();
		if(candidato2 != null) {
			return candidato2;
		}
		return null;
	}
	
	private static Candidato getTotalVotos(Candidato candidato) {
		Candidato candidato2 = Candidato.find("numero=? and nome=?", candidato.numero, candidato.nome).first();
		if(candidato2 != null) {
			return candidato2;
		}
		return null;
	}
	
	public static void emitirBoletim() {
		List<Votacao> votacaos = Votacao.findAll();
		long countValidos = Votacao.count("votoValido =?", (long)1);
		long countBranco = Votacao.count("votoBranco =?", (long)1);
		long countNulo = Votacao.count("votoNulo =?", (long)1);
		List<Votacao> list = new ArrayList<>();
		for(Votacao votacao : votacaos) {
			if((!votacao.candidatos.isEmpty())) {
				votacao.votoValido = countValidos;
				votacao.votoBranco = countBranco;
				votacao.votoNulo = countNulo;
				list.add(votacao);
			}
		}
		String json = g.toJson(list);
		renderJSON(json);
	}
	
	public static void setTerminal(String status) {
		if(status.equals("liberada") || status.equals("bloqueada")) {
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
		}else {
			notFound();
		}
	}
	
	public static void cancelharVotacao(boolean cancelharVotacao) {
		if(cancelharVotacao) {
			long id = 1;
			Status status = Status.findById(id);
			status.status = "cancelou";
			status.save();
			if(isEmptyCancelarVotacao()) {
				CancelarVotacao cancelarVotacao = new CancelarVotacao();
				cancelarVotacao.status = cancelharVotacao;
				cancelarVotacao.save();
				ok();
			}else {
				long id2 = 1;
				CancelarVotacao cancelarVotacao2 = CancelarVotacao.findById(id2);
				cancelarVotacao2.status = cancelharVotacao;
				cancelarVotacao2.save();
				ok();
			}
		}else {
			long id2 = 1;
			Status status = Status.findById(id2);
			status.status = "bloqueada";
			status.save();
			if(isEmptyCancelarVotacao()) {
				CancelarVotacao cancelarVotacao = new CancelarVotacao();
				cancelarVotacao.status = cancelharVotacao;
				cancelarVotacao.save();
				ok();
			}else {
				long id = 1;
				CancelarVotacao cancelarVotacao2 = CancelarVotacao.findById(id);
				cancelarVotacao2.status = cancelharVotacao;
				cancelarVotacao2.save();
				ok();
			}
		}
	}
	
	public static void finalizarVotacao(boolean finalizar) {
		if(finalizar) {
			long id = 1;
			Status status = Status.findById(id);
			status.status = "finalizou";
			status.save();
			if(isEmptyFinalizadaVotacao()) {
				FinalizarVotacao finalizarVotacao = new FinalizarVotacao();
				finalizarVotacao.status = finalizar;
				finalizarVotacao.save();
				ok();
			}else {
				long id2 = 1;
				FinalizarVotacao finalizarVotacao = FinalizarVotacao.findById(id2);
				finalizarVotacao.status = finalizar;
				finalizarVotacao.save();
				ok();
			}

			UrnaTempoVotacao urna = new UrnaTempoVotacao();
			urna.fim = new Date();
			urna.save();
			
		}else {
			long id = 1;
			Status status = Status.findById(id);
			status.status = "bloqueada";
			status.save();
			if(isEmptyFinalizadaVotacao()) {
				FinalizarVotacao finalizarVotacao = new FinalizarVotacao();
				finalizarVotacao.status = finalizar;
				finalizarVotacao.save();
				ok();
			}else {
				long id2 = 1;
				FinalizarVotacao finalizarVotacao = FinalizarVotacao.findById(id2);
				finalizarVotacao.status = finalizar;
				finalizarVotacao.save();
				ok();
			}
		}
	}
	
	
	
	public static void enviarSecao(String idSecao, String ipTerminal) {
		try {
			if(verificarSecao(idSecao, ipTerminal)) {
				String ipUrna = InetAddress.getLocalHost().getHostAddress();
				IpTerminal ipTerminal2 = new IpTerminal();
				IpUrna ipUrna2 = new IpUrna();
				ipUrna2.ipUrna = ipUrna;
				ipUrna2.save();
				ipTerminal2.ip = ipTerminal;
				ipTerminal2.save();
				Secao secao2 = new Secao();
				secao2.secao = idSecao;
				secao2.terminal = ipTerminal2;
				secao2.ipUrna = ipUrna2;
				secao2.save();
				ok();
			}else {
				notFound();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void getSecao(String ipUrna) {
		IpUrna ipUrna2 = IpUrna.find("ipUrna =?",ipUrna).first();
		if(ipUrna2 == null) {
			Status notFound = new Status();
			notFound.status = "Ip não vingulado";
			String json = g.toJson(notFound);
			renderJSON(json);
		}
		String json = g.toJson(ipUrna2);
		renderJSON(json);
	}
	
	private static boolean verificarSecao(String idSecao, String ipTerminal) {
		Secao secao2 = Secao.find("secao =?", idSecao).first();
		IpTerminal terminal = IpTerminal.find("ip=?", ipTerminal).first();
		if(secao2 == null && terminal == null) {
			return true;
		}
		return false;
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
