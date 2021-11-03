package com.wtkj.oa.common.config;

import org.springframework.util.ObjectUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

public class RepeatedlyReadRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public RepeatedlyReadRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        BufferedReader reader = request.getReader();
        try (StringWriter writer = new StringWriter()) {
            int read;
            char[] buf = new char[1024 * 8];
            while ((read = reader.read(buf)) != -1) {
                writer.write(buf, 0, read);
            }
            this.body = writer.getBuffer().toString().getBytes();
        }
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        };
    }

    public Boolean isBodyNotNull() {
        return !ObjectUtils.isEmpty(this.body);
    }
}
