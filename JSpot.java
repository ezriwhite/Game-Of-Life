package a8;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

/*
 * JSpot
 * 
 * A custom user interface component that implements a spot on a spot board
 * as an extension of JPanel.
 * 
 * A JSpot acts as its own mouse listener and will translate mouse events into
 * notifications to registered SpotListener objects when the spot is entered,
 * exited, or clicked using the appropriate methods (see SpotListener).
 *
 */

public class JSpot extends JPanel implements MouseListener, Spot {

	private Color boxColor;
	private Color highlightColor;
	private static final Color deadColor = new Color(0.8f, 0.8f, 0.8f);
	private static final Color aliveColor = Color.BLACK;
	private boolean isDead;
	private boolean willChange;
	private boolean isHighlighted;
	private SpotBoard board;
	private int x;
	private int y;
	private int boardDimension;

	private List<SpotListener> spotListeners;

	public JSpot(Color highlight, SpotBoard board, int x, int y, int boardDimension) {

		// Background color inherited from JPanel
		setBackground(deadColor);

		this.boardDimension = boardDimension;
		boxColor = deadColor;
		highlightColor = highlight;
		isDead = true;
		isHighlighted = false;
		this.board = board;
		this.x = x;
		this.y = y;

		spotListeners = new ArrayList<SpotListener>();

		addMouseListener(this);
	}

	// Getters for X, Y, and Board properties

	public int getSpotX() {
		return x;
	}

	public int getSpotY() {
		return y;
	}

	@Override
	public SpotBoard getBoard() {
		return board;
	}

	// willChange methods: used to evaluate game state completely before updating

	// gets willChange value
	public boolean willChange() {
		return willChange;
	}

	// changes willChange value to false
	public void turnOffWC() {
		willChange = false;
	}

	// changes willChange value to true
	public void turnOnWC() {
		willChange = true;
	}

	// alive status methods

	// returns value of isDead
	public boolean isDead() {
		return isDead;
	}

	// births a cell: switches isDead to false and changes box color to black
	public void populate() {
		isDead = false;
		boxColor = aliveColor;
		setBackground(boxColor);
		trigger_update();
	}

	// kills a cell: switches isDead to true and changes box color to grey
	public void kill() {
		isDead = true;
		boxColor = deadColor;
		setBackground(boxColor);
		trigger_update();
	}

	// Highlight status methods
	public boolean isHighlighted() {
		return isHighlighted;
	}

	public void highlightSpot() {
		isHighlighted = true;
		trigger_update();
	}

	public void unhighlightSpot() {
		isHighlighted = false;
		trigger_update();
	}

	// Getters / Setters for color properties Highlight and SpotColor.
	// getBackground and setBackground are inherited from JPanel.
	public void setHighlight(Color c) {
		if (c == null)
			throw new IllegalArgumentException("null color");

		highlightColor = c;
		trigger_update();
	}

	public Color getHighlight() {
		return highlightColor;
	}

	// Spot listener (de)registration methods.
	public void addSpotListener(SpotListener l) {
		spotListeners.add(l);
	}

	public void removeSpotListener(SpotListener l) {
		spotListeners.remove(l);
	}

	// Overriding paintComponent from JPanel to paint ourselves
	// the way we want.
	public void paintComponent(Graphics g) {
		// Super class paintComponent will take care of
		// painting the background.
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		if (isHighlighted()) {
			g2d.setColor(getHighlight());
			g2d.setStroke(new BasicStroke(60 / boardDimension));
			g2d.drawRect(0, 0, getWidth(), getHeight());
		}
	}

	// Mouse listener implementation below for translating
	// mouse events into spot listener events.
	public void mouseClicked(MouseEvent e) {
		for (SpotListener s : spotListeners) {
			s.spotClicked(this);
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		for (SpotListener s : spotListeners) {
			s.spotEntered(this);
		}
	}

	public void mouseExited(MouseEvent e) {
		for (SpotListener s : spotListeners) {
			s.spotExited(this);
		}
	}

	private void trigger_update() {
		repaint();

		// Not sure why, but need to schedule a call
		// to repaint for a little bit into the future
		// as well as the one we just did above
		// in order to make sure that we don't end up
		// with visual artifacts due to race conditions.

		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
				repaint();
			}
		}).start();

	}

}
