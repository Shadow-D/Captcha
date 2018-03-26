package images;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;

public class FindImages {
	// 根据平均值判断颜色
	public static int isWhite(int colorInt,long count) {
		Color color=new Color(colorInt);
		if ((color.getRed()+color.getBlue()+color.getGreen())>count) {
			return 1;
		} else {
			return -1;
		}
	}
	// 重载，默认值，用于预处理后的黑白图片
	public static int isWhite(int colorInt) {
		Color color=new Color(colorInt);
		if ((color.getRed()+color.getBlue()+color.getGreen())>300) {
			return 1;
		} else {
			return -1;
		}
	}
	// 将图片和模板对比，获得字符
	public static String comparImage(BufferedImage image) throws FileNotFoundException, IOException {
		String str="";
		int count=0;
		File f1=new File("/home/shadz/demo");
		int width=image.getWidth();
		int height=image.getHeight();
		for (File f2:f1.listFiles()) {// 遍历模板文件夹，与所有图片对比
			if (f2.isDirectory()) {
				for (File f3:f2.listFiles()) {
					BufferedImage bfi=ImageIO.read(new FileInputStream(f3));
					int flag=0;// 相似度
					for (int i=0;i<width;i++) {
				    	for (int j=0;j<height;j++) {
				    		// 只比较黑色像素
				    		boolean flag1=isWhite(image.getRGB(i, j))==-1||isWhite(bfi.getRGB(i, j))==-1;
				    		// 颜色是否相同
				    		boolean flag2=isWhite(bfi.getRGB(i, j))==isWhite(image.getRGB(i, j));
				    		if (flag1&&flag2) {// 相同加分，不同减分
				    			flag+=2;
				    		} else if (flag1&&!flag2) {
				    			flag+=-1;
				    		}
				    	}
				    }
					if (flag>count) {// 存储当前相似度最高的图片的信息
						count=flag;
						str=f3.getParent().substring(f3.getParent().length()-1);
					}
				}
			}
		}
		return str;
	}
	// 识别字符串，调试用
	public static void FindString(URL url,File targetDir) throws FileNotFoundException, IOException {
		BufferedImage img=GetImages.getImages(url);
		StringBuilder string=new StringBuilder();
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		Long name=new Date().getTime()%1000000;
		File file=new File(targetDir.getPath()+File.separator+String.valueOf(name));
		ImageIO.write(img, "jpeg", file);// 存入本地
		BufferedImage[] img2=CutImages.unionCut(img);
		for (int i=0;i<4;i++) {
			string.append(comparImage(img2[i]));
		}
		file.renameTo(new File(targetDir.getPath()+File.separator+string.toString()));// 以识别的字符串命名
	}
	// 识别字符串，识别用
	public static String FindString(URL url) throws FileNotFoundException, IOException {
		BufferedImage img=GetImages.getImages(url);
		StringBuilder string=new StringBuilder();
		BufferedImage[] img2=CutImages.unionCut(img);
		for (int i=0;i<4;i++) {
			string.append(comparImage(img2[i]));
		}
		return string.toString();// 返回识别结果
	}
}
