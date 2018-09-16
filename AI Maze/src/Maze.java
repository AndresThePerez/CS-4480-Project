import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JFrame;

public class Maze extends JFrame {
	public static int rows = 20;
	public static int columns = 20;
	public static int panelSize = 25;
	public static int map[][] = new int[columns][rows];
	public static int endLevelLoc;
	Agent a;

	public static void main(String args[]) {
		new Menu();
	}

	public Maze(String str) {
		loadMap(str);
		this.setResizable(false);
		this.setSize((columns * panelSize) + 50, (rows * panelSize) + 70);
		this.setTitle("maze");
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		a = new Agent();
		a.setVisible(true);
		this.add(a);

		for (int y = 0; y < columns; y++) {
			for (int x = 0; x < rows; x++) {
				Tile tile = new Tile(x, y);
				tile.setSize(panelSize, panelSize);
				tile.setLocation((x * panelSize) + 23, (y * panelSize) + 25);
				if (map[x][y] == 0) {
					tile.setBackground(Color.BLACK);

				} else {
					tile.setBackground(Color.WHITE);
					tile.setWall(false);
					if (x == 0) {
						a.setLocation((x * panelSize) + 23,
								(y * panelSize) + 25);
						a.y = y;
					}
					if (x == columns - 1) {
						endLevelLoc = y;
					}
				}
				tile.setVisible(true);
				this.add(tile);
			}
		}
		this.setVisible(true);
		run();
	}

	public void run() {

		a.moveRight();
		a.moveRight();
		a.moveRight();
		a.moveRight();
		a.moveRight();
		a.moveRight();
		revalidate();
		repaint();

	}

	public void loadMap(String str) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(str));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String mapStr = sb.toString();

			int counter = 0;
			for (int y = 0; y < columns; y++) {
				for (int x = 0; x < rows; x++) {
					String mapChar = mapStr.substring(counter, counter + 1);
					if (!mapChar.equals("\r\n") && !mapChar.equals("\n")
							&& !mapChar.equals("\r")) {// If it's a number
						// System.out.print(mapChar);
						map[x][y] = Integer.parseInt(mapChar);
					} else {// If it is a line break
						x--;
						System.out.print(mapChar);
					}
					counter++;
				}
			}
		} catch (Exception e) {
			System.out
					.println("Unable to load existing map(if exists), creating new map.");
		}
	}
}
