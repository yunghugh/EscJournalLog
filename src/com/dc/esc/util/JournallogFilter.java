package com.dc.esc.util;

import java.io.File;
import java.io.FilenameFilter;

public class JournallogFilter implements FilenameFilter {
	private String type;

	public JournallogFilter(String tp) {
		this.type = tp;
	}

	@Override
	public boolean accept(File dir, String path) {
		File file = new File(path);
		String filename = file.getName();
		return filename.indexOf(type) != -1;
	}

}
