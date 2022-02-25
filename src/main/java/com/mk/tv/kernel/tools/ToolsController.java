package com.mk.tv.kernel.tools;

import com.mk.tv.kernel.Config;
import com.mk.tv.kernel.generic.FuncController;
import jPlus.io.file.DirUtils;
import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.awt.RobotUtils;
import jPlus.util.io.TimeUtils;

import java.io.File;
import java.util.Collections;
import java.util.Map;

public class ToolsController extends FuncController {

    private String capturePath = null;

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
        final File img = RobotUtils.capture(capturePath() + TimeUtils.fileDateTime(), config.tools.captureFormat);
        api.send(img);
    }

    private String capturePath() {
        if (capturePath == null)
            capturePath = DirUtils.make(DirUtils.fromUserDir("repos" + File.separator + "captures"), true).getAbsolutePath() + File.separator;
        return capturePath;
    }
}
