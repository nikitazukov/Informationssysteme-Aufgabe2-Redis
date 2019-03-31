package jedis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class Calculate {

	Calculate(String anfang, String ende, String sensor, String ipVM) {
		this.anfang = anfang;
		this.ende = ende;
		this.sensor = sensor;
		this.ipVM = ipVM;
	}

	// Konstruktor Vars
	private String anfang;
	private String ende;
	private String sensor;
	private String ipVM;

	// Daterechnung Vars
	private DateTimeFormatter formater;
	private LocalDateTime date_anfang;
	private LocalDateTime date_ende;
	private LocalDateTime date_counter;

	// jedis - pipe vars
	private Jedis jedis;
	private Pipeline pipe;

	// Allgemeine Vars
	private String date_counter_string;
	private int datenCount;
	private List<Response<?>> werte;
	private String str_clean;
	private double umwandler = 0;
	private double maxi = 0;
	private String str;

	public double getMaxSum() {

		formater = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		date_anfang = LocalDateTime.parse(anfang, formater);
		date_ende = LocalDateTime.parse(ende, formater);
		date_counter = date_anfang;
		jedis = new Jedis(ipVM);
		pipe = jedis.pipelined();
		pipe.multi();
		werte = new ArrayList<Response<?>>();
	

		// redis abfragebefehle werden in die pipeline gespeichert
		int i = 0;
		while (i < 2) {

			//alle daten abgefragt wird pipe executed und gesynct
			if (date_counter.isEqual(date_ende)) {

				pipe.exec();
				pipe.sync();
				break;
			} else {

				//date wird in string umgewandelt und in pipe geschoben - date wird auf nächsten wert hochgezählt
				date_counter_string = formater.format(date_counter);
				werte.add(pipe.hmget(date_counter_string, sensor));
				date_counter = date_counter.plusMinutes(10);
			}
		}

		//rückgabewerte der pipe werden ausgelesen -> in substrings umgewandlet um [ ] zu enfernen -> zu double geparst -> max wert rausgefiltert
		for (Response<?> resp : werte) {
			str = resp.get().toString();
			str_clean = str.substring(1, str.length() - 1);
				if (!str_clean.startsWith("n")) {
					umwandler = Double.parseDouble(str_clean);
				}
				if (maxi < umwandler) {
					maxi = umwandler;
				}

			datenCount++;

		}
//		System.out.println("Bearbeitete Datensätze: "+datenCount );
		return maxi;
	}

}
