package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

public class Zona extends Model{
	public String nome;
	public int numZona;
	
	@OneToMany(mappedBy="zona")	
	public List<Candidato> candidatos;
}
