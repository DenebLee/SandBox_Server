package kr.nanoit.config;

import lombok.AllArgsConstructor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * prop.getProperty("key")로 값을 가져오도록 Properties를 읽어 Propertues 객체로 반환
 * Default Location : src/main/resources
 *
 * @author LeeJungSeob
 *
 */
@AllArgsConstructor
public class readProperties {

    public static Properties read(){
        Properties prop = new Properties();
        String propFileName = "NanoitServer.properties";
        InputStream inputStream = readProperties.class.getClassLoader().getResourceAsStream(propFileName);

        try{
            if (inputStream != null) {
                prop.load(inputStream);
                return prop;
            }else{
                throw new FileNotFoundException("프로퍼티 값이 없음");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
