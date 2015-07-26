package com.hhf.img.config;

import java.io.Serializable;

public class Config implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2879986543545967267L;

	private long fileSizeMax;
	private String fileSavePath;
	private String globalSearchPath;

	public long getFileSizeMax() {
		return fileSizeMax;
	}

	public void setFileSizeMax(long fileSizeMax) {
		this.fileSizeMax = fileSizeMax;
	}

	public String getFileSavePath() {
		return fileSavePath;
	}

	public void setFileSavePath(String fileSavePath) {
		this.fileSavePath = fileSavePath;
	}

	public String getGlobalSearchPath() {
		return globalSearchPath;
	}

	public void setGlobalSearchPath(String globalSearchPath) {
		this.globalSearchPath = globalSearchPath;
	}

}
