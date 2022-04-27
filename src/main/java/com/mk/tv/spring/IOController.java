package com.mk.tv.spring;

import jPlus.io.in.DummyAPIWrapper;
import jPlus.io.in.IAPIWrapper;
import jPlus.io.in.PrintStreamWrapper;
import jPlus.io.out.IClientResponse;
import jPlus.lang.callback.Retrievable1;
import jPlusLibs.spring.HTTPUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/kb")
@CrossOrigin
public class IOController {

    public static IOController instance;

    protected Retrievable1<IClientResponse, IAPIWrapper> recipient;

    public IOController() {
        instance = this;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView kb() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("kb.html");

        return modelAndView;
    }

    @RequestMapping(value = "/",
            method = RequestMethod.POST,
            headers = "Accept=application/json")
    public ResponseEntity<Map<String, String>> postKBIO(@RequestBody String commandString) {

        final PrintStreamWrapper dummy = new PrintStreamWrapper(System.out);
        dummy.setIn(commandString);
        final IClientResponse resp = recipient.retrieve(dummy);


        final Map<String, String> ret = new HashMap<>();
        ret.put("resp", resp.resolution().name());

        return HTTPUtils.jsonCreate(ret);
    }

    public void setRecipient(Retrievable1<IClientResponse, IAPIWrapper> recipient) {
        this.recipient = recipient;
    }
}
