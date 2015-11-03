package com.spssol.action;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spssol.service.UploadService;
import com.spssol.util.Common;
import com.spssol.util.ExcelUtil;

@Controller
@RequestMapping("/file")
public class UploadAction {

	/**
	 * 上传歌曲
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload")
	@ResponseBody
	public Map<String, Object> uploadFile(HttpServletRequest request,
			HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Iterator<String> fileNames = multipartRequest.getFileNames();
		while (fileNames.hasNext()) {
			MultipartFile multipartFile = multipartRequest.getFile(fileNames
					.next());
			// upload路径
			String realPath = request.getSession().getServletContext()
					.getRealPath("/upload");
			// 文件名
			String filename = multipartFile.getOriginalFilename();
			// 获取文件的后缀
			String suffix = filename.substring(filename.lastIndexOf("."));
			// 文件MD5
			String md5 = Common.getMD5(multipartFile.getBytes());
			// 保存的路径,文件名保存为MD5值
			String filePath = realPath + "/" + md5 + suffix;
			// 判断文件是否存在。若存在则急速秒传
			File file = Common.fileExists(filePath);
			if (file != null) {
				map.put("jsmc", "极速秒传！");
			} else {
				file = Common.saveFileAndReturn(filePath,
						multipartFile.getBytes());
			}
			if (file != null) {
				ExcelUtil eu = new ExcelUtil();  
    	        eu.setExcelPath(filePath);  
    	          
    	        System.out.println("=======测试Excel 默认 读取========");  
    	        map.put("data",eu.readExcel());
    	        System.out.println(map.toString());
			} else {
				map.put("status", false);
				map.put("msg", "文件保存失败");
			}
		}
		return map;
	}
}