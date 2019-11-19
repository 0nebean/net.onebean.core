package net.onebean.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

/**
 * 图像处理，封装了诸如缩略图生成、水印、格式转换等API
 * 
 * @author yc
 *
 */
public class ImageUtil {

	private final static int LIMIT_SIZE = 720;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
	private static final Logger logger = Logger.getLogger(ImageUtil.class);

	// 最大允许下载2M以内的图像
	public final static int DOWNLOAD_IMG_MAX_LENGHT = 1024 * 1024 * 2;

	public enum IMAGE_FORMAT {
		BMP("bmp"), JPG("jpg"), WBMP("wbmp"), JPEG("jpg"), PNG("png"), GIF(
				"gif"), WEBP("webp");

		private String value;

		IMAGE_FORMAT(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * 获取以当前时间时分秒加四位随机数的图片名称
	 * 
	 * @return String
	 */
	public static String imageName() {
		Calendar cl = Calendar.getInstance();
		int max = 999999;
		int min = 100000;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return sdf.format(cl.getTime()) + s;
	}



	/**
	 * 为图片添加水印图片
	 * @param sourceFile 源文件
	 * @param toFile 目标文件
	 * @param logoImg logo图片
	 * @throws IOException
	 */
	public static void addLogo(String sourceFile, String toFile, String logoImg) throws IOException {
		// watermark(位置，水印图，透明度)
		Thumbnails
				.of(sourceFile)
				.watermark(Positions.BOTTOM_RIGHT,
						ImageIO.read(new File(logoImg)), 0.5f)
				.outputQuality(0.8f).toFile(toFile);
	}



	/**
	 * 给图片添加水印文字、可设置水印文字的旋转角度
	 * @param logoText 要写入的文字
	 * @param srcImgPath 源图片路径
	 * @param newImagePath 新图片路径
	 * @param degree 旋转角度
	 * @param color  字体颜色
	 * @param formatName 图片后缀
	 */
	public static void markImageByText(String logoText, String srcImgPath,String newImagePath,Integer degree,Color color,String formatName) {
		InputStream is = null;
		OutputStream os = null;
		try {
			// 1、源图片
			Image srcImg = ImageIO.read(new File(srcImgPath));
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
			// 2、得到画笔对象
			Graphics2D g = buffImg.createGraphics();
			// 3、设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
			// 4、设置水印旋转
			if (null != degree) {
				g.rotate(Math.toRadians(degree),  buffImg.getWidth()/2,buffImg.getHeight() /2);
			}
			// 5、设置水印文字颜色
			g.setColor(color);
			// 6、设置水印文字Font
			g.setFont(new Font("宋体", Font.BOLD, buffImg.getHeight() /2));
			// 7、设置水印文字透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.15f));
			// 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
			g.drawString(logoText,  buffImg.getWidth()/2 , buffImg.getHeight()/2);
			// 9、释放资源
			g.dispose();
			// 10、生成图片
			os = new FileOutputStream(newImagePath);
			ImageIO.write(buffImg, formatName, os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is)
					is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 旋转照片
	 *
	 * @param sourceFile
	 * @param toFile
	 * @param angle
	 */
	public static void rotate(String sourceFile, String toFile, double angle) {
		try {
			Thumbnails.of(sourceFile).scale(1).rotate(angle).toFile(toFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 使用ImageIO的方式下载图片
	 * @param urlStr 远程地址
	 * @param filePath 本地路径
	 * @param formatName 目标文件名
	 */
	public static void downLoadImageUseImageIO(String urlStr, String filePath,String formatName) {
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e1) {
			logger.error(e1);
		}
		if (formatName == null) {
			HttpURLConnection urlconnection;
			try {
				urlconnection = (HttpURLConnection) url.openConnection();
				urlconnection.connect();
				BufferedInputStream bis = new BufferedInputStream(
						urlconnection.getInputStream());
				String fileType = HttpURLConnection
						.guessContentTypeFromStream(bis);
				bis.close();
				urlconnection.disconnect();
				String imgtype = "jpg";
				if (fileType != null) {
					imgtype = imgtype.substring(imgtype.indexOf("/") + 1)
							.toLowerCase();
					imgtype = checkImageType(imgtype);
					formatName = imgtype;
				}
			} catch (IOException e) {
				logger.error(e);
			}
			try {
				BufferedImage img = ImageIO.read(url);
				ImageIO.write(img, formatName, new File(filePath));
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	/**
	 * 从给定的地址下载图片到本地
	 * @param urlPath 远程地址
	 * @param localPath 本地路径
	 * @param fileName 文件名
	 * @return fulPath 完整图片本地路径
	 */
	public static String downLoadImage(String urlPath, String localPath,String fileName) throws  Exception{
		URL  url = new URL(urlPath);
		logger.debug("开始从给定URL下载图片" + url.toString());
		String type = "jpg";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		if(url.toString().indexOf(" ") != -1){
			return fileName;
		}
		HttpGet httpget = new HttpGet(url.toString());
		httpget.setHeader(HTTP.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
		httpget.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
		httpget.setConfig(requestConfig);

		try {
			HttpResponse resp = httpClient.execute(httpget);
			if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				Header cType = resp.getFirstHeader("content-type");
				String imgtype = "jpg";
				if (cType != null) {
					imgtype = cType.getValue();
				} else {
					imgtype = URLConnection.guessContentTypeFromStream(in);
				}

				if (imgtype != null) {
					imgtype = imgtype.substring(imgtype.indexOf("/") + 1)
							.toLowerCase();
					imgtype = checkImageType(imgtype);
					fileName = fileName + "." + imgtype;
				} else {
					fileName = fileName + "." + type;
				}

				savePicToDisk(in, localPath, fileName);

				if ("webp".equals(imgtype)) {
					// webp形式的图像需要另外处理
					logger.debug("图像为webp格式，需要转换为jpg进行显示");
					imgtype = "jpg";
					ImageUtil.webpToJpg(
							localPath + File.separator + fileName,
							localPath + File.separator
									+ fileName.replace("webp", imgtype));
					fileName = fileName.replace("webp", imgtype);
				}
			}
		} catch (Exception e) {
			logger.error("图片下载失败", e);
		} finally {

		}
		return fileName;
	}

	private static String checkImageType(String imgType) {
		if ("png".equalsIgnoreCase(imgType)) {
			return "png";
		} else if ("jpeg".equalsIgnoreCase(imgType)) {
			return "jpg";
		} else if ("gif".equalsIgnoreCase(imgType)) {
			return "gif";
		} else if ("bmp".equalsIgnoreCase(imgType)) {
			return "bmp";
		} else if ("webp".equalsIgnoreCase(imgType)) {
			return "webp";
		} else {
			return "jpg";
		}
	}

	/**
	 * 将图片写到 硬盘指定目录下
	 * @param in InputStream
	 * @param dirPath 硬盘目录
	 * @param filePath 目标文件路径
	 */
	public static void savePicToDisk(InputStream in, String dirPath,String filePath) {
		try {
			File dir = new File(dirPath);
			if (dir == null || !dir.exists()) {
				dir.mkdirs();
			}

			// 文件真实路径
			String realPath = dirPath.concat(filePath);
			File file = new File(realPath);
			if (file == null || !file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = in.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.flush();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Webp格式图片转换为jpg图片格式
	 * @param webpPath 原文件路径
	 * @param jpgPath 目标文件路径
	 */
	public static void webpToJpg(String webpPath, String jpgPath) {
//		if (!include) {
//			IIORegistry r = IIORegistry.getDefaultInstance();
//			WebPImageReaderSpi s = new WebPImageReaderSpi();
//			r.registerServiceProvider(s);
//			include = true;
//		}
		BufferedImage bi;
		try {
			bi = ImageIO.read(new File(webpPath));
			ImageIO.write(bi, "jpg", new File(jpgPath));
		}catch (IIOException e) {
			logger.error("webp转换为jpg时发生异常",e);
		} catch (IOException e) {
			logger.error("webp转换jpg时发生异常,webpPath:" + webpPath);
		} catch (Exception e) {
			 logger.error("webp转换为jpg时发生异常",e);
		}
	}


	/**
	 * 获取图片格式
	 * @param file 图片文件
	 * @return 图片格式
	 */
	public static String getImageFormatName(File file) {
		String formatName = null;
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> imageReader = ImageIO.getImageReaders(iis);
			if (imageReader.hasNext()) {
				ImageReader reader = imageReader.next();
				formatName = reader.getFormatName();
			}
		} catch (IOException e) {
			logger.error("获取图片类型发生异常", e);
		}
		return formatName;
	}

	/**
	 * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片,以及图片是否完整，这是一种非常简单的方式。
	 * @param imageFilePath 文件本地路径
	 * @return bool
	 */
	public static boolean checkImage(String imageFilePath) {
		File imageFile = new File(imageFilePath);
		if (!imageFile.exists()) {
			return false;
		}
		Image img = null;
		try {
			img = ImageIO.read(imageFile);
			if (img == null || img.getWidth(null) <= 0
					|| img.getHeight(null) <= 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			img = null;
		}
	}


	/**
	 * 获取图片宽度
	 * @param file  图片文件
	 * @return 宽度
	 */
	public static int getImgWidth(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int ret = -1;
		try {
			is = new FileInputStream(file);
			src = ImageIO.read(is);
			// 得到源图宽
			ret = src.getWidth(null);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}


	/**
	 * 获取图片高度
	 * @param file  图片文件
	 * @return 高度
	 */
	public static int getImgHeight(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int ret = -1;
		try {
			is = new FileInputStream(file);
			src = ImageIO.read(is);
			// 得到源图高
			ret = src.getHeight(null);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String zipImageFileWithCustomHeight(File oldFile,File newFile,int height,float quality){
		return zipWidthHeightImageFile(oldFile,newFile,0,height,quality);
	}

	public static String zipImageFileWithCustomWidth(File oldFile,File newFile,int width,float quality){
		return zipWidthHeightImageFile(oldFile,newFile,width,0,quality);
	}
	public static String zipImageFileWithCustomHeightAndWidth(File oldFile,File newFile,int width,int height,float quality){
		return zipImageFile(oldFile,newFile,width,height,quality);
	}

	public static String zipImageFileWithKeepOriginalSize(File oldFile,File newFile,float quality){
		return zipImageFile(oldFile,newFile,getImgWidth(oldFile),getImgHeight(oldFile),quality);
	}

	/**
	 * 根据设置的宽高等比例压缩图片文件 先保存原文件，再压缩、上传
	 * @param oldFile  要进行压缩的文件
	 * @param newFile  新文件
	 * @param width  宽度 //设置宽度时（高度传入0，等比例缩放）
	 * @param height 高度 //设置高度时（宽度传入0，等比例缩放）
	 * @return 返回压缩后的文件的全路径
	 */
	private static String zipWidthHeightImageFile(File oldFile,File newFile,int width,int height,float quality) {
		if (oldFile == null) {
			return null;
		}
		try {
			/** 对服务器上的临时文件进行处理 */
			Image srcFile = ImageIO.read(oldFile);
			int w = srcFile.getWidth(null);
			int h = srcFile.getHeight(null);
			double bili;
			if(width>0){
				width = (width> LIMIT_SIZE)? LIMIT_SIZE :width;
				bili=width/(double)w;
				height = (int) (h*bili);
			}else{
				height = (height> LIMIT_SIZE)? LIMIT_SIZE :height;
				if(height>0){
					bili=height/(double)h;
					width = (int) (w*bili);
				}
			}

			String srcImgPath = newFile.getAbsoluteFile().toString();
			String suffix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1);

			BufferedImage buffImg;
			if(suffix.equals("png")){
				buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			}else{
				buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			}

			Graphics2D graphics = buffImg.createGraphics();
			graphics.setBackground(new Color(255,255,255));
			graphics.setColor(new Color(255,255,255));
			graphics.fillRect(0, 0, width, height);
			graphics.drawImage(srcFile.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

			/*压缩之后临时存放位置 */
			FileOutputStream out = new FileOutputStream(newFile);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(buffImg);
			/*压缩质量*/
			jep.setQuality(quality, true);
			encoder.encode(buffImg, jep);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newFile.getAbsolutePath();
	}

	/**
	 * 按设置的宽度高度压缩图片文件 先保存原文件，再压缩、上传
	 * @param oldFile  要进行压缩的文件全路径
	 * @param newFile  新文件
	 * @param width  宽度
	 * @param height 高度
	 * @return 返回压缩后的文件的全路径
	 */
	private static String zipImageFile(File oldFile,File newFile, int width, int height,float quality) {
		if (oldFile == null) {
			return null;
		}
		String newImage = null;
		try {
			/** 对服务器上的临时文件进行处理 */
			Image srcFile = ImageIO.read(oldFile);
			String srcImgPath = newFile.getAbsoluteFile().toString();
			String subfix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1);
			BufferedImage buffImg;
			if(subfix.equals("png")){
				buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			}else{
				buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			}

			Graphics2D graphics = buffImg.createGraphics();
			graphics.setBackground(new Color(255,255,255));
			graphics.setColor(new Color(255,255,255));
			graphics.fillRect(0, 0, width, height);
			graphics.drawImage(srcFile.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);


			/*压缩之后临时存放位置 */
			FileOutputStream out = new FileOutputStream(newFile);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(buffImg);
			/*压缩质量*/
			jep.setQuality(quality, true);
			encoder.encode(buffImg, jep);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newFile.getAbsolutePath();
	}

}
