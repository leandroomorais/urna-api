package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.google.gson.annotations.Expose;

import play.db.jpa.Model;
@Entity
public class IpTerminal extends Model{
	@Expose
	public String ip;
	@OneToMany(mappedBy="terminal")
	public List<Secao> secaos;
}
