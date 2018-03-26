package images;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.imageio.ImageIO;

public class GetImages {
	// 下载验证码，调试用
	public static void getImages(URL sourceUrl,File targetDir) throws IOException {
		URLConnection con=sourceUrl.openConnection();
		con.setConnectTimeout(5000);
		InputStream in=con.getInputStream();
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		long now=new Date().getTime()%1000000;// 以时间毫秒的后六位命名
		String path=targetDir.getAbsolutePath()+File.separator+String.valueOf(now);
		File file=new File(path);
		file.createNewFile();
		BufferedOutputStream bo=new BufferedOutputStream(new FileOutputStream(file));
		byte[] bs=new byte[1024];
		int len;
		while ((len=in.read(bs))!=-1) {
			bo.write(bs, 0, len);
		}
		in.close();
		bo.close();
	}
	// 获取验证码图片，识别用
	public static BufferedImage getImages(URL sourceUrl) throws IOException {
		URLConnection con=sourceUrl.openConnection();
		con.setConnectTimeout(5000);
		InputStream in=con.getInputStream();
		BufferedImage img=ImageIO.read(in);
		return img;
	}
}
