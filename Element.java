public abstract class Element{
	public abstract String getType ();

	public String toString () {
		return getType () ;
	}

	public static final String DOSSIER="Dossier";
	public static final String FICHIER_TEXTE="Fichier texte";
	public static final String FICHIER_SYSTEME="Fichier système";
}