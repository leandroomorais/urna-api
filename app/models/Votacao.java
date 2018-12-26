package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.google.gson.annotations.Expose;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
public class Votacao extends Model{
	
	 @Expose
    public long votoBranco;
	 @Expose
	public long votoNulo;
	 @Expose
	public long votoValido;

    @Expose
    @ManyToMany(mappedBy="votoValidos")
	public List<Candidato> candidatos;
}
