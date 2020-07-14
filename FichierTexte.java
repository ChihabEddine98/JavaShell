public class FichierTexte extends Element implements Affichable,Cloneable {
	private String chaine;

	public void afficher(){
		System.out.println(chaine);
	}

	public void setContenu(String s){ chaine=s; }

	public FichierTexte(String s){ chaine=s; }

	public String getType(){ return Element.FICHIER_TEXTE; }

	public FichierTexte clone() { 
		return new FichierTexte(chaine);
	}
}