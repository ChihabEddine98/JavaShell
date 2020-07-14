public class test{

	/*
	* TP réalisé en binome entre :
	* 							  BENAMARA ABDELKADER (2 MI 1)
	* 							  ELHOUFI OTHMAN      (2 MI 2)
	*
	*/
	/*
		3.  Pour qu'ils puissent être désalloués il faut qu'il n'y ait plus de référence vers ces objets
			On peut appeler les deux méthodes ci-dessous pour inciter le système à appeler finalize() 
			mais ce ne sera qu'une suggestion et on ne peut pas savoir si ce sera vraiment fait ou pas...

			System.runFinalization()
			Runtime.getRuntime().runFinalization()
		
		4. Les objets sont finalisés en fonction des besoins (donc non régulièrement), de plus ils ne sont pas finalisés dans l'ordre et les derniers objets crées ne sont pas forcément finalizés




		Redéfinit clone de cette manière n'est pas satisfaisant car cela ne clone pas le contenu des dossiers, si l'on supprime un fichier dans un dossier cloné cela supprimera aussi ce fichier dans le dossier d'origine.
		Il faut redéfinir tous les attributs non immuables.
	*/

	/*
	* Remarques Importantes :
	* 						- Pour mieux illustrer le fonctionnement de notre programme on a opté pour un arborescence précrée.
	* 					    - Il faut aussi respecter les Majuscules et les miniscules pour les noms des élements.
	*/


	public static void main(String[] args){

		Dossier racine=new Racine();
		Dossier mi2=new Dossier(racine,"math_info");

		Dossier s3=new Dossier(mi2,"S3");
		Dossier mm3=new Dossier(s3,"MM3");
		Dossier ia3=new Dossier(s3,"IA3");
		Dossier pooig=new Dossier(s3,"POOIG");
		Dossier tp6=new Dossier(pooig,"TP6");
		Dossier tp7=new Dossier(pooig,"TP7");

		Dossier s4=new Dossier(mi2,"S4");
		Dossier proba=new Dossier(s4,"PROBA1");
		Dossier anum=new Dossier(s4,"ANUM1");


		FichierSysteme f1=new FichierSysteme("système : 1");
		FichierSysteme f2=new FichierSysteme("système : 2");
		FichierSysteme f3=new FichierSysteme("système : 3");
		FichierSysteme f4=new FichierSysteme("système : 4");
		FichierSysteme f5=new FichierSysteme("système : 5");
		FichierSysteme f6=new FichierSysteme("système : 6");

		FichierTexte ft1=new FichierTexte("Cours de Mathématiques MM3");
		FichierTexte ft2=new FichierTexte("\\documentclass[a4paper,10pt]{book}");
		FichierTexte ft3=new FichierTexte("cours d'arithmétique IA3");
		FichierTexte ft4=new FichierTexte("tp6");
		FichierTexte ft5=new FichierTexte("tp7");
		FichierTexte ft6=new FichierTexte("public static void main()");
		FichierTexte ft7=new FichierTexte("Probabilité et statistiques !");
		FichierTexte ft8=new FichierTexte("Analyse Numérique ! ");

		mi2.add(f1,".math_info");
		s3.add(f2,".s3");
		s3.add(f3,"trash");
		s4.add(f6,".s4");
		//s4.add(proba,".probabilites");
		racine.add(f4,".config");
		pooig.add(f5,"préférences");

		mm3.add(ft1,"mm3.tex");
		mm3.add(ft2,"mm3.pdf");
		ia3.add(ft3,"ia3.pdf");
		tp6.add(ft4,"tp6.java");
		tp7.add(ft5,"tp7.java");
		tp7.add(ft6,"test.java");
		proba.add(ft7,"proba.pdf");
		anum.add(ft8,"analyse_numerique.docs");


		/*
		 * Ici on va juste afficher l'arborescence crée !
		 */

		for (int i=0;i<2;i++)
			for(int j=0;j<2;j++)
				for(int k=0;k<2;k++){

					s3.afficher(k==1,j==1,i==1);
					System.out.println("\n\n");
					s4.afficher(k==1,j==1,i==1);
					System.out.println("\n\n");

				}

		Shell s=new Shell((Racine)racine);
		
		s.executer();
	}
}