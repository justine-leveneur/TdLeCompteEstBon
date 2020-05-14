package modelTester;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.Etape;

class TestEtape{
	private static Etape etape1;

	@BeforeAll
	public static void init() {
		int[] plaques = {25,60,80,100,99,77};
		etape1 = new Etape(plaques);
	}

	@Test
	void testCalculerAddition() {
		etape1.calculer(0,1,"+");
		assertTrue(etape1.calculIsOk());
		assertEquals(25+60,etape1.getResultat());
	}
	
	@Test
	void testCalculerSoustraction() {
		etape1.calculer(2,1,"-");
		assertTrue(etape1.calculIsOk());
		assertEquals(80-60,etape1.getResultat());
	}
	
	@Test
	void testCalculerMutiliplication() {
		etape1.calculer(4,5,"x");
		assertTrue(etape1.calculIsOk());
		assertEquals(99*77,etape1.getResultat());
	}
	
	@Test
	void testCalculerDivisionEntiere() {
		etape1.calculer(3,0,"/");
		assertTrue(etape1.calculIsOk());
		assertEquals(100/25,etape1.getResultat());
	}
	
	@Test
	void testCalculerDivisionDecimale() {
		etape1.calculer(3,2,"/");
		assertFalse(etape1.calculIsOk());
	}
	
	@Test
	void testResultatSousFormeDeString() {
		etape1.calculer(3,0,"/");	
		assertTrue(etape1.calculIsOk());
		assertEquals("100/25="+100/25,etape1.resultatSousFormeDeString());
	}
	
	@Test
	void testGetTabPlaques() {
		int[] plaquesATester = {25,60,80,100,99,77};
		int[] plaquesDeTest = etape1.getTabPlaques();
		for (int i = 0; i < plaquesATester.length; i++)
		  assertEquals(plaquesATester[i],plaquesDeTest[i]);
	}

}
