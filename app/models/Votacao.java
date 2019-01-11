package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.google.gson.annotations.Expose;

import play.db.jpa.Model;

@Entity
public class Votacao extends Model{
	
	 @Expose
    public long votoBranco;
	 @Expose
	public long votoNulo;
	 @Expose
	public long votoValido;

    	@ManyToOne
	@JoinColumn(name="id_ipUrna")
	public IpUrna ipUrna;

    @Expose
    @ManyToMany(mappedBy="votoValidos")
	public List<Candidato> candidatos;
}
