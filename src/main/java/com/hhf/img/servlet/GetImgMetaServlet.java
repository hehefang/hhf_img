package com.hhf.img.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MimeTypeUtils;

import com.hhf.img.core.ImgHelper;
import com.hhf.model.img.ImgResource;
import com.hhf.model.img.ImgResponse;
import com.alibaba.fastjson.JSONObject;

public class GetImgMetaServlet extends HttpServlet {
	private Logger logger = LoggerFactory.getLogger(GetImgMetaServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1291133731000403271L;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = request.getParameter("rid");
		if (id == null) {
			throw new IllegalArgumentException();
		}

		ImgResponse imgMetaResponse = new ImgResponse();
		ImgResource resource = ImgHelper.getImageInfo(id);

		if (resource != null) {
			try {
				BeanUtils.copyProperties(imgMetaResponse, resource);
				imgMetaResponse.setCode(1);

			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				logger.error(e.getMessage(), e);
			}
		}

		response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
		PrintWriter out = response.getWriter();
		out.print(JSONObject.toJSONString(imgMetaResponse));
		out.flush();
		out.close();
	}
}
