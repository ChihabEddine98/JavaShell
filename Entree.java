public class Entree implements Cloneable {
	private Dossier contenant;
	private String nom ;
	private Element element ;
	public Entree (Dossier c, String n, Element e) {
		contenant=c;
		nom=n;
		element=e;
	}

	public void setParent(Dossier d){ if (d==null){ System.out.println("Erreur, parent ne peut être null"); System.exit(1); } contenant=d; }
	public void setNom(String s){ nom=s; }

	public boolean estDossier(){
		return (element instanceof Dossier);
	}

	public String getChemin(){
		StringBuilder s=new StringBuilder();
		Dossier e=this.contenant;
		while (e!=null){
			s.insert(0,e.getMoiMeme().getNom()+"/");
			e=e.getMoiMeme().contenant;
		}
		return s.toString();
	}

	public String getNom(){ return nom; }
	public Element getElement(){ return element; }
	public Dossier getContenant(){ return contenant; }
	public Entree getParent(){ return ((contenant==null)?null:contenant.getMoiMeme()); }

	@Override
	public String toString(){
		return nom+" "+element.getType();
	}

	public void supprimer(){ contenant.supprimer(nom); contenant=null; if (element instanceof Dossier) ((Dossier)element).setParent(null); }
	public void inserer(Element e){
		element=e;
		if (e instanceof Dossier) ((Dossier)e).setParent(contenant.getMoiMeme());
	}


	public Entree clone(){
		return clone(contenant);
	}

	public Entree clone(Dossier d){
		if (element instanceof Dossier) return ((Dossier)element).clone(d).getMoiMeme();
		else if (element instanceof FichierTexte) return new Entree(d,nom,((FichierTexte)element).clone());
		else if (element instanceof FichierSysteme) return new Entree(d,nom,((FichierSysteme)element).clone());
		else return null;		
	}
}