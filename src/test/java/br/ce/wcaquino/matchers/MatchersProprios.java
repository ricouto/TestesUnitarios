package br.ce.wcaquino.matchers;

//import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.Date;

public class MatchersProprios {
	
	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
	
	/* testes
	public static DiaSemanaMatcher ehHoje() {
		Calendar c = Calendar.getInstance();
		Date data = c.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println("Data formatada: "+sdf.format(data));
		return null;
		
		//return new DiaSemanaMatcher(Calendar.date);
	}*/
	
	public static DataDiferencaDiasMatcher ehHojeComDiferencaDias(Integer qtdDias) {
		return new DataDiferencaDiasMatcher(qtdDias);
	}
	
	public static DataDiferencaDiasMatcher ehHojeMesmo() {
		return new DataDiferencaDiasMatcher(0);
	}//
	

}
