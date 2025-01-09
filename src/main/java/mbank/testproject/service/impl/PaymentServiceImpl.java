package mbank.testproject.service.impl;

import mbank.testproject.model.dto.request.PaymentRequest;
import mbank.testproject.model.dto.response.PaymentResponse;
import mbank.testproject.model.entity.Payment;
import mbank.testproject.model.enums.StatusType;
import mbank.testproject.repository.PaymentRepository;
import mbank.testproject.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse handlePayment(PaymentRequest request) {
        try{
            logger.info("запрос с типом: {}, описание: {}, сумма: {}",
                    request.statusType(), request.description(), request.amount());
            StatusType type = StatusType.valueOf(request.statusType());
            if(type == StatusType.PAY){

                final Payment payment = new Payment();
                payment.setType(type);
                payment.setDescription(request.description());
                payment.setAmount(request.amount());

                paymentRepository.save(payment);
                logger.info("Платеж сохранен в базу данных: {}", payment);

                return new PaymentResponse("Успешно","Платеж обработан и сохранен!");
            }else if(type == StatusType.CHECK){
                logger.info("Обработан запрос типа CHECK");
                return new PaymentResponse("Успешно","Запрос на проверку обработан!");
            }else {
                logger.info("Получен запрос с неверным типом: {}", type);
                return new PaymentResponse("Ошибка","Неверный тип запроса!");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка преобразования типа запроса: {}", request.statusType(), e);
            return new PaymentResponse("Ошибка", "Некорректный тип запроса!");
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при обработке запроса", e);
            return new PaymentResponse("Ошибка", "Произошла ошибка при обработке запроса!");
        }
    }
}
