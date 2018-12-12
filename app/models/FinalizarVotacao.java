package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class FinalizarVotacao extends Model{
	public boolean status;
}
