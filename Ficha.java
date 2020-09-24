public class Ficha
{
	private String figura;
	private String color;
	private int x;
	private int y;

	public Ficha(String figura, String color) 
	{		
		this.figura = figura;
		this.color = color;
		this.x = -1;
		this.y = -1;
	}

	public Ficha(String figura, String color, int pX, int pY) 
	{		
		this.figura = figura;
		this.color = color;
		this.x = pX;
		this.y = pY;
	}

	public String getColor()
	{
		return this.color;
	}

	public String getFigura(){
		return figura;
	}

	public void setFigura(String pFigura)
    {
		this.figura = pFigura;
	}

	public void setColor(String pColor)
	{
		this.color = pColor;
	}

	public String toString()
	{
		return "Figura: "+figura+"$Color: "+color;
	}

}