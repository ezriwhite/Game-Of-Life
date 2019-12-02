package a8;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameOfLifeWidget extends JPanel implements ActionListener, SpotListener, ChangeListener {

	/* Enum to identify player. */
	private JSpotBoard board; /* SpotBoard playing area. */
	private JLabel message; /* Label for messages. */
	private int lowBirthThreshold;
	private int highBirthThreshold;
	private int lowSurviveThreshold;
	private int highSurviveThreshold;
	private JSlider gameResizer;
	private JLabel gameSize;
	private JSlider lowBirth;
	private JSlider highBirth;
	private JSlider lowSurvive;
	private JSlider highSurvive;
	private JButton torusButton;
	private JButton startButton;
	private boolean torusModeOn;
	private boolean startPressed;
	private JLabel updateDelay;
	private int waitTime;
	private JSlider updateDelayer;
	private int dimension;
	

	public GameOfLifeWidget(int dimension) {
		this.dimension = dimension;
		resetThresholds();

		torusModeOn = false;
		startPressed = false;
		waitTime = 100;

		/* Create SpotBoard and message label. */
		board = new JSpotBoard(dimension, dimension);
		message = new JLabel();
		message.setFont(new Font("Dialog", Font.BOLD, 18));

		/* Set layout and place SpotBoard at center. */
		this.setLayout(new BorderLayout());
		this.add(board, BorderLayout.CENTER);

		/* Create subpanels for message area, buttons, sliders */
		JPanel topPanel = new JPanel();
		JPanel messagePanel = new JPanel();
		JPanel gameSizePanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel advancementPanel = new JPanel();
		JPanel sliderPanel = new JPanel();
		JPanel resetSlidersPanel = new JPanel();
		
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));
		bottomPanel.add(advancementPanel);
		bottomPanel.add(buttonPanel);
		bottomPanel.add(sliderPanel);
		bottomPanel.add(resetSlidersPanel);
		buttonPanel.setLayout(new FlowLayout());
		advancementPanel.setLayout(new FlowLayout());
		sliderPanel.setLayout(new GridLayout(4, 2));
		
		/* game size slider and label */
		// label
		gameSize = new JLabel("Game Size: " + dimension);
		gameSizePanel.add(gameSize);
		// slider
		gameResizer = new JSlider(10, 70, 51);
		gameResizer.addChangeListener(this);
		gameSizePanel.add(gameResizer);
		

		/* Reset button. Add ourselves as the action listener. */
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		clearButton.setActionCommand("clear");
		buttonPanel.add(clearButton);

		/* Randomize button. Add ourselves as the action listener. */
		JButton randomizeButton = new JButton("Randomize");
		randomizeButton.addActionListener(this);
		randomizeButton.setActionCommand("randomize");
		buttonPanel.add(randomizeButton);
		
		/* torus mode button. add ourselves as action listener */
		torusButton = new JButton("Torus Mode On");
		torusButton.addActionListener(this);
		torusButton.setActionCommand("torus");
		buttonPanel.add(torusButton);
		
		/* update time length input channel */
		// label
		updateDelay = new JLabel("Update Delay: " + waitTime + " milliseconds");
		advancementPanel.add(updateDelay);
		// slider
		updateDelayer = new JSlider(10, 1000, 300);
		updateDelayer.addChangeListener(this);
		advancementPanel.add(updateDelayer);

		/* start/stop button , add ourselves as action listener */
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		startButton.setActionCommand("start");
		advancementPanel.add(startButton);
		
		/* sleep measure slider */
		
		/* Advance button. Add ourselves as the action listener. */
		JButton advanceButton = new JButton("Advance");
		advanceButton.addActionListener(this);
		advanceButton.setActionCommand("advance");
		advancementPanel.add(advanceButton);

		/* reset sliders button , add ourselves as action listener */
//		JButton resetSlidersButton = new JButton("Reset Threshold Values");
//		resetSlidersButton.addActionListener(this);
//		resetSlidersButton.setActionCommand("resetsliders");
//		resetSlidersPanel.add(resetSlidersButton);

		/* low birth threshold slider, add ourselves as change listener */
		lowBirth = new JSlider(0, 8, 3);
		lowBirth.addChangeListener(this);
		lowBirth.setMajorTickSpacing(1);
		lowBirth.setPaintTicks(true);
		lowBirth.setSnapToTicks(true);
		sliderPanel.add(lowBirth);

		/* high birth threshold slider, add ourselves as change listener */
		highBirth = new JSlider(0, 8, 3);
		highBirth.addChangeListener(this);
		highBirth.setMajorTickSpacing(1);
		highBirth.setPaintTicks(true);
		highBirth.setSnapToTicks(true);
		sliderPanel.add(highBirth);

		/* jlabels for births */
		JLabel lowBirthLabel = new JLabel("Lower Birth Threshold", SwingConstants.CENTER);
		lowBirthLabel.setVerticalAlignment(SwingConstants.TOP);
		sliderPanel.add(lowBirthLabel);

		JLabel highBirthLabel = new JLabel("Upper Birth Threshold", SwingConstants.CENTER);
		highBirthLabel.setVerticalAlignment(SwingConstants.TOP);
		sliderPanel.add(highBirthLabel);

		/* low survive threshold slider, add ourselves as change listener */
		lowSurvive = new JSlider(0, 8, 2);
		lowSurvive.addChangeListener(this);
		lowSurvive.setMajorTickSpacing(1);
		lowSurvive.setPaintTicks(true);
		lowSurvive.setSnapToTicks(true);
		sliderPanel.add(lowSurvive);

		/* high survive threshold slider, add ourselves as change listener */
		highSurvive = new JSlider(0, 8, 3);
		highSurvive.addChangeListener(this);
		highSurvive.setMajorTickSpacing(1);
		highSurvive.setPaintTicks(true);
		highSurvive.setSnapToTicks(true);
		sliderPanel.add(highSurvive);

		/* jlabels for births */
		JLabel lowSurviveLabel = new JLabel("Lower Survive Threshold", SwingConstants.CENTER);
		lowSurviveLabel.setVerticalAlignment(SwingConstants.TOP);
		sliderPanel.add(lowSurviveLabel);

		JLabel highSurviveLabel = new JLabel("Upper Survive Threshold", SwingConstants.CENTER);
		highSurviveLabel.setVerticalAlignment(SwingConstants.TOP);
		sliderPanel.add(highSurviveLabel);

		/* Add welcome message */
		message.setText("Welcome to the Game Of Life");
		messagePanel.add(message, BorderLayout.CENTER);

		/* Add subpanel in south area of layout. */
		topPanel.add(messagePanel);
		topPanel.add(gameSizePanel);
		this.add(topPanel, BorderLayout.NORTH);
		this.add(bottomPanel, BorderLayout.SOUTH);

		// add the widget as a listener for all the spots on the board
		board.addSpotListener(this);

	}

	// resets game by kills all live spots and sets welcome message
	private void resetGame() {
		for (Spot s : board) {
			s.kill();
		}
	}

	// randomly populates random number of spots
	private void randomlyPopulate() {
		resetGame();
		for (Spot s : board) {
			Random random = new Random();
			boolean populate = random.nextBoolean();
			if (populate) {
				s.populate();
			}
		}
	}

	// advances game state
	private void advanceGame() {
		for (Spot s : board) {
			// get number of live cells around based on if torus is off/on
			int popCount = 0;
			if (torusModeOn) {
				popCount = nearbyPopCount(s);
			} else {
				popCount = nearbyPopCountTorus(s);
			}
			// figure out which cells would change w/o changing them
			if (s.isDead()) {
				if (popCount >= highBirthThreshold && popCount <= lowBirthThreshold) {
					s.turnOnWC();
				}
				continue;
			}
			if (popCount < lowSurviveThreshold || popCount > highSurviveThreshold) {
				s.turnOnWC();
			}
		}

		for (Spot s : board) {
			if (s.willChange()) {
				s.toggleSpot();
			}
			s.turnOffWC();
		}

	}

	// toggle torus mode on vs off
	private void toggleTorus() {
		if (torusModeOn) {
			torusButton.setLabel("Torus Mode On");
			torusModeOn = false;
		} else {
			torusButton.setLabel("Torus Mode Off");
			torusModeOn = true;
		}
	}

	// reset to original thresholds
	private void resetThresholds() {
		lowBirthThreshold = 3;
		highBirthThreshold = 3;
		lowSurviveThreshold = 2;
		highSurviveThreshold = 3;
	}

	// counts number of alive cells around given spot with torus off
	private int nearbyPopCount(Spot s) {
		int count = 0;
		int x = s.getSpotX();
		int y = s.getSpotY();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (j == 0 && i == 0) {
					continue;
				}
				if (onBoard(x + i, y + j)) {
					if (!board.getSpotAt(x + i, y + j).isDead()) {
						count++;
					}
				}
			}
		}
		return count;
	}

	// counts number of alive cells with torus on
	private int nearbyPopCountTorus(Spot s) {
		int count = 0;
		int x = s.getSpotX();
		int y = s.getSpotY();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (j == 0 && i == 0) {
					continue;
				}
				if (onBoard(x + i, y + j)) {
					if (!board.getSpotAt(x + i, y + j).isDead()) {
						count++;
					}
				} else {
					int xHold;
					int yHold;
					if (x+i==-1) {
						xHold=dimension-1;
					} else if (x+i==dimension) {
						xHold=0;
					} else {
						xHold = x+i;
					}
					if (y+j==-1) {
						yHold=dimension-1;
					} else if (y+j==dimension) {
						yHold=0;
					} else {
						yHold = y+j;
					}
					if (!board.getSpotAt(xHold, yHold).isDead()) {
						count++;
					}
				}
			}
		}
		return count;
	}

	// checks to see if a spot is on board
	private boolean onBoard(int x, int y) {
		if (x < 0 || y < 0 || x > dimension - 1 || y > dimension - 1) {
			return false;
		}
		return true;
	}

	// button handler for reset, randomize, advance, torus, start

	// action event handler, checks which button pressed then responds
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contentEquals("clear")) {
			resetGame();
		}
		if (e.getActionCommand().contentEquals("randomize")) {
			randomlyPopulate();
		}
		if (e.getActionCommand().contentEquals("advance")) {
			advanceGame();
		}
		if (e.getActionCommand().contentEquals("resetSliders")) {
			resetThresholds();
		}
		if (e.getActionCommand().contentEquals("torus")) {
			toggleTorus();
		}
		if (e.getActionCommand().contentEquals("start")) {
			if(startPressed) {
				startButton.setLabel("Start");
				startPressed=false;
			} else {
				startButton.setLabel("Stop");
				startPressed = true;
				start();
			}
		}

	}
	
	// handles if a spot is clicked

	// toggles spots on and off when clicked
	public void spotClicked(Spot s) {
		s.toggleSpot();
	}
	
	// handles if a spot is entered

	// highlights spot when moused over
	public void spotEntered(Spot s) {
		s.highlightSpot();
	}
	

	// un-highlights spot when mouse no longer over
	public void spotExited(Spot s) {
		s.unhighlightSpot();
	}

	// handles slider adjustment
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(lowBirth)) {
			lowBirthThreshold = lowBirth.getValue();
		}
		if (e.getSource().equals(highBirth)) {
			highBirthThreshold = highBirth.getValue();
		}
		if (e.getSource().equals(lowSurvive)) {
			lowSurviveThreshold = lowSurvive.getValue();
		}
		if (e.getSource().equals(highSurvive)) {
			lowSurviveThreshold = lowSurvive.getValue();
		}
		
		if (e.getSource().equals(gameResizer)) {
			this.remove(board);
			dimension = (int) gameResizer.getValue();
			gameSize.setText("Game Size: " + dimension);
			board = new JSpotBoard(dimension, dimension);
			this.add(board, BorderLayout.CENTER);
			board.addSpotListener(this);
			
		}
		if (e.getSource().equals(updateDelayer)) {
			waitTime = (int) updateDelayer.getValue();
			updateDelay.setText("Update Delay: " + waitTime + " milliseconds");
		}

	}
	
	// handles start and stop thread
	private void start() {
		advanceGame();
		new Thread(new Runnable() {
			public void run() {
				while (startPressed) {
					try {
						Thread.sleep(waitTime);
					} catch (InterruptedException e) {
					}
					advanceGame();
				}
			}
		}).start();
	}
}