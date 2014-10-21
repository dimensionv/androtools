package de.dimensionv.android.androtools.filetools;

import java.io.File;
import java.io.FileFilter;

/**
* Created by vseifert on 14/10/14.
*/
class DirectoryFileFilter implements FileFilter {
  @Override
  public boolean accept(File file) {
    final String fileName = file.getName();
    // Return directories only and skip hidden directories
    return file.isDirectory() && ! fileName.startsWith(FileUtils.HIDDEN_PREFIX);
  }
}
