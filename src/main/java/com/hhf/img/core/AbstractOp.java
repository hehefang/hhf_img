package com.hhf.img.core;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhf.common.exception.TechException;
import com.hhf.common.util.RegUtils;
import com.google.common.collect.Maps;

public abstract class AbstractOp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3890932606459578632L;

	public String opStr; // 操作字符串
	public char code; // 操作码
	public int type; // 操作类型

	public String getOpStr() {
		return opStr;
	}

	public void setOpStr(String opStr) {
		this.opStr = opStr;
	}

	public static interface IOpCode {
		public static final char RESIZE = 's';
		public static final char CUTOFF = 'c';
		public static final char PADDING ='p';
	}

	public static final class OpHelper {
		private static final Logger logger = LoggerFactory
				.getLogger(OpHelper.class);

		/**
		 * 解析图片处理参数
		 * 
		 * @param ops
		 * @return
		 * @throws TechException
		 */
		public static AbstractOp[] parseOps(String ops) {
			if (ops == null)
				return null;

			String[] opArray = ops.split("\\-");
			AbstractOp[] ret = new AbstractOp[opArray.length];

			for (int i = 0; i < opArray.length; i++) {
				ret[i] = parseOp(opArray[i]);
			}

			return ret;
		}

		/**
		 * 解析单个指令
		 * 
		 * @param op
		 * @return
		 * @throws TechException
		 */
		private static AbstractOp parseOp(String op) {
			if (!RegUtils.validateString("^[scp]\\d(_[a-z]\\d+)*$", op)) {
				throw new TechException("incorrect op");
			}

			AbstractOp ret = null;
			String[] arr = op.split("_");
            if(arr[0].charAt(0) == 'p'){
            	ret = new PaddingResizeOp();
            }
			if (arr[0].charAt(0) == 's') {
				ret = new ResizeOp();
			} 
            if(arr[0].charAt(0) == 'c') {
				ret = new CutOffOp();
			}

			ret.type = Integer.parseInt(arr[0].substring(1));

			if (arr.length > 1) {
				Map<String, Integer> map = Maps.newHashMap();

				for (int i = 1; i < arr.length; i++) {
					map.put(arr[i].substring(0, 1),
							Integer.parseInt(arr[i].substring(1)));
				}

				try {
					BeanUtils.copyProperties(ret, map);
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				}
			}

			ret.setOpStr(op);

			return ret;
		}
	}
    public static final class PaddingResizeOp extends AbstractOp {
		private static final long serialVersionUID = 6561193453946625785L;
		public PaddingResizeOp() {
			this.code = IOpCode.PADDING;
		}
		public Integer e;
		public Integer w;
		public Integer h;

		public Integer getE() {
			return e;
		}

		public void setE(Integer e) {
			this.e = e;
		}

		public Integer getW() {
			return w;
		}

		public void setW(Integer w) {
			this.w = w;
		}

		public Integer getH() {
			return h;
		}

		public void setH(Integer h) {
			this.h = h;
		}
    }
    
	public static final class ResizeOp extends AbstractOp {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2662083752017955256L;

		public ResizeOp() {
			this.code = IOpCode.RESIZE;
		}

		public Integer e;
		public Integer w;
		public Integer h;

		public Integer getE() {
			return e;
		}

		public void setE(Integer e) {
			this.e = e;
		}

		public Integer getW() {
			return w;
		}

		public void setW(Integer w) {
			this.w = w;
		}

		public Integer getH() {
			return h;
		}

		public void setH(Integer h) {
			this.h = h;
		}

	}

	public static final class CutOffOp extends AbstractOp {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8816812131801649246L;

		public CutOffOp() {
			this.code = IOpCode.CUTOFF;
		}

		public int x = 0;
		public int y = 0;
		public Integer p;
		public int a = 0;
		public Integer w;
		public Integer h;

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public Integer getP() {
			return p;
		}

		public void setP(Integer p) {
			this.p = p;
		}

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}

		public Integer getW() {
			return w;
		}

		public void setW(Integer w) {
			this.w = w;
		}

		public Integer getH() {
			return h;
		}

		public void setH(Integer h) {
			this.h = h;
		}

	}

}
