package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.google.gson.annotations.Expose;

import play.db.jpa.Model;

@Entity
public class Secao extends Model{
	@Expose
	public String secao;
	@Expose
	@ManyToOne
	@JoinColumn(name="id_terminal")
	public IpTerminal terminal;
	@ManyToOne
	@JoinColumn(name="id_ip_urna")
	public IpUrna ipUrna;
	
}
