package br.ce.wcaquino.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.Assertions;
import br.ce.wcaquino.servicos.LocacaoServiceTest;

//@RunWith(Suite.class)
@SuiteClasses({
	Assertions.class,
	LocacaoServiceTest.class
})

public class SuiteExecucao {
	//Remova se puder!
}
