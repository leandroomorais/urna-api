package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class TempoVoto extends Model{
	
	public Date inicioVoto;
	public Date fimVoto;
	
	@ManyToOne
	public UrnaTempoVotacao tempoVotacaoGeral;
	

}
