import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Agent extends JPanel implements ActionListener {
	int x;
	int y;
	Timer timer = new Timer(x, this);

	// TimerTask timertask = new TimerTask();

	public Agent() {
		this.setBackground(Color.RED);
		this.setSize(Maze.panelSize, Maze.panelSize);

	}

	public void moveLeft() {
		if (x > 0 && Maze.map[x - 1][y] == 1) {
			this.setLocation(this.getX() - 25, this.getY());
			x--;
		}
	}

	public void moveRight() {
		if (x < Maze.columns - 1 && Maze.map[x + 1][y] == 1) {
			this.setLocation(this.getX() + 25, this.getY());
			timer.setDelay(1000);
			x++;
		}

	}

	public void moveDown() {
		if (x < Maze.columns - 1 && Maze.map[x][y - 1] == 1) {
			this.setLocation(this.getX(), this.getY() - 25);
			x--;
			Maze.map[x - 1][y] = 0;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
