package a8;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

/*
 * JSpotBoard is a user interface component that implements SpotBoard.
 * 
 * Spot width and spot height are specified to the constructor. 
 * 
 * By default, the spots on the spot board are set up with a checker board pattern
 * for background colors and yellow highlighting.
 * 
 * Uses SpotBoardIterator to implement Iterable<Spot>
 * 
 */

public class JSpotBoard extends JPanel implements SpotBoard {
	

	private static final int DEFAULT_SCREEN_WIDTH = 500;
	private static final int DEFAULT_SCREEN_HEIGHT = 500;
	private static final Color DEFAULT_HIGHLIGHT_COLOR = Color.YELLOW;

	private Spot[][] spots;
	
	public JSpotBoard(int width, int height) {
		if (width < 10 || height < 10 || width > 500 || height > 500) {
			throw new IllegalArgumentException("Illegal spot board geometry");
		}
		this.setLayout(new GridLayout(height, width));
		spots = new Spot[width][height];
		
		Dimension preferredSize = new Dimension(DEFAULT_SCREEN_WIDTH/width, DEFAULT_SCREEN_HEIGHT/height);
		
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				spots[x][y] = new JSpot(DEFAULT_HIGHLIGHT_COLOR, this, x, y, width);
				((JSpot)spots[x][y]).setPreferredSize(preferredSize);
				this.add(((JSpot) spots[x][y]));
			}			
		}
	}

	// Getters for SpotWidth and SpotHeight properties
	public int getSpotWidth() {
		return spots.length;
	}
	
	public int getSpotHeight() {
		return spots[0].length;
	}

	// Lookup method for Spot at position (x,y)
	public Spot getSpotAt(int x, int y) {
		if (x < 0 || x >= getSpotWidth() || y < 0 || y >= getSpotHeight()) {
			throw new IllegalArgumentException("Illegal spot coordinates");
		}
		
		return spots[x][y];
	}
	
	// Convenience methods for (de)registering spot listeners.
	public void addSpotListener(SpotListener spot_listener) {
		for (int x=0; x<getSpotWidth(); x++) {
			for (int y=0; y<getSpotHeight(); y++) {
				spots[x][y].addSpotListener(spot_listener);
			}
		}
	}
	
	public void removeSpotListener(SpotListener spot_listener) {
		for (int x=0; x<getSpotWidth(); x++) {
			for (int y=0; y<getSpotHeight(); y++) {
				spots[x][y].removeSpotListener(spot_listener);
			}
		}
	}

	@Override
	public Iterator<Spot> iterator() {
		return new SpotBoardIterator(this);
	}
}
