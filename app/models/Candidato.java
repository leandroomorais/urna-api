 package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.google.gson.annotations.Expose;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
public class Candidato extends GenericModel{
	@Expose
	@Id
    @GeneratedValue
    public Long id;
	
	@Expose
	public String nome;
	@Expose
	public int numero;
	@Expose
	public int totalVotos;
	
	@ManyToMany
	@JoinTable(name="votos_ampurados")
	public List<Votacao> votoValidos;
	
	
}
