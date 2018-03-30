package com.cvte.controller;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.cvte.cons.Constant;
import com.cvte.entity.EyeInfo;
import com.cvte.test.ImageGenerateTest;
import com.cvte.util.AutoImage;
import com.cvte.util.ImageGenerate;
import com.cvte.util.ImgGenerate;
import com.cvte.util.ProcessImage;

@Controller
public class GlaucomaController {
	
	@RequestMapping("/data")
	public String imgList(Model model, HttpServletRequest request) {
		System.out.println("999999");	
		return "data";
	}

	@RequestMapping("/result")
	public String fileList(Model model, HttpServletRequest request) {
		System.out.println("999999");	
		return "test1";
	}
	
	@RequestMapping("/pdf")
	public String pdfList(Model model, HttpServletRequest request) {
		System.out.println("999999");	
		return "aa";
	}
	
	public List<EyeInfo> getInfo(){
        List<EyeInfo> list = new ArrayList<EyeInfo>();
		
		Logger logger = Logger.getLogger(GlaucomaController.class);
		logger.info("开始----");
		//test
		EyeInfo info1 = new EyeInfo();
		info1.setFlag(0);
		info1.setCdr("0.666");
		info1.setCupconf("1.00");
		info1.setE_result1("img/full_u13_p13_2018_R_CON_1.jpg");
		info1.setE_result2("img/crop_u13_p13_2018_R_CON_1.jpg");
		info1.setE_url("img/u13_p13_2018_R_CON_1.jpg");
		String[] str = "u13_p13_2018_L_CON_1.jpg".split("_");
		System.out.println("uid=" + str[0]);
		
		info1.setEid("U0001");
		info1.setFullconf("0.76");
		info1.setQulity(1);
		info1.setPercent1("4%");
		info1.setPercent2("5%");
		info1.setPercent3("3%");
		info1.setPercent4("90%");
		
		EyeInfo info2 = new EyeInfo();
		info2.setFlag(1);
		info2.setCdr("0.7777");
		info2.setCupconf("1.00");
		info2.setE_result1("img/full_u13_p13_2018_L_CON_1.jpg");
		info2.setE_result2("img/crop_u13_p13_2018_L_CON_1.jpg");
		info2.setE_url("img/u13_p13_2018_L_CON_1.jpg");
		info2.setEid("U0001");
		info2.setFullconf("0.8989");
		info2.setQulity(0);
		info2.setPercent1("5%");
		info2.setPercent2("4%");
		info2.setPercent3("3%");
		info2.setPercent4("80%");
		
		list.add(info1);
		list.add(info2);
		System.out.println("list=" + list);
		return list;
	}
	
	@RequestMapping("/img")
	@ResponseBody
	public List<EyeInfo> dataCheck(Model model, HttpServletRequest request) {
		System.out.println("in====");	
		Logger logger = Logger.getLogger(GlaucomaController.class);
		List<EyeInfo> list = ProcessImage.getListInfo();
		logger.info("start===");
		logger.info("list=" + list);
		return list;
	}
	
	@RequestMapping("/watch")
	@ResponseBody
	public List<EyeInfo> stateCheck(Model model, HttpServletRequest request) {
		System.out.println("in====");	
		Logger logger = Logger.getLogger(GlaucomaController.class);
		List<EyeInfo> list = getInfo();
		return list;
//		if(Constant.L_Queue.size() == 0) {
//			return list;
//		}else {
////			list = AutoImage.getData();
//			list = ImageGenerate.getData();
//			System.out.println("list=" + list);
//			return list;
//		}
		
		//测试
//		list = ImageGenerateTest.getData();
//		logger.info("start===");
//		System.out.println("list=" + list);
//		return list;
		
//		List<EyeInfo> list = ImageGenerate.getData();
//		logger.info("start===");
//		logger.info("list=" + list);
//		return list;
	}
	
	@RequestMapping(value = "/upload")
	@ResponseBody
	public void upload(@RequestParam("file") CommonsMultipartFile file,
			HttpServletRequest request,HttpServletResponse response, String tid) throws IOException {
		String fileName = file.getOriginalFilename();
		String ffname = file.getName();
		System.out.println(ffname);
		System.out.println(fileName);
		System.out.println("tid = " + tid);
		String name = (String) request.getSession().getAttribute("userName");
		int a = 2;
		if(name == null || "".equals(name)) {
			PrintWriter out = response.getWriter();
			out.print("false");
			out.flush();
			out.close();
		}else {
			if(a == 2) {
				PrintWriter out = response.getWriter();
				out.print(1);
				out.flush();
				out.close();
			}else {
				Calendar ca = Calendar.getInstance();
				String year = "" + ca.get(Calendar.YEAR);
				//String savePath = "D:/materialFile/" + year;
				String type = fileName.substring(fileName.lastIndexOf(".") + 1);
				String fName = System.currentTimeMillis() + "." + type;
				
				String rootPath = this.getClass().getResource("/").getPath().replaceAll("%20", "");
				rootPath = rootPath.substring(0, rootPath.indexOf("WEB-INF")) + "media/" + year;
				File ff = new File(rootPath);
				if(!ff.exists()) {
					ff.mkdir();
				}
				rootPath = rootPath + "/" + fName;
				//String path = savePath + "/" + fName;
				try {
					//方式1
//					File f = new File(rootPath);
//					BufferedInputStream in = new BufferedInputStream(file.getInputStream());
//					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f)); 
//			        byte[] bb = new byte[1024*1024*10];// 用来存储每次读取到的字节数组  
//			        int n = 0;// 每次读取到的字节数组的长度  
//			        while ((n = in.read(bb)) != -1) {
//			            out.write(bb, 0, n);// 写入到输出流 
//			            out.flush();
//			        }
//			        out.close();// 关闭流  
//			        in.close();
			        
			        
//			        FileInputStream is = (FileInputStream) file.getInputStream();
//					FileOutputStream fos = new FileOutputStream(f);
//					byte[] by = new byte[1024*1024*10];
//					int leng = 0;
//					while((leng = is.read(by))>0){
//						fos.write(by, 0, leng);
//					}
//					fos.flush();
//					fos.close();
//					is.close();
					
					//方式2
//					File f = new File(rootPath);
//					FileOutputStream fos = new FileOutputStream(f);
//					InputStream is = file.getInputStream();
//					byte[] bts = new byte[1024*1024];
//		            //一个一个字节的读取并写入
//		            while(is.read(bts)!=-1)
//		            {
//		                fos.write(bts);
//		            }
//		           fos.flush();
//		           fos.close();
//		           is.close();
					
					//方式3
					File f = new File(rootPath);
					file.transferTo(f);
				}catch(IOException e) {
					e.printStackTrace();
				}
				if("".equals(tid) || tid.charAt(0) == 'p' || tid == null) {
					tid = "";
				}
//				Material material = setMaterialParameter(fileName, fName, name, request, tid);
//				materialService.saveFile(material);
				PrintWriter out = response.getWriter();
				out.print("true");
				out.flush();
				out.close();
			}
		}
	}
	
	public void setMaterialParameter(String fileName, String fName, String name,
			HttpServletRequest request, String tid) throws IOException {
		Calendar ca = Calendar.getInstance();
		String year = "" + ca.get(Calendar.YEAR);
		//String savePath = "D:/materialFile/" + year;
		String type = fileName.substring(fileName.lastIndexOf(".") + 1);
		//String path = savePath + "/" + fName;
		String rootPa = request.getSession().getServletContext().getRealPath("");
		System.out.println("rootPa==" + rootPa);
		String rootPath = this.getClass().getResource("/").getPath().replaceAll("%20", "");
		System.out.println("root==" + rootPath);
		rootPath = rootPath.substring(0, rootPath.indexOf("WEB-INF")) + "media/" + year;
		System.out.println("rootPath=" + rootPath);
		File f = new File(rootPath);
		if(!f.exists()) {
			f.mkdir();
		}
		rootPath = rootPath + "/" + fName;
		//rootPath1 = rootPath;
		int durTime = 1;
//		BigInteger bigIntMD5 = FileUtil.getMD5(rootPath);
//		String md = bigIntMD5.toString(16);
//		//savePath = savePath + "/" + fName;
//		Material material = new Material();
//		material.setDeleted(0);
//		material.setTis("0");
//		material.setFileName(fileName);
//		material.setFilePath(rootPath);
//		String materialName = fileName.substring(0, fileName.lastIndexOf("."));
//		System.out.println(materialName);
//		material.setMaterialName(materialName);
//		material.setMd5(md);
//		material.setUploadName(name);
//		material.setStatusId("1");
//		material.setInfo("0");
//		material.setUsedNum(0);
		if(!"".equals(tid)) {
//			Terminal terminal = commonService.getTerminalById(tid.substring(1));
//			material.setTerminal(terminal);
		}
//		material.setUploadTime(new Timestamp(new Date().getTime()));
//		int typeId = judgeType(type);
//		String resolution = "1X1";
//		String size = "0kb";
//		if (typeId == 0) {
//			material.setFileType("vedio");
//			durTime = FileUtil.getDurTime(rootPath);
//			resolution = FileUtil.getResolution(rootPath);
//			size = FileUtil.getSize(rootPath);
//			material.setResolution(resolution);
//			material.setSize(size);
//			material.setDuration(durTime);
//		} else if (typeId == 1) {
//			material.setFileType("picture");
//			material.setDuration(durTime);
//			material.setResolution(resolution);
//			material.setSize(size);
//		} else {
//			material.setFileType("other");
//			material.setDuration(durTime);
//			material.setSize(size);
//			material.setResolution(resolution);
//		}
	}
	
	public int judgeType(String type){
		String str = type.toLowerCase();
		String[] vedio = new String[]{"wmv", "asf", "rm","rmvb","mov","avi","dat","mpg","mpeg","mp4","dmv","amv","3gp"
				,"mtv","mkv","mpe","m2v","vob","divx","flv","wmvhd","3g2","qt","ogg","ogv","oga","mod"};
		String[] picture = new String[]{"jpg","png","bmp","gif","psd","jpeg","ilbm","iff","tif","tiff","mng","xpm"
				,"sai","psp","ufo","xcf","pcx","ppm","webp","wdp","tga","tpic","pct","pic","pict","jp2","j2c","ima"
				,"cdr","ai"};
		for(String s:vedio){
			if(s.equals(str)){
				return 0;
			}
		}
		for(String st:picture){
			if(st.equals(str)){
				return 1;
			}
		}
		return 3;
	}
}
