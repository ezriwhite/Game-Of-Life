package a8;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameOfLifeGame {
	public static void main(String[] args) {

		/* Create top level window. */
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("Game Of Life");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Create panel for content. Uses BorderLayout. */
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		mainFrame.setContentPane(topPanel);

		/*
		 * Create ExampleWidget component and put into center of content panel.
		 */

		GameOfLifeWidget gol = new GameOfLifeWidget(70);
		topPanel.add(gol, BorderLayout.CENTER);

		/* Pack main frame and make visible. */
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
}
