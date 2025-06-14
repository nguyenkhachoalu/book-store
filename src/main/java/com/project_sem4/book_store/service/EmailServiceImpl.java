package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.handle.handle_email.EmailMessage;
import com.project_sem4.book_store.exception.AppException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    JavaMailSender mailSender;

    @NonFinal
    @Value("${email.from}")
    protected String from;

    @Override
    public String sendEmail(EmailMessage emailMessage) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress(from));
            helper.setTo(emailMessage.getTo().toArray(new String[0]));
            helper.setSubject(emailMessage.getSubject());
            helper.setText(emailMessage.getContent(), true);

            mailSender.send(message);

            return "Gửi email thành công tới: " + String.join(", ", emailMessage.getTo());
        }catch (AppException e) {
            throw e;
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email thất bại: " + e.getMessage());
        }
    }

    @Override
    public String generateConfirmationCodeEmail(String confirmationCode) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
                "<div style='border: 1px solid #ddd; padding: 20px; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #333;'>Nhận mã xác nhận tài khoản</h2>" +
                "<p>Xin chào,</p>" +
                "<p>Bạn đã yêu cầu một mã xác nhận. Dưới đây là mã của bạn:</p>" +
                "<p style='font-size: 31px; font-weight: bold; color: #007BFF; background-color: #f0f0f0; padding: 10px; border-radius: 5px; letter-spacing: 10px; display: inline-block;'>" + confirmationCode + "</p>" +
                "<p>Vui lòng nhập mã này để xác nhận tài khoản của bạn.</p><br/>" +
                "<p>Chúc bạn một ngày tốt lành!</p>" +
                "<p>Trân trọng,</p>" +
                "<p><i>Đội ngũ hỗ trợ của chúng tôi</i></p>" +
                "</div></body></html>";
    }

    @Override
    public String generateForgotPassword(String newPassword) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
                "<div style='border: 1px solid #ddd; padding: 20px; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #333;'>Lấy lại mật khẩu</h2>" +
                "<p>Xin chào,</p>" +
                "<p>Bạn đã lấy lại tài khoản của mình thành công. Dưới đây là mật khẩu mới của bạn:</p>" +
                "<p style='font-size: 31px; font-weight: bold; color: #007BFF; background-color: #f0f0f0; padding: 10px; border-radius: 5px; letter-spacing: 10px; display: inline-block;'>" + newPassword + "</p>" +
                "<p>Vui lòng nhập mật khẩu này để đăng nhập tài khoản của bạn.</p><br/>" +
                "<p>Chúc bạn một ngày tốt lành!</p>" +
                "<p>Trân trọng,</p>" +
                "<p><i>Đội ngũ hỗ trợ của chúng tôi</i></p>" +
                "</div></body></html>";
    }

    @Override
    public String generateOrderConfirmEmail(String userName) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
                "<div style='border: 1px solid #ddd; padding: 20px; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #333;'>Đơn Hàng của " + userName + " đã được xác nhận và bàn giao cho đơn vị vận chuyển</h2>" +
                "<p>Xin chào,</p>" +
                "<p>Đơn hàng đang được vận chuyển tới quý khách trong thời gian sớm nhất</p>" +
                "<p>Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi.</p><br/>" +
                "<p>Chúc bạn một ngày tốt lành!</p>" +
                "<p>Trân trọng,</p>" +
                "<p><i>Đội ngũ hỗ trợ của chúng tôi</i></p>" +
                "</div></body></html>";
    }

    @Override
    public String generateDeliveryCompletionEmail(String full_name) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
                "<div style='border: 1px solid #ddd; padding: 20px; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #333;'>Đơn hàng " + full_name + " đã được giao thành công</h2>" +
                "<p>Xin chào,</p>" +
                "<p>Đơn hàng của bạn đã được giao hàng thành công.</p>" +
                "<p>Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi.</p><br/>" +
                "<p>Chúc bạn một ngày tốt lành!</p>" +
                "<p>Trân trọng,</p>" +
                "<p><i>Đội ngũ hỗ trợ của chúng tôi</i></p>" +
                "</div></body></html>";
    }
    public String generateCancelledEmail(String full_name){
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
                "<div style='border: 1px solid #ddd; padding: 20px; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #333;'>Đơn hàng " + full_name + " đã bị hủy</h2>" +
                "<p>Xin chào,</p>" +
                "<p>Đơn hàng của bạn đã bị hủy.</p>" +
                "<p>Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi.</p><br/>" +
                "<p>Chúc bạn một ngày tốt lành!</p>" +
                "<p>Trân trọng,</p>" +
                "<p><i>Đội ngũ hỗ trợ của chúng tôi</i></p>" +
                "</div></body></html>";
    }
}