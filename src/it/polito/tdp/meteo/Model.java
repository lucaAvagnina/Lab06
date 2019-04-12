package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private static double costo_best; 
	private static List<Citta> best;
	
	private MeteoDAO meteoDAO;
	private List<Citta> cittaList;
	
	public Model() {
		meteoDAO = new MeteoDAO();
		cittaList = new ArrayList<Citta>();
	}

	public String getUmiditaMedia(int mese) {
		
		cittaList = meteoDAO.getCitta();
		
		String risultato ="";
		
		for(Citta c : cittaList) {
			risultato += c.getNome() + ": " + (Double.toString(meteoDAO.getAvgRilevamentiLocalitaMese(mese, c.getNome()))) + "\n"; 
		}
		
		return risultato.trim();
	}

	public String trovaSequenza(int mese) {
		
		String lista ="";
		best = new ArrayList<Citta>();
		costo_best = 0.0;
		
		cittaList = meteoDAO.getCitta();
		
		for(Citta c : cittaList) {
			c.setRilevamenti(meteoDAO.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		
		List<Citta> parziale = new ArrayList<Citta>();
		
		this.cercaSequenza(parziale, 0);
		
		for(Citta c : best) {
			lista += ((Citta) best).getNome(); 
		}
		
		return lista;
	}
	
	private void cercaSequenza(List<Citta> parziale, int livello) {
		
		if(livello == NUMERO_GIORNI_TOTALI) {
			double costo = calcoloCosto(parziale);
			if(costo < costo_best) {
				costo_best = costo;
				best = parziale;
				return;
			}else {
				return;
			}
			
		}else {
			int count = 0;
			for(Citta c : cittaList) {
				count++;
				if(count>1) {
					if(verificaCondizioni(c, parziale) == true) {
							parziale.add(c);
							cercaSequenza(parziale, livello+1);
							parziale.remove(c);
						}
					}
				else {
					parziale.add(c);
					cercaSequenza(parziale, livello+1);
					parziale.remove(c);
				}
			}
		}
		
	}


	private boolean verificaCondizioni(Citta citta, List<Citta> parziale) {
		int conta = 0;
		boolean validoMax = true;
		boolean validoMin = false;
		
		for(Citta c : parziale) {
			if(c.equals(citta)) {
				conta++;
				if(conta > NUMERO_GIORNI_CITTA_MAX)
					validoMax = false;
			}
		}
		
		for(int i=2; i < parziale.size()-1; i++) {
			if(parziale.get(i).equals(citta) && parziale.get(i-1).equals(citta) && parziale.get(i-2).equals(citta)) {
				validoMin = true;
			}
		}
		
		if((validoMin == true && validoMax == true) || (validoMin == false && validoMax == true)) {
			return true;
		}		
		else{
			return false;
		}
		
	}

	private double calcoloCosto(List<Citta> parziale) {
		
		double costo = 0.0;
		
		for(int i = 0; i<parziale.size(); i++) {
				costo += parziale.get(i).getRilevamenti().get(i).getUmidita();
		}
		
		for(int i = 0; i < (parziale.size()-1); i++) {
			if(!((parziale.get(i)).equals(parziale.get(i+1)))) {
				costo += COST;
			}
				
		}
		
		return costo;
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}

}
