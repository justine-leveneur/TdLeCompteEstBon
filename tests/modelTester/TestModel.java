package modelTester;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import model.Model;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestModel {
	private static Model model;
	private static String PATH = "score/scores.ser";
	private static String HTMLFILE = "score/scores.html";
	private static File htmlScores = new File(HTMLFILE);
	private static File serScores = new File(PATH);

	@BeforeAll
	public static void init() {		
		model = Model.getInstance();
	}
	
	@Test
    @Order(0)
	void testConstructor () {
		assertEquals("00",model.getModeDeJeu());
		model.setDureeMax(60);
		assertEquals(60,model.getDureeMax());
		assertNull(model.getPseudo());		
	}
	
	@Test
    @Order(1)
	void testAttendre () {
		model.attendre();
		assertEquals("01",model.getModeDeJeu());		
	}
	
	@Test
    @Order(2)
	void testPreparer () {
		assertNotNull(model.preparer("MARCEL"));
		assertEquals("02",model.getModeDeJeu());
		assertEquals("MARCEL",model.getPseudo());
		assertNotNull(model.getNbATrouver());
	}
	
	@Test
    @Order(3)
	void testJouer () {
		model.jouer();
		assertEquals("03",model.getModeDeJeu());
	}
	
	@Test
    @Order(4)
	void testCalculer () {
		if(!(model.calculer(0,1,"/") instanceof String)) assertNull(model.calculer(0,1,"/"));
		if(model.calculer(0,1,"+") != null) assertTrue(model.calculer(0,1,"+") instanceof String);
	}
	
	@Test
    @Order(5)
	void testCreerNouvellesPlaques () {
		assertTrue(model.creerNouvellesPlaques(0, 1) instanceof int[]);
	}
	
	@Test
    @Order(6)
	void testScore () {
		model.score(20);
		assertNotNull(model.getScore());
	}
	
	@Test
    @Order(7)
	void testSupprimerEtape () {
		assertNotNull(model.supprimerEtape());
	}
	
	
	@Test
    @Order(8)
	void testGetScores () {
		assertTrue(model.getScores() instanceof String[]);
	}
	
	@Test
    @Order(9)
	void testExportScores () {
		htmlScores.delete();
		serScores.delete();
		model.exportTheScores();
		assertEquals("05", model.getModeDeJeu());
		assertTrue(htmlScores.exists());
		assertTrue(serScores.exists());
	}

}
