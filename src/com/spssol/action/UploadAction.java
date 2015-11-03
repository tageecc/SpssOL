package com.spssol.action;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spssol.util.Common;
import com.spssol.util.ExcelInfo;

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
			HttpServletResponse response, HttpSession session) throws Exception {
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
				session.setAttribute("path", filePath);
				response.sendRedirect("/SpssOL/data-select.html");
			} else {
				map.put("status", false);
				map.put("msg", "文件保存失败");
			}
		}
		return map;
	}

	/**
	 * 
	 * @Description: 读取文件
	 * @author big
	 * @date 2015年11月3日 下午2:21:54
	 */
	@RequestMapping(value = "/read/sheet")
	@ResponseBody
	public Map<String, Object> readFile(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String path = (String) session.getAttribute("path");

		if (path != null && path.length() > 0) {
			map.put("status", true);
			map.put("sheet", ExcelInfo.getSheet(path));
		} else {
			map.put("status", true);
			map.put("msg", "请先上传文件！");
		}
		return map;
	}

	/**
	 * 
	 * @Description: 读取第N个sheet的值
	 * @author big
	 * @date 2015年11月3日 下午2:25:26
	 * @version V1.0
	 */
	@RequestMapping(value = "/read/sheet/{n}")
	@ResponseBody
	public Map<String, Object> readSheet(@PathVariable int n,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String path = (String) session.getAttribute("path");

		if (path != null && path.length() > 0) {
			map.put("status", true);
			map.put("data", ExcelInfo.getData(path, n));
		} else {
			map.put("status", true);
			map.put("msg", "请先上传文件！");
		}
		return map;
	}
}