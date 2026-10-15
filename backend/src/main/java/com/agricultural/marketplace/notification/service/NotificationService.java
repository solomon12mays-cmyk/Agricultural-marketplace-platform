package com.agricultural.marketplace.notification.service;

import com.agricultural.marketplace.notification.dto.NotificationResponse;
import com.agricultural.marketplace.notification.dto.SendNotificationRequest;
import com.agricultural.marketplace.notification.entity.Notification;
import com.agricultural.marketplace.notification.entity.NotificationDelivery;
import com.agricultural.marketplace.notification.provider.PushNotificationService;
import com.agricultural.marketplace.notification.provider.SmsService;
import com.agricultural.marketplace.notification.provider.UssdService;
import com.agricultural.marketplace.notification.repository.NotificationDeliveryRepository;
import com.agricultural.marketplace.notification.repository.NotificationRepository;
import com.agricultural.marketplace.shared.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotificationDeliveryRepository deliveryRepository;
    private final SmsService smsService;
    private final PushNotificationService pushNotificationService;
    private final UssdService ussdService;

    public NotificationService(NotificationRepository notificationRepository,
                               NotificationDeliveryRepository deliveryRepository,
                               SmsService smsService,
                               PushNotificationService pushNotificationService,
                               UssdService ussdService) {
        this.notificationRepository = notificationRepository;
        this.deliveryRepository = deliveryRepository;
        this.smsService = smsService;
        this.pushNotificationService = pushNotificationService;
        this.ussdService = ussdService;
    }

    public NotificationResponse sendNotification(SendNotificationRequest request) {
        Notification notification = new Notification(
                request.userId(),
                request.channel().toUpperCase(),
                request.template(),
                request.subject(),
                request.body(),
                "PENDING"
        );
        Notification saved = notificationRepository.save(notification);

        String provider = request.channel().toUpperCase();
        try {
            String reference = null;
            if ("SMS".equalsIgnoreCase(request.channel())) {
                String phone = request.recipientAddress() != null ? request.recipientAddress() : "UNKNOWN";
                reference = smsService.send(phone, request.body());
            } else if ("PUSH".equalsIgnoreCase(request.channel())) {
                String token = request.recipientAddress() != null ? request.recipientAddress() : "UNKNOWN";
                reference = pushNotificationService.send(token, request.subject(), request.body());
            } else if ("USSD".equalsIgnoreCase(request.channel())) {
                String phone = request.recipientAddress() != null ? request.recipientAddress() : "UNKNOWN";
                reference = ussdService.push(phone, request.body());
            } else {
                log.warn("Unknown notification channel: {}", request.channel());
            }

            saved.markSent();
            notificationRepository.save(saved);

            NotificationDelivery delivery = new NotificationDelivery(
                    saved.getId(),
                    provider,
                    "DELIVERED",
                    1
            );
            deliveryRepository.save(delivery);

        } catch (Exception ex) {
            log.error("Failed to deliver notification id={}: {}", saved.getId(), ex.getMessage());
            saved.markFailed();
            notificationRepository.save(saved);

            NotificationDelivery delivery = new NotificationDelivery(
                    saved.getId(),
                    provider,
                    "FAILED",
                    1
            );
            deliveryRepository.save(delivery);
        }

        return NotificationResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return NotificationResponse.fromEntity(notification);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable)
                .map(NotificationResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> listNotifications(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return notificationRepository.findByStatus(status.toUpperCase(), pageable)
                    .map(NotificationResponse::fromEntity);
        }
        return notificationRepository.findAll(pageable)
                .map(NotificationResponse::fromEntity);
    }
}

