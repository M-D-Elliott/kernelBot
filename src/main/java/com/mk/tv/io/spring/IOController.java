package com.mk.tv.io.spring;

import com.mk.tv.io.generic.IAPIWrapper;
import com.mk.tv.io.generic.IClientResponse;
import jPlus.lang.callback.Retrievable1;
import jPlusLibs.spring.HTTPUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@CrossOrigin
public class IOController {

    public static IOController instance;

    protected Retrievable1<IClientResponse, IAPIWrapper> recipient;
    protected Map<String, List<String>> functionNames;

    public IOController() {
        instance = this;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView kb() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("kb/index.html");

        final Map<String, Object> model = modelAndView.getModel();

        model.put("functionNames",  functionNames);

        return modelAndView;
    }

    @RequestMapping(value = "/",
            method = RequestMethod.POST,
            headers = "Accept=application/json")
    public ResponseEntity<Map<String, Object>> postKBIO(@RequestBody String commandString) {

        final AtomicBoolean gate = new AtomicBoolean(true);

        final RESTAPIWrapper api = new RESTAPIWrapper(commandString, () -> {
            gate.set(false);
        });
        final IClientResponse resp = recipient.retrieve(api);

        final Map<String, Object> ret = new HashMap<>();

        ret.put("resp", "test");
        ret.put("status", "test");

        try {
            while (gate.get()) {
                Thread.sleep(100);
            }

//        System.out.println("out: "+ api.getOut());
            ret.put("resp", api.getOut());
            ret.put("status", resp.resolution().name());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return HTTPUtils.jsonCreate(ret);
    }

    public void setRecipient(Retrievable1<IClientResponse, IAPIWrapper> recipient) {
        this.recipient = recipient;
    }

    public void setFunctionNames(Map<String, List<String>> functionNames) {
        this.functionNames = functionNames;
    }
}
