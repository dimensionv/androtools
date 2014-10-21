package de.dimensionv.android.androtools.filetools;

import java.io.File;
import java.io.FileFilter;

/**
* Created by vseifert on 14/10/14.
*/
class FileFileFilter implements FileFilter {
  @Override
  public boolean accept(File file) {
    final String fileName = file.getName();
    // Return files only (not directories) and skip hidden files
    return file.isFile() && ! fileName.startsWith(FileUtils.HIDDEN_PREFIX);
  }
}
