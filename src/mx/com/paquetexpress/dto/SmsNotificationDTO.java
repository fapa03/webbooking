package mx.com.paquetexpress.dto;

import java.io.Serializable;

public class SmsNotificationDTO implements Serializable {

    private String phone;
    private String message;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	@Override
	public String toString() {
		return "SmsNotificationDTO [phone=" + phone + ", message=" + message+ "]";
	}

}
