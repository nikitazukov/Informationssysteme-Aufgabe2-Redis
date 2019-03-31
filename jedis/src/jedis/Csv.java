package jedis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.csvreader.CsvReader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class Csv {
	
	//konstruktor
	Csv(String anfang, String ende, String sensor, String ipVM){
		
		this.anfang = anfang;
		this.ende = ende;
		this.sensor = sensor;
		this.ipVM = ipVM;
	}
	
	//klimadaten vars
	private String a1,b2,c3,d4,e5,f6,g7,h8,i9,j10,k11,l12,m13,n14,o15;
	private Map hash_map;
	
	//konstruktor vars
	private String anfang, ende, sensor, ipVM;
	
	//jedis - pipe
	private Jedis jedis;
	private Pipeline pipelined;
	
	//reder vars
	private CsvReader klimaDaten;

	public void impotCsv() {
		
		try {
			
			hash_map = new HashMap();
			jedis = new Jedis(ipVM);
			pipelined = jedis.pipelined();
			klimaDaten = new CsvReader("klima_daten.csv");
						
			klimaDaten.readHeaders();

			//holt alle daten der spalten pro reihe
			while (klimaDaten.readRecord()) {

				a1 = klimaDaten.get("Date Time");
				b2 = klimaDaten.get("p (mbar)");
				c3 = klimaDaten.get("T (degC)");
				d4 = klimaDaten.get("Tpot (K)");
				e5 = klimaDaten.get("Tdew (degC)");
				f6 = klimaDaten.get("rh (%)");
				g7 = klimaDaten.get("VPmax (mbar)");
				h8 = klimaDaten.get("VPact (mbar)");
				i9 = klimaDaten.get("VPdef (mbar)");
				j10 = klimaDaten.get("sh (g/kg)");
				k11 = klimaDaten.get("H2OC (mmol/mol)");
				l12 = klimaDaten.get("rho (g/m**3)");
				m13 = klimaDaten.get("wv (m/s)");
				n14 = klimaDaten.get("max. wv (m/s)");
				o15 = klimaDaten.get("wd (deg)");	
				
				//speichert alle daten einer reihe in maps
				hash_map.put("b2", b2);
				hash_map.put("c3", c3);
				hash_map.put("d4", d4);
				hash_map.put("e5", e5);
				hash_map.put("f6", f6);
				hash_map.put("g7", g7);
				hash_map.put("h8", h8);
				hash_map.put("i9", i9);
				hash_map.put("j10", j10);
				hash_map.put("k11", k11);
				hash_map.put("l12", l12);
				hash_map.put("m13", m13);
				hash_map.put("n14", n14);
				hash_map.put("o15", o15);
				
				//speichert alle maps in einen hash mit dem key a1
				pipelined.hmset(a1, hash_map);
				
			}

			//pipe, reader, jedis geschlossen
			pipelined.sync();
			klimaDaten.close();
			jedis.close();
		
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
}
}
