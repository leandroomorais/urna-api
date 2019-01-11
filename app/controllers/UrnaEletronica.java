package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import models.CancelarVotacao;
import models.Candidato;
import models.FinalizarVotacao;
import models.IpTerminal;
import models.IpUrna;
import models.IpUrnaCache;
import models.Secao;
import models.Status;
import models.TempoVoto;
import models.UrnaTempoVotacao;
import models.Votacao;
import models.VotosCancelados;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;

public class UrnaEletronica extends Controller{
	
	private static boolean votoValido = false;
	private static boolean votoNulo = false;
	private static boolean votoBranco = false;
	private static String ipUrnaAtual = "";
	private static boolean recebeuIp = false;
	private static long idTempoVoto = 0;
	private static long idTempoVotoGeral = 0;
	//private static UrnaTempoVotacao urnaTempoVotacao = new UrnaTempoVotacao();
	private static final Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
	
	public static void index() {
		render();
	}
	
	
	public static void listIpUrna() {
		List<IpUrna> urnas = IpUrna.findAll();
		if(urnas.isEmpty()) {
			Status status = new Status();
			status.status = "Não existe ipUrnas";
			String json = g.toJson(status);
			renderJSON(json);
		}else {
			String json = g.toJson(urnas);
			renderJSON(json);
		}
	}

	public static void enviarVoto(int numCandidato, int idCargo, String nome, String ipUrna, String voto, String tipo){
		Votacao votacao = new Votacao();
		IpUrna ipUrna2 = IpUrna.find("ipUrna=?", ipUrna).first();
		votacao.ipUrna = ipUrna2;
		if(voto.equals("Branco")) {
			votacao.votoBranco = 1;
			votoBranco = true;
			votacao.save();
		}else if(tipo.equals("votosCancelados")) {
			System.out.println("ENTROU AQUI!!!!");
			VotosCancelados votos = new VotosCancelados();
			votos.data = new Date();
			votos.ipUrnaVotCancel = ipUrna2;
			votos.save();
			votacao.votoNulo = 1;
			votoNulo = true;
			votacao.save();
		}else if(voto.equals("Nulo")) {
			votacao.votoNulo = 1;
			votoNulo = true;
			votacao.save();
		}else if(voto.equals("Valido")){
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
	
	public static void emitirBoletim(String ipUrna) {
		IpUrna ipUrna2 = IpUrna.find("ipUrna=?", ipUrna).first();
		List<Candidato> list = new ArrayList<>();
		if(ipUrna2 == null) {
			Status status = new Status();
			status.status = "Não existe votos para esse IP";
			String json = g.toJson(status);
			renderJSON(json);
		}else {
			List<Votacao> votacaos = Votacao.find("id_ipurna =?", ipUrna2.id).fetch();
			long countValidos = Votacao.count("votoValido =?", (long)1);
			long countBranco = Votacao.count("votoBranco =?", (long)1);
			long countNulo = Votacao.count("votoNulo =?", (long)1);
			Votacao votacao = new Votacao();
			votacao.votoValido = countValidos;
			votacao.votoBranco = countBranco;
			votacao.votoNulo = countNulo;
			for(Votacao votacao2 : votacaos) {
				if((!votacao2.candidatos.isEmpty())) {
					list.addAll(votacao2.candidatos);
				}
			}
			votacao.candidatos = list;
			String json2 = g.toJson(votacao);
			renderJSON(json2);
			
		}
		
	}
	
	public static void setTerminal(String status) {
		if(status.equals("liberada") || status.equals("bloqueada")) {
			if(status.equals("liberada")){
				TempoVoto tempovoto = new TempoVoto();
				tempovoto.inicioVoto = new Date();
				IpUrna ipurna = IpUrna.find("ipUrna=?", ipUrnaAtual((long)1).ipUrnaCache).first();
				long u =  ipurna.id;
				UrnaTempoVotacao urna = UrnaTempoVotacao.find("ipUrna=?", u).first();
				tempovoto.tempoVotacaoGeral = urna;
				tempovoto.save();
				idTempoVoto = tempovoto.id;
			}
			if(status.equals("bloqueada")){
				TempoVoto tempovoto = TempoVoto.findById(idTempoVoto);
				tempovoto.fimVoto = new Date();
				tempovoto.save();
			}
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
	
	private static IpUrnaCache ipUrnaAtual(long id) {
		IpUrnaCache cache = IpUrnaCache.findById(id);
		return cache;
	}
	
	public static void cancelarVotacao(boolean cancelarVotacao) {
		if(cancelarVotacao) {
			long id = 1;
			Status status = Status.findById(id);
			status.status = "cancelou";
			status.save();
			if(isEmptyCancelarVotacao()) {
				IpUrna ipurna = IpUrna.find("ipUrna=?", ipUrnaAtual(1).ipUrnaCache).first();
				CancelarVotacao cancelarVotacao2 = new CancelarVotacao();
				cancelarVotacao2.status = cancelarVotacao;
				cancelarVotacao2.save();
				//long u =  ipurna.id;
				//UrnaTempoVotacao urna = UrnaTempoVotacao.find("ipUrna=?", u).first();
				ok();
			}else {
				long id2 = 1;
				CancelarVotacao cancelarVotacao2 = CancelarVotacao.findById(id2);
				cancelarVotacao2.status = cancelarVotacao;
				cancelarVotacao2.save();
				ok();
			}
			
		}else {
			long id2 = 1;
			Status status = Status.findById(id2);
			status.status = "bloqueada";
			status.save();
			if(isEmptyCancelarVotacao()) {
				CancelarVotacao cancelarVotacao2 = new CancelarVotacao();
				cancelarVotacao2.status = cancelarVotacao;
				cancelarVotacao2.save();
				ok();
			}else {
				long id = 1;
				CancelarVotacao cancelarVotacao2 = CancelarVotacao.findById(id);
				cancelarVotacao2.status = cancelarVotacao;
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
			
			//IpUrna ipurna = IpUrna.find("ipUrna", ipUrnaAtual).first();
			UrnaTempoVotacao urna = UrnaTempoVotacao.findById(idTempoVotoGeral);
			urna.fim = new Date();
			urna.save();
			//urnaTempoVotacao = new UrnaTempoVotacao();
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
	
	private static boolean existIpUrna(String ipUrna) {
		IpUrna ipUrna2 = IpUrna.find("ipUrna=?", ipUrna).first();
		if(ipUrna2 == null) { 
			return true;
		}
		return false;
	}
	
	public static void receberIpUrna(String ipUrna) {
		if(existIpUrna(ipUrna)) {
			ipUrnaAtual = ipUrna;
			IpUrnaCache cache2 = new IpUrnaCache();
			cache2.ipUrnaCache = ipUrna;
			cache2.save();
			recebeuIp = true;
			ok();
			System.out.println("OK");
		}else {
			System.out.println("ERRO");
			notFound();
		}
		
	}
		
	
	public static void enviarSecao(String idSecao, String ipTerminal, String ipUrna) {
		try {
			System.out.println("Entrou na funcao enviarSecao");
			IpUrnaCache ipUrnaCache = IpUrnaCache.find("ipUrnaCache=?", ipUrna).first();
			//if(recebeuIp) {
				if(verificarSecao(idSecao, ipTerminal) && ipUrnaCache != null) {
					//recebeuIp = false;
					IpTerminal ipTerminal2 = new IpTerminal();
					
					IpUrna ipUrna2 = new IpUrna();
					ipUrna2.ipUrna = ipUrna;
					ipUrna2.qtd_votosValidos = 0;
					ipUrna2.qtd_votosCancelados = 0;
					ipUrna2.save();
					
					ipTerminal2.ip = ipTerminal;
					ipTerminal2.save();
					//ipUrnaAtual = "";
					Secao secao2 = new Secao();
					secao2.secao = idSecao;
					secao2.terminal = ipTerminal2;
					secao2.ipUrna = ipUrna2;
					secao2.save();
					
					UrnaTempoVotacao urnaTempoVotacao = new UrnaTempoVotacao();
					urnaTempoVotacao.inicio = new Date();
					urnaTempoVotacao.ipUrnaVotacao = ipUrna2;
					urnaTempoVotacao.save();
					idTempoVotoGeral = urnaTempoVotacao.id;
					//urnaTempoVotacao = new UrnaTempoVotacao();
					ok();
				}else {
					System.out.println("Secao já está vinculada e o ipterminal");
					notFound("Secao já está vinculada e o ipterminal");
				}
			/*}else {
				System.out.println("A Urna não enviou o ip");
				notFound("A Urna não enviou o ip");
			}*/
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void getSecao(String ipUrna) {
		IpUrna ipUrna2 = IpUrna.find("ipUrna =?",ipUrna).first();
		if(ipUrna2 == null) {
			Status notFound = new Status();
			notFound.status = "Ip da urna não está vinculado a seção";
			String json = g.toJson(notFound);
			renderJSON(json);
		}
		System.out.println(ipUrna2.ipUrna);
		session.put("ipUrnaAtual", ipUrna2.id);
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
