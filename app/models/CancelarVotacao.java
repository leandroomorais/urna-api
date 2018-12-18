package models;

import javax.persistence.Entity;

import com.google.gson.annotations.Expose;

import play.db.jpa.Model;

@Entity
public class CancelarVotacao extends Model{
	@Expose
	public boolean status;
}
