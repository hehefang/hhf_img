/**
 * Copyright (c)2013-2014 by www.hhf.com. All rights reserved.
 * 
 */

package com.hhf.img.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.MultipartStream.MalformedStreamException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MimeTypeUtils;

import com.hhf.img.config.ConfigFactory;
import com.hhf.img.core.ImgHelper;
import com.hhf.model.img.ImgResource;
import com.hhf.model.img.ImgResponse;
import com.alibaba.fastjson.JSONObject;

/**
 * 上传图片
 * 
 * @author xuzunyuan
 * @date 2015年1月14日
 */
public class UploadImgServlet extends HttpServlet {

	private Logger logger = LoggerFactory.getLogger(UploadImgServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -1300892392668416970L;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ImgResponse uploadResponse = doUpload(request);

		response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
		PrintWriter out = response.getWriter();
		out.print(JSONObject.toJSONString(uploadResponse));
		out.flush();
		out.close();
	}

	/**
	 * 上传处理
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private ImgResponse doUpload(HttpServletRequest request) throws IOException {
		ImgResponse uploadResponse = new ImgResponse();

		// 表单格式不正确
		if (!ServletFileUpload.isMultipartContent(request)) {
			uploadResponse.setCode(0);
			uploadResponse.setMsg("error form");
			return uploadResponse;
		}

		ServletFileUpload upload = new ServletFileUpload();
		upload.setFileSizeMax(ConfigFactory.getConfig().getFileSizeMax());
		upload.setSizeMax(ConfigFactory.getConfig().getFileSizeMax());

		try {
			FileItemIterator it = upload.getItemIterator(request);
			boolean found = false;
			FileItemStream item = null;

			// 如果有多个文件，则只处理第一个文件
			try {
				while (it.hasNext()) {
					item = it.next();

					if (!item.isFormField()) {
						found = true;
						break;
					}
				}
			} catch (MalformedStreamException e) {
				// Ignore this to fixed flash bug
			}

			// 没有上传文件
			if (!found) {
				uploadResponse.setCode(-1);
				uploadResponse.setMsg("no file");
				return uploadResponse;

			}

			// 保存文件
			String fileName = item.getName();
			if (fileName != null) {
				fileName = FilenameUtils.getName(fileName);
			}

			String fileExt = "jpeg";
			int index = fileName.lastIndexOf(".");

			if (index != -1)
				fileExt = fileName.substring(index + 1);

			InputStream inputStream = item.openStream();

			ImgResource resource = ImgHelper.saveImg(fileName, fileExt,
					inputStream);

			// 保存失败
			if (resource == null) {
				uploadResponse.setCode(-3);
				uploadResponse.setMsg("file save failure");
				return uploadResponse;
			}

			inputStream.close();

			try {
				BeanUtils.copyProperties(uploadResponse, resource);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				logger.error(e.getMessage(), e);
			}
			uploadResponse.setCode(1);
			uploadResponse.setMsg("upload success");
			return uploadResponse;

		} catch (FileUploadException e) {
			// 发生异常，可能由于文件大小超过限制等原因引起
			logger.error(e.getMessage(), e);

			uploadResponse.setCode(-100);
			uploadResponse.setMsg("exception occurs");
			return uploadResponse;
		}
	}

}
