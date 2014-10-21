package de.dimensionv.android.androtools.filetools;

import java.io.File;
import java.util.Comparator;

/**
* Created by vseifert on 14/10/14.
*/
class FileComparator implements Comparator<File> {
  @Override
  public int compare(File f1, File f2) {
    // Sort alphabetically by lower case, which is much cleaner
    return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
  }
}
