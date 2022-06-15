package kr.nanoit.main;

import kr.nanoit.config.QueueList;
import kr.nanoit.config.Verification;
import kr.nanoit.http.SandBoxHttpserver;
import kr.nanoit.server.ReceiveServer;
import kr.nanoit.server.SendServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 클라이언트가 http통신으로 서버에 아이디와 비번을 통해 xml을 요청하면 서버는 해당 요청 쿼리스트링을 검증후 xml을 보냄
 * xml을 파싱해서 소켓 접속 엔드포이트를 얻은 클라이언트는 서버와 socket연결을 시도
 * 서버는 socket Thread와 http용 Thread를 따로 두어 지속적인 요청을 받음
 */
@Slf4j
public class Main {

    public static Configuration configuration;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
    public static Map<String, Verification> valificationMap = new HashMap<>();


    public static void main(String[] args) throws Exception {
        final Configurations configurations = new Configurations();
        configuration = configurations.properties(new File("src/main/java/resources/NanoitServer.properties"));

        int count = configuration.getInt("auth.id.count");

        for (int i = 1; i <= count; i++) {
            String id = configuration.getString(("auth.id." + i));
            String password = configuration.getString(("auth.password." + i));
            String encryptkey = configuration.getString(("auth.encryptkey." + i));

            valificationMap.put(id, Verification.builder()
                    .id(id)
                    .password(password)
                    .encryptKey(encryptkey)
                    .build());
        }
        try {
            log.info("[TCPSERVER START AND HTTPSERVER START] {}", SIMPLE_DATE_FORMAT.format(new Date()));

            SandBoxHttpserver sandBoxHttpserver = new SandBoxHttpserver(configuration.getString("auth.server.host"), configuration.getInt("auth.server.port"));
            sandBoxHttpserver.start();

            ServerSocket serverSocket = new ServerSocket(configuration.getInt("tcp.server.port"));
            Socket socket = serverSocket.accept();

            // Hand over to queue param for each thread
            QueueList queueList = new QueueList();

            // Thread List
            Thread thread = new Thread(new ReceiveServer(socket, queueList));
            thread.setName("Receive-Server");

            Thread thread2 = new Thread(new SendServer(socket, queueList));
            thread2.setName("Send-Server");

//            Thread thread3 = new Thread(new ReportServer(socket, queueList));
//            thread3.setName("MobileOperator-Server");


            // Thread Start
            thread.start();
            thread2.start();
//            thread3.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println(String.format("[%s][HTTP SERVER][STOP]", SIMPLE_DATE_FORMAT.format(new Date())))));
        } catch (IOException e) {
            log.error("IOException error occurred", e);
        }
    }
}
