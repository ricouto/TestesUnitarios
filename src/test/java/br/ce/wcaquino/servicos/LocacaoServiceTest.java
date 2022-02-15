package br.ce.wcaquino.servicos;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;
import sun.applet.Main;

public class LocacaoServiceTest {

	private LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Before
	public void setup() {
		service = new LocacaoService();
	}

	@Test
	public void primeiroTeste() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//sai esse trecho >> Usuario usuario = new Usuario("Usuario 1");
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		// Filme filme = new Filme("Filme 1", 2, 5.0);
		//sai esse trecho >> List<Filme> filme = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		List<Filme> filme = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());

		Locacao locacao = LocacaoBuilder.umLocacao().comValor(5.0).agora();//service.alugarFilme(usuario, filme);

		/*
		Assert.assertEquals(5.0, locacao.getValor(), 0.01);

		assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5.0)));
		assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(6.0)));

		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

		assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
		*/

		// Collector
		error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5.0)));
		//error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		//error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));

		// nova classe
		//error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeMesmo());
		
		error.checkThat(new Date(), MatchersProprios.ehHojeMesmo());
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));

	}

	@Test(expected = Exception.class)
	public void testeLocacao_filmeSemEstoque() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		// Filme filme = new Filme("Filme 1", 0, 5.0);
		List<Filme> filme = Arrays.asList(FilmeBuilder.umFilmeSemEstoque().agora());

		service.alugarFilme(usuario, filme);

	}

	@Test
	public void testeLocacao_filmeSemEstoque2() {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		// Filme filme = new Filme("Filme 1", 0, 5.0);
		List<Filme> filme = Arrays.asList(FilmeBuilder.umFilme().agora());

		try {
			service.alugarFilme(usuario, filme);
		} catch (Exception e) {
			assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque!"));

		}
	}

	// @Test
	@Ignore
	public void testeLocacao_filmeSemEstoque2ComEstoqueExcecao() {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		// Filme filme = new Filme("Filme 1", 2, 5.0);
		List<Filme> filme = Arrays.asList(FilmeBuilder.umFilme().agora());

		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lancado uma excecao!");
		} catch (Exception e) {
			assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque!"));

		}
	}

	@Test
	public void testeLocacao_filmeSemEstoque3() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		// Filme filme = new Filme("Filme 1", 0, 5.0);
		List<Filme> filme = Arrays.asList(FilmeBuilder.umFilme().semEstoque().agora());

		expected.expect(Exception.class);
		expected.expectMessage("Filme sem estoque!");

		service.alugarFilme(usuario, filme);

	}

	@Test
	public void testeLocacao_UsuarioVazio() throws Exception {
		// Filme filme = new Filme("Filme 1", 2, 5.0);
		List<Filme> filme = Arrays.asList(FilmeBuilder.umFilme().agora());

		try {
			service.alugarFilme(null, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), CoreMatchers.is("Usuario vazio!"));
		}
	}

	@Test
	public void testeLocacao_FilmeVazio() throws Exception, LocadoraException {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		expected.expect(LocadoraException.class);
		expected.expectMessage("Filme vazio!");

		service.alugarFilme(usuario, null);

	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		// acao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// verificacao
		// boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(),
		// Calendar.MONDAY);
		// Assert.assertTrue(ehSegunda);

		// assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}
	
	/*
	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}*/

}
