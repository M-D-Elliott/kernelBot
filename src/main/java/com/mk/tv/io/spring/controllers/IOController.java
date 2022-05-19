package com.mk.tv.io.spring.controllers;

import com.mk.tv.io.generic.IClientResponse;
import com.mk.tv.kernel.Kernel;
import com.mk.tv.kernel.generic.Config;
import jPlus.util.concurrent.AtomicBooleanPlus;
import jPlusLibs.spring.HTTPUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Controller
@CrossOrigin
//@EnableAsync
public class IOController {

    public static IOController instance;

    protected Kernel kernel;

    public IOController() {
        instance = this;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView kb() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("kb/index.html");

        final Map<String, Object> model = modelAndView.getModel();

        model.put("functionNames", kernel.functionNamesByController());
        final Config config = kernel.config();
        model.put("links", config.system.links);
        model.put("projectName", config.system.projectName);

        return modelAndView;
    }

    //    @Async
    @RequestMapping(value = "/",
            method = RequestMethod.POST,
            headers = "Accept=application/json")
    public Future<ResponseEntity<Map<String, Object>>> postKBIO(@RequestBody String commandString) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        final AtomicBooleanPlus kernelIsActive = new AtomicBooleanPlus(true);
        final RESTAPIWrapper api = new RESTAPIWrapper(commandString, userName, kernelIsActive::setF);
        final IClientResponse kernelResp = kernel.retrieve(api);

        final Map<String, Object> json = new HashMap<>();

        try {
            while (kernelIsActive.get()) {
                Thread.sleep(100);
            }

            json.put("resp", api.getOut());
            json.put("status", kernelResp.resolution().name());
            json.put("images", api.getEncodedImages());

            return new AsyncResult<>(HTTPUtils.jsonCreate(json));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }
}
