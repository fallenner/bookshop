package cn.hnust.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class ResourceUtil {

    /**
     * 资源存放路径
     */
    public final static String RESOURCE_IMG_PATHNAME = "img";

    /**
     * 上传文件并返回上传后的路径
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = null;
        if (index >= 0) {
            suffix = originalFilename.substring(index + 1);
        }
        String filepath = "/" + UUIDutil.gen() + "/" + UUIDutil.gen() + "/" + UUIDutil.gen() + (StringUtils.isEmpty(suffix) ? "" : "." + suffix.toLowerCase());
        VFSUtil.saveFile(file, filepath);
        return filepath;
    }
}
