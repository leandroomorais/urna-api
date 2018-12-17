package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.google.gson.annotations.Expose;

import play.db.jpa.Model;

@Entity
public class Secao extends Model{
	@Expose
	public String secao;
	@Expose
	@OneToOne
	@JoinColumn(name="id_terminal")
	public IpTerminal terminal;
	
}
