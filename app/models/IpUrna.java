package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.google.gson.annotations.Expose;

import play.db.jpa.Model;
@Entity
public class IpUrna extends Model{
	public String ipUrna;
	public int qtd_votosValidos;
	public int qtd_votosCancelados;
	@Expose
	@OneToMany(mappedBy="ipUrna")
	public List<Secao> secao;
	
	@OneToOne(mappedBy="ipUrnaVotacao")
	public UrnaTempoVotacao urnaTempoVotacao;
	
	@OneToMany(mappedBy="ipUrnaVotCancel")
	public List<VotosCancelados> votoscancelados;
}
