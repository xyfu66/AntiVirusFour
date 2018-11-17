package com.jinyun.antivirusfour.emptyFolderCleanup;

import java.io.File;

/**
 *
 */
public class DataBean {
    private File file;
    private boolean isSelected;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
