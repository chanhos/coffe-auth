package com.coffepotato.common.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.*;
import java.security.Key;


@Slf4j
public class PEMGenerator {

    private PemObject pemObject;

    private PEMGenerator(){
        //No Constructor
    }

    public static PEMGenerator build(Key key, String description){
        PEMGenerator gen = new PEMGenerator();
        gen.pemObject = new PemObject(description , key.getEncoded());

        return gen;
    }

    public void write(String fileName) throws FileNotFoundException, IOException {
        try {
            File file = new File(fileName);
            file = new File(file.getParent());
            if (!(file.exists()))
                file.mkdir();
        }catch (Exception ex) {
            log.error("파일 경로를 생성할 수 없습니다.");
        }

        try (PemWriter pemWriter = new PemWriter(
                new OutputStreamWriter(
                        new FileOutputStream(fileName)
                )
        )) {
            pemWriter.writeObject(this.pemObject);
        }

    }

}
