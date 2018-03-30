package com.cvte.netty;


import com.cvte.cons.Constant;
import com.cvte.netty.msg.TransferMsg;
import com.cvte.util.FileUtil;

public class TransferFileHandler {

	public static boolean saveImage(TransferMsg transferMsg) {
		System.out.println("length=" + transferMsg.getAttachment().length);
		//解压
		  byte[] unGZipBytes = FileUtil.unGZip(transferMsg.getAttachment());
		  boolean success = FileUtil.bytesToFile(unGZipBytes, Constant.ImgServerPath, transferMsg.getFileName());
	      return success;
	}

}
