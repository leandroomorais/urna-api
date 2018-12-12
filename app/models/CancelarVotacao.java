package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class CancelarVotacao extends Model{
	public boolean status;
}
