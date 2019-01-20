import java.awt.Font;
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
	ImageIcon picture = new ImageIcon("Img/10.v1.png");
	JLabel imageLabel = new JLabel(picture);

	ArrayList<String> mapList = new ArrayList<String>();
	static JComboBox<String> lvlList;
	Font font1 = new Font("Verdana", Font.BOLD , 20);

	int bWidth = 350;
	int bHeight = 50;
	int WIDTH = 750;
	int HEIGHT = 750;

	public Menu() {
		mapList.add("Level 0.map");
		mapList.add("Level 1.map");
		//mapList.add("Level 2.map");
		lvlList = new JComboBox<String>(mapList.toArray(new String[mapList
				.size()]));
		lvlList.setFont(font1);

		Menu.setResizable(false);
		Menu.setSize(WIDTH, HEIGHT);
		Menu.setLayout(null);
		Menu.setLocationRelativeTo(null);
		Menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Menu.setVisible(true);
		Menu.setFont(font1);

		Start.setSize(bWidth, bHeight);
		Start.setLocation(10, 655);
		Start.setFont(font1);
		Menu.add(Start);
		Start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Maze(lvlList.getSelectedItem().toString());
				Menu.setVisible(false);
			}
		});

		lvlList.setSize(bWidth, bHeight);
		lvlList.setLocation(375, 655);
		Menu.add(lvlList);

		imageLabel.setBounds(25, 25, 700, 600);
		imageLabel.setVisible(true);
		Menu.add(imageLabel);
	}
}
