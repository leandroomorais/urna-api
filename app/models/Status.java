package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.gson.annotations.Expose;

import play.db.jpa.Model;

@Entity
public class Status extends Model{
	@Expose
	public String status;
	
	@ManyToOne
	@JoinColumn(name="id_ipUrna")
	public IpUrna ipUrna;
}
