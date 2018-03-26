package images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
/*
 * 工具类，实现一些便捷操作
 */
public class TestImages {
	// 删除当前所有模板
	public static void deleteAll() {
		File f1=new File("/home/shadz/demo");
		for (File f:f1.listFiles()) {
			if (f.isDirectory()) {
				for (File f2:f.listFiles()) {
					f2.delete();
				}
			}
		}
	}
	// 创建模板文件夹
	public static void createDir() {
		String path="/home/shadz/demo";
		for (int i=97;i<=122;i++) {
			new File(path+File.separator+(char)i).mkdirs();
		}
		for (int i=48;i<=57;i++) {
			new File(path+File.separator+(char)i).mkdirs();
		}
	}
	// 验证码下载，调试用
	public static void testDowload() throws IOException {
		URL url=new URL("http://hfutspk.hfut.edu.cn/getRandomImage.do");
		File f1=new File("/home/shadz/pictures");
		GetImages.getImages(url, f1);
	}
	// 批量处理并切割图片，调试用
	public static void testCut() throws FileNotFoundException, IOException {
		File f1=new File("/home/shadz/pictures");
		File f2=new File("/home/shadz/changed");
		File f3=new File("/home/shadz/demo");
		for (File f:f1.listFiles()) {
			CutImages.changeImage(f, f2);
		}
		for (File f:f2.listFiles()) {
			CutImages.removeSpot(f);
			CutImages.removeSpot(f);
		}
		for (File f:f2.listFiles()) {
			CutImages.cutImages(f, f3);
		}
		for (File f:f3.listFiles()) {
			CutImages.reCutImage(f);
		}
	}
	// 处理单张图片,调试用
	public static void testCut(String name) throws FileNotFoundException, IOException {
		String path ="/home/shadz/result";
		BufferedImage image=ImageIO.read(new FileInputStream(new File(path+File.separator+name)));
		for (int i=0;i<4;i++) {
			ImageIO.write(CutImages.unionCut(image)[i], "jpeg", new File(path+File.separator+name+String.valueOf(i)));
		}
	}
	// 测试识别率，调试用
	public static void testIsSuccess() throws FileNotFoundException, IOException, InterruptedException {
		URL url=new URL("http://hfutspk.hfut.edu.cn/getRandomImage.do");
		File file=new File("/home/shadz/result");
		for (int i=0;i<10;i++) {
			FindImages.FindString(url, file);
			Thread.sleep(100);
		}
	}
	// 模板图片规范命名
	public static void reName() {
		File file=new File("/home/shadz/demo");
		for (File f1:file.listFiles()) {
			if (f1.isDirectory()) {
				int count=0;
				for (File f2:f1.listFiles()) {
					String str=f2.getParent().substring(f2.getParent().length()-1, f2.getParent().length());
					f2.renameTo(new File(file.getPath()+File.separator+str+File.separator+str+"_"+count));
					count++;
				}
			}
		}
	}
	// 统计各模板数量
	public static void getNumber() {
		File file=new File("/home/shadz/demo");
		for (File f1:file.listFiles()) {
			if (f1.isDirectory()) {
				System.out.print(f1.listFiles().length+" ");
			}
		}
	}
	
	public static void main(String args[]) {
	}
}
