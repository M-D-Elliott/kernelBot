package com.mk.tv.kernel.tools;

import com.mk.tv.kernel.generic.Config;
import com.mk.tv.kernel.generic.FuncController;
import com.mk.tv.kernel.system.SystemController;
import jPlus.io.file.DirUtils;
import jPlus.io.in.IAPIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.awt.RobotUtils;
import jPlus.util.awt.image.ImageUtils;
import jPlus.util.io.TimeUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.Map;

public class ToolsController extends FuncController {

    private final String capturePath = getCapturePath();
    private final String watermarkText = getWaterMarkText();

    public ToolsController(Config config) {
        super(config);
    }

    //***************************************************************//

    @Override
    public void read(Map<String, Receivable2<IAPIWrapper, String[]>> sync,
                     Map<String, Receivable2<IAPIWrapper, String[]>> async) {
        super.read(sync, async);

        sync.put("cap", this::cap);
        Collections.addAll(menu, "cap");
    }

    private String getCapturePath() {
        final String captureFolderPath = DirUtils.fromUserDir("repos/system" + File.separatorChar + "captures");
        final File captureDir = DirUtils.make(captureFolderPath, true);
        return captureDir.getAbsolutePath() + File.separatorChar;
    }

    private String getWaterMarkText() {
        final String[] urlSplit = SystemController.INSTALL_URL.split("/");
        return urlSplit[urlSplit.length - 1];
    }

    //***************************************************************//

    @Override
    public String menuName() {
        return "tools";
    }

    @Override
    public char indicator() {
        return 't';
    }

    @Override
    protected String menuPrefix() {
        return "Tools";
    }

    @Override
    protected String menuSuffix() {
        return "";
    }

    @Override
    protected String funcDesc(String item) {
        return "";
    }

    //***************************************************************//

    protected void cap(IAPIWrapper api, String[] strings) {
        final BufferedImage img = RobotUtils.capture();
        ImageUtils.caption(img, watermarkText);
        final String captureFilePath = capturePath + TimeUtils.fileDateTime();
        final File file = ImageUtils.writeBliss(img, captureFilePath, config.tools.captureFormat);
        api.send(file);
    }
}
