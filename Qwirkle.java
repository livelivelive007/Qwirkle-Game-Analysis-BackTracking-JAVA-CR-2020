import java.util.*; 
import javax.swing.*;

class Qwirkle
{
	private JFrame frame;
	private Jugador jugador_1, jugador_2, jugador_3;
	private Tablero tablero;
	private BolsaFichas bolsa_fichas;
	private	String[] colores = {"ROJO", "VERDE", "AMARILLO", "AZUL", "MORADO", "NARANJA"};
	private	String[] figuras = {"TREBOL", "SOL", "ROMBO", "CUADRADO", "CIRCULO", "X"};
	private int opcion;
	private int size_tablero;
	private Map<Ficha, ArrayList<Fichas>> grupitos;

	public Qwirkle() 
	{		
		this.frame = new JFrame();

		this.bolsa_fichas = new BolsaFichas();
		this.fullFichasToBolsa(this.figuras);

		this.jugador_1 = new Jugador("Jeremy");
		this.jugador_2 = new Jugador("Esteban");
		this.jugador_3 = new Jugador("Computadora");
		this.dealCards();

		this.size_tablero = 20;
		this.tablero = new Tablero(this.size_tablero);
		
		this.grupitos = new HashMap<Ficha, ArrayList<Fichas>>();
		this.getPossiblePlaysHand();
		this.showPossiblePlaysHand();

		this.llenarTableroConEjemplo();
		this.imprimirTablero();
		
		this.opcion = 0;
		this.controlMenu();
	}

	public void printPlays(ArrayList<Fichas> pJugadas)
	{
		for (Ficha ficha : pJugadas) 
		{
			System.out.println(fichaToSimbol(ficha));	
		}
	}

	public void showPossiblePlaysHand()
	{
		for(Map.Entry<Ficha, ArrayList<Fichas>> entry:this.grupitos.entrySet())
		{    
        	Ficha key = entry.getKey();  
        	ArrayList<Fichas> value = entry.getValue(); 
        	System.out.println("\nFicha:" + key + "\nJugadas:" + printPlays(value));
		}
	}

	public Map<Ficha, ArrayList<Fichas>> getPossiblePlaysHand()
	{
		for(int indice_i=0;indice_i<CANT_FICHAS_MANO;indice_i++)
		{
			for(int indice_j=indice_i+1;indice_j<CANT_FICHAS_MANO;indice_j++)
			{
				if(!mano.get(indice_i).noCombina(mano.get(indice_j)))
				{
					grupitos[mano.get(indice_i)].append(mano.get(indice_j));

				}
			}
		}
		return this.grupitos;
	}

	public void dealCards()
	{
		for (int index=0; index<6; index++) 
		{
			this.giveHandPlayer(this.jugador_1);
			this.giveHandPlayer(this.jugador_2);
			this.giveHandPlayer(this.jugador_3);
		}
	}

	public void giveHandPlayer(Jugador pJugador)
	{
		int pRandom = (int) ((Math.random() *  this.bolsa_fichas.getLengthBolsaFichas()));
		pJugador.setFicha(this.bolsa_fichas.getFichaXIndex(pRandom));
		this.bolsa_fichas.clearFichaXIndex(pRandom);
	}

	public void fullFichasToBolsa(String[] pLista)
	{	
		for (int index=0; index<pLista.length; index++) 
		{
			this.llenoBolsaFichas(pLista[index]);	
		}
	}

	public void llenoBolsaFichas(String pFigura)
	{
		for (int index=0; index<3; index++) 
		{
			for (String color : this.colores) {
				this.asignoFicha(pFigura, color);		
			}
		}
	}

	public void asignoFicha(String pFigura, String pColor)
	{
		Ficha ficha = new Ficha();
		ficha.setFigura(pFigura);
		ficha.setColor(pColor);
		this.bolsa_fichas.addFicha(ficha);
	}

	public void controlMenu()
	{
		while(this.opcion < 2)
		{
			this.menu(this.jugador_1);
			this.menu(this.jugador_2);
			this.menu(this.jugador_3);
		}
	}

	public void menu(Jugador pJugador)
	{
		this.showMano(pJugador);
		pJugador.getTurno().setSuTurno(true);

		do{
			JOptionPane.showMessageDialog(this.frame, "Es el turno del jugador " 
				+ pJugador.getNombre());

			this.opcion = Integer.parseInt(JOptionPane.showInputDialog("0. Pasar de turno.\n"
				+ "1. Seleccionar jugada de la mano.\n"
				+ "2. Solicitas salir del Juego al final de la ronda de turnos."));

			if(opcion==0)
			{
				JOptionPane.showMessageDialog(this.frame, "No seleccionaste nada, pasas de turno." );
				pJugador.getTurno().setSuTurno(false);
			}
			else if(opcion==1)
			{
				JOptionPane.showMessageDialog(this.frame, "Se esta pensando que jugada seleccionar." );
				pJugador.getTurno().setSuTurno(false);
			}
			else 
				JOptionPane.showMessageDialog(this.frame, "El juego terminara cuando el jugador 3 termine su turno." );
				pJugador.getTurno().setSuTurno(false);
				break;

		}while(pJugador.getTurno().getSuTurno() != false);
	
	}

	public void showPtsJugador(Jugador pJugador)
	{
		System.out.println(pJugador.getScore().getPtsTotales());
	}


	//------------------------------------------------------------------


	public void showMano(Jugador pJugador)
	{
		String out="\n[ ";
		for (Ficha ficha:pJugador.getMano())
		{
			out+= fichaToSimbol(ficha)+", ";
		}
		System.out.println(out+"]");
	}

	private String getSimboloColor(String c)
	{
		if(c=="AMARILLO")
			return "am";
		else if(c=="AZUL")
			return "az";
		else if(c=="NARANJA")
			return "na";
		else if(c=="MORADO")
			return "mo";
		else if(c=="ROJO")
			return "ro";
		else if(c=="VERDE")
			return "ve";
		else return "";
		
	}

	private String getSimboloFigura(String f)
	{
		switch(f){
			case "CIRCULO":
				return "O";
			case "CUADRADO":
				return "C";
			case "ROMBO":
				return "R";
			case "SOL":
				return "S";
			case "TREBOL":
				return "T";
			case "X":
				return "X";
		}
		return "";
	}

	private String fichaToSimbol(Ficha ficha)
	{
		if(ficha==null)return "---";
		return getSimboloFigura(ficha.getFigura())+getSimboloColor(ficha.getColor());
	}

	public void imprimirTablero()
	{
		String out="";
		for(int i=0;i<this.size_tablero;i++){
			for(int j=0;j<this.size_tablero;j++)
				out+="# "+fichaToSimbol(this.tablero.getTablero()[i][j]) + " #";
			out+="\n";
		}
		System.out.println("\n"+out);
	}

	public void llenarTableroConEjemplo()
	{
		int mitadDeLaMatriz=this.size_tablero/2;
		meterFichaEnXY(new Ficha("ROMBO","AMARILLO"), mitadDeLaMatriz, mitadDeLaMatriz);
		meterFichaEnXY(new Ficha("SOL","AMARILLO"), mitadDeLaMatriz+1, mitadDeLaMatriz);
		meterFichaEnXY(new Ficha("TREBOL","AMARILLO"), mitadDeLaMatriz+2, mitadDeLaMatriz);
		meterFichaEnXY(new Ficha("CIRCULO","AMARILLO"), mitadDeLaMatriz-1, mitadDeLaMatriz);
	}

	public boolean meterFichaEnXY(Ficha ficha,int x,int y)
	{
		if(x<0||y<0||x>=this.size_tablero||y>=this.size_tablero)
			return false;
		this.tablero.setTablero(x,y,ficha);
		return true;
	}
}

/*

	public int getCantPuntos(int x,int y,Ficha ficha)
	{
		ArrayList<Ficha> hileraHorizontal=new ArrayList<Ficha>();
		ArrayList<Ficha> hileraVertical=new ArrayList<Ficha>();
		int inicioHilera=x,finHilera=x;
		int puntos=0;

		while(inicioHilera>0){
			if(fichas[inicioHilera-1][y]==null)
				break;
			else inicioHilera--;
		}
		while(finHilera<size_tablero-1){
			if(fichas[finHilera+1][y]==null)
				break;
			else finHilera++;
		}
		for(int i=inicioHilera;i<=finHilera;i++){
			if(i==x) hileraVertical.add(ficha);
			else hileraVertical.add(fichas[i][y]);
		}
		inicioHilera=finHilera=y;
		while(inicioHilera>0){
			if(fichas[x][inicioHilera-1]==null)
				break;
			else inicioHilera--;
		}
		while(finHilera<size_tablero-1){
			if(fichas[x][finHilera+1]==null)
				break;
			else finHilera++;
		}
		for(int i=inicioHilera;i<=finHilera;i++){
			if(i==y)hileraHorizontal.add(ficha);
			else hileraHorizontal.add(fichas[x][i]);
		}
		//buscar repetidos
		Map<Figura,Map<Color,Boolean>> mapaParaEncontrarRepetidos = new HashMap<>();

		for(Ficha f:hileraHorizontal){
			if(mapaParaEncontrarRepetidos.containsKey(f.getFigura()&&mapaParaEncontrarRepetidos.get(f.getFigura().containsKey(f.getColor()))
				return 0;
			// ArrayList<Ficha> pFichas_disponibles = getFichasDisponiblesAJugar(hileraHorizontal);
			// //recorrer pFichas_disponibles para saber si puedo poner la ficha que estoy colocando.
			// Boolean canI = canIDoPutFicha(pFichas_disponibles);
			mapaParaEncontrarRepetidos.putIfAbsent(f.getFigura(,new HashMap<>());
			mapaParaEncontrarRepetidos.get(f.getFigura().put(f.getColor(), true);
		}
		mapaParaEncontrarRepetidos=new HashMap<>();
		for(Ficha f:hileraVertical){
			if(mapaParaEncontrarRepetidos.containsKey(f.getFigura)&&mapaParaEncontrarRepetidos.get(f.getFigura().containsKey(f.getColor()))
				return 0;
			mapaParaEncontrarRepetidos.putIfAbsent(f.getFigura(,new HashMap<>());
			mapaParaEncontrarRepetidos.get(f.getFigura().put(f.getColor(), true);
		}
		System.out.println("pts horizonaral : "+hileraHorizontal.size() + "\npts vertical : "+hileraVertical.size());
		if(hileraHorizontal.size()>1)puntos+=hileraHorizontal.size();
		if(hileraVertical.size()>1)puntos+=hileraVertical.size();
		if(hileraVertical.size()==6)puntos+=6;
		if(hileraHorizontal.size()==6)puntos+=6;
		return puntos;
	}

*/

/*

	public List<Ficha> getCualesPuedoPoner(int x,int y)
	{
		List<Ficha>todasLasFichas=new ArrayList<>();
		for (Figura figura:Qwirkle.FIGURAS)
			for(Color color:Qwirkle.COLORES)
				 	.add(new Ficha(figura,color));
		int inicioHilera=x;
		int finHilera=x;
		while(inicioHilera>0){
			if(fichas[inicioHilera-1][y]==null)
				break;
			else inicioHilera--;
		}
		while(finHilera<MATRIX_SIDE-1){
			if(fichas[finHilera+1][y]==null)
				break;
			else finHilera++;
		}
		if(finHilera - inicioHilera>=6)return new ArrayList<>();
		for(int i=inicioHilera;i<=finHilera;i++){
			if(i!=x) {
				for(int j=0;j<todasLasFichas.size();){
					if(todasLasFichas.get(j).noCombina(fichas[i][y])){
						todasLasFichas.remove(j);
					} else j++;
				}
			}
		}
		inicioHilera=finHilera=y;
		while(inicioHilera>0){
			if(fichas[x][inicioHilera-1]==null)
				break;
			else inicioHilera--;
		}
		while(finHilera<MATRIX_SIDE-1){
			if(fichas[x][finHilera+1]==null)
				break;
			else finHilera++;
		}
		if(finHilera - inicioHilera>=6)return new ArrayList<>();
		for(int i=inicioHilera;i<=finHilera;i++){
			if(i!=y){
				for(int j=0;j<todasLasFichas.size();){
					if(todasLasFichas.get(j).noCombina(fichas[x][i])){
						todasLasFichas.remove(j);
					}else j++;
				}
			}
		}
		return todasLasFichas;		
	}

*/





/*	public void showBolsaFichas()
	{
		System.out.println(this.bolsa_fichas.getLengthBolsaFichas());		

		for (int index=0; index<this.bolsa_fichas.getLengthBolsaFichas(); index++) 
		{
			System.out.println("Estoy en el indice de la bolsa de fichas : " + index);

			System.out.println( "Figura -> " 
				+ this.bolsa_fichas.getFichaXIndex(index).getFigura() 
				+ "\n" 
				+ "Color -> "
				+ this.bolsa_fichas.getFichaXIndex(index).getColor());
		}
	}*/





/*
	public Tablero getTablero()
	{
		return this.tablero;
	}

	public int getSizeTablero()
	{
		return this.size_tablero;
	}
*/





	/*
			EJEMPLO #1
			Main ----> 
			this.llenarTableroConEjemplo();
			this.imprimirTablero();

	*/
/*


	*/



/*





		public String getFicha()
	{
		String ficha = getFigura()+getColor();
		return ficha;
	}
*/






/*	
	//Lleno matriz en blanco
	public void fullMatrizEmpty()
	{
		//Matriz 6x6
		for (int i=0; i<this.dimencion_inicial; i++) {
			this.fichas_totales = new Fichas();
			for (int j=0; j<this.dimencion_inicial; j++) {
				this.ficha = new Ficha();
				this.fichas_totales.setFichasFila(ficha);
			}
			this.tablero.setOneFilaFichas(fichas_totales);		
		}

	}
*/





/* 	public Boolean canIDoPutFicha(ArrayList<Ficha> pFichas_pDisponibles)
	{

	}
	public ArrayList<Ficha> getFichasDisponiblesAJugar(ArrayList<Ficha> pJugadas_tablero)
	{
		ArrayList<Ficha> pFichas_disponibles = new ArrayList<Ficha>();
		String[] pFiguras_disponibles = {"TREBOL","SOL","ROMBO","CUADRADO","CIRCULO","X"};
		String[] pColores_disponibles = {"ROJO","VERDE","AMARILLO","AZUL","MORADO","NARANJA"};

		for (pFicha : pJugadas_tablero) 
		{
			for (int pIndex=0; pIndex<pFichas_disponibles.length; pIndex++) 
			{
				if(pFicha.getFigura()==pFiguras_disponibles[pIndex])
				{
					if(pFicha.getColor()==pColores_disponibles[pIndex])
					{

					}
				}
				pFiguras_disponibles[pIndex];
			}

			pFichas_disponibles.add(pFicha);
		}

		return pFichas_disponibles;
	} */