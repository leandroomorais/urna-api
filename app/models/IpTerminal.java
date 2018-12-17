package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.google.gson.annotations.Expose;

import play.db.jpa.Model;
@Entity
public class IpTerminal extends Model{
	@Expose
	public String ip;
	@OneToOne(mappedBy="terminal")
	public Secao secao;
}
