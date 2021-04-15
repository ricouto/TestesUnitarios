package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class Assertions {
	
	
	@Test
	public void AssertionTest() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		Assert.assertEquals("Erro de compara��o", 1, 1);
		Assert.assertEquals(0.51234, 0.512, 0.01);
		Assert.assertEquals(Math.PI, 3.14, 0.01);
		
		int i = 5;
		Integer i2 = 5;
		
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i, i2.intValue());
		
		Assert.assertEquals("casa", "casa");
		Assert.assertNotEquals("casa", "bola");
		
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = null;
		
		Assert.assertEquals(u1, u2);
		
		Assert.assertSame(u2, u2);
		Assert.assertNotSame(u1, u2);
		
		Assert.assertNull(u3);
		Assert.assertNotNull(u2);
		
	}

}
