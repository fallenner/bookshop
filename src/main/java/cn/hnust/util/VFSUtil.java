package cn.hnust.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import cn.hnust.exception.FileOperationException;

public class VFSUtil {
	private static final Log log = LogFactory.getLog(VFSUtil.class);
	// VFS路径，以/结尾
	private static String VFS_ROOT_PATH = "";

	private static String BREAK_STR = "$$$$";

	static {
		readVfsPath();
	}

	/** 读取cloud vfs路径 **/
	private static void readVfsPath() {
		boolean isWindows = (System.getProperty("os.name").toUpperCase()
				.indexOf("WINDOWS") > -1) ? true : false;
		try {
			Properties p = new Properties();
			InputStream inStream = new ClassPathResource("vfs.properties")
					.getInputStream();
			p.load(inStream);
			String key = isWindows ? "VFS_ROOT_PATH_WINDOWS"
					: "VFS_ROOT_PATH_LINUX";
			VFS_ROOT_PATH = p.getProperty(key);
		} catch (Exception e1) {
			VFS_ROOT_PATH = "";
			log.error(
					"[vfsroot路径读取]配置文件模式出错！使用默认位置,win:   D:/vfsroot ,linux : /opt/vfsroot",
					e1);
		}
		if (StringUtils.isBlank(VFS_ROOT_PATH)) {
			if (isWindows)
				VFS_ROOT_PATH = "D:/vfsroot";
			else
				VFS_ROOT_PATH = "/opt/vfsroot";
		}
		VFS_ROOT_PATH += "/bookshop";
		try {
			new File(VFS_ROOT_PATH).mkdirs();
		} catch (Exception e) {
			log.error("创建VFS失败[" + VFS_ROOT_PATH + "]", e);
			throw new FileOperationException("创建VFS失败[" + VFS_ROOT_PATH + "]");
		}
		log.error("readVfsPath 结束，位置：" + VFS_ROOT_PATH);

	}

	/**
	 * @param filePath
	 *            基于VFS根路径的文件路径
	 * @return
	 */
	public static String getVFSPath(String filePath) {
		return normalize(VFS_ROOT_PATH + "/" + filePath);
	}

	/**
	 * 根据文件路径查找某个文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static File getFile(String filePath) {
		if (filePath == null) {
			return null;
		}
		return new File(getVFSPath(filePath));
	}

	/**
	 * 根据文件路径查找某个文件，如果文件不存在的话则创建这个文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static File getFileIsNotExistCreate(String filePath) {
		File file = new File(getVFSPath(filePath));
		if (!file.exists()) {
			file = recursiveMakeDirectory(filePath, VFS_ROOT_PATH);
		}
		return file;
	}

	public static boolean isFileExist(String filePath) {
		File file = new File(getVFSPath(filePath));
		return file.exists();
	}

	/**
	 * 列出某个文件夹下的子文件，不包括目录
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<File> listChildFiles(String filePath) {
		File parentFile = new File(getVFSPath(filePath));
		List<File> childFiles = new ArrayList<File>();
		if (parentFile.exists() && parentFile.canRead()) {
			File[] files = parentFile.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					childFiles.add(file);
				}
			}
		}
		return childFiles;
	}

	/**
	 * 删除VFS下的某个文件
	 * 
	 * @param filePath
	 */
	public static void removeFile(String filePath) {
		if (StringUtils.isNotEmpty(filePath)) {
			File file = new File(getVFSPath(filePath));
			if (file.exists()) {
				try {
					FileUtils.forceDelete(file);
				} catch (IOException ex) {
					throw new FileOperationException("删除文件失败,请检查文件是否正在被使用", ex);
				}
			}
		}
	}

	public static void saveFile(MultipartFile thefile, String path) {
		try {
			thefile.transferTo(VFSUtil.getFileIsNotExistCreate(path));
		} catch (Exception e) {
			throw new FileOperationException("保存文件出错", e);
		}
	}

	public static void saveFile(InputStream is, String path) {
		OutputStream out = null;
		try {
			String filedirpath = path.substring(0, path.lastIndexOf("/"));
			String filename = path.substring(path.lastIndexOf("/") + 1);
			File destFile = VFSUtil.getFileIsNotExistCreate(filedirpath);

			File file = new File(destFile.getAbsolutePath() + "/" + filename);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			throw new FileOperationException("保存文件出错", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new FileOperationException("保存文件出错", e);
			}
			try {
				out.close();
			} catch (IOException e) {
				throw new FileOperationException("保存文件出错", e);
			}
		}
	}

	/**
	 * 拷贝文件到某个目录（如果不存在则新建）下
	 * 
	 * @param fromFilePath
	 * @param destDir
	 * @param isIncludeSelf
	 *            如果源文件是目录的话是否拷贝自己
	 */
	public static void copyVFSFile(String fromFilePath, String destDir,
			boolean isIncludeSelf) {
		fromFilePath = getVFSPath(fromFilePath);
		destDir = getVFSPath(destDir);
		File fromFile = new File(fromFilePath);
		if (!fromFile.exists()) {
			return;
		}
		File destDirectory = new File(destDir);
		if (destDirectory.exists() && !destDirectory.isDirectory()) {
			throw new FileOperationException("拷贝文件失败,目标文件不是目录");
		}
		if (fromFile.isFile()) {
			try {
				FileUtils.copyFileToDirectory(fromFile, destDirectory);
			} catch (IOException ex) {
				throw new FileOperationException("拷贝文件【" + fromFilePath
						+ "】到目录【" + destDir + "】下失败，失败原因：" + ex.getMessage(),
						ex);
			}
		}
		if (fromFile.isDirectory()) {
			if (fromFile.list().length == 0) { // 空文件夹
				return;
			}
			try {
				if (isIncludeSelf) {
					FileUtils.copyDirectoryToDirectory(fromFile, destDirectory);
				} else {
					FileUtils.copyDirectory(fromFile, destDirectory);
				}
			} catch (IOException ex) {
				throw new FileOperationException("拷贝目录【" + fromFilePath
						+ "】到目录【" + destDir + "】下失败，失败原因：" + ex.getMessage(),
						ex);
			}
		}
	}

	/**
	 * 允许创建多级目录，目录名之间用/隔开，暂不支持创建失败回滚的功能
	 * 
	 * @param fileName
	 *            要创建的目录名
	 * @return 已创建的最底层的目录
	 */
	private static File recursiveMakeDirectory(String fileName, String root) {
		root = normalize(root);
		StringTokenizer directories = new StringTokenizer(normalize(fileName),
				"/");
		File parent = new File(root);
		while (directories.hasMoreTokens()) {
			String name = directories.nextToken();
			parent = new File(parent, name);

			if (log.isInfoEnabled()) {
				log.info("will create the directory ["
						+ parent.getAbsolutePath() + "]");
			}
			if (!parent.exists()) {
				if (log.isInfoEnabled()) {
					log.info("creating the directory ["
							+ parent.getAbsolutePath() + "]");
				}

				if (!parent.mkdir()) {
					String msg = "fail to created the directory ["
							+ parent.getAbsolutePath() + "]";
					if (log.isInfoEnabled()) {
						log.info(msg);
					}
					throw new FileOperationException(msg);
				}
			}
		}
		return parent;
	}

	private static String normalize(String original) {
		if (log.isDebugEnabled()) {
			log.debug("normalize前[" + original + "]");
		}

		original = original.replace('\\', '/');
		original = eliminateRedundantSlassh(original);

		/**
		 * in Linux(Unix like) system, must add prefix "/", unckecked, and in
		 * WindowNT, if there is no ":", shoulb add one.
		 */
		if (original.indexOf(':') == -1) {
			if (!original.startsWith("/")) {
				original = "/" + original;
			}
		} else {
			if (original.startsWith("/")) {
				original = original.substring(1);
			}
		}

		if (original.endsWith("/")) {
			original = original.substring(0, original.length() - 1);
		}

		if (log.isDebugEnabled()) {
			log.debug("normalize后[" + original + "]");
		}

		return FileUtil.getValidFolderName(original);
	}

	/**
	 * 从路径名称中排除多余的 "/", 如果路径中有"\", 结果我没有测试过; 通常调用这个方法之前 一定要把 "\" 转成 "/"
	 * 
	 * @return 返回排除了多余的"/"的路径
	 */
	private static String eliminateRedundantSlassh(String path) {
		if (log.isDebugEnabled()) {
			log.debug("要排除多余的'/'之前[" + path + "]");
		}

		boolean isSlash = false;
		StringBuffer result = new StringBuffer(path.length());

		for (int i = 0; i < path.length(); i++) {
			char c = path.charAt(i);

			if (c != '/' || !isSlash) {
				result.append(c);
			}

			isSlash = (c == '/');
		}

		if (log.isDebugEnabled()) {
			log.debug("要排除多余的'/'之前[" + path + "]");
		}

		return result.toString();
	}

	public static void downloadFile(HttpServletResponse response,
			HttpServletRequest request, String filePath, String downloadFileName)
			throws Exception {
		File file = new File(getVFSPath(filePath));
		if (!file.exists()) {
			log.error("找不到文件：" + getVFSPath(filePath));
			return;
		}

		InputStream reader = null;
		OutputStream out = null;
		try {
			reader = getInputStream(file, true);
			byte[] buf = new byte[1024];
			int len = 0;
			response.addHeader("Content-Length", "" + file.length());
			response.setContentType("application/octet-stream");// 均不提供直接打开
			response.setHeader("Content-Disposition", "attachment;filename="
					+ HttpUtils.convert(downloadFileName));
			out = response.getOutputStream();
			while ((len = reader.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.flush();
		} catch (IOException e) {
			log.error("读取文件[" + filePath + "]报错", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception ex) {
					log.error("关闭文件流出错", ex);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception ex) {
					log.error("关闭输出流出错", ex);
				}
			}
		}
	}

	public static InputStream getInputStream(File file, boolean fileStream) {
		/**
		 * @author guiyf Apr,04,05 18:33
		 *         当前情况下不需要区分，不存在还是不可访问，而且试读一次文件流没有必要(可能占用大量内存)。
		 */
		if (fileStream == true) {// 使用文件流
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(file);

			} catch (FileNotFoundException e) {
				if (log.isDebugEnabled()) {
					log.debug(e);
				}
				String msg = "找不到指定的文件[" + file.getName() + "]。";
				if (log.isDebugEnabled()) {
					log.debug(msg);
				}
				throw new FileOperationException(msg, e);
			}
			return fin;
		} else { // 使用内存流
			InputStream in = null;
			if (file != null && file.canRead() && file.isFile()) {
				try {
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					FileInputStream stream = new FileInputStream(file);
					BufferedInputStream bin = new BufferedInputStream(stream);
					int len = 0;
					byte[] b = new byte[1024];
					while ((len = bin.read(b)) != -1) {
						buffer.write(b, 0, len);
					}

					stream.close();
					in = new ByteArrayInputStream(buffer.toByteArray());
				} catch (Exception e) {
					String msg = "不能读取文件[" + file.getName() + "]";
					if (log.isErrorEnabled()) {
						log.error(msg, e);
					}
					throw new FileOperationException(msg, e);
				}
			} else {
				String msg = "不是文件或文件不可读[" + file.getName() + "]";
				if (log.isDebugEnabled()) {
					log.debug(msg);
				}
				throw new FileOperationException("不是文件或文件不可读");
			}
			return in;
		}
	}

	/**
	 * 
	 * @param response
	 * @param url
	 *            http://commons.apache.org/vfs/apidocs/org/apache/commons/vfs/
	 *            FileSystemManager.html
	 * @throws Exception
	 */
	public static void showImage(HttpServletRequest request,
			HttpServletResponse response, String url) throws Exception {
		showImage(request, response, url, true);
	}

	public static void showImage(HttpServletRequest request,
			HttpServletResponse response, String url,
			boolean displayDefaultImage) throws Exception {
		FileSystemManager mgr = VFS.getManager();
		FileObject file = mgr.resolveFile(url);
		response.setContentType("image/jpg");
		File tempFile = new File(file.getURL().toURI());
		if (displayDefaultImage && (!tempFile.isFile() || !tempFile.exists())) {
			log.error("找不到图片，" + url + "已使用默认图片代替！");
			String defaultImagePath = "";
			if (url == null) {
			} 
			else
				defaultImagePath = "/resources/images/no_pic.jpg";
			tempFile = new File(request.getSession().getServletContext()
					.getRealPath("/")
					+ defaultImagePath);
			file = mgr.toFileObject(tempFile);
		}
		try {
			OutputStream out = response.getOutputStream();
			org.apache.commons.vfs2.FileUtil.writeContent(file, out);
		} catch (Exception ex) {
			log.error("显示照片时发生错误:" + ex.getMessage(), ex);
		}
	}

	/**
	 * 
	 * @param response
	 * @param url
	 *            相对于vfs根的路径
	 * @throws Exception
	 */
	public static void showVFSImage(HttpServletRequest request,
			HttpServletResponse response, String url) throws Exception {
		if (StringUtils.isEmpty(url)) {
			return;
		}
		if (url.contains(BREAK_STR)) {
			url = url.substring(0, url.indexOf(BREAK_STR));
		}
		showImage(request, response, VFS_ROOT_PATH + url);
	}

	public static boolean isPicture(String pInput, String pImgeFlag)
			throws Exception {
		// 文件名称为空的场合
		if (StringUtils.isEmpty(pInput)) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1,
				pInput.length());
		// 声明图片后缀名数组
		String imgeArray[][] = { { "bmp", "0" }, { "dib", "1" },
				{ "gif", "2" }, { "jfif", "3" }, { "jpe", "4" },
				{ "jpeg", "5" }, { "jpg", "6" }, { "png", "7" },
				{ "tif", "8" }, { "tiff", "9" }, { "ico", "10" } };
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (!StringUtils.isEmpty(pImgeFlag)
					&& imgeArray[i][0].equals(tmpName.toLowerCase())
					&& imgeArray[i][1].equals(pImgeFlag)) {
				return true;
			}
			// 判断符合全部类型的场合
			if (StringUtils.isEmpty(pImgeFlag)
					&& imgeArray[i][0].equals(tmpName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}  
	public static void downLoadFilesAsZip(HttpServletRequest request,HttpServletResponse response,String[] filePaths)throws Exception{
		File zipFile=new File(getVFSPath("files.zip"));
		if (!zipFile.exists()){   
			zipFile.createNewFile();   
        }
		ZipOutputStream zipout=new ZipOutputStream(new FileOutputStream(zipFile));
		for(String path:filePaths){
			File file=new File(getVFSPath(path));
			BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
			ZipEntry zipeEntry=new ZipEntry(file.getName());
			zipout.putNextEntry(zipeEntry);
			int length=0;
			byte[] buffer=new byte[1024];
			while((length=bis.read(buffer)) !=-1){
				zipout.write(buffer, 0, length);
			}
			bis.close();
		}
		zipout.close();
		response.addHeader("Content-Length", "" + zipFile.length());
		response.setContentType("application/octet-stream");// 均不提供直接打开
		response.setHeader("Content-Disposition", "attachment;filename="
				+ HttpUtils.convert(zipFile.getName()));
		InputStream zipInputStream=null;
		OutputStream responseOut=null;
		BufferedOutputStream bufferedOutputStream=null;
		BufferedInputStream bufferedInputStream=null;
		try{
			 zipInputStream=getInputStream(new File(getVFSPath("files.zip")), true);
			 bufferedInputStream=new BufferedInputStream(zipInputStream);
			 responseOut=response.getOutputStream();
			 bufferedOutputStream=new BufferedOutputStream(responseOut);
			int len=0;
			byte[] bytes=new byte[1024];
			while((len=bufferedInputStream.read(bytes))!=-1){
				bufferedOutputStream.write(bytes, 0, len);
			}
			bufferedOutputStream.flush();
		} catch (IOException e) {
			log.error("读取文件[" + zipFile.getPath() + "]报错", e);
		} finally {
			if (bufferedInputStream != null) {
				try {
					bufferedInputStream.close();
				} catch (Exception ex) {
					log.error("关闭文件流出错", ex);
				}
			}
			if (zipInputStream != null) {
				try {
					zipInputStream.close();
				} catch (Exception ex) {
					log.error("关闭文件流出错", ex);
				}
			}
			if (bufferedOutputStream != null) {
				try {
					bufferedOutputStream.close();
				} catch (Exception ex) {
					log.error("关闭文件流出错", ex);
				}
			}
			if (responseOut != null) {
				try {
					responseOut.close();
				} catch (Exception ex) {
					log.error("关闭文件流出错", ex);
				}
			}
		}
	}

}
