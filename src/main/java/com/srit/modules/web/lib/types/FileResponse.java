package com.srit.modules.web.lib.types;

import java.io.*;

public class FileResponse extends Response {

    private File file;

    public FileResponse() {

    }

    @Override
    public String getResponse() throws IOException {
        StringBuilder res = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)));

        String line;

        while ((line = bufferedReader.readLine()) != null) res.append(line);


        return res.toString();
    }

    public FileResponse setBinaryFile(File file) {
        this.file = file;

        this.setHeader("Content-Disposition", "form-data; name=\"binaryFile\"; filename=\"" + file.getName() + "\"");
        this.setHeader("Content-Type", "application/octet-stream" );
        this.setHeader("Content-Transfer-Encoding", "binary");

        return this;
    }

    public FileResponse setTextFile(File file) {
        this.file = file;
        this.setHeader("Content-Disposition", "form-data; name=\"textFile\"; filename=\"" + file.getName() + "\"");
        this.setHeader("Content-Type", "text/plain" );
        return this;
    }

    public FileResponse setHtmlFile(File file) {
        this.file = file;
        return this;
    }

    public File getFile() {
        return file;
    }

}
