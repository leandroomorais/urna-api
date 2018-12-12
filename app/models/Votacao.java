package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.google.gson.annotations.Expose;

import play.db.jpa.Model;

@Entity
public class Votacao extends Model{
	@Expose
    public int votoBranco;
	@Expose
    public int votoNulo;
	@Expose
    public int votoValido;
    
    @Expose
    @ManyToMany(mappedBy="votoValidos")
	public List<Candidato> candidatos;
}
