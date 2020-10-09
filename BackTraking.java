import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.awt.Point;

public class BackTraking
{
	/**
	 * Score Limit For Set Up Easy Qwirkle
	 */
	private static final int SLFSUEQ=12;
	private Tablero tablero;
	private Set<Ficha>mano;
	private List<Jugada>jugadas;
	private boolean esMejorado;
	public final Random r=new Random();
	//Constructor
	BackTraking(Tablero tablero,ArrayList<Ficha>mano) 
	{		
		this.tablero=tablero;
		this.mano=new HashSet<>(mano);
	}
	
	BackTraking(Tablero tablero,ArrayList<Ficha>mano,boolean esMejorado) 
	{		
		this.tablero=tablero;
		this.mano=new HashSet<>(mano);
		this.esMejorado=esMejorado;
		initJugadasWithBackTracking();
		
	}
	private void initJugadasWithBackTracking(){
		jugadas=getJugadas(getPossiblePlaysHand(new ArrayList<>(mano)));
	}
	public Jugada getRespuesta(){
		if(esMejorado)
			return getJugadaMejorado();
		else
			return getJugadaBasico();
	}
	private Jugada getJugadaBasico(){
		if(jugadas.isEmpty())
			System.out.println("Satanásxd");
		jugadas.sort((o1,o2)->Integer.compare(o2.puntos, o1.puntos));
		return jugadas.get(0);
	}
	private Jugada getJugadaMejorado(){
		ejecutarMejorado();
		jugadas.sort((o1,o2)->Integer.compare(o2.puntos, o1.puntos));
		return jugadas.get(0);
	}
	private boolean cumpleAlgunCriterioDePoda(Jugada jugada){
		Boolean esPorFila=jugada.isLine;
		Jugadita parInicial=jugada.jugaditas.get(0);
		//para el criterio de no ponerle un qwirkle fácil al adversario
		if(jugada.puntos<SLFSUEQ){
			if(esPorFila==null||esPorFila){
				int derecha=parInicial.y;
				while(tablero.getFichas()[parInicial.x][derecha]!=null&&derecha<Tablero.MATRIX_SIDE - 1)
					derecha++;// Busca por fila a la derecha algún lugar nulo
				int izquierda=parInicial.y;
				while(tablero.getFichas()[parInicial.x][izquierda]!=null&&izquierda>0)izquierda--;
				//if(fichasPuestas[tablero.placesToPlay.get(x).get(y)]<3)return true;
				if(derecha-izquierda==5)return true;
			}
			if (esPorFila==null||!esPorFila){
				int arriba=parInicial.x;
				while(tablero.getFichas()[arriba][parInicial.y]!=null&&arriba<Tablero.MATRIX_SIDE-1)arriba++;
				int abajo=parInicial.x;
				while(tablero.getFichas()[abajo][parInicial.y]!=null&&abajo>0)abajo--;
				if(arriba-abajo==5)return true;
			}
			//falta evaluar si por cada ficha, prepara un qwirkle fácil pero perpendicular a la orientación de la jugada
			//también falta considerar si el espacio que falta para el qwirkle fácil está como en medio 
			//y diay, ya que estamos, también si prepara un qwirkle fácil perpendicular pero con el espacio vacío atravezado
			//también algo que faltaría pero sería ya demasiado (tal vez), es que si la ficha que falta para ese qwirkle fácil
			//ya no se puede jugar (porque ya hay en el tablero 3 de esa ficha)
			//si en la jugada perpendicular la ficha que falta la tengo en la mano, es una jugada perfecta
		}
		return false;
	}
	private void ejecutarMejorado(){
		jugadas.removeIf(jugada->cumpleAlgunCriterioDePoda(jugada));
	}

	public Map<Ficha, ArrayList<ArrayList<Ficha>>> getPossiblePlaysHand(ArrayList<Ficha> pFichas)
	{
		int cant_man = pFichas.size();
		Map<Ficha, ArrayList<ArrayList<Ficha>>> grupos = new HashMap<Ficha, ArrayList<ArrayList<Ficha>>>();

		for(int pI=0; pI<cant_man; pI++)
		{
			ArrayList<ArrayList<Ficha>> lista_fichas_slices = new ArrayList<ArrayList<Ficha>>();
			ArrayList<Ficha> combination_list_1 = new ArrayList<Ficha>();
			ArrayList<Ficha> combination_list_2 = new ArrayList<Ficha>();

			for(int pJ=0; pJ<cant_man; pJ++)
			{			
				if(!pFichas.get(pI).noCombina(pFichas.get(pJ)))
				{
					if(pFichas.get(pI).getFigura()!=pFichas.get(pJ).getFigura()
						&&pFichas.get(pI).getColor()==pFichas.get(pJ).getColor())
					{
						combination_list_1.add(pFichas.get(pJ));	
					}
					else if(pFichas.get(pI).getFigura()==pFichas.get(pJ).getFigura()
						&&pFichas.get(pI).getColor()!=pFichas.get(pJ).getColor())
					{
						combination_list_2.add(pFichas.get(pJ));
					}
				}
			}
				
			lista_fichas_slices.add(combination_list_1);
			lista_fichas_slices.add(combination_list_2);
			grupos.put(pFichas.get(pI), lista_fichas_slices);
			
		}
		

		return grupos;
	}
	public List<Jugada>getJugadas(Map<Ficha,ArrayList<ArrayList<Ficha>>>grupitos){
		List<Jugada>todasLasPosiblesJugadasCompletas=new ArrayList<>();
		for(Point xy : tablero.demeLasPosicionesEnQuePueddoEmpezarJugada()) {
				for(Entry<Ficha,ArrayList<ArrayList<Ficha>>> entradaGrupito:grupitos.entrySet()){
					if(tablero.getCualesSePuedePoner(xy.x,xy.y).contains(entradaGrupito.getKey())){
						generarArbolDeJugadas(entradaGrupito, todasLasPosiblesJugadasCompletas, xy.x, xy.y,tablero.getCualesSePuedePoner(xy.x,xy.y));						
					}
				}
			
		}
		return todasLasPosiblesJugadasCompletas;
	}

	private void generarArbolDeJugadas(Entry<Ficha,ArrayList<ArrayList<Ficha>>>jugadaDeLaMano,List<Jugada>jugadasCompletas,int x, int y,List<Ficha>original){
		//para la jugada con figura y la jugada por color, eliminar las que nothing to see with las otras fichas de ese vector de jugada (x,y)
		int xx=x;
		int yy=y;
		for(int i=0;i<jugadaDeLaMano.getValue().get(0).size();i++){
			if(tablero.getFichas()[x-1][y]!=null){
				if(jugadaDeLaMano.getValue().get(0).get(i).equals(tablero.getFichas()[x][y]))
					jugadaDeLaMano.getValue().get(0).remove(i);
				while(tablero.getFichas()[xx-1][yy]!=null){
					if(tablero.getFichas()[xx-1][yy].equals(jugadaDeLaMano.getValue().get(0).get(i)))
						jugadaDeLaMano.getValue().get(0).remove(i);
					else i++;
					xx--;
				}
				generarArbolDeJugadas(new ArrayList<>(jugadaDeLaMano.getValue().get(0)), jugadaDeLaMano.getKey(), jugadasCompletas, new Jugada(), x, y, true);
			}else if(tablero.getFichas()[x+1][y]!=null){
				if(jugadaDeLaMano.getValue().get(0).get(i).equals(tablero.getFichas()[x][y]))
					jugadaDeLaMano.getValue().get(0).remove(i);
				while(tablero.getFichas()[xx+1][yy]!=null){
					if(tablero.getFichas()[xx+1][yy].equals(jugadaDeLaMano.getValue().get(0).get(i)))
						jugadaDeLaMano.getValue().get(0).remove(i);
					else i++;
					xx++;					
				}
			}else if(tablero.getFichas()[x][y-1]!=null){
				if(jugadaDeLaMano.getValue().get(0).get(i).equals(tablero.getFichas()[x][y]))
					jugadaDeLaMano.getValue().get(0).remove(i);
				while(tablero.getFichas()[xx][yy-1]!=null){
					if(tablero.getFichas()[xx][yy-1].equals(jugadaDeLaMano.getValue().get(0).get(i)))
						jugadaDeLaMano.getValue().get(0).remove(i);
					else i++;
					yy--;
				}
			}else{
				if(jugadaDeLaMano.getValue().get(0).get(i).equals(tablero.getFichas()[x][y]))
					jugadaDeLaMano.getValue().get(0).remove(i);
				while(tablero.getFichas()[xx][yy+1]!=null){
					if(tablero.getFichas()[xx][yy+1].equals(jugadaDeLaMano.getValue().get(0).get(i)))
						jugadaDeLaMano.getValue().get(0).remove(i);
					else i++;
					yy++;
				}
			}
		}
			if(!jugadaDeLaMano.getValue().get(1).isEmpty())
				generarArbolDeJugadas(new ArrayList<>(jugadaDeLaMano.getValue().get(1)), jugadaDeLaMano.getKey(), jugadasCompletas, new Jugada(), x, y, null,original);
	}
	private void generarArbolDeJugadas(List<Ficha>fichasQueFaltanPorColocar,
					Ficha fichaInicial,List<Jugada>jugadasCompletas, 
					Jugada jugada,int x,int y,Boolean esPorFila){
		fichasQueFaltanPorColocar.remove(fichaInicial);
		jugada.jugaditas.add(new Jugadita(x, y, fichaInicial));
		jugada.isLine = esPorFila;
		tablero.getFichas()[x][y]=fichaInicial;//hacer la jugada de forma hipotética (porque luego se deshace la jugada)
		if(fichasQueFaltanPorColocar.isEmpty()){ //Si no hacen falta fichas por colocar, este arbol de posible jugada estaría completo, por lo que termina la recursividad
			jugadasCompletas.add(jugada.copy(tablero.getPuntos(jugada)));
		}
		else{
			boolean flag=false;
			for (int indiceFichasPorColocar=0;indiceFichasPorColocar<fichasQueFaltanPorColocar.size();indiceFichasPorColocar++){//Para cada ficha
				Ficha fichaPorColocar=fichasQueFaltanPorColocar.get(indiceFichasPorColocar);
				if(esPorFila==null||esPorFila){
					int nextY=y;
					while(tablero.getFichas()[x][nextY]!=null&&nextY<Tablero.MATRIX_SIDE-1)
						nextY++;//aquí falta algo--->eliminar de la jugada las que se están "saltando"
					if(tablero.getCualesSePuedePoner(x,nextY).contains(fichaPorColocar)){
						generarArbolDeJugadas(fichasQueFaltanPorColocar, fichaPorColocar, jugadasCompletas, jugada, x,nextY,true);
						flag=true;
					}	
					nextY=y;
					while(tablero.getFichas()[x][nextY]!=null&&nextY>0)nextY--;
					if(tablero.getCualesSePuedePoner(x,nextY).contains(fichaPorColocar)){
						generarArbolDeJugadas(fichasQueFaltanPorColocar, fichaPorColocar, jugadasCompletas, jugada, x,nextY,true);
						flag=true;
					}	
				}if (esPorFila==null||!esPorFila){
					int nextX=x;
					while(tablero.getFichas()[nextX][y]!=null&&nextX<Tablero.MATRIX_SIDE-1)nextX++;
					if(tablero.getCualesSePuedePoner(nextX, y).contains(fichaPorColocar)){
						generarArbolDeJugadas(fichasQueFaltanPorColocar, fichaPorColocar, jugadasCompletas, jugada, nextX, y,false);
						flag=true;
					}
					nextX=x;
					while(tablero.getFichas()[nextX][y]!=null&&nextX>0)nextX--;
					if(tablero.getCualesSePuedePoner(nextX, y).contains(fichaPorColocar)){
						generarArbolDeJugadas(fichasQueFaltanPorColocar, fichaPorColocar, jugadasCompletas, jugada, nextX, y,false);
						flag=true;
					}
				}
			}//Si no encontró lugar para poner 
			if(!flag) jugadasCompletas.add(jugada.copy(tablero.getPuntos(jugada)));
		}
		tablero.getFichas()[x][y]=null;
		jugada.jugaditas.remove(jugada.jugaditas.size()-1);
		fichasQueFaltanPorColocar.add(fichaInicial);
	}
}