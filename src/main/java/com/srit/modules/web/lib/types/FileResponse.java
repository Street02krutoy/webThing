package com.srit.modules.web.lib.types;

import java.io.*;

public class FileResponse extends Response {

    private File file;

    public FileResponse() {}

    @Override
    public String getResponse() throws IOException {
        StringBuilder res = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)));

        String line;

        while ((line = bufferedReader.readLine()) != null) res.append(line);


        return res.toString();
    }

    public FileResponse setFile(File file) {
        this.file = file;
        return this;
    }

    public File getFile() {
        return file;
    }

}
