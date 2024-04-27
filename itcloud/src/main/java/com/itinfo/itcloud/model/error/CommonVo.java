package com.itinfo.itcloud.model.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonVo<T> {
    private Head head;
    private Body<T> body;

    //    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static CommonVo<Boolean> failResponse(String message) {
        CommonVo<Boolean> res = new CommonVo<>();

        Head head = new Head();
        head.setCode(404);
        head.setMessage(message);

        Body<Boolean> body = new Body<>();
        body.setContent(false);
        res.setBody(body);
        res.setHead(head);

        return res;
    }

    public static CommonVo<Boolean> createResponse() {
        CommonVo<Boolean> res = new CommonVo<>();

        Head head = new Head();
        head.setCode(201);
        head.setMessage("생성 성공");
        res.setHead(head);

        Body<Boolean> body = new Body<>();
        body.setContent(true);
        res.setBody(body);

        return res;
    }

    public static CommonVo<Boolean> successResponse() {
        CommonVo<Boolean> res = new CommonVo<>();

        Head head = new Head();
        head.setCode(200);
        head.setMessage("성공");
        res.setHead(head);

        Body<Boolean> body = new Body<>();
        body.setContent(true);
        res.setBody(body);

        return res;
    }

//    public String toJson() {
//        return gson.toJson(this);
//    }
}

