package com.byteflair.parkplan.api.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.byteflair.parkplan.api.domain.type.NotificationType;
import com.byteflair.parkplan.api.exception.IncorrectNotificationTypeException;

public class PushNotification {
	private String user;
	private NotificationType notificationType;
	private String data;
	
	public PushNotification() {
	}
	
	public PushNotification(String notificationType, String user, String data) throws IncorrectNotificationTypeException {
		this.user = user;
		this.data = data;
		
		try {
			this.notificationType = NotificationType.valueOf(notificationType);
		} catch (IllegalArgumentException e) {
			throw new IncorrectNotificationTypeException(notificationType + " is not a valid notificattion type.", e);
		}
	}

	public String getUser() {
		return user;
	}

	public NotificationType getType() {
		return notificationType;
	}

	public String getData() {
		return data;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setNotificationType(String notificationType) throws IncorrectNotificationTypeException {
		try {
			this.notificationType = NotificationType.valueOf(notificationType);
		} catch (IllegalArgumentException e) {
			throw new IncorrectNotificationTypeException(notificationType + " is not a valid notificattion type.", e);
		}
	}
	
	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("notificationType", notificationType).append("user", user).append("data", data).toString();
	}
}