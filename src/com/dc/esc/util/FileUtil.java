package com.dc.esc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.dc.esc.ESCConfig;

/**
 * 写文件
 * */
public class FileUtil {

	/**
	 * 写流水到文件
	 * */
	public static void write(String content, String path, String fileName) {
		try {
			File p = new File(path);
			if(!p.exists()){
				p.mkdirs();
			}
			File file = new File(path + File.separator + fileName + ".tmp");
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw;
			fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			//重命名
			renameFile(path,fileName+".tmp",fileName+ESCConfig.getConfig().getProperty(ESCConfig.JOURNALLOG_FILE_SUFFIX));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** */
	/**
	 * 文件重命名
	 * 
	 * @param path
	 *            文件目录
	 * @param oldname
	 *            原来的文件名
	 * @param newname
	 *            新文件名
	 */
	public static void renameFile(String path, String oldname, String newname) {
		if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(path + "/" + oldname);
			File newfile = new File(path + "/" + newname);
			if (!oldfile.exists()) {
				return;// 重命名文件不存在
			}
			if (newfile.exists())// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				System.out.println(newname + "已经存在！");
			else {
				oldfile.renameTo(newfile);
			}
		} else {
			System.out.println("新文件名和旧文件名相同...");
		}
	}
}
