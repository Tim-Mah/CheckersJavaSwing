import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;

@SuppressWarnings("serial")
/************************************************************
 * @author Tim Mah
 * @dueDate 28/05/21
 * @description This class extends the JButton class 
 * and changes the look of the button depending on what it is.
 ************************************************************/
public class Checkers extends JButton
{	
	private boolean isChecker = false;
	private boolean isBlack = true;
	private boolean isPointer = false;//shows where a piece can move
	public boolean isBlackSquare = false;
	public boolean isActive = false;
	private int[]position = new int[2];
	public boolean canCapture = false;
	public boolean isKing = false;
	public boolean hasCapped = false;

	//old chess.com colours
	//Color baseB = new Color(87, 83, 82);
	//Color shadeB = new Color(70, 66, 66);
	//Color baseW = new Color(248,248,248);
	//Color shadeW = new Color(211, 211, 211);
	//Color pointer = new Color(97,139,71);
	//Color boarderB = new Color(39,39,39);
	//Color boarderW = new Color(70,70,70);
	private Color baseB = new Color(6, 51, 85);
	private Color shadeB = new Color(2, 28, 56);
	private Color baseW = new Color(234,233,247);
	private Color shadeW = new Color(127, 128, 160);
	private Color pointer = new Color(39,67,117);
	private Color boarderB = new Color(0,18,47);
	private Color boarderW = new Color(67,70,96);

	/**
	 * Returns the diameter of a circle slightly smaller than this Checker().
	 * @return
	 */
	private int getDiameter(){
		int diameter = (int) Math.min(getWidth()-(getWidth()*.2), getHeight()-(getHeight()*.2));//make is slightly smaller than the square
		return diameter;
	}
	/**
	 * Changes the look of the checker if it is a checker or a pointer (shows valid moves).
	 * @param g The graphics for this component.
	 */
	@Override
	public void paintComponent(Graphics g){

		int diameter = getDiameter();
		int radius = diameter/2;

		if(isChecker == true && isBlack == true)
		{
			if(isKing)
			{
				//All of the values are ratios to make them slightly smaller or bigger, and to reposition them. They were done by trial an error.
				g.setColor(boarderB);//Do this first, colours the rounded rectangle, which is the border. The top and bottom is a semi-oval, the sides are strait.
				g.fillRoundRect((int)(getWidth()/2 - radius*1.033), (int)(getHeight()/2 - radius*0.95), (int)(diameter*1.06), (int)(diameter*0.98), (int)(diameter), (int)(diameter/1.3));

				g.setColor(shadeB);//Below is the shaded part of the checker, aka the side of the checker.
				g.fillRoundRect((int)(getWidth()/2 - radius), (int)(getHeight()/2- radius/1.1), diameter, (int)(diameter/1.1), diameter, (int)(diameter/1.3));

				g.setColor(baseB);//this is the top of the checker, and it is just an oval
				g.fillOval((int)(getWidth()/2 - radius), (int)(getHeight()/2 - radius/1.1), diameter, (int)(diameter/1.3));//inside of circle

				g.setColor(shadeB);//This is a dot to show that it is a king.
				g.fillOval((int)(getWidth()/2 - radius/3.8), (int)(getHeight()/2 - radius/2.5), (int)(diameter/3.3), (int)(diameter/5));//Dot to show it is a king
				this.repaint();
			}else
			{
				g.setColor(boarderB);//Do this first, colours the circle.
				g.fillRoundRect((int)(getWidth()/2 - radius*1.033), (int)(getHeight()/2 - radius*0.95), (int)(diameter*1.06), (int)(diameter*0.98), (int)(diameter), (int)(diameter/1.3));

				g.setColor(shadeB);//Do this first, colours the circle.
				g.fillRoundRect((int)(getWidth()/2 - radius), (int)(getHeight()/2- radius/1.1), diameter, (int)(diameter/1.1), diameter, (int)(diameter/1.3));

				g.setColor(baseB);
				g.fillOval((int)(getWidth()/2 - radius), (int)(getHeight()/2 - radius/1.1), diameter, (int)(diameter/1.3));//inside of circle	
				this.repaint();
			}
		}else if(isChecker == true && isBlack == false)
		{
			if(isKing)
			{
				g.setColor(boarderW);//Do this first, colours the circle.
				g.fillRoundRect((int)(getWidth()/2 - radius*1.033), (int)(getHeight()/2 - radius*0.95), (int)(diameter*1.06), (int)(diameter*0.98), (int)(diameter), (int)(diameter/1.3));

				g.setColor(shadeW);//Do this first, colours the circle.
				g.fillRoundRect((int)(getWidth()/2 - radius), (int)(getHeight()/2- radius/1.1), diameter, (int)(diameter/1.1), diameter, (int)(diameter/1.3));

				g.setColor(baseW);
				g.fillOval((int)(getWidth()/2 - radius), (int)(getHeight()/2 - radius/1.1), diameter, (int)(diameter/1.3));//inside of circle	

				g.setColor(shadeW);
				g.fillOval((int)(getWidth()/2 - radius/3.8), (int)(getHeight()/2 - radius/2.5), (int)(diameter/3.3), (int)(diameter/5));//Dot to show it is a king
				this.repaint();
			}else
			{
				g.setColor(boarderW);//Do this first, colours the circle.
				g.fillRoundRect((int)(getWidth()/2 - radius*1.033), (int)(getHeight()/2 - radius*0.95), (int)(diameter*1.06), (int)(diameter*0.98), (int)(diameter), (int)(diameter/1.3));

				g.setColor(shadeW);//Do this first, colours the circle.
				g.fillRoundRect((int)(getWidth()/2 - radius), (int)(getHeight()/2- radius/1.1), diameter, (int)(diameter/1.1), diameter, (int)(diameter/1.3));

				g.setColor(baseW);
				g.fillOval((int)(getWidth()/2 - radius), (int)(getHeight()/2 - radius/1.1), diameter, (int)(diameter/1.3));//inside of circle	

				this.repaint();
			}
		}else if(isPointer == true)
		{
			//Dividing everything by 4 to make a very small dot.
			g.setColor(pointer);//Do this first, colours the circle.
			g.drawOval(getWidth()/2 - radius/4, getHeight()/2 - radius/4, diameter/4, diameter/4);
			g.fillOval(getWidth()/2 - radius/4, getHeight()/2 - radius/4, diameter/4, diameter/4);
			this.repaint();
		}else{
			//if it isn't a pointer or a checker, it calls the regular paintComponent method from the super class, Component().
			super.paintComponent(g);
			this.repaint();
		}
	}
	/**
	 * Finds whether this object is a checker piece.
	 * @return true if it is a checker piece, false otherwise.
	 */
	public boolean isChecker()
	{
		return isChecker;
	}
	/**
	 * Sets this object to a checker or not.
	 * @param t if this object should be set to a checker.
	 */
	public void setChecker(boolean t)
	{
		isChecker = t;
	}
	/**
	 * Finds out if this is a black piece.
	 * @return whether this object is black.
	 */
	public boolean isBlack()
	{
		return isBlack;
	}
	/**
	 * Sets this object to black, otherwise it is assumed white.
	 * @param t whether this object is black.
	 */
	public void setBlack(boolean t)
	{
		isBlack = t;
	}
	/**
	 * Determines if this object is a pointer.
	 * @return true if it is, false otherwise.
	 */
	public boolean isPointer()
	{
		return isPointer;
	}
	/**
	 * Sets this object to a pointer or not a pointer.
	 * @param t if the object is a pointer.
	 */
	public void setPointer(boolean t)
	{
		isPointer = t;
	}
	/**
	 * Sets this object's position in 2D space.
	 * @param r the row number (0 is the first).
	 * @param c the column number (0 is the first).
	 */
	public void setPosition(int r, int c)
	{
		position[0] = r;
		position[1] = c;
	}
	/**
	 * Fetches the position of this object.
	 * @return the coordinates of this piece.
	 */
	public int[] getPosition()
	{
		return position;
	}

}//end Checkers
