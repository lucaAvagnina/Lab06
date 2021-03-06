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

	private double costo_best; 
	private List<Citta> best;
	
	private MeteoDAO meteoDAO;
	private List<Citta> cittaList;
	
	public Model() {
		//Potrei anche inizializzare direttamente qui 
		//MeteoDAO meteoDAO = new MeteoDAO();
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
		
		String lista ="La sequenza corretta �:\n";
		best = null;
		costo_best = 0.0;
		
		cittaList = meteoDAO.getCitta();
		
		for(Citta c : cittaList) {
			c.setRilevamenti(meteoDAO.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		
		List<Citta> parziale = new ArrayList<Citta>();
		
		this.cercaSequenza(parziale, 0);
		
		int i = 0;
		for(Citta c : best) {
			lista += "-"+ (i+1) +"\t" +c.toString() + "\t\t"+ best.get(i).getRilevamenti().get(i).getData().toString().substring(5)+ "\n"; 
			i++;
		}
		
		lista += "Il costo totale � di: " + costo_best;
		
		return lista.trim();
	}
	
	private void cercaSequenza(List<Citta> parziale, int livello) {
	
		//CASO TERMINALE
		if(livello == NUMERO_GIORNI_TOTALI) {
			double costo = calcoloCosto(parziale);
			if(costo < costo_best || best == null) {
				costo_best = costo;
				best = new ArrayList<Citta>(parziale);
				return;
			}else {
				return;
			}
			
		}else {
			for(Citta c : cittaList) {
				
				//VINCOLO
				if(livello>0) {
					if(verificaCondizioni(c, parziale) == true) {
							parziale.add(c.clone());
							cercaSequenza(parziale, livello+1);
							parziale.remove(c);
						}
					}
				else {
					parziale.add(c.clone());
					cercaSequenza(parziale, livello+1);
					parziale.remove(c);
				}
			}
		}
		
	}

	/**
	 * Dati i valori
	 * @param citta
	 * @param parziale
	 * @return {@code true} se posso ancora aggiungere la citta, {@code false} se non posso
	 */
	private boolean verificaCondizioni(Citta citta, List<Citta> parziale) {
		int conta = 0;
		boolean validoMax = true;
		boolean validoMin = false;
		
		//Controlla numero giorni massimi
		for(Citta c : parziale) {
			if(c.equals(citta)) {
				conta++;
				if(conta == NUMERO_GIORNI_CITTA_MAX)
					validoMax = false;
			}
		}
		
		//Controlla numergo giorni minimi
		for(int i=2; i < parziale.size()-1; i++) {
			if(parziale.get(i).equals(citta) && parziale.get(i-1).equals(citta) && parziale.get(i-2).equals(citta)) {
				validoMin = true;
			}
		}
		
		/*
		 * Potrei anche fare 
		 * 
		 * if(paziale.size() == 1 || parziale.size() == 2) //Allora non posso cambiare
		 * 	return (parziale.get(parziale.size()-1).equals(prova));
		 * if(parziale.get(parziale.size()-1).equals(prova)) //giorni successivi e posso rimanere
		 * 	return true;
		 * if(parziale.get(parziale.get(parziale.size()-1)).equals(parziale.get(parziale.size()-2)) && parzaiel.get(parziale.get(parziale.size()-2)).equals(parziale.get(parziale.size()-3)))
		 * 	return true;
		 * 
		 * return false;
		 */
		
		if((validoMin == true && validoMax == true) || (validoMin == false && validoMax == true)) {
			return true;
		}		
		else{
			return false;
		}
		
	}

	/**
	 * Data la lista parziale
	 * @param parziale
	 * @return {@code Double} costo che equivale al costo relativo alla sequenza presente nella lista parziale
	 */
	private Double calcoloCosto(List<Citta> parziale) {
		
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
