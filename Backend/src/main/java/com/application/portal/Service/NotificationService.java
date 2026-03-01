package com.application.portal.Service;

import com.application.portal.Model.Job;
import com.application.portal.Model.Notification;
import com.application.portal.Model.User;
import com.application.portal.Repository.NotificationRepository;
import com.application.portal.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Notification> getNotificationsByUserId(Long userId) {
        // Returns notifications sorted by latest first
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setRead(true); // Updates isRead field
        });
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndIsReadFalse(userId);
        unread.forEach(n -> n.setRead(true));
    }

    @Async // Runs in the background
    public void notifyAllUsersAboutJob(Job job) {
        // 1. Fetch all Job Seekers (or all Users)
        List<User> allUsers = userRepository.findAll();

        // 2. Create a notification object for every user
        List<Notification> notifications = allUsers.stream().map(user -> {
            Notification note = new Notification();
            note.setUser(user);
            note.setId(job.getId());
            note.setMessage("New Opening: " + job.getTitle() + " at " + job.getEmployer().getCompanyName());
            note.setRead(false);
            note.setCreatedAt(LocalDateTime.now());
            return note;
        }).collect(Collectors.toList());

        // 3. Batch save for performance
        notificationRepository.saveAll(notifications);
    }
}
