package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class VotosCancelados extends Model{
	
	public Date data;
	
	@ManyToOne
	@JoinColumn(name="id_ipUrna")
	public IpUrna ipUrnaVotCancel;

}
