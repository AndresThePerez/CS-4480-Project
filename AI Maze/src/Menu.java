import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Menu {

	JFrame Menu = new JFrame("CS4480 Artificial Intelligence Maze Program");
	JButton Start = new JButton("Begin");
	ImageIcon picture = new ImageIcon("img/8-bit-Mario-Mushroom.jpg");
	JLabel imageLabel = new JLabel(picture);

	ArrayList<String> mapList = new ArrayList<String>();
	JComboBox<String> lvlList;

	int bWidth = 220;
	int bHeight = 30;
	int space = 430;
	int WIDTH = 500;
	int HEIGHT = 500;

	public Menu() {
		mapList.add("Level 0.map");
		mapList.add("level 1.map");
		lvlList = new JComboBox<String>(mapList.toArray(new String[mapList
				.size()]));

		Menu.setResizable(false);
		Menu.setSize(WIDTH, HEIGHT);
		Menu.setLayout(null);
		Menu.setLocationRelativeTo(null);
		Menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Menu.setVisible(true);

		Start.setSize(bWidth, bHeight);
		Start.setLocation(10, space);
		Menu.add(Start);
		Start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Maze(lvlList.getSelectedItem().toString());
				Menu.setVisible(false);
			}
		});

		lvlList.setSize(bWidth, bHeight);
		lvlList.setLocation(260, space);
		Menu.add(lvlList);

		imageLabel.setBounds((WIDTH - 400) / 2, 25, 400, 400);
		imageLabel.setVisible(true);
		Menu.add(imageLabel);
	}
}
