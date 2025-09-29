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

    // 데미 데이터(5명)
    private static String STUDENTS= """
            이름 : 나길동 | 학교 : 광주서석고 | 전화 : 000-1111-1111
            이름 : 조길동 | 학교 : 광주광덕고 | 전화 : 000-1112-1111 
            이름 : 김길동 | 학교 : 광주인성고 | 전화 : 000-1113-1111
            이름 : 이길동 | 학교 : 광주대동고 | 전화 : 000-1114-1111
            이름 : 박길동 | 학교 : 광주석산고 | 전화 : 000-1115-1111     
            """;
    // 시스템메세지
    private String systemRule= """
            당신은 {hr} 도우미 입니다.
            학생 목록 정보만 근거로 답하세요.
            JSON 형식으로 응답해 주세요.
            정확한 이름이 목록에 없는 내용은 "목록에 없습니다." 라고 답하세요.
            
            JSON 예시:
            {{ name, school, phone }}
            
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
