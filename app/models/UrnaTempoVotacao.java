package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;
@Entity
public class UrnaTempoVotacao extends Model{
	
	public Date inicio;
	public Date fim;
	
	@OneToMany(mappedBy="tempoVotacaoGeral")
	public List<TempoVoto> tempoPorVoto;
	
	@OneToOne
	@JoinColumn(name="ipUrna")
	public IpUrna ipUrnaVotacao;

}
