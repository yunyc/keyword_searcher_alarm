package com.example.alarm.service.impl;

import com.example.alarm.domain.User;
import com.example.alarm.repository.UserRepository;
import com.example.alarm.service.FCMNotificationService;
import com.example.alarm.web.rest.dto.FCMNotificationRequestDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.N;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FCMNotificationServiceImpl implements FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository usersRepository;

    public String sendNotificationByToken(FCMNotificationRequestDto requestDto) {
        Optional<User> user = usersRepository.findByUserId(requestDto.getTargetUserId());

        if (user.isPresent()) {
            if (user.get().getFirebaseToken() != null) {
                Notification notification = new Notification(requestDto.getTitle(), requestDto.getBody());

                Message message = Message
                    .builder()
                    .setToken(user.get().getFirebaseToken())
                    .setNotification(notification)
                    // .putAllData(requestDto.getData())
                    .build();

                try {
                    firebaseMessaging.send(message);
                    return "알림을 성공적으로 전송했습니다. targetUserId=" + requestDto.getTargetUserId();
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                    return "알림 보내기를 실패하였습니다. targetUserId=" + requestDto.getTargetUserId();
                }
            } else {
                return "서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserId=" + requestDto.getTargetUserId();
            }
        } else {
            return "해당 유저가 존재하지 않습니다. targetUserId=" + requestDto.getTargetUserId();
        }
    }
}
