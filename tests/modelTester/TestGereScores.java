package modelTester;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import model.GereScores;
import model.Score;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestGereScores {
	private static GereScores gereScores;
	private static String PATH = "score/scores.ser";
	private static String HTMLFILE = "score/scores.html";
	private static File htmlScores = new File(HTMLFILE);
	private static File serScores = new File(PATH);
	private static TreeSet <Score> scores;
	
	@BeforeAll
	public static void init() throws Exception {		
		htmlScores.delete();
		serScores.delete();
		scores = new TreeSet<> ();
		gereScores = GereScores.getInstance();	
	}
	
	@Test
	@Order(0)
	void testGetTop() {
		GereScores.setTOP(10);
		assertEquals(10,GereScores.getTOP());
	}
	
	@Test
    @Order(1)
	void testConstructor () {
		assertTrue(htmlScores.exists());
		assertTrue(serScores.exists());
	}
	
	@Test
	@Order(2)
	void testAddScore ()  throws Exception {
		gereScores.setTableJeu(scores);
		assertTrue(gereScores.getTableJeu().isEmpty());
		gereScores.addScore("MARCEL", 50, 12);
		assertFalse(gereScores.getTableJeu().isEmpty());
		
		serScores.delete();
		GereScores.setTOP(1);
		gereScores.addScore("PREMIER", 2, 12);
		assertEquals("PREMIER",gereScores.getTableJeu().last().getPseudo());
		assertTrue(serScores.exists());
	}
	
	@Test
	@Order(3)
	void testExport () throws Exception {
		gereScores.export();
		assertNotEquals(0,htmlScores.length());
	}
	
	@Test
	@Order(4)
	void testLoad ()  throws Exception {
		scores = new TreeSet<> ();
		gereScores.setTableJeu(scores);
		assertTrue(gereScores.getTableJeu().isEmpty());
		gereScores.load();
		assertFalse(gereScores.getTableJeu().isEmpty());
	}

}
