package jedis;

import java.util.Calendar;
import java.util.Date;

public class Main {
	
//	Sensoren:
//	a1	Date Time
//	b2	p (mbar)
//	c3	T (degC)
//	d4	Tpot (K)
//	e5	Tdew (degC)
//	f6	rh (%)
//	g7	VPmax (mbar)
//	h8	VPact (mbar)
//	i9	VPdef (mbar)
//	j10	sh (g/kg)
//	k11	H2OC (mmol/mol)
//	l12	rho (g/m**3)
//	m13	wv (m/s)
//	n14 max. wv (m/s)
//	o15 wd (deg)
	
	public static String anfang;
	public static String ende;
	public static String sensor;
	public static String ipVM;

public static void zeitStempel() {
		
		Date ntime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(ntime);
		int std = cal.get(Calendar.HOUR);
		int minuten = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		int millisec = cal.get(Calendar.MILLISECOND);
		System.out.println(std+ ":" + minuten + ":" + sec+ ":" + millisec);
	}

	
	public static void main(String[] args) {
		
		//Configurationsdaten
		anfang = "01.01.2009 00:10:00";
		ende = "01.01.2014 00:10:00";		
		sensor = "h8";
		ipVM = "141.28.137.177";
		
		zeitStempel();
		Csv csv = new Csv(anfang,ende,sensor,ipVM);
		csv.impotCsv();
		System.out.print("Daten Importiert bei: ");
		
		zeitStempel();
		Calculate c = new Calculate(anfang, ende,sensor,ipVM);		
		System.out.println("Max Summe :" + c.getMaxSum() );
		zeitStempel();
		
	}

}
