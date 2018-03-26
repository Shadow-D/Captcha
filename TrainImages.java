package images;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TrainImages extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	URL url=new URL("http://hfutspk.hfut.edu.cn/getRandomImage.do");
	static String path="/home/shadz/demo";
	static BufferedImage img;
	static BufferedImage[] images;
	static ImageIcon image;
	static String string;
	
	JLabel imgLab;
	JTextField strField;
	JButton okButton;
	
	public TrainImages() throws IOException {
		this.setTitle("train");
		this.setBounds(600, 200, 180, 140);
		this.setLayout(null);
		this.setResizable(false);
		
		imgLab=new JLabel(new ImageIcon(img=GetImages.getImages(url)));// 首张图片
		imgLab.setBounds(55, 10, 70, 30);
		this.add(imgLab);
		
		strField=new JTextField();
		strField.setBounds(10, 60, 70, 30);
		strField.setFont(new Font("黑体",1, 15));
		this.add(strField);
		
		okButton=new JButton("ok");
		okButton.setBounds(100, 60, 70, 30);
		okButton.addActionListener(this);// 监听按钮
		this.add(okButton);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("ok")) {
			try {
				string=strField.getText();
				images=CutImages.unionCut(img);// 处理当前图片
				// 将切割后的图片存到对应的模板文件夹并命名
				for (int i=0;i<4;i++) {
					String str=string.substring(i, i+1);
					ImageIO.write(images[i], "jpeg", new File(path+File.separator+str+File.separator+str+"_"+String.valueOf(new File(path+File.separator+str).listFiles().length)));
				}
				// 下一张验证码图片
				img=GetImages.getImages(url);
				image=new ImageIcon(img);
				imgLab.setIcon(image);
				strField.setText("");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
