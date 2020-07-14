public class FichierSysteme extends Element implements Cloneable {
	private String chaine;

	public String getType(){ return Element.FICHIER_SYSTEME; }

	public FichierSysteme(String s){ chaine=s; }

	public FichierSysteme clone() { 
		return new FichierSysteme(chaine);
	}

}