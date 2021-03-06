package com.cvte.netty;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cvte.cons.Constant;
import com.cvte.netty.msg.BaseMsg;
import com.cvte.netty.msg.DataKey;
import com.cvte.netty.msg.LoggerInfo;
import com.cvte.netty.msg.MsgType;
import com.cvte.netty.msg.PDFRecMsg;
import com.cvte.netty.msg.ResultMsg;
import com.cvte.netty.msg.TransferMsg;
import com.cvte.netty.msg.ValidateMsg;
import com.cvte.util.SaveToCSV;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;


public class NettyServerHandler extends SimpleChannelInboundHandler<BaseMsg> {
	
	//private ChannelHandlerContext ctx = null;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BaseMsg msg) throws Exception {
		Logger logger = Logger.getLogger(NettyServerHandler.class);
		switch (msg.getMsgType()) {
			case Heartbeat: // 是心跳包就忽略掉
				SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.print(form.format(new java.util.Date()));
				System.out.println("  server println Heartbeat");
				System.out.println("server println Heartbeat");
				break;
				
			case PDFRec: //收到PC端已接收到PDF文件  传输日志保存信息
				PDFRecMsg pdfMsg = (PDFRecMsg) msg;
				String[] pdfname = pdfMsg.getPdfname().split(",");
				
				Date day=new Date();    
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				

				LoggerInfo log = new LoggerInfo(pdfname[1], pdfname[0], "true", df.format(day));
				System.out.println("start transfer logger=" + log);
				logger.info("start transfer logger=" + log);
				//传输日志处理信息到PC端
				ResultMsg resultMsg1 = new ResultMsg(true, MsgType.ImgMessage);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(DataKey.ImgMessage, log);
				resultMsg1.setData(map);
				NettyChannelMap.get(DataKey.Account).writeAndFlush(resultMsg1);
				
				//日志保存到服务器
				File file = new File(Constant.csvLog);
				if(!file.exists()) {
					file.mkdirs();
				}
				
				SaveToCSV.writeCSV(log);
				
				logger.info("logger transfer is over");
				break;
	
			case Validate: // 客户端请求连接服务器
				ValidateMsg validateMsg = (ValidateMsg) msg;
				String account = validateMsg.getAccount();
				String psw = validateMsg.getPassword();
				if ("1100".equals(account)  && "123456".equals(psw)) {
					System.out.println("连接验证成功!!!!!");
					logger.info("验证成功");
					NettyChannelMap.add("1100", (SocketChannel)ctx.channel());
					//通知客户端, 验证成功
					ResultMsg resultMsg = new ResultMsg(true, MsgType.Validate);
					ctx.writeAndFlush(resultMsg);	
				} else {
					ctx.writeAndFlush(new ResultMsg(MsgType.Validate, "未经允许客户端连接失败, 请检查账号和密码是否正确"));					
					}
				break;
			
			case ImgTransfer:
				TransferMsg transferMsg = (TransferMsg)msg;
				System.out.println("我收到了图片=" + transferMsg.getAttachment());
				logger.info("我收到图片=" +  transferMsg.getFileName() +  transferMsg.getAttachment());
				TransferFileHandler.saveImage(transferMsg);
				break;
				
				
			default:
				break;
		}
	}

    @Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
        NettyChannelMap.remove((SocketChannel)ctx.channel());  
    }  
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		NettyChannelMap.remove((SocketChannel)ctx.channel());
		ctx.close();
	}

}
