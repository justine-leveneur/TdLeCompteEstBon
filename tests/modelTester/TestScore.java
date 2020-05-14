package modelTester;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import model.Score;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestScore {
	private static Score score1;
	private static Score score2;
	
	@BeforeAll
	public static void init() {		
		score1 = new Score("MARCEL", 5, 80);
		score2 = new Score ("JEREMY", 7, 60);
	}

	@Test
	@Order(0)
	void testConvertToTime () {
		assertEquals("1:20",score1.convertToTime());
	}
	
	@Test
	@Order(1)
	void testAffiche () {
		ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bOutput);
	    System.setOut(ps);
		score1.affiche();
		ps.close();
		assertNotEquals(0,bOutput.toByteArray().length);
	}
	
	@Test
	@Order(2)
	void testGetPseudo () {
		assertEquals("MARCEL", score1.getPseudo());
	}
	
	@Test
	@Order(3)
	void testSetPseudo () {
		score1.setPseudo("DOMINIQUE");
		assertEquals("DOMINIQUE", score1.getPseudo());
	}
	
	@Test
	@Order(4)
	void testGetValeur () {
		assertEquals(5, score1.getValeur());
	}
	
	@Test
	@Order(5)
	void testGetTemps () {
		assertEquals(80, score1.getTemps());
	}
	
	@Test
	@Order(6)
	void testGetDate () {
		assertNotNull(score1.getDate());
	}
	
	@Test
	@Order(7)
	void testCompareTo () {
		assertEquals(-1,score1.compareTo(score2));
		assertEquals(1,score2.compareTo(score1));
		score2.setValeur(5);
		assertEquals(1,score1.compareTo(score2));
		assertEquals(-1,score2.compareTo(score1));
		score2.setTemps(80);
		assertEquals(0,score1.compareTo(score2));
	}

	
}
