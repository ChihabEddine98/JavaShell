import java.util.LinkedList;
import java.util.Stack; // pour l'affichage
public class Dossier extends Element implements Affichable,Cloneable{

	private Entree parent;
	private Entree moiMeme;
	private LinkedList<Entree> contenu = new LinkedList<Entree>();

	public boolean isRacine(){ return false; }

	// soit on crée un dossier en lui donnant un nom directement,
	public Dossier(Dossier p, String s){
		parent=((p==null)?null:p.getParent());
		moiMeme=new Entree(p,s,this);
		contenu=new LinkedList<Entree>();
		if (p!=null){
			p.add(this,s);
		}
	}


	// soit on crée un dossier sans nom et on l'ajoute ensuite au dossier parent hors du constructeur
	// (de la même manière que les autres Elements)
	public Dossier(Dossier p){
		parent=((p==null)?null:p.getParent());
		moiMeme=new Entree(p,".",this);
		contenu=new LinkedList<Entree>();
	}

	public Dossier clone(){ return clone((Dossier)parent.getElement()); }

	public Dossier clone(Dossier dossier){
		Dossier d=new Dossier(dossier,moiMeme.getNom());
		for (Entree e : contenu){
			d.add(e.clone(dossier));
		}
		return d;
	}

	public String getType(){ return Element.DOSSIER; }
	public Entree getParent(){ return parent; }
	public Entree getMoiMeme(){ return moiMeme; }
	public Dossier getContenant(){ return (parent==null)?null:(Dossier)parent.getElement(); }

	public void supprimer(String s){
		for (int i=contenu.size()-1;i>=0;i--){
			if (s.equals(contenu.get(i).getNom())){ contenu.remove(i); break; }
			// dans un même dossier, chaque élément possède un nom différent
		}
	}

	public String getChemin(){
		return moiMeme.getChemin();
	}

	public void add(Entree e){
		e.getContenant().retirer(e);
		add(e.getElement(),e.getNom());
	}

	public void add(Element element, String nom){
		lireEntree(nom,true).inserer(element);
	}

	public void afficher(){
		afficher(false,false,false);
	}

	// 	   recursif : affiche le contenu des sous dossiers 		/ seul le dossier courant est affiché 
	// absolutePath : le chemin affiché est absolu 				/ le chemin affiché est relatif
	//   pathForAll : le chemin est affiché pour chaque dossier / le chemin n'est affiché que pour le dossier en cours d'affichage
	public void afficher(boolean recursif, boolean absolutePath, boolean pathForAll){
		if (parent!=null)	System.out.println((absolutePath)?parent.getChemin()+parent.getNom()+"/":"../");
		String tmp=((absolutePath)?moiMeme.getChemin()+moiMeme.getNom():".")+"/";

		Stack<Entree> entrees=new Stack<Entree>(); entrees.push(moiMeme);
		Stack<String> chemin=new Stack<String>(); chemin.push(tmp);
		// on utilise cette seconde pile pour éviter d'appeler 'getChemin()' sur chaque Entree

		System.out.println(tmp);
		tmp="";

		do{
			Entree e=entrees.pop();
			String s=chemin.pop();

			if (!pathForAll)
				System.out.println(tmp+s+":");

			tmp="\n";
			LinkedList<Entree> liste=((Dossier)e.getElement()).contenu;

			for (Entree entree : liste){
				boolean dossier=entree.estDossier();
				String chem=entree.getNom()+((dossier)?"/":"");
				if (pathForAll)
					System.out.println(s+chem);
				else
					System.out.print(chem+"    ");

				if (recursif && dossier){
					entrees.push(entree);
					chemin.push(s+chem);
				}
			}
		}while(!entrees.empty());
	}

	public void setParent(Entree e){ parent=e; }

	public Entree lireEntree(String nom){
		return lireEntree(nom,false);
	}

	public Entree lireEntree(String nom, boolean b){
		if (nom.equals(".")) return moiMeme;
		if (nom.equals("..")) return parent;
		for (int i=contenu.size()-1;i>=0;i--){
			if (nom.equals(contenu.get(i).getNom())) return contenu.get(i);
		}
		Entree e=null;
		if (b){
			e=new Entree(this,nom,null);
			contenu.add(e);
		}
		return e;
	}

	public void supprimerRecursif(){ // supprime le dossier sur lequel la méthode est appelée ainsi que son contenu
		Stack<Entree> s=new Stack<Entree>();
		for (Entree e : contenu){
			s.push(e);	
		}
		while (!s.empty()){ s.pop().supprimer(); }
		moiMeme.supprimer();
	}

	public void retirer(Entree e){
		contenu.remove(e);
	}


}
