package br.ce.wcaquino.daos;

import java.util.List;

import br.ce.wcaquino.entidades.Locacao;

public class LocacaoDaoFake implements LocacaoDAO {

	@Override
	public void salvar(Locacao locacao) {

	}
	
	public List<Locacao> obterLocacoesPendentes(){ 
		return null;
	}
}
