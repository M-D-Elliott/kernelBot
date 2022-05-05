package com.mk.tv.io.spring.controllers;

import com.mk.tv.io.generic.IAPIWrapper;
import jPlus.io.Access;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class RESTAPIWrapper implements IAPIWrapper {

    private final String in;
    private final List<String> out = new ArrayList<>();
    private final Runnable onFinish;
    private final String userName;

    private final List<String> encodedImages = new ArrayList<>();

    public RESTAPIWrapper(String in) {
        this(in, () -> {
        });
    }

    public RESTAPIWrapper(String in, Runnable onFinish) {
        this(in, null, onFinish);
    }

    public RESTAPIWrapper(String in, String userName, Runnable onFinish) {
        this.in = in;
        this.userName = userName == null ? "admin" : userName;
        this.onFinish = onFinish;
    }

    @Override
    public Access access() {
        return Access.PRIVATE;
    }

    @Override
    public String username() {
        return this.userName;
    }

    @Override
    public String in() {
        return this.in;
    }

    @Override
    public void print(String s) {
        final String[] split = s.split(System.lineSeparator());
        Collections.addAll(out, split);
    }

    @Override
    public void onFinish() {
        if (onFinish != null) this.onFinish.run();
    }

    @Override
    public void send(String endpoint, String message) {
        println(message);
    }

    @Override
    public void send(File file) {
        try {
            final String fileType = Files.probeContentType(file.toPath());
            final String[] fileTypesSplit = fileType.split("/");
            if (fileTypesSplit[0].equals("image")) {
                final byte[] fileBytes = Files.readAllBytes(file.toPath());
                final String encodedImage = Base64.getEncoder().withoutPadding().encodeToString(fileBytes);
                encodedImages.add(encodedImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void println(String s) {
        print(s);
    }

    @Override
    public void printLink(String url) {
        println("<a href=\"" + url + "\" target=\"_blank\">" + url + "</a>");
    }

    public String[] getOut() {
        return out.toArray(new String[0]);
    }

    public List<String> getEncodedImages() {
        return encodedImages;
    }
}
