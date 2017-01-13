package cn.hnust.util;

import org.apache.commons.lang3.StringUtils;


public class FileUtil {
	/**
	 * 获得合法的目录，避免目录遍历漏洞，比如说传入/../../这种目录结构会过滤掉
	 * @param folderName
	 * @return
	 */
	public static String getValidFolderName(String folderName) {
		String token = "/..";
		if(StringUtils.contains(folderName, token)) {
			folderName = StringUtils.replace(folderName, token, "/NOT-SUPPORT");
		}
		token = "../";
		if(StringUtils.contains(folderName, token)) {
			folderName = StringUtils.replace(folderName, token, "/NOT-SUPPORT");
		}
		token = "\\..";
		if(StringUtils.contains(folderName, token)) {
			folderName = StringUtils.replace(folderName, token, "\\NOT-SUPPORT");
		}
		token = "..\\";
		if(StringUtils.contains(folderName, token)) {
			folderName = StringUtils.replace(folderName, token, "NOT-SUPPORT\\");
		}
		if(folderName.equals("..")) {
			folderName = "NOT-SUPPORT";
		}
		return folderName;
	}
}
