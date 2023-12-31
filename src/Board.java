import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
/***********************************************************
 * @author Tim Mah
 * @dueDate 28/05/21
 * @description This class is the main class and it contains 
 * an inner class which runs the full game of checkers.
 ***********************************************************/
public class Board
{
	static Board game;//the instance of Board which runs the whole game.
	boolean blackTurn = false;//if it is black's turn.
	boolean used = false;//determines if something has been used once.
	JFrame window;//The window that opens up.
	JPanel backBoard;//holds all the other components
	Checkers[][] squares;//every square in the board

	//Old colour scheme.
	//green Color(108, 155, 79)
	//white tile = new Color(237, 240, 207);
	//background = new Color(49,46,43);

	Color blue = new Color(84, 151, 223);//black square colour.
	Color whiteTile = new Color(248, 235, 240);//white tile colour
	Color background = new Color(32,34,37);//background colour
	Color name = new Color(188, 187, 183);//Colour of the names text label.

	JButton rules;//button that shows the rules.
	JButton reset;//button tha resets the game.

	JButton resign1 = new JButton("Resign");//for White (Player 1) to resign
	JButton resign2 = new JButton("Resign");//for Black (Player 2) to resign

	JPanel[] blank = new JPanel[30];//To fill up the grid so that everything is in the right place.
	JLabel player1 = new JLabel("White");//Was called player 1, but White and Black make it easier to remember
	JLabel player2 = new JLabel("Black");

	Font regular = new Font("Verdana", Font.BOLD,12);//Regular Font, Heading, and Subtitle fonts
	Font subtitle = new Font("Verdana", Font.BOLD,14);
	Font heading = new Font("Verdana", Font.BOLD,18);

	//Rules components
	JFrame rulesF = new JFrame("Rules");
	JPanel rulesP = new JPanel();//The panel that will contain the rules and be added to the frame rulesF.
	JLabel rulesTXT = new JLabel();//The actual rules in text

	//Popup Menu - shows the winner/draw screen
	JPopupMenu end = new JPopupMenu();
	boolean whiteHasWon = false;
	/**
	 * Sets up the rules window and makes it visible.
	 */
	public void rules()//method to set up the rules
	{
		rulesTXT.setIcon(null);
		rulesF.setTitle("Rules");
		rulesTXT.setText("<html>White starts first."
				+ "<br/>You may click on a piece, and it will show you the moves the piece can make, if any."
				+ "<br/>Pieces move diagonally forward only, unless capturing."
				+ "<br/>If you can jump diagonally over 1 piece an opponent controls, it is a capture and you remove their piece."
				+ "<br/>You must capture if you can."
				+ "<br/>Once you capture, if you can capture again with the same piece, you must continue to capture."
				+ "<br/>Once a piece reaches the end, it becomes a stack, aka. King, and can move backwards and forwards."
				+ "<br/>At any point, you may resign (concede/forfeit) The button closest to your side is your resign button."
				+ "<br/>The last person with pieces is the winner.</html>");
		rulesTXT.setFont(regular);
		rulesTXT.setForeground(name);

		if(used == false)
		{
			rulesTXT.addMouseListener(new MouseListener() {

				@Override
				public void mousePressed(MouseEvent e)
				{				
				}

				@Override
				public void mouseReleased(MouseEvent e)
				{				
				}

				@Override
				public void mouseEntered(MouseEvent e)
				{				
				}

				@Override
				public void mouseExited(MouseEvent e)
				{				
				}
			});
		}

		rulesP.add(rulesTXT);
		rulesP.setBackground(background);
		rulesP.setVisible(true);
		rulesF.setBackground(background);

		rulesF.add(rulesP);
		rulesF.pack();
		rulesF.setVisible(true);
	}
	/**
	 * Sets up the checker pieces on the board.
	 */
	public void setPieces()
	{
		for(byte r = 0; r<8; r++)
		{
			for(byte c = 0; c<8; c++)
			{
				squares[r][c].addActionListener(new checkerListen());//allows the checker to be clicked.
				squares[r][c].addMouseMotionListener(new checkerMouse());//Makes the hand icon appear when over a checker.
				if(r<3 && squares[r][c].isBlackSquare)//adds the black checkers
				{
					squares[r][c].setChecker(true);
					squares[r][c].setBlack(true);
				}else if(r>4 && squares[r][c].isBlackSquare)//adds the white checkers
				{
					squares[r][c].setChecker(true);
					squares[r][c].setBlack(false);
				}
			}
		}
	}
	/**
	 * Sets up the main window and all of the buttons, and the board.
	 */
	public void setUpComponents()//Initialises all the variables and gives them the right colour, font and text label
	{
		for(byte b = 0; b<blank.length; b++)
		{
			blank[b] = new JPanel();
			blank[b].setBackground(background);
		}
		rules = new JButton("Rules");
		reset = new JButton("Reset");

		rules.addActionListener(new rulesListen());

		rules.setOpaque(true);
		reset.setOpaque(true);
		rules.setForeground(Color.WHITE);
		rules.setBackground(background);
		reset.setForeground(Color.WHITE);
		reset.setBackground(background);

		reset.setBorder(null);
		rules.setBorder(null);

		reset.setFont(heading);
		rules.setFont(heading);

		reset.addActionListener(new reset());

		player1.setOpaque(true);
		player2.setOpaque(true);
		player1.setBackground(background);
		player2.setBackground(background);

		player1.setForeground(Color.WHITE);//it is player 1's turn first
		player2.setForeground(name);

		player1.setFont(regular);
		player2.setFont(regular);

		resign1.setBorder(null);//So button doesn't appear, just text
		resign2.setBorder(null);

		resign1.setOpaque(true);//allow colours to show on buttons
		resign2.setOpaque(true);
		resign1.setBackground(background);
		resign2.setBackground(background);

		resign1.addActionListener(new resignListen1());
		resign2.addActionListener(new resignListen2());

		resign1.setForeground(name);
		resign2.setForeground(name);
		resign1.setFont(subtitle);
		resign2.setFont(subtitle);

		window = new JFrame();
		squares = new Checkers[8][8];//English Checkers is 8x8, not doing international, which is 12x12
		backBoard = new JPanel(new GridLayout(10,9));

		window.setSize(720,900);
		window.setTitle("Checkers");
	}
	/**
	 * Calls rules() once clicked.
	 * @author Tim Mah
	 */
	class rulesListen implements ActionListener//press rules button for this to show up
	{
		/**
		 * Calls rules
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//when this is clicked, the rules shows up
			rules();
		}
	}
	/**
	 * Changes mouse icon to the hand over the player's pieces if it is their turn.
	 * @author Tim Mah
	 *
	 */
	class checkerMouse implements MouseMotionListener
	{
		/**
		 * If the mouse moves, it checkers whether it is over a checker or pointer, then changes the cursor if nessesary.
		 */
		@Override
		public void mouseMoved(MouseEvent e)
		{
			Checkers temp = (Checkers) e.getSource();
			int[] position = temp.getPosition();
			int r = position[0];
			int c = position[1];

			//only checkers and pointers can be clicked, so the hand cursor will be shown	
			if((squares[r][c].isChecker() && (squares[r][c].isBlack() && blackTurn == true))|| squares[r][c].isPointer())//black checker on black turn
			{
				squares[r][c].setCursor(new Cursor(Cursor.HAND_CURSOR));
			}else if((squares[r][c].isChecker() && !squares[r][c].isBlack() && blackTurn == false)|| squares[r][c].isPointer())//white checker on white turn
			{
				squares[r][c].setCursor(new Cursor(Cursor.HAND_CURSOR));
			}else
			{
				squares[r][c].setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		/**
		 * Did not use this, dragging mouse has no effect.
		 */
		@Override
		public void mouseDragged(MouseEvent e)
		{
			// TODO Auto-generated method stub

		}
	}
	/**
	 * This is the action listener for White's resign(concede/forfeit) button.
	 * @author Tim Mah
	 */
	class resignListen1 implements ActionListener
	{
		/**
		 * Calls winScreen(), which will show that Black won.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//player 2 wins, aka black
			winScreen("resignation");//black is assumed to have won if whiteHasWon isn't true
		}
	}
	/**
	 * This is the action listener for Black's resign(concede/forfeit) button.
	 * @author Tim Mah
	 */
	class resignListen2 implements ActionListener
	{
		/**
		 * Calls winScreen(), which will show that White won.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//player 1 wins, aka white
			whiteHasWon = true;
			winScreen("resignation");
		}
	}
	/**
	 * ACtion listener for reseting the game.
	 * @author Tim Mah
	 */
	class reset implements ActionListener
	{
		/**
		 * Restes all of the variables.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			whiteHasWon = false;
			blackTurn = false;
			for(Checkers[] r:squares) {
				for(Checkers c:r) {
					c.setChecker(false);
					c.canCapture = false;
					c.hasCapped = false;
					c.isActive = false;
					c.isKing = false;
					c.setPointer(false);
				}
			}
			resign1.addActionListener(new resignListen1());
			resign2.addActionListener(new resignListen2());
			setPieces();
			end.setVisible(false);
			end = null;
			end = new JPopupMenu();
		}
	}
	/**
	 * Plays a sound file from the project.
	 * @param soundName the name of the file.
	 */
	public void playSound(String soundName)
	{

		AudioInputStream audioInputStream;
		if(soundName == "rules.wav")
		{
			try
			{
				audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
				Clip clipR = AudioSystem.getClip( );
				clipR.open(audioInputStream);
				clipR.start( );
			}catch(Exception e)
			{
				window.dispose();
				System.exit(0); 
			}
		}else
		{
			try
			{
				audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
				Clip clip = AudioSystem.getClip( );
				clip.open(audioInputStream);
				clip.start( );
			}catch(Exception e)
			{

			}
		}

	}
	/**
	 * Displays a pop-up menu showing the winner and how they won.
	 * @param methodWon the way that they won.
	 */
	public void winScreen(String methodWon)
	{
		playSound("Win.wav");
		//lock board
		lockBoard();
		JLabel text = new JLabel();
		JPanel winComponents = new JPanel(new GridLayout(9, 3));
		String winText = null;
		//player x won by 
		//1. capturing all the pieces
		//2. resignation

		if(whiteHasWon)
		{
			winText = "<html>White beat Black <br/> by " + methodWon + "<html>";
		}else
		{
			winText = "<html>Black beat White <br/> by " + methodWon + "<html>";
		}
		text.setText(winText);
		end.setBackground(background);
		text.setBackground(background);
		text.setFont(subtitle);
		text.setForeground(Color.WHITE);
		for(byte b = 0; b<13; b++)
		{
			winComponents.add(blank[b]);
		}
		winComponents.add(text);
		for(byte b = 13; b<26; b++)
		{
			winComponents.add(blank[b]);
		}
		winComponents.setBackground(background);
		end.add(winComponents);
		end.setVisible(true);//have to make this visible, otherwise it doesn't have a width or height
		//Opens the popup depending on where the Checkers window is.
		end.setLocation(window.getX() + (int)(window.getWidth()/2) - (int)(end.getWidth()/2), window.getY() + (int)(window.getHeight()/2) - (int)(end.getHeight()/2));

	}
	/**
	 * Checks if anyone has won the game.
	 */
	public void checkWin()
	{
		//See if the opposing team's pieces are all captured
		boolean blackWins = true;
		for(byte r = 0; r<8; r++)
		{
			for(byte c = 0; c<8; c++)
			{
				if(!squares[r][c].isBlack() && squares[r][c].isChecker())//if there is 1 white piece, then black has not won via capturing
				{
					blackWins = false;
				}
			}
		}
		if(blackWins)
		{
			winScreen("capturing all pieces");
			return;
		}//if black has won, the other conditions don't have to be checked

		blackWins = false;//whiteWins, but saving a variable

		for(byte r = 0; r<8; r++)
		{
			for(byte c = 0; c<8; c++)
			{
				if(squares[r][c].isBlack() && squares[r][c].isChecker())//check that there are no black pieces
				{
					blackWins = true;
				}
			}
		}
		if(!blackWins)
		{
			whiteHasWon = true;
			winScreen("capturing all pieces");
			return;
		}

		if(canMove() == false)
		{
			if(blackTurn)//white wins
			{
				whiteHasWon = true;
			}
			winScreen("blocking all of opponent's moves");
			return;
		}

		//last case is if there is 1 king on each side, it is a draw
		int blackKings = 0;
		int whiteKings = 0;
		int totalPieces = 0;
		for(byte r = 0; r<8; r++)
		{
			for(byte c = 0; c<8; c++)
			{
				if(squares[r][c].isChecker())
				{
					totalPieces++;
					if(squares[r][c].isKing)//check that there are no black pieces
					{
						if(squares[r][c].isBlack())
						{
							blackKings++;
						}else
						{
							whiteKings++;
						}
					}
				}
			}
		}
		if(blackKings == 1 && whiteKings == 1 && totalPieces == 2) draw();
	}
	/**
	 * Displays a pop-up menu showing the draw (tie) screen.
	 */

	public void draw()
	{
		//lock board
		lockBoard();
		JLabel text = new JLabel();
		JPanel winComponents = new JPanel(new GridLayout(9, 3));
		String winText = null; 
		winText = "**Draw**";

		text.setText(winText);
		end.setBackground(background);
		text.setBackground(background);
		text.setFont(heading);
		text.setForeground(Color.WHITE);
		for(byte b = 0; b<13; b++)
		{
			winComponents.add(blank[b]);
		}
		winComponents.add(text);
		for(byte b = 13; b<26; b++)
		{
			winComponents.add(blank[b]);
		}
		winComponents.setBackground(background);
		end.add(winComponents);
		end.setVisible(true);//have to make this visible, otherwise it doesn't have a width or height
		//Opens the popup depending on where the Checkers window is.
		end.setLocation(window.getX() + (int)(window.getWidth()/2) - (int)(end.getWidth()/2), window.getY() + (int)(window.getHeight()/2) - (int)(end.getHeight()/2));

	}

	/**
	 * Removes ActionListeners from all the checkers and the resign buttons.
	 */
	public void lockBoard()
	{
		//remove all action listeners from the checkers
		for(JButton[] r:squares)
		{
			for(JButton c:r)
			{
				for(ActionListener a: c.getActionListeners())
				{
					c.removeActionListener(a);
				}
				for(MouseMotionListener m: c.getMouseMotionListeners())
				{
					c.removeMouseMotionListener(m);
				}
			}
		}
		//remove actionListeners from the resign button
		for(ActionListener a: resign1.getActionListeners())
		{
			resign1.removeActionListener(a);
		}
		for(ActionListener a: resign2.getActionListeners())
		{
			resign2.removeActionListener(a);
		}
	}
	/**
	 * Runs methods associated with a checker piece, showing valid spaces and moving the piece.
	 * @author Tim Mah
	 */
	class checkerListen implements ActionListener//press for available spaces to show up.
	{
		/**
		 * Checks if this is a pointer, if it is, it calls move(), if it is a checker, 
		 * it shows valid spaces, and if it is neither, it hides the pointers.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {	
			//gets the position of this checker
			Checkers temp = (Checkers) e.getSource();
			int[] position = temp.getPosition();
			int r = position[0];
			int c = position[1];

			//clear canCapture checkers info
			for(byte r1 = 0; r1<8; r1++)
			{
				for(byte c1 = 0; c1<8; c1++)
				{
					squares[r1][c1].canCapture = false;
					squares[r1][c1].hasCapped = false;
				}
			}
			if(squares[r][c].isPointer() == false)//we don't want this to run if it is a pointer.
			{

				//If a checker is already showing moves, and another one is clicked, we want to clear the pointers
				for(byte row = 0; row<8; row++)
				{
					for(byte column= 0; column<8; column++)
					{
						squares[row][column].setPointer(false);
						squares[row][column].isActive = false;//also if we click away, no checker will be active
					}
				}

				if(temp.isChecker())//Show all the valid moves if the button is a checker
				{
					//check all pieces that can capture.
					canCapture();
					boolean canCap = false;
					for(byte row = 0; row<8; row++)
					{
						for(byte column= 0; column<8; column++)
						{
							if(squares[row][column].canCapture) {
								canCap = true;
							}
						}
					}
					if(canCap)//if any piece can capture, then any checkers that can capture are valid spaces.
					{
						if(blackTurn && temp.isBlack())
						{
							squares[r][c].isActive = true;
							blackCapMoves(r, c);
						}else if(!blackTurn && !temp.isBlack())
						{
							squares[r][c].isActive = true;
							whiteCapMoves(r, c);
						}
						//else (no pieces can capture) then regular move, run code below
					}else
					{
						if(temp.isBlack() == true && blackTurn == true)//This is when pointers are set, so it has to be your turn to click on your piece.
						{
							squares[r][c].isActive = true;
							validSpaces(r, c);
						}else if(temp.isBlack() == false && blackTurn == false)
						{
							squares[r][c].isActive = true;
							validSpaces(r, c);
						}
					}
				}
			}else//at this point, we already established that this piece can me moved, so we move it.
			{
				//Clear all the pointers
				for(byte row = 0; row<8; row++)
				{
					for(byte column= 0; column<8; column++)
					{
						squares[row][column].setPointer(false);
					}
				}
				move(r, c);
			}
		}//end actionPerformed()
	}
	/**
	 *  
	 */
	/**
	 * Finds out of a player has any moves.
	 * @return true if they can move, false otherwise.
	 */
	public boolean canMove()//if any pieces of a player can move
	{
		if(blackTurn)//same as blackMoves, but uses return statements instead of showing pointers
		{
			for(byte r =0; r<8; r++)//check if any of the black pieces can move
			{
				for(byte c = 0; c<8; c++)
				{
					if(squares[r][c].isKing && squares[r][c].isBlack() && squares[r][c].isChecker())
					{
						//regular moves, but opposite direction
						try
						{
							if(squares[r-1][c-1].isBlackSquare && !squares[r-1][c-1].isChecker())//empty black square
							{
								return true;
							}
						}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
						{

						}

						try
						{
							if(squares[r-1][c+1].isBlackSquare && squares[r-1][c+1].isChecker() == false)//empty black square
							{
								return true;
							}
						}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
						{

						}

						try//if it is a king, it needs to be able to skip backwards
						{
							if((!squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker())&& !squares[r-2][c-2].isChecker())
							{
								return true;
							}

						}catch(ArrayIndexOutOfBoundsException e)
						{

						}

						try//if it is a king, it needs to be able to skip backwards
						{
							if((!squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker())&& !squares[r-2][c+2].isChecker())
							{
								return true;
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{

						}
					}//end of the king's legal moves

					if(squares[r][c].isBlack() && squares[r][c].isChecker())//makes sure it s a black checker
					{
						//regular piece's moves
						try//checks the left diagonal
						{
							if(squares[r+1][c-1].isBlackSquare && !squares[r+1][c-1].isChecker())//empty black square
							{
								return true;
							}

						}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error, means I don't have to check if it will throw an error
						{

						}
						try//checks the right diagonal
						{
							if(squares[r+1][c+1].isBlackSquare && !squares[r+1][c+1].isChecker())//empty black square
							{
								return true;
							}
						}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
						{

						}

						//checks for skips
						try
						{
							if((!squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
							{
								return true;
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{
						}

						try
						{
							if((!squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
							{
								return true;
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{

						}
					}
				}
			}
		}else
		{
			for(byte r =0; r<8; r++)//check if any of the white pieces can move
			{
				for(byte c = 0; c<8; c++)
				{
					if(squares[r][c].isKing && !squares[r][c].isBlack())//King moves (captures and regular)
					{
						//regular moves, but opposite direction
						try//checks the left diagonal
						{
							if(squares[r+1][c-1].isBlackSquare && !squares[r+1][c-1].isChecker())//empty black square
							{
								return true;
							}

						}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error, means I don't have to check if it will throw an error
						{

						}
						try//checks the right diagonal
						{
							if(squares[r+1][c+1].isBlackSquare && !squares[r+1][c+1].isChecker())//empty black square
							{
								return true;
							}
						}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
						{

						}

						try
						{
							//checkers diagonal to it are black, and not 2nd last row
							if((squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
							{
								return true;
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{

						}

						try
						{
							if((squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
							{
								return true;
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{

						}
					}//end of all the king's moves

					//regular moves
					try
					{
						if(squares[r-1][c-1].isBlackSquare && !squares[r-1][c-1].isChecker() && !squares[r][c].isBlack())//empty black square
						{
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
					{

					}

					try
					{
						if(squares[r-1][c+1].isBlackSquare && squares[r-1][c+1].isChecker() == false && !squares[r][c].isBlack())//empty black square
						{
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
					{

					}

					try
					{
						if((squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker())&& !squares[r-2][c+2].isChecker() && !squares[r][c].isBlack())
						{
							return true;
						}
					}catch(ArrayIndexOutOfBoundsException e)
					{

					}

					try
					{
						if((squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker())&& !squares[r-2][c-2].isChecker() && !squares[r][c].isBlack())
						{
							return true;
						}

					}catch(ArrayIndexOutOfBoundsException e)
					{

					}
				}
			}
		}
		return false;
	}
	/**
	 * For each checker, this method sets the canCapture attribute of the Checker if it can capture.
	 */
	public void canCapture()//determines if any pieces can capture
	{
		for(byte r = 0;r<8; r++)
		{
			for(byte c = 0;c<8; c++)
			{
				if(squares[r][c].isChecker())
				{
					if(blackTurn && squares[r][c].isBlack())
					{
						if(squares[r][c].isKing)//black king's potential captures
						{
							try {
								if((!squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
								{
									squares[r][c].canCapture = true;
								}
							}catch(ArrayIndexOutOfBoundsException e)
							{

							}
							try {
								if((!squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
								{
									squares[r][c].canCapture = true;
								}
							}catch(ArrayIndexOutOfBoundsException e)
							{

							}
							try {
								if((!squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker()) && !squares[r-2][c-2].isChecker())
								{
									squares[r][c].canCapture = true;
								}
							}catch(ArrayIndexOutOfBoundsException e)
							{

							}
							try {
								if((!squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker()) && !squares[r-2][c+2].isChecker())
								{
									squares[r][c].canCapture = true;
								}
							}catch(ArrayIndexOutOfBoundsException e)
							{

							}
						}

						//normal black potential captures

						try {
							if((!squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
							{
								squares[r][c].canCapture = true;
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{

						}
						try {
							if((!squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
							{
								squares[r][c].canCapture = true;
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{

						}
					}else if(!blackTurn && !squares[r][c].isBlack())//white's turn
					{
						if(squares[r][c].isKing)//white king's potential captures
						{
							try {
								if((squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker()) && !squares[r-2][c-2].isChecker())
								{
									squares[r][c].canCapture = true;
								}
							}catch(ArrayIndexOutOfBoundsException e)
							{

							}
							try {
								if((squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker()) && !squares[r-2][c+2].isChecker())
								{
									squares[r][c].canCapture = true;
								}
							}catch(ArrayIndexOutOfBoundsException e)
							{

							}
							try {
								if((squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
								{
									squares[r][c].canCapture = true;
								}
							}catch(ArrayIndexOutOfBoundsException e)
							{

							}
							try {
								if((squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
								{
									squares[r][c].canCapture = true;
								}
							}catch(ArrayIndexOutOfBoundsException e)
							{

							}
						}
						//normal white potential captures
						try {
							if((squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker()) && !squares[r-2][c-2].isChecker())//checkers diagonal to it are black, and not 2nd last row
							{
								squares[r][c].canCapture = true;
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{

						}
						try {
							if((squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker()) && !squares[r-2][c+2].isChecker())
							{
								squares[r][c].canCapture = true;
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{

						}
					}
				}
			}
		}
	}
	/**
	 * Sets the canCapture attribute of the Checker at row r and column c if it can capture.
	 * @param r the row of the checker being checked
	 * @param c the column of the checker being checked
	 */
	public void canCapture(int r, int c)
	{
		//king pieces
		if(squares[r][c].isKing)
		{
			//if it's a white king piece
			if(!blackTurn)
			{
				try {//if it doesn't run, then it isn't a valid move.
					if((squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker()) && !squares[r-2][c-2].isChecker())
					{
						squares[r][c].canCapture = true;
					}
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
				try {
					if((squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker()) && !squares[r-2][c+2].isChecker())
					{
						squares[r][c].canCapture = true;
					}
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
				try {
					if((squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
					{
						squares[r][c].canCapture = true;
					}
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
				try {
					if((squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
					{
						squares[r][c].canCapture = true;
					}
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
			}else
			{//black king piece
				try {
					if((!squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
					{
						squares[r][c].canCapture = true;
					}
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
				try {
					if((!squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
					{
						squares[r][c].canCapture = true;
					}
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
				try {
					if((!squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker()) && !squares[r-2][c-2].isChecker())
					{
						squares[r][c].canCapture = true;
					}
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
				try {
					if((!squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker()) && !squares[r-2][c+2].isChecker())
					{
						squares[r][c].canCapture = true;
					}
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
			}//if it is black's king
		}//end check if it is a king

		//white
		if(!blackTurn)
		{
			try {
				if((squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker()) && !squares[r-2][c-2].isChecker())//checkers diagonal to it are black, and not 2nd last row
				{
					squares[r][c].canCapture = true;
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}
			try {
				if((squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker()) && !squares[r-2][c+2].isChecker())
				{
					squares[r][c].canCapture = true;
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}
		}else
		{
			try {
				if((!squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
				{
					squares[r][c].canCapture = true;
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}
			try {
				if((!squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
				{
					squares[r][c].canCapture = true;
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}
		}
	}

	/**
	 * Shows what capture moves a black checker at row r and column c can do.
	 * @param r the row of the checker
	 * @param c the column of the checker.
	 */
	public void blackCapMoves(int r, int c)
	{
		if(squares[r][c].isKing)//king's capture checks
		{
			try//if it is a king, it needs to be able to skip backwards
			{
				if((!squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker())&& !squares[r-2][c-2].isChecker())
				{
					squares[r-2][c-2].setPointer(true);
				}

			}catch(ArrayIndexOutOfBoundsException e)
			{

			}

			try//if it is a king, it needs to be able to skip backwards
			{
				if((!squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker())&& !squares[r-2][c+2].isChecker())
				{
					squares[r-2][c+2].setPointer(true);
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}
		}//end of the king's captures

		//regular captures
		try
		{
			if((!squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
			{
				squares[r+2][c-2].setPointer(true);
			}
		}catch(ArrayIndexOutOfBoundsException e)
		{
		}

		try
		{
			if((!squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
			{
				squares[r+2][c+2].setPointer(true);
			}
		}catch(ArrayIndexOutOfBoundsException e)
		{

		}
	}

	/**
	 * Shows all of the black piece's moves.
	 * @param r the row the piece is in.
	 * @param c the column the piece is in.
	 */
	public void blackMoves(int r, int c)
	{
		if(squares[r][c].isKing)
		{
			//regular moves, but opposite direction
			try
			{
				if(squares[r-1][c-1].isBlackSquare && !squares[r-1][c-1].isChecker())//empty black square
				{
					squares[r-1][c-1].setPointer(true);
				}
			}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
			{

			}

			try
			{
				if(squares[r-1][c+1].isBlackSquare && squares[r-1][c+1].isChecker() == false)//empty black square
				{
					squares[r-1][c+1].setPointer(true);
				}
			}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
			{

			}

			try//if it is a king, it needs to be able to skip backwards
			{
				if((!squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker())&& !squares[r-2][c-2].isChecker())
				{
					squares[r-2][c-2].setPointer(true);
				}

			}catch(ArrayIndexOutOfBoundsException e)
			{

			}

			try//if it is a king, it needs to be able to skip backwards
			{
				if((!squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker())&& !squares[r-2][c+2].isChecker())
				{
					squares[r-2][c+2].setPointer(true);
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}
		}//end of the king's legal moves

		//regular piece's moves
		try//checks the left diagonal
		{
			if(squares[r+1][c-1].isBlackSquare && !squares[r+1][c-1].isChecker())//empty black square
			{
				squares[r+1][c-1].setPointer(true);
			}

		}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error, means I don't have to check if it will throw an error
		{

		}
		try//checks the right diagonal
		{
			if(squares[r+1][c+1].isBlackSquare && !squares[r+1][c+1].isChecker())//empty black square
			{
				squares[r+1][c+1].setPointer(true);
			}
		}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
		{

		}

		//checks for skips
		try
		{
			if((!squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
			{
				squares[r+2][c-2].setPointer(true);
			}
		}catch(ArrayIndexOutOfBoundsException e)
		{
		}

		try
		{
			if((!squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
			{
				squares[r+2][c+2].setPointer(true);
			}
		}catch(ArrayIndexOutOfBoundsException e)
		{

		}
	}

	/**
	 * Shows what capture moves a white checker at row r and column c can do.
	 * @param r the row of the checker
	 * @param c the column of the checker.
	 */
	public void whiteCapMoves(int r, int c)
	{
		if(squares[r][c].isKing)//king captures
		{
			try
			{
				//checkers diagonal to it are black, and not 2nd last row
				if((squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
				{
					squares[r+2][c+2].setPointer(true);
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}

			try
			{
				if((squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
				{
					squares[r+2][c-2].setPointer(true);
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}
		}

		//regular captures

		try
		{
			if((squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker())&& !squares[r-2][c+2].isChecker())
			{
				squares[r-2][c+2].setPointer(true);
			}
		}catch(ArrayIndexOutOfBoundsException e)
		{

		}

		try
		{
			if((squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker())&& !squares[r-2][c-2].isChecker())
			{
				squares[r-2][c-2].setPointer(true);
			}

		}catch(ArrayIndexOutOfBoundsException e)
		{

		}
	}

	/**
	 * Shows all of the white piece's moves.
	 * @param r the row the piece is in.
	 * @param c the column the piece is in.
	 */
	public void whiteMoves(int r, int c)
	{
		if(squares[r][c].isKing)//King moves (captures and regular)
		{
			//regular moves, but opposite direction
			try//checks the left diagonal
			{
				if(squares[r+1][c-1].isBlackSquare && !squares[r+1][c-1].isChecker())//empty black square
				{
					squares[r+1][c-1].setPointer(true);
				}

			}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error, means I don't have to check if it will throw an error
			{

			}
			try//checks the right diagonal
			{
				if(squares[r+1][c+1].isBlackSquare && !squares[r+1][c+1].isChecker())//empty black square
				{
					squares[r+1][c+1].setPointer(true);
				}
			}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
			{

			}

			try
			{
				//checkers diagonal to it are black, and not 2nd last row
				if((squares[r+1][c+1].isBlack() && squares[r+1][c+1].isChecker()) && !squares[r+2][c+2].isChecker())
				{
					squares[r+2][c+2].setPointer(true);
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}

			try
			{
				if((squares[r+1][c-1].isBlack() && squares[r+1][c-1].isChecker()) && !squares[r+2][c-2].isChecker())
				{
					squares[r+2][c-2].setPointer(true);
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}
		}//end of all the king's moves

		//regular moves
		try
		{
			if(squares[r-1][c-1].isBlackSquare && !squares[r-1][c-1].isChecker())//empty black square
			{
				squares[r-1][c-1].setPointer(true);
			}
		}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
		{

		}

		try
		{
			if(squares[r-1][c+1].isBlackSquare && squares[r-1][c+1].isChecker() == false)//empty black square
			{
				squares[r-1][c+1].setPointer(true);
			}
		}catch(ArrayIndexOutOfBoundsException e)//if is is on the sides or very end, it will catch this error
		{

		}

		try
		{
			if((squares[r-1][c+1].isBlack() && squares[r-1][c+1].isChecker())&& !squares[r-2][c+2].isChecker())
			{
				squares[r-2][c+2].setPointer(true);
			}
		}catch(ArrayIndexOutOfBoundsException e)
		{

		}

		try
		{
			if((squares[r-1][c-1].isBlack() && squares[r-1][c-1].isChecker())&& !squares[r-2][c-2].isChecker())
			{
				squares[r-2][c-2].setPointer(true);
			}

		}catch(ArrayIndexOutOfBoundsException e)
		{

		}
	}

	/**
	 * Determines whether a piece can be clicked and shows valid moves.
	 * If it has just captured, it sets the attribute hasCapped to true.
	 * @param r the row the checker is in.
	 * @param c the column the checker is in.
	 */
	public void validSpaces(int r, int c)
	{
		if(blackTurn == true)//if this is a black checker that was clicked
		{
			if(squares[r][c].hasCapped)
			{
				blackCapMoves(r, c);
				squares[r][c].hasCapped = false;
			}else
			{
				blackMoves(r, c);
			}
		}else//then it is a white checker
		{
			if(squares[r][c].hasCapped)
			{
				whiteCapMoves(r, c);
				squares[r][c].hasCapped = false;
			}else
			{
				whiteMoves(r, c);
			}
		}
	}
	/**
	 * Moves a checker to where the user clicked.
	 * @param r row that the checker is going to.
	 * @param c column that the checker is going to.
	 */
	public void move(int r, int c)//r and c is where the checker moves
	{	
		int oldRow =-1;
		int oldCol =-1;
		for(byte row = 0; row<8; row++)
		{	
			for(byte column= 0; column<8; column++)
			{
				if(squares[row][column].isActive == true)//deletes the old checker
				{
					oldRow = row;
					oldCol = column;
					squares[row][column].setChecker(false);
					squares[row][column].isActive = false;
					squares[r][c].setBlack(squares[row][column].isBlack());//if the previously active square was black, makes it black
					squares[r][c].isKing = squares[row][column].isKing;//check if it is a king
					squares[r][c].setChecker(true);//the new position of the checker
				}
				squares[row][column].repaint();
			}
		}

		//Don't have to check the legality of the move, as validSpace() handles that, this removes the correct piece when the curent player's checker skips
		try
		{
			if(oldRow-r == 2 && oldCol-c ==2)//checker skipped up and left
			{
				squares[oldRow-1][oldCol-1].setChecker(false);
				squares[oldRow-1][oldCol-1].isKing = false;
				playSound("capture.wav");//Play the sound for capturing
				squares[r][c].hasCapped = true;
			}
		}catch(ArrayIndexOutOfBoundsException e)
		{

		}

		try
		{
			if(oldRow-r == 2 && c-oldCol ==2)
			{
				squares[oldRow-1][oldCol+1].setChecker(false);
				squares[oldRow-1][oldCol+1].isKing = false;
				playSound("capture.wav");
				squares[r][c].hasCapped = true;
			}
		}catch(ArrayIndexOutOfBoundsException e)
		{

		}
		try
		{
			if(r-oldRow == 2 && oldCol-c == 2)//black is now capturing, down and left
			{
				squares[oldRow+1][oldCol-1].setChecker(false);
				squares[oldRow+1][oldCol-1].isKing = false;
				playSound("capture.wav");
				squares[r][c].hasCapped = true;
			}
		}catch(ArrayIndexOutOfBoundsException e)
		{

		}
		try
		{
			if(r-oldRow == 2 && c-oldCol ==2)
			{
				squares[oldRow+1][oldCol+1].setChecker(false);
				squares[oldRow+1][oldCol+1].isKing = false;
				playSound("capture.wav");
				squares[r][c].hasCapped = true;
			}
		}catch(ArrayIndexOutOfBoundsException e)
		{

		}

		if(squares[r][c].hasCapped == false)
		{
			playSound("move.wav");//Play the sound for moving if a piece wasn't captured.
		}
		//check if it is, or can be a king
		reachEnd(r, c);

		//nothing should be a pointer after a move
		for(byte row = 0; row<8; row++)
		{
			for(byte column= 0; column<8; column++)
			{
				squares[row][column].setPointer(false);
				squares[row][column].canCapture = false;
			}
		}
		canCapture(r, c);//check if the moved piece can capture
		//change player's turn
		if(squares[r][c].canCapture && squares[r][c].hasCapped == true)//doesn't change the turn if the player has capped, so they can chain moves
		{
			squares[r][c].isActive = true;
			validSpaces(r, c);
		}else
		{
			if(blackTurn == true)
			{
				blackTurn = false;
			}else
			{
				blackTurn = true;
			}
		}

		if(blackTurn == true)//current player's name is brighter
		{
			player1.setForeground(name);
			player2.setForeground(Color.WHITE);
		}else
		{
			player1.setForeground(Color.WHITE);
			player2.setForeground(name);
		}

		checkWin();
	}
	/**
	 * Determines if a piece reaches the end, then promotes them.
	 * @param r row that the checker is in.
	 * @param c column that the checker is in.
	 */
	public void reachEnd(int r, int c)
	{
		if(r == 0 || r == 7)//they behave the same if they are kings
		{
			squares[r][c].isKing = true;
		}
	}
	/**
	 * Runs methods to set up the board and the window.
	 */
	public Board()
	{
		setUpComponents();
		backBoard.add(blank[12]);
		backBoard.add(blank[0]);
		backBoard.add(blank[1]);
		backBoard.add(player2);
		backBoard.add(blank[2]);
		backBoard.add(blank[3]);
		backBoard.add(blank[4]);
		backBoard.add(blank[13]);
		backBoard.add(blank[14]);

		//Initialising all of the buttons
		boolean isBlack = false;
		int blankCounter = 16;
		for(byte r = 0; r<8; r++)//Initialises every button, changes it to the right colour, and adds it to the backboard panel
		{
			for(byte c = 0; c<9; c++)
			{
				if(c<8)//make sure that the column is below 8 for the checkers squares buttons
				{
					squares[r][c] = new Checkers();
					squares[r][c].setSize(90, 90);
					squares[r][c].setOpaque(true);
					squares[r][c].setBorder(null);
				}
				if(r%2 == 1 && c ==0)//If the row is odd, start with black
				{
					isBlack = true;
					if(isBlack == true)
					{
						squares[r][c].setBackground(blue);
						squares[r][c].isBlackSquare = true;
						isBlack = false;
					}else
					{
						squares[r][c].setBackground(whiteTile);
						isBlack = true;
					}
					backBoard.add(squares[r][c]);
					squares[r][c].setVisible(true);
				}else if(r%2 == 0 && c ==0)//if the row is even, start with white
				{
					isBlack = false;
					if(isBlack == true)
					{
						squares[r][c].setBackground(blue);
						squares[r][c].isBlackSquare = true;
						isBlack = false;
					}else
					{
						squares[r][c].setBackground(whiteTile);
						isBlack = true;
					}
					backBoard.add(squares[r][c]);
					squares[r][c].setVisible(true);
				}else if(r == 1 && c==8)//2nd row, adding in resign button
				{
					backBoard.add(resign2);
				}else if(r == 6 && c==8)//7th row, adding in resign button
				{
					backBoard.add(resign1);
				}else if(r == 3 && c==8)//4th row, adding in rules button
				{
					backBoard.add(rules);
				}else if(r == 4 && c==8)//5th row, adding in reset button
				{
					backBoard.add(reset);
				}else if(c==8)//if it is the final row, it is the side panel, so use blank JLabels as spacers
				{
					backBoard.add(blank[blankCounter]);
					blankCounter++;
				}else// if it isn't the first of a row, then it alternates.
				{
					if(isBlack == true)
					{
						squares[r][c].setBackground(blue);
						squares[r][c].isBlackSquare = true;
						isBlack = false;
					}else
					{
						squares[r][c].setBackground(whiteTile);
						isBlack = true;
					}
					backBoard.add(squares[r][c]);
					squares[r][c].setVisible(true);
				}
			}//end for loop of the columns
		}//end for loop of the rows

		//set every button's position
		for(byte r = 0; r<8; r++)
		{
			for(byte c = 0; c<8; c++)
			{
				squares[r][c].setPosition(r, c);
			}
		}
		backBoard.add(blank[5]);
		backBoard.add(blank[6]);
		backBoard.add(blank[7]);
		backBoard.add(player1);
		backBoard.add(blank[8]);
		backBoard.add(blank[9]);
		backBoard.add(blank[10]);
		backBoard.add(blank[11]);
		backBoard.add(blank[15]);
		window.setLocationRelativeTo(null);//make it pop up in the centre of the user's screen
		window.add(backBoard);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		setPieces();
	}//end inner class Board()
	/**
	 * The main method which makes a new instance of Board(), creating the game window.
	 * @param args the command-line arguments.
	 */
	public static void main(String[] args)
	{
		game = new Board();

	}
}//end Board
