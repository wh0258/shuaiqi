package com.cnpc.pms.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 针对2007 word文件进行zip和unzip操作。 <br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Li Songtao
 * @since 2011/01/15
 */
public class ZipUtil {

	/** The Constant log. */
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	/**
	 * Creates the new file.
	 * 
	 * @param fFile
	 *            the f file
	 * @throws Exception
	 *             the exception
	 */
	private void createNewFile(File fFile) throws Exception {
		if (fFile.isDirectory()) {
			this.createNewFolder(fFile);
		} else {
			File fParent = fFile.getParentFile();
			this.createNewFolder(fParent);
			boolean b = false;
			b = fFile.createNewFile();
			LOG.debug(fFile.getAbsolutePath() + "file is already existed:" + b);
		}
	}

	/**
	 * Creates the new folder.
	 * 
	 * @param fObj
	 *            the f obj
	 * @throws Exception
	 *             the exception
	 */
	private void createNewFolder(File fObj) throws Exception {
		Stack<File> fList = new Stack<File>();
		if (fObj.exists()) {
			return;
		}
		this.searchNewFolder(fObj, fList);
		while (fList.size() > 0) {
			File fTemp = fList.pop();
			boolean b = false;
			b = fTemp.mkdir();
			LOG.debug("folder created:" + b);
		}
	}

	/**
	 * Search new folder.
	 * 
	 * @param fObj
	 *            the f obj
	 * @param allFolder
	 *            the all folder
	 * @throws Exception
	 *             the exception
	 */
	private void searchNewFolder(File fObj, Stack<File> allFolder)
			throws Exception {
		if (fObj.exists()) {
			return;
		} else {
			allFolder.push(fObj);
			File fParent = fObj.getParentFile();
			searchNewFolder(fParent, allFolder);
		}
	}

	/**
	 * Unzip.
	 * 
	 * @param zipFileName
	 *            the zip file name
	 * @param outputDirectory
	 *            the output directory
	 * @throws Exception
	 *             the exception
	 */
	public void unzip(String zipFileName, String outputDirectory)
			throws Exception {
		ZipInputStream in = null;
		FileOutputStream out = null;
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(zipFileName);
			in = new ZipInputStream(fileIn);
			ZipEntry z;
			while ((z = in.getNextEntry()) != null) {
				LOG.debug("unziping " + z.getName());
				if (z.isDirectory()) {
					String name = z.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(outputDirectory + File.separator + name);
					boolean b = false;
					b = f.mkdir();
					LOG.debug("file is created:" + b);

				} else {
					File f = new File(outputDirectory + File.separator
							+ z.getName());
					// f.createNewFile();
					this.createNewFile(f);
					out = new FileOutputStream(f);
					int b;
					while ((b = in.read()) != -1) {
						out.write(b);
					}
				}
			}

		} catch (Exception ex) {
			throw ex;
		} finally {
			in.close();
		}
	}

	/**
	 * Zip.
	 * 
	 * @param zipFileName
	 *            the zip file name
	 * @param inputFile
	 *            the input file
	 * @throws Exception
	 *             the exception
	 */
	public void zip(String zipFileName, File inputFile) throws Exception {
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFileName));
			zip(out, inputFile, "");
		} catch (Exception ex) {
			throw ex;
		} finally {
			out.close();
		}
	}

	/**
	 * Zip.
	 * 
	 * @param zipFileName
	 *            the zip file name
	 * @param inputFile
	 *            the input file
	 * @throws Exception
	 *             the exception
	 */
	public void zip(String zipFileName, String inputFile) throws Exception {
		zip(zipFileName, new File(inputFile));
	}

	/**
	 * Zip.
	 * 
	 * @param out
	 *            the out
	 * @param f
	 *            the f
	 * @param base
	 *            the base
	 * @throws Exception
	 *             the exception
	 */
	public void zip(ZipOutputStream out, File f, String base) throws Exception {
		FileInputStream in = null;
		LOG.debug("Zipping  " + f.getName());
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				File fff = fl[i];
				zip(out, fff, base + fff.getName());
			}
		} else {
			// if (base == null || base != null && base.equals("")) {
			if ((base == null) || "".equals(base)) {
				base = f.getName();
			}
		}
		out.putNextEntry(new ZipEntry(base));

		try {
			in = new FileInputStream(f);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
		} catch (FileNotFoundException exTemp) {
			throw exTemp;
		} finally {
			in.close();
		}
	}
}
