package br.ce.wcaquino.servicos;

import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@InjectMocks @Spy
	private LocacaoService service;
	@Mock
	private SPCService spc;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private EmailService email;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		//service = PowerMockito.spy(service);
		/* Podem ser removidas pois o Annotion faz este papel
		service = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
		spc = Mockito.mock(SPCService.class);
		service.setSPCService(spc);
		email = Mockito.mock(EmailService.class);
		service.setEmailService(email);
		*/
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		
		//Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//sai esse trecho >> Usuario usuario = new Usuario("Usuario 1");
		//Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(25, 02, 2022));
		
		// Filme filme = new Filme("Filme 1", 2, 5.0);
		//sai esse trecho >> List<Filme> filme = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());
		
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(25, 02, 2022));
		
		Mockito.doReturn(DataUtils.obterData(25, 02, 2022)).when(service).obterData();

		Locacao locacao = service.alugarFilme(usuario, filmes);
		//= LocacaoBuilder.umLocacao().comUsuario(usuario).comValor(5.0).agora();

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
		
		//error.checkThat(new Date(), MatchersProprios.ehHojeMesmo());
		//error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
		
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(25, 02, 2022)),  CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(26, 02, 2022)),  CoreMatchers.is(true));

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
		//Removido para usar o PowerMockito
		//Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(26, 02, 2022));
		Mockito.doReturn(DataUtils.obterData(26, 02, 2022)).when(service).obterData();

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
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		//acao
		try {
			service.alugarFilme(usuario, filmes);
		//verificacao
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertEquals(e.getMessage(),"Usuário Negativado!");
		}
		
		//Mockito.verify(spc.possuiNegativacao(usuario));
		Mockito.verify(spc, Mockito.atLeastOnce()).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado!").agora();
		
		List<Locacao> locacoes = Arrays.asList(
				LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario).agora(),
				LocacaoBuilder.umLocacao().comUsuario(usuario2).agora(),
				LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario3).agora(),
				LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario3).agora());

		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		Mockito.verify(email, Mockito.times(3)).notificarAtrasos(Mockito.any(Usuario.class));
		Mockito.verify(email).notificarAtrasos(usuario);
		Mockito.verify(email, Mockito.atLeastOnce()).notificarAtrasos(usuario3);
		Mockito.verify(email, Mockito.never()).notificarAtrasos(usuario2);
		Mockito.verifyNoMoreInteractions(email);
		//Antes do refactory
		//LocacaoBuilder.umLocacao().comUsuario(usuario).comDataRetorno(DataUtils.obterDataComDiferencaDias(-2)).agora(),
	}
	
	@Test
	public void deveTratarErroNoSPC() throws Exception {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		Mockito.when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha Catastrófica!"));
		
		//verificacao
		expected.expect(LocadoraException.class);
		expected.expectMessage("Problemas com SPC, tente novamente!");
		
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		//cenario
		Locacao locacao = LocacaoBuilder.umLocacao().agora();
		
		//acao
		service.prorrogarLocacao(locacao, 3);
		
		//verificacao
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();

		/*
		Assert.assertEquals(locacaoRetornada.getValor(), CoreMatchers.is(4.0));
		Assert.assertEquals(locacaoRetornada.getDataLocacao(), MatchersProprios.ehHojeMesmo());
		Assert.assertEquals(locacaoRetornada.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(5));
		*/
		
		//alterando para Role gerada
		error.checkThat(locacaoRetornada.getValor(), CoreMatchers.is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), MatchersProprios.ehHojeMesmo());
		error.checkThat(locacaoRetornada.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(3));
		
	}
	
	/*@Test
	public void deveAlugarFilem_SemCalcularValor() throws Exception {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//vericacao
		assertThat(locacao.getValor(), CoreMatchers.is(1.0));
		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
	}*/
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//acao
		Class<LocacaoService> clazz = LocacaoService.class;
		Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		Double valor = (Double) metodo.invoke(service, filmes);
		
		//verificacao
		assertThat(valor, CoreMatchers.is(4.0));
	}
	
	
}
