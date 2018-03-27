package images;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CutImages {
	/*
	 * 该类负责对图片的处理与切割
	 */
	// 将图片转成黑白,调试用
	public static void changeImage(File image,File targetDir) throws FileNotFoundException, IOException {
		BufferedImage img=ImageIO.read(new FileInputStream(image));
		int width=img.getWidth();
		int height=img.getHeight();
		String path=targetDir.getAbsolutePath()+File.separator+image.getName();
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		File target=new File(path);
		long count=0;
		// 求颜色值总和
		for (int i=0;i<width;i++) {
			for (int j=0;j<height;j++) {
				int rgb=img.getRGB(i, j);
				Color color=new Color(rgb);
				count+=color.getBlue()+color.getGreen()+color.getRed();
			}
		}
		count=(long)(1.1*(count/(width*height))); // 图片颜色平均值
		for (int i=0;i<width;i++) {
			// 各像素点浅的设为白色，深的设为黑色
			for (int j=0;j<height;j++) {
				if ((FindImages.isWhite(img.getRGB(i, j),count)==1)) {
					img.setRGB(i, j, Color.white.getRGB());
				} else {
					img.setRGB(i, j, Color.black.getRGB());
				}
			}
		}
		ImageIO.write(img, "jpeg", target);// 存入本地
	}
	
	// 重载，识别用
	public static void changeImage(BufferedImage img) throws FileNotFoundException, IOException {
		int width=img.getWidth();
		int height=img.getHeight();
		long count=0;
		for (int i=0;i<width;i++) {
			for (int j=0;j<height;j++) {
				int rgb=img.getRGB(i, j);
				Color color=new Color(rgb);
				count+=color.getBlue()+color.getGreen()+color.getRed();
			}
		}
		count=(long)(1.1*(count/(width*height)));
		for (int i=0;i<width;i++) {
			for (int j=0;j<height;j++) {
				if ((FindImages.isWhite(img.getRGB(i, j),count)==1)) {
					img.setRGB(i, j, Color.white.getRGB());
				} else {
					img.setRGB(i, j, Color.black.getRGB());
				}
			}
		}
	}
	
	// 切割图片，调试用
	public static void cutImages(File image,File targetDir) throws FileNotFoundException, IOException {
		BufferedImage bfi=ImageIO.read(new FileInputStream(image));
		BufferedImage[] img=new BufferedImage[4];
		// 粗略切割，尽量保存各字符，可根据图片实际修改裁剪参数
		img[0]=bfi.getSubimage(0, 4, 20, 15);
		img[1]=bfi.getSubimage(15, 2, 20, 15);
		img[2]=bfi.getSubimage(30, 5, 20, 15);
		img[3]=bfi.getSubimage(45, 3, 20, 15);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		String path=targetDir.getAbsolutePath();
		// 将切割后的图片存到目标文件夹
		for (int i=0;i<img.length;i++) {
			File f1=new File(path+File.separator+image.getName()+"_"+String.valueOf(i));
			f1.createNewFile();
			ImageIO.write(img[i], "jpeg", f1);
		}
	}
	// 重载，识别用
	public static BufferedImage[] cutImages(BufferedImage bfi) throws FileNotFoundException, IOException {
		BufferedImage[] img=new BufferedImage[4];
		img[0]=bfi.getSubimage(0, 4, 20, 15);
		img[1]=bfi.getSubimage(15, 2, 20, 15);
		img[2]=bfi.getSubimage(30, 5, 20, 15);
		img[3]=bfi.getSubimage(45, 3, 20, 15);
	    return img;
	}
	// 二次裁剪，调试用
	public static void reCutImage(File image) throws FileNotFoundException ,IOException {
		BufferedImage img=ImageIO.read(new FileInputStream(image));
		int width=img.getWidth();
		int height =img.getHeight();
		int[] index=new int[width];
		int indexRight=1;
		for (int i=0;i<width;i++) {// 统计每列黑色像素数，大于两个则认为此列有字符的一部分
			int count=0;
			for (int j=0;j<height;j++) {
				if (FindImages.isWhite(img.getRGB(i, j))==-1) {
					count++;
			    }
		    }
			if (count>=2) {
				index[i]=1;
			} else {
				index[i]=0;
			}
		}
		for (int i=1;i<10;i++) {// 图片前一半
			if (index[i]==1&&index[i+1]==1&&index[i-1]==0) {// 寻找左边界
				indexRight=i;
				break;
			}
		}
		img=img.getSubimage(indexRight-1, 0, 12, 15);// 左边多切一个像素
		ImageIO.write(img, "jpeg", image);
	}
	// 重载，识别用
	public static BufferedImage reCutImage(BufferedImage img) throws FileNotFoundException ,IOException {
		int width=img.getWidth();
		int height =img.getHeight();
		int[] index=new int[width];
		int indexRight=2;
		for (int i=0;i<width;i++) {
			int count=0;
			for (int j=0;j<height;j++) {
				if (FindImages.isWhite(img.getRGB(i, j))==-1) {
					count++;
			    }
		    }
			if (count>=2) {
				index[i]=1;
			} else {
				index[i]=0;
			}
		}
		for (int i=1;i<10;i++) {
			if (index[i]==1&&index[i+1]==1&&index[i-1]==0) {
				indexRight=i;
				break;
			}
		}
		return img.getSubimage(indexRight-1, 0, 12, 15);
	}
	// 去除噪点，调试用
	public static void removeSpot(File image) throws FileNotFoundException, IOException {
		BufferedImage img=ImageIO.read(new FileInputStream(image));
		for (int i=1;i<img.getHeight()-1;i++) {// 左边界
			if (img.getRGB(0, i)!=-1) {
				// 如果为黑色，判断改点周围点的个数，小于两个则设为白色
				int count=FindImages.isWhite(img.getRGB(0, i-1))+FindImages.isWhite(img.getRGB(0, i+1))+FindImages.isWhite(img.getRGB(1, i));
				if(count>1) {
					img.setRGB(0, i, Color.white.getRGB());
				}
			}
		}
		for (int i=1;i<img.getHeight()-1;i++) {// 除边界外的像素
			for (int j=1;j<img.getWidth()-1;j++) {
				if (img.getRGB(j, i)!=-1) {
					int count=FindImages.isWhite(img.getRGB(j+1, i))+FindImages.isWhite(img.getRGB(j-1, i))+FindImages.isWhite(img.getRGB(j, i+1))+FindImages.isWhite(img.getRGB(j, i-1));
				    if (count>=2) {
				    	img.setRGB(j, i, Color.white.getRGB());
				    }
				}
			}
		}
		ImageIO.write(img, "jpeg", image);// 存入本地
	}
	// 重载，识别用
	public static void removeSpot(BufferedImage img) throws FileNotFoundException, IOException {
		for (int i=1;i<img.getHeight()-1;i++) {
			if (img.getRGB(0, i)!=-1) {
				int count=FindImages.isWhite(img.getRGB(0, i-1))+FindImages.isWhite(img.getRGB(0, i+1))+FindImages.isWhite(img.getRGB(1, i));
				if(count>1) {
					img.setRGB(0, i, Color.white.getRGB());
				}
			}
		}
		for (int i=1;i<img.getHeight()-1;i++) {
			for (int j=1;j<img.getWidth()-1;j++) {
				if (img.getRGB(j, i)!=-1) {
					int count=FindImages.isWhite(img.getRGB(j+1, i))+FindImages.isWhite(img.getRGB(j-1, i))+FindImages.isWhite(img.getRGB(j, i+1))+FindImages.isWhite(img.getRGB(j, i-1));
				    if (count>=2) {
				    	img.setRGB(j, i, Color.white.getRGB());
				    }
				}
			}
		}
	}
	// 综合处理
	public static BufferedImage[] unionCut(BufferedImage image) throws FileNotFoundException, IOException {
		BufferedImage[] img;
		changeImage(image);
		removeSpot(image);
		removeSpot(image);
		img=cutImages(image);
		for (int i=0;i<4;i++) {
			img[i]=reCutImage(img[i]);
		}
		return img;
	}
}
