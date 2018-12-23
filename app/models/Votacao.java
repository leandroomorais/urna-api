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
	
    public long votoBranco;
	public long votoNulo;
	public long votoValido;

    @Expose
	public long contValidos = 0;
    @Expose
	public long contBranco = 0;
    @Expose
	public long contNulo = 0;
    
    @Expose
    @ManyToMany(mappedBy="votoValidos")
	public List<Candidato> candidatos;
}
