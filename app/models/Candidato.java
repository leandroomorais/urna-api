 package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.google.gson.annotations.Expose;

import play.db.jpa.Model;


@Entity
public class Candidato extends  Model{
	
	@Expose
	public String nome;
	@Expose
	@OneToOne
	@JoinColumn(name="cargo_candidato")
	public Cargo cargo;
	@Expose
	@OneToOne
	@JoinColumn(name="partido_candidato")
	public Partido partido;
	@Expose
	public int numero;
	@ManyToMany
	@JoinTable(name="votos_ampurados")
	public List<Votacao> votoValidos;
	@Expose
	@ManyToOne
	@JoinColumn(name="zona_id")
	public Zona zona;
	
}
