import java.util.Scanner;
import java.util.ArrayList;
public class Shell{

	private static Scanner sc;
	static{	
		try{ 
			sc=new Scanner(System.in); 
		}catch(Exception e){ 
			System.out.println(e); System.exit(1); 
		}
	}

	private final Racine racine;
	private Dossier courant;

	public Shell(Racine r){ 
		racine=r;
		courant=r;
	}

	public void executer(){
		String[] commande;
		do{
			System.out.print(courant.getChemin()+courant.getMoiMeme().getNom()+" /$ => ");
			commande=lireCommande();
			if (commande==null || commande.length==0) continue;
			switch(commande[0]){
				case "quit" : return;
				case "ls": ls(commande); break;
				case "rm": rm(commande); break;
				case "cp": cp(commande); break;
				case "mv": mv(commande); break;
				case "man": man (commande); break;
				case "cd": cd(commande); break;
				case "mkdir": mkdir(commande); break;
				case "touch": touch(commande); break;
				case "cat": cat(commande); break;
				case "edite": edite(commande); break;
				default: commandeInvalide();
			}
		}while(true);
	}

	/*
		on a hésité à créer une classe abstraite ou une interface "commande"
		et puis une classe par commande
		Et implémenter dans chaque classe les méthodes nécéssaires ("execute", "getArguments",...)
		Mais cela aurait fait beaucoup de classes en plus et aurait compliqué le programme pour pas grand chose
	*/

	private void ls(String[] commande){
		// -R affiche récursivement le contenu du dossier
		// -l affiche le chemin absolu plutôt que relatif
		// -a affiche le chemin de chaque fichier
		boolean r=false,l=false,a=false;
		boolean parametres=false;
		boolean dossier=false;
		for(int i=1;i<commande.length;i++){
			String s=commande[i];
			// si la chaine de caractère commence par un - et ne contient que des lettres d'option valide (rRla), on considère qu'il s'agit de parametres
			if (!parametres && s.length()>1 && s.charAt(0)=='-'){
				boolean rr=false, ll=false, aa=false;
				for (int j=1;j<s.length();j++){
					char c=s.charAt(j);
					if (c=='r' || c=='R') rr=true; 
					else if(c=='l') ll=true;
					else if (c=='a') aa=true;
					else{
						parametres=true;
						dossier=true;
						break;
					}
				}
				if (!parametres){
					r=r || rr; l=l || ll; a=a || aa;
				}
			}else{ parametres=true; dossier=true; }
			if (parametres){
				dossier=true;
				Entree x=this.acceder(s);
				if (x==null) cheminInvalide("ls",s);
				else ((Dossier)x.getElement()).afficher(r,l,a);
			}
		}
		if(!dossier) courant.afficher(r,l,a);
		System.out.println("");
	}

	private void cd(String[] commande){ //pas d'arguments
		if (commande.length==1) return;
		if (commande.length!=2 || commande[1]==null){ commandeInvalide(); return; }
		String chemin=commande[1];
		Entree e=acceder(chemin);
		if (e==null  || !(e.getElement() instanceof Dossier)) cheminInvalide("cd",chemin);
		else courant=(Dossier) e.getElement();
	}
	private void touch(String[] commande){ //pas d'arguments
		if (commande.length<2){ commandeInvalide(); return; }
		for (int i=1;i<commande.length;i++){
			String s=commande[i];
			Entree e=acceder(s,true);

			if (e==null) cheminInvalide("touch",s);
			else if (e.getElement()==null){
				e.inserer(new FichierTexte(""));
			}
		}
	}
	private void mkdir(String[] commande){ // pas d'arguments
		if (commande.length<2){ commandeInvalide(); return; }
		for (int i=1;i<commande.length;i++){
			String s=commande[i];
			Entree e=acceder(s,true);

			if (e==null) cheminInvalide("mkdir",s);
			else if (e.getElement()==null){
				e.inserer(new Dossier(e.getContenant()));
			}
		}
	}
	private void man(String[] commande){ //pas d'arguments
		if (commande.length<2){ commandeInvalide(); return; }
		for (int i=1;i<commande.length;i++){
			String s=commande[i];
			switch (s){
				case "man" : System.out.println("Affiche le manuel concernant la commande qu'on lui donne en option."); break;
				case "ls" : System.out.println("Affiche le contenu d'un dossier dont le chemin est donné en parametre.\nPossède 3 paramètres possibles : \n    -R ou -r pour l'affichage récursif (affichage du contenu des sous dossiers)\n    -l pour l'affichage du chemin absolu (par défaut le chemin relatif est affiché)\n    -a pour que le chemin de chaque fichier soit affiché (au lieu d'avoir juste le chemin du dossier d'affiché)"); break;
				case "mkdir" : System.out.println("Crée un dossier vide au chemin indiqué. Ne crée pas les dossiers intermédiaires."); break;
				case "touch" : System.out.println("Si le chemin donné en parametres ne correspond pas à un fichier ou à un dossier existant, crée un fichier."); break;
				case "rm" : System.out.println("Supprime le fichier donné en parametres. Si le paramètre -R est donné cela supprime les dossiers et leurs contenus."); break;
				case "cp" : System.out.println("Copie le premier chemin donné en parametres dans le second. Si le chemin donné en second finit par un /, on copie sans changer le nom dans le dossier indiqué, sinon on copie au chemin indiqué en renommant selon la fin du chemin. Le fichier/dossier source et la copie sont indépendants : modifier l'un ne modifie pas l'autre."); break;
				case "mv" : System.out.println("Déplace le premier chemin donné en parametres au second. Si le chemin donné en second finit par un /, on déplace sans changer le nom dans le dossier indiqué, sinon on déplace au chemin indiqué en renommant selon la fin du chemin."); break;
				case "cat" : System.out.println("Affiche le contenu du fichier donné en parametres."); break;
				case "edite" : System.out.println("Modifie le fichier donné en parametres (s'il peut l'être). Le contenu de ce fichier devient alors ce qui est entré à la ligne suivante."); break;
				default : commandeInvalide();
			}
		}
	}
	private void cat(String[] commande){ //pas d'arguments
		if (commande.length<2){ commandeInvalide(); return; }
		for (int i=1;i<commande.length;i++){
			String s=commande[i];
			Entree e=acceder(s,false);
			if (e==null) cheminInvalide("cat",s);
			else if(!(e.getElement() instanceof FichierTexte)) System.out.println(e.getNom()+" ne peut être affiché");
			else ((FichierTexte)e.getElement()).afficher();
		}
	}
	private void edite(String[] commande){ //pas d'arguments
		if (commande.length!=2){ commandeInvalide(); return; }
		String s=commande[1];
		Entree e=acceder(s,false);
		if (e==null) cheminInvalide("edite",s);
		else if(!(e.getElement() instanceof FichierTexte)) System.out.println(e.getNom()+" ne peut être modifié");
		else ((FichierTexte)e.getElement()).setContenu(sc.nextLine());
	}
	private void rm(String[] commande){ // -R : supprime les dossiers et leurs contenus aussi
		if (commande.length<2){ commandeInvalide(); return; }
		boolean dossier=false;
		if (commande[1].equals("-R")) dossier=true;
		for (int i=((dossier)?2:1);i<commande.length;i++){
			String s=commande[i];
			Entree e=acceder(s,false);
			if (e==null) cheminInvalide("rm",s);
			else if(!dossier && (e.getElement() instanceof Dossier)) System.out.println(e.getNom()+" ne peut être supprimé.");
			else if(dossier && (e.getElement() instanceof Dossier)) ((Dossier)e.getElement()).supprimerRecursif();
			else e.supprimer();
		}
		if (courant.getContenant()==null) courant=racine;
	}
	private void mv(String[] commande){ //pas d'arguments
		if (commande.length!=3){ commandeInvalide(); return; }
		Entree origine=acceder(commande[1]);
		if (origine==null){ cheminInvalide(commande[0],commande[1]); return; }
		String arrivee=commande[2];
		if (arrivee.charAt(arrivee.length()-1)=='/'){
			Entree arriveE=acceder(commande[2]);
			if (arriveE==null || !(arriveE.getElement() instanceof Dossier)) cheminInvalide(commande[0],commande[2]);
			else ((Dossier)arriveE.getElement()).add(origine);
		}else{
			Entree arriveE=acceder(commande[2],true);
			if (arriveE==null) cheminInvalide(commande[0],commande[2]);
			else if (arriveE.getElement()!=null){ System.out.println(commande[2]+" : ce fichier existe déjà !"); arriveE.supprimer(); }
			else{ 
				if (origine.getContenant()!=null) origine.getContenant().retirer(origine);
				arriveE.inserer(origine.getElement());
			}
		}
	}
	private void cp(String[] commande){ //pas d'arguments
		Entree origine=acceder(commande[1]);
		if (origine==null){ cheminInvalide("cp",commande[1]); return; }



		String arrivee=commande[2];
		if (arrivee.charAt(arrivee.length()-1)=='/'){
			Entree arriveE=acceder(commande[2]);
			if (arriveE==null || !(arriveE.getElement() instanceof Dossier)) cheminInvalide(commande[0],commande[2]);
			else ((Dossier)arriveE.getElement()).add(origine.clone((Dossier)arriveE.getElement()));
		}else{
			Entree arriveE=acceder(commande[2],true);
			if (arriveE==null) cheminInvalide(commande[0],commande[2]);
			else if (arriveE.getElement()!=null){ System.out.println(commande[2]+" : ce fichier existe déjà !"); arriveE.supprimer(); }
			else{
				arriveE.inserer(origine.clone().getElement());
			}
		}
	}

	private void cheminInvalide(String c, String s){ System.out.print(c+": "); cheminInvalide(s); }
	private void cheminInvalide(String s){ System.out.println("impossible d'accéder à '"+s+"': Aucun fichier ou dossier de ce type"); }
	private void commandeInvalide(){ System.out.println("Commande invalide ! "); manuel(); }
	private void argumentsInvalides(){ System.out.println("Argument invalide ! "); manuel(); }
	private void manuel(){ System.out.println("Voir le manuel pour le détail des commandes et leur utilisation."); }

	private Entree acceder(String chemin){ return acceder(chemin, false); }
	private Entree acceder(String chemin, boolean b){
		if (chemin.equals("/")) return racine.getMoiMeme();
		Scanner sc=new Scanner(chemin).useDelimiter("/");
		ArrayList<String> s=new ArrayList<String>();
		while (sc.hasNext()) s.add(sc.next());
		if (s.get(0)==null){ s.remove(0); return acceder(racine.getMoiMeme(),s,b); }
		else return acceder(courant.getMoiMeme(),s,b);
	}
	private Entree acceder(Entree e, ArrayList<String> d, boolean b){
		if (e==null || d.get(0)==null) return null;
		else if (d.size()==1) return ((Dossier)e.getElement()).lireEntree(d.get(0),b);
		else if (d.get(0).equals(".")){ d.remove(0); return acceder(e,d,b); }
		else if (d.get(0).equals("..")){ d.remove(0); return acceder(e.getContenant().getMoiMeme(),d,b); }
		else { String s=d.get(0); d.remove(0); return acceder(((Dossier)e.getElement()).lireEntree(s),d,b); }
	}

	private String[] lireCommande(){
		String commande=sc.nextLine();
		ArrayList<String> a=new ArrayList<String>();
		try{
			Scanner s=new Scanner(commande);
			while (s.hasNext()){
				a.add(s.next());
			}
			s.close();
			String[] t=new String[a.size()];
			int i=0;
			for (String aa:a){ t[i]=aa; i++; }
				return t;
		}catch(Exception e){ System.out.println("Une erreur s'est produite."); }
		return null;
	}
}