/**
 * Copyright (c)2013-2014 by www.hhf.com. All rights reserved.
 * 
 */
package com.hhf.img.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hhf.img.core.ImgHelper;

/**
 * 下载图片
 * 
 * @author xuzunyuan
 * @date 2015年1月14日
 */
public class GetImgServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7444971157577211916L;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = request.getParameter("rid");
		if (id == null) {
			throw new IllegalArgumentException();
		}

		String op = request.getParameter("op");

		InputStream in = ImgHelper.getImg(id, op);
		if (in == null) {
			throw new FileNotFoundException();
		}
		long adddays=100;
		request.setAttribute("myExpire", adddays);
		long adddaysM = adddays * 1000;
		long header = request.getDateHeader("If-Modified-Since");
		long now = System.currentTimeMillis();
		if (header > 0 && adddaysM > 0) {
			
			if (header + adddaysM > now) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			}
		}

		response.setContentType(ImgHelper.getContentTypeFromResourceId(id));
		ServletOutputStream out = response.getOutputStream();
		request.setAttribute("myExpire", 1000);
		String maxAgeDirective = "max-age=" + 1000;
		response.setHeader("Cache-Control", maxAgeDirective);
		response.setStatus(HttpServletResponse.SC_OK);
		response.addDateHeader("Last-Modified", System.currentTimeMillis());
		response.addDateHeader("Expires", System.currentTimeMillis() + adddaysM);
		int b;
		byte[] buffer = new byte[4096];

		while ((b = in.read(buffer)) != -1) {
			out.write(buffer, 0, b);
			out.flush();
		}

		in.close();
		out.close();
	}

}
