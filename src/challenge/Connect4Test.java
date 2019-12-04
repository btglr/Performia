package challenge;

import org.json.JSONObject;

import junit.framework.TestCase;

public class Connect4Test extends TestCase {

//	public void testJouerCoupInterieur() {
//		Connect4 c = new Connect4(new Participant(1));
//
//		JSONObject coup = new JSONObject();
//		coup.put("colonne", 2);
//		assertTrue("La colonne valide", c.jouerCoup(coup));
//	}

	public void testJouerCoupExterieur1() {
		Connect4 c = new Connect4(new Participant(1));

		JSONObject coup = new JSONObject();
		coup.put("colonne", -1);

		assertFalse("La colonne est invalide", c.jouerCoup(coup));
	}

	public void testJouerCoupExterieur2() {
		Connect4 c = new Connect4(new Participant(1));

		JSONObject coup = new JSONObject();
		coup.put("colonne", 8);

		assertFalse("La colonne est invalide", c.jouerCoup(coup));
	}

	public void testVictoireLigne() {
		int[] g = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0};
		Connect4 c = new Connect4(g,new Participant(1),new Participant(2));

		JSONObject coup = new JSONObject();
		coup.put("colonne", 3);
		c.jouerCoup(coup);

		assertTrue("La partie est terminée", c.estFini());
	}

	public void testVictoireColonne() {
		int[] g = {0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,
			1, 0, 0, 0, 0, 0, 0,
			1, 0, 0, 0, 0, 0, 0,
			1, 0, 0, 0, 0, 0, 0};
		Connect4 c = new Connect4(g,new Participant(1),new Participant(2));

		JSONObject coup = new JSONObject();
		coup.put("colonne", 0);
		c.jouerCoup(coup);

		assertTrue("La partie est terminée", c.estFini());
	}

	public void testVictoireDiagonale1() {
		int[] g = {0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,
			0, 0, 1, 2, 0, 0, 0,
			0, 1, 2, 2, 0, 0, 0,
			1, 2, 2, 2, 0, 0, 0};
		Connect4 c = new Connect4(g,new Participant(1),new Participant(2));

		JSONObject coup = new JSONObject();
		coup.put("colonne", 3);
		c.jouerCoup(coup);

		assertTrue("La partie est terminée", c.estFini());
	}

	public void testVictoireDiagonale2() {
		int[] g = {0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,
			2, 1, 0, 0, 0, 0, 0,
			2, 2, 1, 0, 0, 0, 0,
			2, 2, 2, 1, 0, 0, 0};
		Connect4 c = new Connect4(g,new Participant(1),new Participant(2));

		JSONObject coup = new JSONObject();
		coup.put("colonne", 0);
		c.jouerCoup(coup);

		assertTrue("La partie est terminée", c.estFini());
	}


}
