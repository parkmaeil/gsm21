package com.example.springgsm.controller;

import com.example.springgsm.entity.Student;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

@RestController // new ChatController()
public class ChatController{ // POJO
    // http://localhost:8080/chat?question=21212

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }
    //                     OllamaChatModel

    // private final OllamaChatModel ollama;
    //public ChatController(OllamaChatModel ollama) {
    //    chatClient=ChatClient.create(ollama);
       //this.ollama = ollama;
    //}

    // 데미 데이터(3명)
    private static String STUDENTS= """
            이름 : 나길동 | 학교 : 광주소프트웨어마이스터고 | 이메일 : aaa@aaa.aaa
            이름 : 조길동 | 학교 : 서석고 | 이메일 : bbb@bbb.bbb
            이름 : 김길동 | 학교 : 대동고 | 이메일 : ccc@ccc.ccc            
            """;
    // 시스템메세지
    private String systemRule= """
            당신은 {hr} 도우미 입니다.
            학생 목록 정보만 근거로 답하세요.
            JSON 형식으로 응답해 주세요.
            목록에 없는 내용은 "목록에 없습니다." 라고 답하세요.
            
            JSON 예시:
            {{ name, school, email }}
            
            """;

    @GetMapping("/chat")
    public List<Student> chat(String question){
        //return ollama.call(question);  // 안녕 나는 홍길동 이야~
        String userMsg= """
                [학생 목록 시작]
                %s
                [학생 목록 끝]
                질문 : %s
                """.formatted(STUDENTS, question);

        return chatClient.prompt()
                .system(s->s.text(systemRule).param("hr","HR"))
                .user(userMsg)
                .call() //----> ChatResponse -->
                //.content(); // JSON, Object(VO Student), List<Student>
                //.entity(Student.class); //new Student() . setter ~~n
                .entity(new ParameterizedTypeReference<List<Student>>() {});
    }
}
