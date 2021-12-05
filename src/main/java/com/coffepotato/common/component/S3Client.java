package com.coffepotato.common.component;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @author Chan's
 * <p>
 * Description : s3 사용을 위한 클라이언트 모듈 <br>
 * </p>
 * <br>
 * Updated : 2021/11/17
 */
@Slf4j
public class S3Client {

    private final AmazonS3 client;

    public S3Client(AmazonS3 client) {
        this.client = client;
    }

    /**
     * s3 오브젝트를 내려받는다.
     * @param bucketName    내려받는 bucket 위치
     * @param pathAndKey    내려받는 파일의 key값 (path포함)
     * @return              s3Object.
     */
    public S3Object downLoadObject(String bucketName ,String pathAndKey){
        GetObjectRequest request = new GetObjectRequest(bucketName , pathAndKey);
        return client.getObject(request);
    }

    /**
     * s3 오브젝트르 정해진 위치로 내려받는다.
     * @param bucketName    내려받는 bucket 위치
     * @param pathAndKey    내려받는 파일의 key값 (path포함)
     * @param dest          파일저장위치 (path,file명 포함 )
     */
    public void downLoadObject(String bucketName ,String pathAndKey ,String dest) throws Exception {
        S3Object targetObject = downLoadObject(bucketName,pathAndKey);

        if(targetObject == null)
            throw new Exception("해당하는 s3 오브젝트를 찾을 수 없습니다.");


        //dest의 파일 위치를 검사한다.
        try {
            File file = new File(dest);
            file = new File(file.getParent());
            if (!(file.exists()))
                file.mkdir();
        }catch (Exception ex) {
            throw new Exception("지정한 경로를 생성할 수 없습니다.");
        }
        File fileWriter = new File(dest);

        try(FileOutputStream fileOutputStream = new FileOutputStream(fileWriter)){
            byte[] content = IOUtils.toByteArray(targetObject.getObjectContent());
            int contentSize = content.length;
            log.info("s3 오브젝트 사이즈 체크 : " + contentSize + "byte");
            fileOutputStream.write(content);
        }catch (IOException ex){
            throw new Exception("s3 오브젝트 다운로드중 문제가 발생하였습니다.");
        }

    }
}
